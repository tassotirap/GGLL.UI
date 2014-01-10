package org.ggll.syntax.graph;

import ggll.core.list.ExtendedList;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JComponent;

import org.ggll.images.GGLLImages;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.adapter.ExtendedActionFactory;
import org.ggll.syntax.graph.decorator.ConnectorDecoratorFactory;
import org.ggll.syntax.graph.provider.GridProvider;
import org.ggll.syntax.graph.provider.LineProvider;
import org.ggll.syntax.graph.provider.NodeSelectProvider;
import org.ggll.syntax.graph.state.State;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateHistory;
import org.ggll.syntax.graph.state.StateNode;
import org.ggll.syntax.graph.widget.GridWidget;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.ggll.syntax.graph.widget.LineWidget;
import org.ggll.syntax.graph.widget.MarkedWidget;
import org.ggll.syntax.graph.widget.TypedWidget;
import org.ggll.util.Log;
import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

/**
 * 
 * @author Tasso Tirapani Silva Pinto
 * 
 */
public class SyntaxGraph extends GraphScene.StringGraph implements PropertyChangeListener
{
	private static final double MIN_ZOOM = 0.5;
	private static final double MAX_ZOOM = 1.5;

	// Canvas States
	private State canvasState;
	private StateHistory canvasStateHistory;

	private final ExtendedActionFactory actionFactory = new ExtendedActionFactory(this);

	// Monitors
	private PropertyChangeSupport monitor;

	private boolean showingGrid;
	private boolean showingLines;

	private Router activeRouter;
	private final LayerWidget backgroundLayer = new LayerWidget(this);
	private final LayerWidget connectionLayer = new LayerWidget(this);
	private final LayerWidget interractionLayer = new LayerWidget(this);
	private final LayerWidget mainLayer = new LayerWidget(this);

	private String connectionStrategy;
	private String moveStrategy;

	private final AbstractMap<String, Cursor> cursors = new HashMap<String, Cursor>();

	// Decorator
	private final ConnectorDecoratorFactory decorator = new ConnectorDecoratorFactory(this);

	// Components
	private final ExtendedList<String> alternatives = new ExtendedList<String>();
	private ExtendedList<String> labels = new ExtendedList<String>();
	private final ExtendedList<String> lambdas = new ExtendedList<String>();
	private final ExtendedList<String> leftSides = new ExtendedList<String>();
	private final ExtendedList<String> nterminals = new ExtendedList<String>();
	private final ExtendedList<String> start = new ExtendedList<String>();
	private final ExtendedList<String> successors = new ExtendedList<String>();
	private final ExtendedList<String> terminals = new ExtendedList<String>();

	public SyntaxGraph(String cursor, String connectionStrategy, String movementStrategy, String file)
	{
		try
		{
			this.connectionStrategy = connectionStrategy;
			this.moveStrategy = movementStrategy;
			this.canvasState = State.read(file);
			this.canvasStateHistory = new StateHistory(this);

			this.monitor = new PropertyChangeSupport(this);

			createCursors();
			addChild(this.backgroundLayer);
			addChild(this.mainLayer);
			addChild(this.connectionLayer);
			addChild(this.interractionLayer);

			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.SELECT));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.SELECT_LABEL));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.CREATE_NODE));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.POPUP_MENU_MAIN));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.NODE_HOVER));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.LABEL_HOVER));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.MOUSE_CENTERED_ZOOM));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.PAN));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.RECTANGULAR_SELECT));
			getActions().addAction(this.actionFactory.getAction(ExtendedActionFactory.DELETE));
			setActiveTool(cursor);

			if (this.canvasState.getPreferences().getConnectionStrategy() != null)
			{
				setConnectionStrategy(this.canvasState.getPreferences().getConnectionStrategy());
			}
			else
			{
				setConnectionStrategy(connectionStrategy);
			}
			if (this.canvasState.getPreferences().getMoveStrategy() != null)
			{
				setMoveStrategy(this.canvasState.getPreferences().getMoveStrategy());
			}
			else
			{
				setMoveStrategy(this.moveStrategy);
			}

			updateState(this.canvasState);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	private void clearWidgets()
	{
		final Object[] edges = getEdges().toArray();
		for (final Object edge : edges)
		{
			removeEdgeSafely((String) edge);
		}

		final Object[] nodes = getNodes().toArray();
		for (final Object node : nodes)
		{
			removeNodeSafely((String) node);
		}

		getTerminals().removeAll();
		getNterminals().removeAll();
		getLeftSides().removeAll();
		getLambdas().removeAll();
		getStart().removeAll();
		getSuccessors().removeAll();
		getAlternatives().removeAll();
	}

	@Override
	protected void attachEdgeSourceAnchor(String edge, String oldSourceNode, String sourceNode)
	{
		final Widget widget = sourceNode != null ? findWidget(sourceNode) : null;
		final ConnectionWidget conn = (ConnectionWidget) findWidget(edge);
		if (isSuccessor(edge))
		{
			conn.setSourceAnchor(new UnidirectionalAnchor(this, widget, Direction.RIGHT));
		}
		else if (isAlternative(edge))
		{
			conn.setSourceAnchor(new UnidirectionalAnchor(this, widget, Direction.BOTTOM));
		}
		else
		{
			conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(widget));
		}
	}

	@Override
	protected void attachEdgeTargetAnchor(String edge, String oldTargetNode, String targetNode)
	{
		final Widget widget = targetNode != null ? findWidget(targetNode) : null;
		final ConnectionWidget conn = (ConnectionWidget) findWidget(edge);
		if (isSuccessor(edge))
		{
			conn.setTargetAnchor(new UnidirectionalAnchor(this, widget, Direction.LEFT, edge, Direction.TOP));
		}
		else if (isAlternative(edge))
		{
			conn.setTargetAnchor(new UnidirectionalAnchor(this, widget, Direction.TOP, edge, Direction.LEFT));
		}
		else
		{
			conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(widget));
		}
	}

	@Override
	protected Widget attachEdgeWidget(String edge)
	{
		ConnectionWidget connection = null;
		final String activeTool = getCanvasActiveTool();

		if (activeTool.equals(CanvasResource.SUCCESSOR) || activeTool.equals(CanvasResource.ALTERNATIVE))
		{
			connection = this.decorator.drawConnection(activeTool, this, edge);
			connection.setRouter(getActiveRouter());
			connection.createActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.CONN_SELECT));
			connection.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.RECONNECT));

			connection.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.FREE_MOVE_CP));
			connection.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.ADD_REMOVE_CP));
			this.connectionLayer.addChild(connection);
			if (activeTool.equals(CanvasResource.SUCCESSOR))
			{
				if (!isSuccessor(edge))
				{
					getSuccessors().append(edge);
				}
			}
			else if (activeTool.equals(CanvasResource.ALTERNATIVE))
			{
				if (!isAlternative(edge))
				{
					getAlternatives().append(edge);
				}
			}
		}
		return connection;
	}

	@Override
	protected Widget attachNodeWidget(String node)
	{
		Widget widget = null;
		final String activeTool = getCanvasActiveTool();

		if (node.startsWith(LineWidget.class.getCanonicalName()))
		{
			final LineWidget lWidget = new LineWidget(this);
			this.backgroundLayer.addChild(lWidget);
			widget = lWidget;
			setShowingLines(true);
			this.canvasState.getPreferences().setShowLines(true);
		}
		else if (node.startsWith(GridWidget.class.getCanonicalName()))
		{
			final GridWidget gWidget = new GridWidget(this);
			this.backgroundLayer.addChild(gWidget);
			widget = gWidget;
			setShowingGrid(true);
			this.canvasState.getPreferences().setShowGrid(true);
		}
		else if (!activeTool.equals(CanvasResource.SELECT))
		{
			final String tool = activeTool;
			if (tool.equals(CanvasResource.N_TERMINAL) || tool.equals(CanvasResource.TERMINAL) || tool.equals(CanvasResource.LEFT_SIDE) || tool.equals(CanvasResource.LAMBDA) || tool.equals(CanvasResource.START))
			{
				try
				{
					widget = this.decorator.drawIcon(activeTool, this, node);
				}
				catch (final Exception e)
				{
					Log.log(Log.ERROR, this, "Could not create widget!", e);
					widget = new LabelWidgetExt(this.mainLayer.getScene(), node);
				}
				widget.createActions(CanvasResource.SELECT);
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.NODE_HOVER));
				}
				widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.SELECT));
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.EDITOR));
				}
				widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.MOVE));
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.createActions(CanvasResource.SUCCESSOR).addAction(this.actionFactory.getAction(ExtendedActionFactory.SUCCESSOR));
				}
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.createActions(CanvasResource.ALTERNATIVE).addAction(this.actionFactory.getAction(ExtendedActionFactory.ALTERNATIVE));
				}
				this.mainLayer.addChild(widget);
				if (tool.equals(CanvasResource.LEFT_SIDE))
				{
					this.leftSides.append(node);
				}
				else if (tool.equals(CanvasResource.TERMINAL))
				{
					this.terminals.append(node);
				}
				else if (tool.equals(CanvasResource.N_TERMINAL))
				{
					this.nterminals.append(node);
				}
				else if (tool.equals(CanvasResource.LAMBDA))
				{
					this.lambdas.append(node);
				}
				else if (tool.equals(CanvasResource.START))
				{
					this.start.append(node);
				}
			}
			else if (tool.equals(CanvasResource.LABEL))
			{
				widget = new LabelWidgetExt(this.mainLayer.getScene(), "Double Click Here to Edit");
				widget.createActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.LABEL_HOVER));
				widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.SELECT_LABEL));
				widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.EDITOR));
				widget.getActions(CanvasResource.SELECT).addAction(this.actionFactory.getAction(ExtendedActionFactory.MOVE));
				this.labels.append(node);
				this.mainLayer.addChild(widget);
			}
			if (widget != null)
			{
				if (widget instanceof TypedWidget)
				{
					((TypedWidget) widget).setType(tool);
				}
			}
		}
		return widget;
	}

	public boolean canZoomIn()
	{
		return getZoomFactor() < MAX_ZOOM;
	}

	public boolean canZoomOut()
	{
		return getZoomFactor() > MIN_ZOOM;
	}

	public void createCursors()
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();

		Image image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_LEFT_SIDE_ENABLED));
		this.cursors.put(CanvasResource.LEFT_SIDE, toolkit.createCustomCursor(image, new Point(0, 0), "Left Side"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_TERMINAL_ENABLED));
		this.cursors.put(CanvasResource.TERMINAL, toolkit.createCustomCursor(image, new Point(0, 0), "Terminal"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_N_TERMINAL_ENABLED));
		this.cursors.put(CanvasResource.N_TERMINAL, toolkit.createCustomCursor(image, new Point(0, 0), "Non-Terminal"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_LAMBDA_ENABLED));
		this.cursors.put(CanvasResource.LAMBDA, toolkit.createCustomCursor(image, new Point(0, 0), "Lambda Alternative"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_SUCCESSOR_ENABLED));
		this.cursors.put(CanvasResource.SUCCESSOR, toolkit.createCustomCursor(image, new Point(0, 0), "Successor"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_ALTERNATIVE_ENABLED));
		this.cursors.put(CanvasResource.ALTERNATIVE, toolkit.createCustomCursor(image, new Point(0, 0), "Alternative"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_LABEL_ENABLED));
		this.cursors.put(CanvasResource.LABEL, toolkit.createCustomCursor(image, new Point(0, 0), "Label"));

		image = toolkit.getImage(SyntaxGraph.class.getResource(GGLLImages.CURSOS_START_ENABLED));
		this.cursors.put(CanvasResource.START, toolkit.createCustomCursor(image, new Point(0, 0), "Start"));
	}

	@Override
	public JComponent createView()
	{
		final JComponent component = super.createView();
		component.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				setFocused();
			}
		});
		return component;
	}

	public ExtendedActionFactory getActionFactory()
	{
		return this.actionFactory;
	}

	public Router getActiveRouter()
	{
		return this.activeRouter;
	}

	public ExtendedList<String> getAlternatives()
	{
		return this.alternatives;
	}

	public LayerWidget getBackgroundLayer()
	{
		return this.backgroundLayer;
	}

	public String getCanvasActiveTool()
	{
		String tool = super.getActiveTool();
		if (tool == null)
		{
			tool = CanvasResource.SELECT;
		}
		return tool;
	}

	public ConnectorDecoratorFactory getCanvasDecorator()
	{
		return this.decorator;
	}

	public State getCanvasState()
	{
		return this.canvasState;
	}

	/**
	 * This method is here for a mere convenience, is really essential for the
	 * canvas itself
	 * 
	 * @return the volatile state manager of this canvas
	 */
	public StateHistory getCanvasStateHistory()
	{
		return this.canvasStateHistory;
	}

	public LayerWidget getConnectionLayer()
	{
		return this.connectionLayer;
	}

	public String getConnectionStrategy()
	{
		return this.connectionStrategy;
	}

	public String getFile()
	{
		return this.canvasState.getFile();
	}

	public LayerWidget getInterractionLayer()
	{
		return this.interractionLayer;
	}

	public Collection<?> getLabels()
	{
		return this.labels.getAll();
	}

	public ExtendedList<String> getLambdas()
	{
		return this.lambdas;
	}

	public ExtendedList<String> getLeftSides()
	{
		return this.leftSides;
	}

	public LayerWidget getMainLayer()
	{
		return this.mainLayer;
	}

	public PropertyChangeSupport getMonitor()
	{
		return this.monitor;
	}

	public String getMoveStrategy()
	{
		return this.moveStrategy;
	}

	public String getNodeType(Object node)
	{
		final Widget w = findWidget(node);
		if (w != null)
		{
			if (w instanceof LabelWidgetExt)
			{
				return ((LabelWidgetExt) w).getType();
			}
		}
		return null;
	}

	public ExtendedList<String> getNterminals()
	{
		return this.nterminals;
	}

	public BufferedImage getScreenshot()
	{
		final BufferedImage bi = new BufferedImage(getPreferredSize().width, getPreferredSize().height, BufferedImage.TYPE_4BYTE_ABGR);
		final Graphics2D graphics = bi.createGraphics();
		getScene().paint(graphics);
		graphics.dispose();
		return bi;
	}

	public ExtendedList<String> getStart()
	{
		return this.start;
	}

	public ExtendedList<String> getSuccessors()
	{
		return this.successors;
	}

	public ExtendedList<String> getTerminals()
	{
		return this.terminals;
	}

	public boolean isAlternative(String edge)
	{
		return this.alternatives.contains(edge);
	}

	public boolean isLabel(Object o)
	{
		return this.labels.contains((String) o);
	}

	public boolean isLambda(Object o)
	{
		return this.lambdas.contains((String) o);
	}

	public boolean isLeftSide(Object o)
	{
		return this.leftSides.contains((String) o);
	}

	@Override
	public boolean isNode(Object o)
	{
		if (!isLabel(o) && (isTerminal(o) || isNonTerminal(o) || isLambda(o) || isLeftSide(o) || isStart(o)))
		{
			return super.isNode(o);
		}
		return false;
	}

	public boolean isNonTerminal(Object o)
	{
		return this.nterminals.contains((String) o);
	}

	/**
	 * @return the showingGrid
	 */
	public boolean isShowingGrid()
	{
		return this.showingGrid;
	}

	/**
	 * @return the showingLines
	 */
	public boolean isShowingLines()
	{
		return this.showingLines;
	}

	public boolean isStart(Object o)
	{
		return this.start.contains((String) o);
	}

	public boolean isSuccessor(String edge)
	{
		return this.successors.contains(edge);
	}

	public boolean isTerminal(Object o)
	{
		return this.terminals.contains((String) o);
	}

	/**
	 * painting with antialias
	 */
	@Override
	public void paintChildren()
	{
		final Object anti = getGraphics().getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		final Object textAnti = getGraphics().getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);

		getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		super.paintChildren();

		getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, anti);
		getGraphics().setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, textAnti);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		if (event.getSource() instanceof StateHistory)
		{
			switch (event.getPropertyName())
			{
				case "object_state":
					this.canvasState = (State) event.getNewValue();
					updateState(this.canvasState);
					this.revalidate();
					break;

				case "writing":
					this.canvasState.refresh(this);
					break;
			}
		}
		this.monitor.firePropertyChange(event);
	}

	public void removeEdgeSafely(String edge)
	{
		if (isEdge(edge))
		{
			if (getSuccessors().contains(edge))
			{
				getSuccessors().remove(edge);
			}
			if (getAlternatives().contains(edge))
			{
				getAlternatives().remove(edge);
			}
			super.removeEdge(edge);
		}
	}

	public void removeNodeSafely(String node)
	{
		if (isLabel(node))
		{
			this.labels.remove(node);
		}
		super.removeNode(node);
		if (node.startsWith(GridWidget.class.getCanonicalName()))
		{
			setShowingGrid(false);
		}
		else if (node.startsWith(LineWidget.class.getCanonicalName()))
		{
			setShowingLines(false);
		}
	}

	public void select(Object object)
	{
		select(object, false);
	}

	public void select(Object object, boolean invertSelection)
	{
		Widget w;
		if ((w = findWidget(object)) != null)
		{
			final NodeSelectProvider nsp = new NodeSelectProvider(this);
			if (!getSelectedObjects().contains(w))
			{
				if (nsp.isSelectionAllowed(w, null, invertSelection))
				{
					nsp.select(w, null, invertSelection);
				}
				validate();
			}
		}
	}

	@Override
	public void setActiveTool(String activeTool)
	{
		super.setActiveTool(activeTool);
		if (activeTool.equals(CanvasResource.SELECT))
		{
			setCursor(Cursor.getDefaultCursor());
		}
		else
		{
			setCursor(this.cursors.get(activeTool));
		}
	}

	public void setConnectionStrategy(String strategy)
	{
		if (strategy != null)
		{
			this.canvasState.getPreferences().setConnectionStrategy(strategy);
			if (strategy.equals(CanvasResource.R_ORTHOGONAL) || strategy.equals(CanvasResource.R_FREE) || strategy.equals(CanvasResource.R_DIRECT))
			{
				if (!strategy.equals(this.connectionStrategy) || this.activeRouter == null)
				{
					this.connectionStrategy = strategy;
					if (strategy.equals(CanvasResource.R_ORTHOGONAL))
					{
						this.activeRouter = RouterFactory.createOrthogonalSearchRouter(this.mainLayer);
					}
					else if (strategy.equals(CanvasResource.R_DIRECT))
					{
						this.activeRouter = RouterFactory.createDirectRouter();
					}
					else if (strategy.equals(CanvasResource.R_FREE))
					{
						this.activeRouter = RouterFactory.createFreeRouter();
					}
				}
			}
		}
	}

	/**
	 * Called to give properly give focus to this canvas
	 */
	public void setFocused()
	{
		getView().grabFocus();
		final Component component = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
		if (component != null)
		{
			if (component.getKeyListeners().length == 0)
			{
				component.addKeyListener(new KeyListener()
				{

					@Override
					public void keyPressed(KeyEvent e)
					{
						for (final KeyListener keyListener : getView().getKeyListeners())
						{
							keyListener.keyPressed(e);
						}
					}

					@Override
					public void keyReleased(KeyEvent e)
					{
						for (final KeyListener keyListener : getView().getKeyListeners())
						{
							keyListener.keyReleased(e);
						}
					}

					@Override
					public void keyTyped(KeyEvent e)
					{
						for (final KeyListener keyListener : getView().getKeyListeners())
						{
							keyListener.keyTyped(e);
						}
					}
				});
			}
		}
	}

	/**
	 * 
	 * @param labels
	 */
	public void setLabels(ExtendedList<String> labels)
	{
		this.labels = labels;
	}

	public void setMoveStrategy(String strategy)
	{
		final MoveTracker moveTracker = new MoveTracker(this);
		if (strategy != null)
		{
			this.canvasState.getPreferences().setMoveStrategy(strategy);
			if (strategy.equals(CanvasResource.M_ALIGN) || strategy.equals(CanvasResource.M_SNAP) || strategy.equals(CanvasResource.M_FREE) || strategy.equals(CanvasResource.M_LINES))
			{
				this.moveStrategy = strategy;
				moveTracker.notifyObservers(strategy);
			}
		}
	}

	/**
	 * @param showingGrid
	 *            the showingGrid to set
	 */
	public void setShowingGrid(boolean showingGrid)
	{
		this.showingGrid = showingGrid;
		getCanvasState().getPreferences().setShowGrid(showingGrid);
	}

	public void setShowingLines(boolean showingLines)
	{
		this.showingLines = showingLines;
		getCanvasState().getPreferences().setShowLines(showingLines);
	}

	public void updateState(State state)
	{
		clearWidgets();
		this.canvasState = state;
		setConnectionStrategy(state.getPreferences().getConnectionStrategy());
		setMoveStrategy(state.getPreferences().getMoveStrategy());

		for (final String element : state.getNodes())
		{
			final StateNode node = state.findNode(element);
			setActiveTool(state.getType(element));
			final Widget widget = addNode(element);
			if (node.getLocation() != null)
			{
				widget.setPreferredLocation(node.getLocation());
			}
			if (widget instanceof LabelWidgetExt)
			{
				((LabelWidgetExt) widget).setLabel(node.getTitle());
			}
			if (widget instanceof MarkedWidget)
			{
				((MarkedWidget) widget).setMark(node.getSemanticRoutine());
			}
			if (widget instanceof TypedWidget)
			{
				((TypedWidget) widget).setType(node.getType());
			}
		}

		for (final String element : state.getConnections())
		{
			final StateConnection connection = state.findConnection(element);
			setActiveTool(state.getType(element));
			final ConnectionWidget connectionWidget = (ConnectionWidget) addEdge(element);
			connectionWidget.setRouter(this.activeRouter);
			connectionWidget.setRoutingPolicy(RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED);
			connectionWidget.setControlPoints(connection.getPoints(), true);

			setEdgeSource(element, connection.getSource());
			setEdgeTarget(element, connection.getTarget());
		}

		if (state.getPreferences().isShowGrid())
		{
			GridProvider.getInstance(this).setVisible(true);
		}
		if (state.getPreferences().isShowLines())
		{
			LineProvider.getInstance(this).populateCanvas();
		}

		setActiveTool(CanvasResource.SELECT);
		validate();
	}
}

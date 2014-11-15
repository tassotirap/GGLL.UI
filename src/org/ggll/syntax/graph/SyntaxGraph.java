package org.ggll.syntax.graph;

import ggll.core.list.ExtendedList;

import java.awt.Color;
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
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import org.ggll.images.ImageResource;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.adapter.ExtendedActionFactory;
import org.ggll.syntax.graph.connector.ConnectorFactory;
import org.ggll.syntax.graph.provider.GridProvider;
import org.ggll.syntax.graph.provider.LineProvider;
import org.ggll.syntax.graph.state.State;
import org.ggll.syntax.graph.state.StateConnection;
import org.ggll.syntax.graph.state.StateHistory;
import org.ggll.syntax.graph.state.StateNode;
import org.ggll.syntax.graph.widget.GridWidget;
import org.ggll.syntax.graph.widget.LabelWidgetExt;
import org.ggll.syntax.graph.widget.LineWidget;
import org.ggll.syntax.graph.widget.MarkedWidget;
import org.ggll.syntax.graph.widget.TypedWidget;
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
	private final ConnectorFactory connectorFactory = new ConnectorFactory(this);

	// Components
	private final ExtendedList<String> alternatives = new ExtendedList<String>();
	private final ExtendedList<String> successors = new ExtendedList<String>();

	private final ExtendedList<String> labels = new ExtendedList<String>();
	private final ExtendedList<String> lambdas = new ExtendedList<String>();
	private final ExtendedList<String> leftSides = new ExtendedList<String>();
	private final ExtendedList<String> nterminals = new ExtendedList<String>();
	private final ExtendedList<String> start = new ExtendedList<String>();

	private final ExtendedList<String> terminals = new ExtendedList<String>();

	public SyntaxGraph(final String cursor, final String connectionStrategy, final String movementStrategy, final String file)
	{
		try
		{
			this.connectionStrategy = connectionStrategy;
			moveStrategy = movementStrategy;
			canvasState = State.read(file);
			canvasStateHistory = new StateHistory(this);

			monitor = new PropertyChangeSupport(this);

			createCursors();
			addChild(backgroundLayer);
			addChild(mainLayer);
			addChild(connectionLayer);
			addChild(interractionLayer);

			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.SELECT));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.SELECT_LABEL));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.CREATE_NODE));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.POPUP_MENU_MAIN));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.NODE_HOVER));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.LABEL_HOVER));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.MOUSE_CENTERED_ZOOM));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.PAN));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.RECTANGULAR_SELECT));
			getActions().addAction(actionFactory.getAction(ExtendedActionFactory.DELETE));
			setActiveTool(cursor);

			if (canvasState.getPreferences().getConnectionStrategy() != null)
			{
				setConnectionStrategy(canvasState.getPreferences().getConnectionStrategy());
			}
			else
			{
				setConnectionStrategy(connectionStrategy);
			}
			if (canvasState.getPreferences().getMoveStrategy() != null)
			{
				setMoveStrategy(canvasState.getPreferences().getMoveStrategy());
			}
			else
			{
				setMoveStrategy(moveStrategy);
			}

			updateState(canvasState);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	private void appendNode(final String node, final String activeTool)
	{
		switch (activeTool)
		{
			case CanvasResource.LEFT_SIDE:
				leftSides.append(node);
				break;
			case CanvasResource.TERMINAL:
				terminals.append(node);
				break;
			case CanvasResource.N_TERMINAL:
				nterminals.append(node);
				break;
			case CanvasResource.LAMBDA:
				lambdas.append(node);
				break;
			case CanvasResource.START:
				start.append(node);
				break;
		}
	}

	@Override
	protected void attachEdgeSourceAnchor(final String edge, final String oldSourceNode, final String sourceNode)
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
	protected void attachEdgeTargetAnchor(final String edge, final String oldTargetNode, final String targetNode)
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
	protected Widget attachEdgeWidget(final String edge)
	{
		ConnectionWidget connection = null;
		final String activeTool = getCanvasActiveTool();

		if (activeTool.equals(CanvasResource.SUCCESSOR) || activeTool.equals(CanvasResource.ALTERNATIVE))
		{
			connection = connectorFactory.drawConnection(activeTool, this, edge);
			connection.setRouter(getActiveRouter());
			connection.createActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.CONN_SELECT));
			connection.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.RECONNECT));

			connection.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.FREE_MOVE_CP));
			connection.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.ADD_REMOVE_CP));
			connectionLayer.addChild(connection);
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
	protected Widget attachNodeWidget(final String node)
	{
		final String activeTool = getCanvasActiveTool();

		if (node.startsWith(LineWidget.class.getCanonicalName()))
		{
			return createLineWidget();
		}
		else if (node.startsWith(GridWidget.class.getCanonicalName()))
		{
			return createGridWidget();
		}
		else if (!activeTool.equals(CanvasResource.SELECT)) { return createNodeWidget(node, activeTool); }
		return null;
	}

	public boolean canZoomIn()
	{
		return getZoomFactor() < SyntaxGraph.MAX_ZOOM;
	}

	public boolean canZoomOut()
	{
		return getZoomFactor() > SyntaxGraph.MIN_ZOOM;
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

	public void createCursors()
	{
		final Toolkit toolkit = Toolkit.getDefaultToolkit();

		Image image = toolkit.getImage(ImageResource.CURSOS_LEFT_SIDE_ENABLED);
		cursors.put(CanvasResource.LEFT_SIDE, toolkit.createCustomCursor(image, new Point(0, 0), "Left Side"));

		image = toolkit.getImage(ImageResource.CURSOS_TERMINAL_ENABLED);
		cursors.put(CanvasResource.TERMINAL, toolkit.createCustomCursor(image, new Point(0, 0), "Terminal"));

		image = toolkit.getImage(ImageResource.CURSOS_N_TERMINAL_ENABLED);
		cursors.put(CanvasResource.N_TERMINAL, toolkit.createCustomCursor(image, new Point(0, 0), "Non-Terminal"));

		image = toolkit.getImage(ImageResource.CURSOS_LAMBDA_ENABLED);
		cursors.put(CanvasResource.LAMBDA, toolkit.createCustomCursor(image, new Point(0, 0), "Lambda Alternative"));

		image = toolkit.getImage(ImageResource.CURSOS_SUCCESSOR_ENABLED);
		cursors.put(CanvasResource.SUCCESSOR, toolkit.createCustomCursor(image, new Point(0, 0), "Successor"));

		image = toolkit.getImage(ImageResource.CURSOS_ALTERNATIVE_ENABLED);
		cursors.put(CanvasResource.ALTERNATIVE, toolkit.createCustomCursor(image, new Point(0, 0), "Alternative"));

		image = toolkit.getImage(ImageResource.CURSOS_LABEL_ENABLED);
		cursors.put(CanvasResource.LABEL, toolkit.createCustomCursor(image, new Point(0, 0), "Label"));

		image = toolkit.getImage(ImageResource.CURSOS_START_ENABLED);
		cursors.put(CanvasResource.START, toolkit.createCustomCursor(image, new Point(0, 0), "Start"));
	}

	private Widget createGridWidget()
	{
		final GridWidget widget = new GridWidget(this);
		backgroundLayer.addChild(widget);
		setShowingGrid(true);
		canvasState.getPreferences().setShowGrid(true);
		return widget;
	}

	private Widget createLineWidget()
	{
		final LineWidget widget = new LineWidget(this);
		backgroundLayer.addChild(widget);
		setShowingLines(true);
		canvasState.getPreferences().setShowLines(true);
		return widget;
	}

	private Widget createNodeWidget(final String node, final String activeTool)
	{
		Widget widget = null;
		if (activeTool.equals(CanvasResource.N_TERMINAL) || activeTool.equals(CanvasResource.TERMINAL) || activeTool.equals(CanvasResource.LEFT_SIDE) || activeTool.equals(CanvasResource.LAMBDA) || activeTool.equals(CanvasResource.START))
		{

			widget = connectorFactory.drawIcon(activeTool, this, node);
			widget.createActions(CanvasResource.SELECT);
			widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.SELECT));
			widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.MOVE));
			widget.createActions(CanvasResource.SUCCESSOR).addAction(actionFactory.getAction(ExtendedActionFactory.SUCCESSOR));

			if (!activeTool.equals(CanvasResource.LAMBDA))
			{
				widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.NODE_HOVER));
				widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.EDITOR));
				widget.createActions(CanvasResource.ALTERNATIVE).addAction(actionFactory.getAction(ExtendedActionFactory.ALTERNATIVE));
			}
			mainLayer.addChild(widget);
			appendNode(node, activeTool);
		}
		else if (activeTool.equals(CanvasResource.LABEL))
		{
			widget = new LabelWidgetExt(mainLayer.getScene(), "Double Click Here to Edit");
			widget.createActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.LABEL_HOVER));
			widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.SELECT_LABEL));
			widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.EDITOR));
			widget.getActions(CanvasResource.SELECT).addAction(actionFactory.getAction(ExtendedActionFactory.MOVE));
			labels.append(node);
			mainLayer.addChild(widget);
		}
		if (widget != null)
		{
			if (widget instanceof TypedWidget)
			{
				((TypedWidget) widget).setType(activeTool);
			}
		}
		return widget;
	}

	@Override
	public JComponent createView()
	{
		final JComponent component = super.createView();
		component.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(final MouseEvent e)
			{
				setFocused();
			}
		});
		return component;
	}

	public ExtendedActionFactory getActionFactory()
	{
		return actionFactory;
	}

	public Router getActiveRouter()
	{
		return activeRouter;
	}

	public ExtendedList<String> getAlternatives()
	{
		return alternatives;
	}

	public LayerWidget getBackgroundLayer()
	{
		return backgroundLayer;
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

	public State getCanvasState()
	{
		return canvasState;
	}

	public StateHistory getCanvasStateHistory()
	{
		return canvasStateHistory;
	}

	public LayerWidget getConnectionLayer()
	{
		return connectionLayer;
	}

	public String getConnectionStrategy()
	{
		return connectionStrategy;
	}

	public ConnectorFactory getConnectorFactory()
	{
		return connectorFactory;
	}

	public String getFile()
	{
		return canvasState.getFullFileName();
	}

	public LayerWidget getInterractionLayer()
	{
		return interractionLayer;
	}

	public Collection<?> getLabels()
	{
		return labels.getAll();
	}

	public ExtendedList<String> getLambdas()
	{
		return lambdas;
	}

	public ExtendedList<String> getLeftSides()
	{
		return leftSides;
	}

	public LayerWidget getMainLayer()
	{
		return mainLayer;
	}

	public PropertyChangeSupport getMonitor()
	{
		return monitor;
	}

	public String getMoveStrategy()
	{
		return moveStrategy;
	}

	public String getNodeType(final Object node)
	{
		final Widget w = findWidget(node);
		if (w != null)
		{
			if (w instanceof LabelWidgetExt) { return ((LabelWidgetExt) w).getType(); }
		}
		return null;
	}

	public ExtendedList<String> getNterminals()
	{
		return nterminals;
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
		return start;
	}

	public ExtendedList<String> getSuccessors()
	{
		return successors;
	}

	public ExtendedList<String> getTerminals()
	{
		return terminals;
	}

	public boolean isAlternative(final String edge)
	{
		return alternatives.contains(edge);
	}

	public boolean isLabel(final Object o)
	{
		return labels.contains((String) o);
	}

	public boolean isLambda(final Object o)
	{
		return lambdas.contains((String) o);
	}

	public boolean isLeftSide(final Object o)
	{
		return leftSides.contains((String) o);
	}

	@Override
	public boolean isNode(final Object o)
	{
		if (!isLabel(o) && (isTerminal(o) || isNonTerminal(o) || isLambda(o) || isLeftSide(o) || isStart(o))) { return super.isNode(o); }
		return false;
	}

	public boolean isNonTerminal(final Object o)
	{
		return nterminals.contains((String) o);
	}

	public boolean isShowingGrid()
	{
		return showingGrid;
	}

	public boolean isShowingLines()
	{
		return showingLines;
	}

	public boolean isStart(final Object o)
	{
		return start.contains((String) o);
	}

	public boolean isSuccessor(final String edge)
	{
		return successors.contains(edge);
	}

	public boolean isTerminal(final Object o)
	{
		return terminals.contains((String) o);
	}

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
	public void propertyChange(final PropertyChangeEvent event)
	{
		if (event.getSource() instanceof StateHistory)
		{
			switch (event.getPropertyName())
			{
				case "object_state":
					canvasState = (State) event.getNewValue();
					updateState(canvasState);
					this.revalidate();
					break;

				case "writing":
					canvasState.refresh(this);
					break;
			}
		}
		monitor.firePropertyChange(event);
	}

	public void removeEdgeSafely(final String edge)
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

	public void removeNodeSafely(final String node)
	{
		if (isLabel(node))
		{
			labels.remove(node);
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

	public void select(final String target)
	{
		final String[] array = target.split("\\|");
		if (array.length > 1)
		{
			if (array[0].equals("Id"))
			{
				selectById(array[1]);
			}
			else
			{
				selectByLabel(array[1]);
			}
		}
		else
		{
			selectById(target);
		}

	}

	public void selectById(final String label)
	{
		setFocused();
		final Set<String> selectedObjects = new HashSet<String>();
		for (final String node : getNodes())
		{
			final Widget widget = findWidget(node);
			if (widget instanceof LabelWidgetExt)
			{
				if (node.equals(label))
				{
					selectedObjects.add(node);
					setFocusedObject(node);
					widget.setBackground(Color.BLUE);
					widget.setForeground(Color.WHITE);
					continue;
				}
			}
			widget.setBackground(Color.WHITE);
			widget.setForeground(Color.BLACK);
		}
		setSelectedObjects(selectedObjects);
	}

	public void selectByLabel(final String label)
	{
		setFocused();
		final Set<String> selectedObjects = new HashSet<String>();
		for (final String node : getNodes())
		{
			final Widget widget = findWidget(node);
			if (widget instanceof LabelWidgetExt)
			{
				final LabelWidgetExt labelWidgetExt = (LabelWidgetExt) widget;
				if (labelWidgetExt.getLabel().equals(label))
				{
					selectedObjects.add(node);
					setFocusedObject(node);
					widget.setBackground(Color.BLUE);
					widget.setForeground(Color.WHITE);
					continue;
				}
			}
			widget.setBackground(Color.WHITE);
			widget.setForeground(Color.BLACK);
		}
		setSelectedObjects(selectedObjects);
	}

	@Override
	public void setActiveTool(final String activeTool)
	{
		super.setActiveTool(activeTool);
		if (activeTool.equals(CanvasResource.SELECT))
		{
			setCursor(Cursor.getDefaultCursor());
		}
		else
		{
			setCursor(cursors.get(activeTool));
		}
	}

	public void setConnectionStrategy(final String strategy)
	{
		if (strategy != null)
		{
			canvasState.getPreferences().setConnectionStrategy(strategy);
			if (strategy.equals(CanvasResource.R_ORTHOGONAL) || strategy.equals(CanvasResource.R_FREE) || strategy.equals(CanvasResource.R_DIRECT))
			{
				if (!strategy.equals(connectionStrategy) || activeRouter == null)
				{
					connectionStrategy = strategy;
					if (strategy.equals(CanvasResource.R_ORTHOGONAL))
					{
						activeRouter = RouterFactory.createOrthogonalSearchRouter(mainLayer);
					}
					else if (strategy.equals(CanvasResource.R_DIRECT))
					{
						activeRouter = RouterFactory.createDirectRouter();
					}
					else if (strategy.equals(CanvasResource.R_FREE))
					{
						activeRouter = RouterFactory.createFreeRouter();
					}
				}
			}
		}
	}

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
					public void keyPressed(final KeyEvent e)
					{
						for (final KeyListener keyListener : getView().getKeyListeners())
						{
							keyListener.keyPressed(e);
						}
					}

					@Override
					public void keyReleased(final KeyEvent e)
					{
						for (final KeyListener keyListener : getView().getKeyListeners())
						{
							keyListener.keyReleased(e);
						}
					}

					@Override
					public void keyTyped(final KeyEvent e)
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

	public void setMoveStrategy(final String strategy)
	{
		final MoveTracker moveTracker = new MoveTracker(this);
		if (strategy != null)
		{
			canvasState.getPreferences().setMoveStrategy(strategy);
			if (strategy.equals(CanvasResource.M_ALIGN) || strategy.equals(CanvasResource.M_SNAP) || strategy.equals(CanvasResource.M_FREE) || strategy.equals(CanvasResource.M_LINES))
			{
				moveStrategy = strategy;
				moveTracker.notifyObservers(strategy);
			}
		}
	}

	public void setShowingGrid(final boolean showingGrid)
	{
		this.showingGrid = showingGrid;
		getCanvasState().getPreferences().setShowGrid(showingGrid);
	}

	public void setShowingLines(final boolean showingLines)
	{
		this.showingLines = showingLines;
		getCanvasState().getPreferences().setShowLines(showingLines);
	}

	public void updateState(final State state)
	{
		clearWidgets();
		canvasState = state;
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
			connectionWidget.setRouter(activeRouter);
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

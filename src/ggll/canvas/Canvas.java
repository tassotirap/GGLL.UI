package ggll.canvas;

import ggll.canvas.action.WidgetActionFactory;
import ggll.canvas.provider.GridProvider;
import ggll.canvas.provider.LineProvider;
import ggll.canvas.provider.NodeSelectProvider;
import ggll.canvas.state.CanvasState;
import ggll.canvas.state.Connection;
import ggll.canvas.state.Node;
import ggll.canvas.state.StaticStateManager;
import ggll.canvas.state.VolatileStateManager;
import ggll.canvas.widget.GridWidget;
import ggll.canvas.widget.LabelWidgetExt;
import ggll.canvas.widget.LineWidget;
import ggll.canvas.widget.MarkedWidget;
import ggll.canvas.widget.TypedWidget;
import ggll.resource.CanvasResource;
import ggll.util.Log;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.Collection;

import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

public class Canvas extends AbstractCanvas
{
	private static int defaultBufferCapacity = 20;

	private Router activeRouter;

	private LayerWidget backgroundLayer = new LayerWidget(this);
	private LayerWidget connectionLayer = new LayerWidget(this);
	private LayerWidget interractionLayer = new LayerWidget(this);
	private LayerWidget mainLayer = new LayerWidget(this);

	private String connStrategy;
	private String cursor;
	private String moveStrategy;

	private CanvasState state;

	public Canvas(String cursor, String connectionStrategy, String movementStrategy, String file)
	{
		super(cursor, connectionStrategy, movementStrategy, file);
		try
		{
			this.connStrategy = connectionStrategy;
			this.moveStrategy = movementStrategy;
			this.cursor = cursor;

			this.staticStateManager = new StaticStateManager();
			this.staticStateManager.setFile(new File(file));
			Object oState = staticStateManager.read();
			if (oState == null || !(oState instanceof CanvasState))
			{
				state = new CanvasState(file);
			}
			else
			{
				state = (CanvasState) oState;
			}
			this.staticStateManager.setObject(state);
			this.volatileStateManager = new VolatileStateManager(state, defaultBufferCapacity);
			this.volatileStateManager.init();
			this.volatileStateManager.getMonitor().addPropertyChangeListener("object_state", this);
			this.volatileStateManager.getMonitor().addPropertyChangeListener("writing", this);
			this.volatileStateManager.getMonitor().addPropertyChangeListener("undoable", this);

			this.monitor = new PropertyChangeSupport(this);
			this.monitor.addPropertyChangeListener("object_state", this);
			this.monitor.addPropertyChangeListener("writing", state);
			this.init();
			this.updateState(state);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void attachEdgeSourceAnchor(String edge, String oldSourceNode, String sourceNode)
	{
		Widget widget = sourceNode != null ? findWidget(sourceNode) : null;
		ConnectionWidget conn = ((ConnectionWidget) findWidget(edge));
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
		Widget widget = targetNode != null ? findWidget(targetNode) : null;
		ConnectionWidget conn = ((ConnectionWidget) findWidget(edge));
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
		String activeTool = getCanvasActiveTool();

		if (activeTool.equals(CanvasResource.SUCCESSOR) || activeTool.equals(CanvasResource.ALTERNATIVE))
		{
			connection = decorator.drawConnection(activeTool, this, edge);
			connection.setRouter(getActiveRouter());
			connection.createActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.CONN_SELECT, this));
			connection.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.RECONNECT, this));

			connection.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.FREE_MOVE_CP, this));
			connection.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.ADD_REMOVE_CP, this));
			connectionLayer.addChild(connection);
			if (activeTool.equals(CanvasResource.SUCCESSOR))
			{
				if (!isSuccessor(edge))
				{
					getSuccessors().add(edge);
				}
			}
			else if (activeTool.equals(CanvasResource.ALTERNATIVE))
			{
				if (!isAlternative(edge))
				{
					getAlternatives().add(edge);
				}
			}
		}
		return connection;
	}

	@Override
	protected Widget attachNodeWidget(String node)
	{
		Widget widget = null;
		String activeTool = getCanvasActiveTool();

		if (node.startsWith(LineWidget.class.getCanonicalName()))
		{
			LineWidget lWidget = new LineWidget(this);
			backgroundLayer.addChild(lWidget);
			widget = lWidget;
			setShowingLines(true);
			state.getPreferences().setShowLines(true);
		}
		else if (node.startsWith(GridWidget.class.getCanonicalName()))
		{
			GridWidget gWidget = new GridWidget(this);
			backgroundLayer.addChild(gWidget);
			widget = gWidget;
			setShowingGrid(true);
			state.getPreferences().setShowGrid(true);
		}
		else if (!activeTool.equals(CanvasResource.SELECT))
		{
			String tool = activeTool;
			if (tool.equals(CanvasResource.N_TERMINAL) || tool.equals(CanvasResource.TERMINAL) || tool.equals(CanvasResource.LEFT_SIDE) || tool.equals(CanvasResource.LAMBDA) || tool.equals(CanvasResource.START))
			{
				try
				{
					widget = decorator.drawIcon(activeTool, this, node);
				}
				catch (Exception e)
				{
					Log.log(Log.ERROR, this, "Could not create widget!", e);
					widget = new LabelWidgetExt(mainLayer.getScene(), node);
				}
				widget.createActions(CanvasResource.SELECT);
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.NODE_HOVER, this));
				}
				widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.SELECT, this));
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.EDITOR, this));
				}
				widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.MOVE, this));
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.createActions(CanvasResource.SUCCESSOR).addAction(WidgetActionFactory.getAction(WidgetActionFactory.SUCCESSOR, this));
				}
				if (!tool.equals(CanvasResource.LAMBDA))
				{
					widget.createActions(CanvasResource.ALTERNATIVE).addAction(WidgetActionFactory.getAction(WidgetActionFactory.ALTERNATIVE, this));
				}
				mainLayer.addChild(widget);
				if (tool.equals(CanvasResource.LEFT_SIDE))
				{
					leftSides.add(node);
				}
				else if (tool.equals(CanvasResource.TERMINAL))
				{
					terminals.add(node);
				}
				else if (tool.equals(CanvasResource.N_TERMINAL))
				{
					nterminals.add(node);
				}
				else if (tool.equals(CanvasResource.LAMBDA))
				{
					lambdas.add(node);
				}
				else if (tool.equals(CanvasResource.START))
				{
					start.add(node);
				}
			}
			else if (tool.equals(CanvasResource.LABEL))
			{
				widget = new LabelWidgetExt(mainLayer.getScene(), "Double Click Here to Edit");
				widget.createActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.LABEL_HOVER, this));
				widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.SELECT_LABEL, this));
				widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.EDITOR, this));
				widget.getActions(CanvasResource.SELECT).addAction(WidgetActionFactory.getAction(WidgetActionFactory.MOVE, this));
				labels.add(node);
				mainLayer.addChild(widget);
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

	@Override
	public Router getActiveRouter()
	{
		return activeRouter;
	}

	@Override
	public LayerWidget getBackgroundLayer()
	{
		return backgroundLayer;
	}

	@Override
	public CanvasDecorator getCanvasDecorator()
	{
		return decorator;
	}

	@Override
	public CanvasState getCanvasState()
	{
		return state;
	}

	@Override
	public LayerWidget getConnectionLayer()
	{
		return connectionLayer;
	}

	@Override
	public String getConnStrategy()
	{
		return this.connStrategy;
	}

	@Override
	public LayerWidget getInterractionLayer()
	{
		return interractionLayer;
	}

	@Override
	public Collection<?> getLabels()
	{
		return labels;
	}

	@Override
	public LayerWidget getMainLayer()
	{
		return mainLayer;
	}

	@Override
	public String getMoveStrategy()
	{
		return moveStrategy;
	}

	@Override
	public String getNodeType(Object node)
	{
		Widget w = findWidget(node);
		if (w != null)
		{
			if (w instanceof LabelWidgetExt)
			{
				return ((LabelWidgetExt) w).getType();
			}
		}
		return null;
	}

	@Override
	public void init()
	{
		super.init();

		addChild(backgroundLayer);
		addChild(mainLayer);
		addChild(connectionLayer);
		addChild(interractionLayer);

		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.SELECT, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.SELECT_LABEL, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.CREATE, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.POPUP_MENU_MAIN, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.NODE_HOVER, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.LABEL_HOVER, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.MOUSE_CENTERED_ZOOM, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.PAN, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.RECTANGULAR_SELECT, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.DELETE, this));
		getActions().addAction(WidgetActionFactory.getAction(WidgetActionFactory.COPY_PASTE, this));
		setActiveTool(cursor);

		if (state.getPreferences().getConnectionStrategy() != null)
		{
			setConnStrategy(state.getPreferences().getConnectionStrategy());
		}
		else
		{
			setConnStrategy(connStrategy);
		}
		if (state.getPreferences().getMoveStrategy() != null)
		{
			setMoveStrategy(state.getPreferences().getMoveStrategy());
		}
		else
		{
			setMoveStrategy(moveStrategy);
		}
	}

	@Override
	public boolean isLabel(Object o)
	{
		return labels.contains(o);
	}

	public boolean isLambda(Object o)
	{
		return this.lambdas.contains(o);
	}

	public boolean isLeftSide(Object o)
	{
		return this.leftSides.contains(o);
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
		return this.nterminals.contains(o);
	}

	public boolean isStart(Object o)
	{
		return this.start.contains(o);
	}

	public boolean isTerminal(Object o)
	{
		return this.terminals.contains(o);
	}

	@Override
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

	@Override
	public void removeNodeSafely(String node)
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

	@Override
	public void select(Object object)
	{
		select(object, false);
	}

	@Override
	public void select(Object object, boolean invertSelection)
	{
		Widget w;
		if ((w = findWidget(object)) != null)
		{
			NodeSelectProvider nsp = new NodeSelectProvider(this);
			if (!getSelectedObjects().contains(w))
			{
				if (nsp.isSelectionAllowed(w, null, invertSelection))
					nsp.select(w, null, invertSelection);
				validate();
			}
		}
	}

	@Override
	public void setConnStrategy(String policy)
	{
		if (policy != null)
		{
			state.getPreferences().setConnectionStrategy(policy);
			if (policy.equals(CanvasResource.R_ORTHOGONAL) || policy.equals(CanvasResource.R_FREE) || policy.equals(CanvasResource.R_DIRECT))
			{
				if (!policy.equals(connStrategy) || activeRouter == null)
				{
					connStrategy = policy;
					if (policy.equals(CanvasResource.R_ORTHOGONAL))
					{
						activeRouter = RouterFactory.createOrthogonalSearchRouter(mainLayer);
					}
					else if (policy.equals(CanvasResource.R_DIRECT))
					{
						activeRouter = RouterFactory.createDirectRouter();
					}
					else if (policy.equals(CanvasResource.R_FREE))
					{
						activeRouter = RouterFactory.createFreeRouter();
					}
				}
			}
		}
	}

	@Override
	public void setMoveStrategy(String strategy)
	{
		MoveTracker moveTracker = new MoveTracker(this);
		if (strategy != null)
		{
			state.getPreferences().setMoveStrategy(strategy);
			if (strategy.equals(CanvasResource.M_ALIGN) || strategy.equals(CanvasResource.M_SNAP) || strategy.equals(CanvasResource.M_FREE) || strategy.equals(CanvasResource.M_LINES))
			{
				if (moveStrategy == null || !moveStrategy.equals(strategy))
				{
					moveStrategy = strategy;
					moveTracker.notifyObservers(strategy);
				}
			}
		}
	}
	
	private void clearWidgets()
	{
		for (String element : state.getNodes())
		{
			if (findWidget(element) != null)
			{
				removeNode(element);
			}
		}

		for (String element : state.getConnections())
		{
			if (findWidget(element) != null)
			{
				removeEdge(element);
			}
		}
	}

	@Override
	public void updateState(CanvasState state)
	{
		clearWidgets();
		this.state = state;
		setConnStrategy(state.getPreferences().getConnectionStrategy());
		setMoveStrategy(state.getPreferences().getMoveStrategy());

		for (String element : state.getNodes())
		{
			Node node = state.findNode(element);
			setActiveTool(state.getType(element));
			Widget widget = addNode(element);
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
				((MarkedWidget) widget).setMark(node.getMark());
			}
			if (widget instanceof TypedWidget)
			{
				((TypedWidget) widget).setType(node.getType());
			}
		}

		for (String element : state.getConnections())
		{
			Connection connection = state.findConnection(element);
			setActiveTool(state.getType(element));
			ConnectionWidget connectionWidget = (ConnectionWidget) addEdge(element);
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

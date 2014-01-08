package org.ggll.syntax.graph.adapter;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.util.EnumSet;

import javax.swing.JComponent;

import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.SyntaxGraph;
import org.ggll.syntax.graph.SyntaxGraphPopupMenu;
import org.ggll.syntax.graph.provider.AlignWithMultiMoveProvider;
import org.ggll.syntax.graph.provider.CanvasRectangularSelectProvider;
import org.ggll.syntax.graph.provider.FreeMoveControl;
import org.ggll.syntax.graph.provider.LabelHoverProvider;
import org.ggll.syntax.graph.provider.LabelSelectProvider;
import org.ggll.syntax.graph.provider.LineProvider;
import org.ggll.syntax.graph.provider.MultiMoveProvider;
import org.ggll.syntax.graph.provider.NodeConnectProvider;
import org.ggll.syntax.graph.provider.NodeHoverProvider;
import org.ggll.syntax.graph.provider.NodeMultiSelectProvider;
import org.ggll.syntax.graph.provider.NodeReconnectProvider;
import org.ggll.syntax.graph.provider.NodeSelectProvider;
import org.ggll.syntax.graph.provider.WidgetDeleteProvider;
import org.ggll.syntax.graph.strategy.SnapToLineMoveStrategy;
import org.ggll.syntax.graph.widget.GridWidget;
import org.ggll.syntax.graph.widget.LabelTextFieldEditor;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ReconnectDecorator;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.action.RectangularSelectProvider;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.action.ConnectAction;
import org.netbeans.modules.visual.action.DefaultRectangularSelectDecorator;
import org.netbeans.modules.visual.action.InplaceEditorAction;
import org.netbeans.modules.visual.action.MouseCenteredZoomAction;
import org.netbeans.modules.visual.action.MoveAction;
import org.netbeans.modules.visual.action.MoveControlPointAction;
import org.netbeans.modules.visual.action.PanAction;
import org.netbeans.modules.visual.action.PopupMenuAction;
import org.netbeans.modules.visual.action.ReconnectAction;
import org.netbeans.modules.visual.action.RectangularSelectAction;
import org.netbeans.modules.visual.action.SelectAction;
import org.netbeans.modules.visual.action.SingleLayerAlignWithWidgetCollector;
import org.netbeans.modules.visual.action.SnapToGridMoveStrategy;
import org.netbeans.modules.visual.action.TextFieldInplaceEditorProvider;
import org.netbeans.modules.visual.action.TwoStatedMouseHoverAction;

public class ExtendedActionFactory
{
	public final static String ADD_REMOVE_CP = "AddRemoveCP";
	public final static String ALTERNATIVE = "Alternative";
	public final static String CONN_SELECT = "ConnSelect";
	public final static String CREATE_NODE = "Create";
	public final static String DELETE = "Delete";
	public final static String EDITOR = "Editor";
	public final static String FREE_MOVE_CP = "FreeMoveCP";
	public final static String LABEL_HOVER = "LabelHover";
	public final static String MOUSE_CENTERED_ZOOM = "MouseCenteredZoom";
	public final static String MOVE = "Move";
	public final static String MOVE_ALIGN = "MoveAlign";
	public final static String MOVE_FREE = "MoveFree";
	public final static String MOVE_LINES = "MoveLines";
	public final static String MOVE_SNAP = "MoveSnap";
	public final static String MULTI_SELECT = "Multi Select";
	public final static String NODE_HOVER = "NodeHover";
	public final static String PAN = "Pan";
	public final static String POPUP_MENU_MAIN = "PopupMenuMain";
	public final static String RECONNECT = "Reconnect";
	public final static String RECTANGULAR_SELECT = "RectangularSelect";
	public final static String SELECT = "Select";
	public final static String SELECT_LABEL = "SelectLabel";
	public final static String STATIC_MOVE_FREE = "StaticMoveFree";
	public final static String SUCCESSOR = "Successor";

	private String activeMoveAction = null;
	private SyntaxGraph canvas = null;

	private final BasicStroke STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{ 6.0f, 3.0f }, 0.0f);

	public ExtendedActionFactory(SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}

	private WidgetAction createAlignWithMultiMoveAction(SyntaxGraph canvas, AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		final AlignWithMultiMoveProvider alignWithMultiMoveProvidersp = new AlignWithMultiMoveProvider(canvas, collector, interractionLayer, decorator, outerBounds);
		return createMoveAction(alignWithMultiMoveProvidersp, alignWithMultiMoveProvidersp);
	}

	private ConnectAction createConnectAction(ConnectDecorator decorator, LayerWidget interractionLayer, ConnectProvider provider)
	{
		return new ConnectAction(decorator != null ? decorator : createDefaultConnectDecorator(), interractionLayer, provider);
	}

	private AlignWithMoveDecorator createDefaultAlignWithMoveDecorator()
	{
		return new AlignWithMoveDecorator()
		{
			@Override
			public ConnectionWidget createLineWidget(Scene scene)
			{
				final ConnectionWidget widget = new ConnectionWidget(scene);
				widget.setStroke(ExtendedActionFactory.this.STROKE);
				widget.setForeground(Color.BLUE);
				return widget;
			}
		};
	}

	private ConnectDecorator createDefaultConnectDecorator()
	{
		return new ConnectDecorator()
		{
			@Override
			public ConnectionWidget createConnectionWidget(Scene scene)
			{
				final ConnectionWidget widget = new ConnectionWidget(scene);
				widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
				return widget;
			}

			@Override
			public Anchor createFloatAnchor(Point location)
			{
				return AnchorFactory.createFixedAnchor(location);
			}

			@Override
			public Anchor createSourceAnchor(Widget sourceWidget)
			{
				return AnchorFactory.createCenterAnchor(sourceWidget);
			}

			@Override
			public Anchor createTargetAnchor(Widget targetWidget)
			{
				return AnchorFactory.createCenterAnchor(targetWidget);
			}
		};
	}

	private RectangularSelectDecorator createDefaultRectangularSelectDecorator(Scene scene)
	{
		return new DefaultRectangularSelectDecorator(scene);
	}

	private DeleteAdapter createDeleteAction(WidgetDeleteProvider wdp)
	{
		return new DeleteAdapter(wdp);
	}

	private WidgetAction createEditorAction(TextFieldInplaceEditor editor)
	{
		return createInplaceEditorAction(editor, null);
	}

	private MoveStrategy createFreeMoveStrategy()
	{
		return new MoveStrategy()
		{
			@Override
			public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation)
			{
				return suggestedLocation;
			}
		};
	}

	private WidgetAction createHoverAction(TwoStateHoverProvider provider)
	{
		return new TwoStatedMouseHoverAction(provider);
	}

	private <T extends JComponent> WidgetAction createInplaceEditorAction(InplaceEditorProvider<T> provider)
	{
		return new InplaceEditorAction<T>(provider);
	}

	private WidgetAction createInplaceEditorAction(TextFieldInplaceEditor editor, EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections)
	{
		return createInplaceEditorAction(new TextFieldInplaceEditorProvider(editor, expansionDirections));
	}

	private WidgetAction createMouseCenteredZoomAction(double zoomMultiplier)
	{
		return new MouseCenteredZoomAction(zoomMultiplier);
	}

	private WidgetAction createMoveAction()
	{
		return createMoveAction(null, null);
	}

	private WidgetAction createMoveAction(MoveStrategy strategy, MoveProvider provider)
	{
		final MoveStrategy freeMoveStrategy = new MoveStrategy()
		{
			@Override
			public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation)
			{
				return suggestedLocation;
			}
		};

		final MoveProvider defaultMoveProvider = new MoveProvider()
		{
			@Override
			public Point getOriginalLocation(Widget widget)
			{
				return widget.getPreferredLocation();
			}

			@Override
			public void movementFinished(Widget widget)
			{
			}

			@Override
			public void movementStarted(Widget widget)
			{
			}

			@Override
			public void setNewLocation(Widget widget, Point location)
			{
				widget.setPreferredLocation(location);
			}
		};

		return new MoveAction(strategy != null ? strategy : freeMoveStrategy, provider != null ? provider : defaultMoveProvider);
	}

	private WidgetAction createPanAction()
	{
		return new PanAction();
	}

	private WidgetAction createPopupMenuAction(final PopupMenuProvider provider)
	{
		return new PopupMenuAction(provider);
	}

	private WidgetAction createReconnectAction(ReconnectDecorator decorator, ReconnectProvider provider)
	{
		final ReconnectDecorator defaultReconnectDecorator = new ReconnectDecorator()
		{
			@Override
			public Anchor createFloatAnchor(Point location)
			{
				return AnchorFactory.createFixedAnchor(location);
			}

			@Override
			public Anchor createReplacementWidgetAnchor(Widget replacementWidget)
			{
				return AnchorFactory.createCenterAnchor(replacementWidget);
			}
		};

		return new ReconnectAction(decorator != null ? decorator : defaultReconnectDecorator, provider);
	}

	private WidgetAction createReconnectAction(ReconnectProvider provider)
	{
		return createReconnectAction(null, provider);
	}

	private WidgetAction createRectangularSelectAction(RectangularSelectDecorator decorator, LayerWidget interractionLayer, RectangularSelectProvider provider)
	{
		return new RectangularSelectAction(decorator, interractionLayer, provider);
	}

	private WidgetAction createSelectAction(SelectProvider provider)
	{
		return new SelectAction(provider);
	}

	private MoveStrategy createSnapToGridMoveStrategy(int horizontalGridSize, int verticalGridSize)
	{
		return new SnapToGridMoveStrategy(horizontalGridSize, verticalGridSize);
	}

	private MoveStrategy createSnapToLineMoveStrategy(SyntaxGraph canvas)
	{
		return new SnapToLineMoveStrategy(LineProvider.getInstance(canvas));
	}

	public WidgetAction getAction(String action)
	{
		switch (action)
		{
			case MOVE:
				if (this.activeMoveAction != null)
				{
					switch (this.activeMoveAction)
					{
						case CanvasResource.M_FREE:
							return getAction(MOVE_FREE);
						case CanvasResource.M_SNAP:
							return getAction(MOVE_SNAP);
						case CanvasResource.M_ALIGN:
							return getAction(MOVE_ALIGN);
						case CanvasResource.M_LINES:
							return getAction(MOVE_LINES);
					}
				}
				return getAction(MOVE_FREE);
			case MOVE_FREE:
				return createMoveAction(createFreeMoveStrategy(), new MultiMoveProvider(this.canvas));
			case MOVE_SNAP:
				return createMoveAction(createSnapToGridMoveStrategy(GridWidget.GRID_SIZE, GridWidget.GRID_SIZE), new MultiMoveProvider(this.canvas));
			case MOVE_ALIGN:
				return createAlignWithMultiMoveAction(this.canvas, new SingleLayerAlignWithWidgetCollector(this.canvas.getMainLayer(), true), this.canvas.getInterractionLayer(), createDefaultAlignWithMoveDecorator(), true);
			case MOVE_LINES:
				return createMoveAction(createSnapToLineMoveStrategy(this.canvas), new MultiMoveProvider(this.canvas));
			case CREATE_NODE:
				return new NodeCreateAction(this.canvas);
			case SELECT:
				return createSelectAction(new NodeSelectProvider(this.canvas));
			case MULTI_SELECT:
				return createSelectAction(new NodeMultiSelectProvider(this.canvas));
			case NODE_HOVER:
				return createHoverAction(new NodeHoverProvider(this.canvas));
			case ALTERNATIVE:
				return createConnectAction(this.canvas.getCanvasDecorator().getConnectDecoratorAlternative(), this.canvas.getInterractionLayer(), new NodeConnectProvider(this.canvas));
			case SUCCESSOR:
				return createConnectAction(this.canvas.getCanvasDecorator().getConnectDecoratorSuccessor(), this.canvas.getInterractionLayer(), new NodeConnectProvider(this.canvas));
			case RECONNECT:
				return createReconnectAction(new NodeReconnectProvider(this.canvas));
			case EDITOR:
				return createEditorAction(new LabelTextFieldEditor(this.canvas));
			case POPUP_MENU_MAIN:
				return createPopupMenuAction(new SyntaxGraphPopupMenu(this.canvas));
			case RECTANGULAR_SELECT:
				return createRectangularSelectAction(createDefaultRectangularSelectDecorator(this.canvas), this.canvas.getBackgroundLayer(), new CanvasRectangularSelectProvider(this.canvas));
			case MOUSE_CENTERED_ZOOM:
				return createMouseCenteredZoomAction(1.05);
			case PAN:
				return createPanAction();
			case CONN_SELECT:
				return this.canvas.createSelectAction();
			case FREE_MOVE_CP:
				return new MoveControlPointAction(new FreeMoveControl(this.canvas), RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED);
			case ADD_REMOVE_CP:
				return ActionFactory.createAddRemoveControlPointAction();
			case SELECT_LABEL:
				return createSelectAction(new LabelSelectProvider(this.canvas));
			case LABEL_HOVER:
				return createHoverAction(new LabelHoverProvider(this.canvas));
			case STATIC_MOVE_FREE:
				return createMoveAction();
			case DELETE:
				return createDeleteAction(new WidgetDeleteProvider(this.canvas));
			default:
				return null;

		}
	}

	public void setActiveMoveAction(String activeMoveAction)
	{
		this.activeMoveAction = activeMoveAction;
	}
}
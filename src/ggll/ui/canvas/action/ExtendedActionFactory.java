package ggll.ui.canvas.action;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.canvas.CanvasPopupMenu;
import ggll.ui.canvas.SnapToLineMoveStrategy;
import ggll.ui.canvas.provider.AlignWithMultiMoveProvider;
import ggll.ui.canvas.provider.CanvasRectangularSelectProvider;
import ggll.ui.canvas.provider.FreeMoveControl;
import ggll.ui.canvas.provider.LabelHoverProvider;
import ggll.ui.canvas.provider.LabelSelectProvider;
import ggll.ui.canvas.provider.LineProvider;
import ggll.ui.canvas.provider.MultiMoveProvider;
import ggll.ui.canvas.provider.NodeConnectProvider;
import ggll.ui.canvas.provider.NodeHoverProvider;
import ggll.ui.canvas.provider.NodeMultiSelectProvider;
import ggll.ui.canvas.provider.NodeReconnectProvider;
import ggll.ui.canvas.provider.NodeSelectProvider;
import ggll.ui.canvas.provider.WidgetCopyPasteProvider;
import ggll.ui.canvas.provider.WidgetDeleteProvider;
import ggll.ui.canvas.widget.GridWidget;
import ggll.ui.canvas.widget.LabelTextFieldEditor;
import ggll.ui.resource.CanvasResource;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.util.EnumSet;

import javax.swing.JComponent;

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
	public final static String COPY_PASTE = "CopyPaste";
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
	private AbstractCanvas canvas = null;

	private final BasicStroke STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{ 6.0f, 3.0f }, 0.0f);

	public ExtendedActionFactory(AbstractCanvas canvas)
	{
		this.canvas = canvas;
	}

	private WidgetAction createAlignWithMultiMoveAction(AbstractCanvas canvas, AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		AlignWithMultiMoveProvider alignWithMultiMoveProvidersp = new AlignWithMultiMoveProvider(canvas, collector, interractionLayer, decorator, outerBounds);
		return createMoveAction(alignWithMultiMoveProvidersp, alignWithMultiMoveProvidersp);
	}

	private ConnectAction createConnectAction(ConnectDecorator decorator, LayerWidget interractionLayer, ConnectProvider provider)
	{
		return new ConnectAction(decorator != null ? decorator : createDefaultConnectDecorator(), interractionLayer, provider);
	}

	private CopyPasteAction createCopyPasteAction(WidgetCopyPasteProvider wcpp)
	{
		return new CopyPasteAction(wcpp);
	}

	private AlignWithMoveDecorator createDefaultAlignWithMoveDecorator()
	{
		return new AlignWithMoveDecorator()
		{
			@Override
			public ConnectionWidget createLineWidget(Scene scene)
			{
				ConnectionWidget widget = new ConnectionWidget(scene);
				widget.setStroke(STROKE);
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
				ConnectionWidget widget = new ConnectionWidget(scene);
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

	private DeleteAction createDeleteAction(WidgetDeleteProvider wdp)
	{
		return new DeleteAction(wdp);
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

	private WidgetAction createEditorAction(TextFieldInplaceEditor editor)
	{
		return createInplaceEditorAction(editor, null);
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
		MoveStrategy freeMoveStrategy = new MoveStrategy()
		{
			@Override
			public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation)
			{
				return suggestedLocation;
			}
		};

		MoveProvider defaultMoveProvider = new MoveProvider()
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
		ReconnectDecorator defaultReconnectDecorator = new ReconnectDecorator()
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

	private MoveStrategy createSnapToLineMoveStrategy(AbstractCanvas canvas)
	{
		return new SnapToLineMoveStrategy(LineProvider.getInstance(canvas));
	}

	public WidgetAction getAction(String action)
	{
		switch (action)
		{
			case MOVE:
				if (activeMoveAction != null)
				{
					switch (activeMoveAction)
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
				return createMoveAction(createFreeMoveStrategy(), new MultiMoveProvider(canvas));
			case MOVE_SNAP:
				return createMoveAction(createSnapToGridMoveStrategy(GridWidget.GRID_SIZE, GridWidget.GRID_SIZE), new MultiMoveProvider(canvas));
			case MOVE_ALIGN:
				return createAlignWithMultiMoveAction(canvas, new SingleLayerAlignWithWidgetCollector(canvas.getMainLayer(), true), canvas.getInterractionLayer(), createDefaultAlignWithMoveDecorator(), true);
			case MOVE_LINES:
				return createMoveAction(createSnapToLineMoveStrategy(canvas), new MultiMoveProvider(canvas));
			case CREATE_NODE:
				return new NodeCreateAction(canvas);
			case SELECT:
				return createSelectAction(new NodeSelectProvider(canvas));
			case MULTI_SELECT:
				return createSelectAction(new NodeMultiSelectProvider(canvas));
			case NODE_HOVER:
				return createHoverAction(new NodeHoverProvider(canvas));
			case ALTERNATIVE:
				return createConnectAction(canvas.getCanvasDecorator().getConnDecoratorAlt(), canvas.getInterractionLayer(), new NodeConnectProvider(canvas));
			case SUCCESSOR:
				return createConnectAction(canvas.getCanvasDecorator().getConnDecoratorSuc(), canvas.getInterractionLayer(), new NodeConnectProvider(canvas));
			case RECONNECT:
				return createReconnectAction(new NodeReconnectProvider(canvas));
			case EDITOR:
				return createEditorAction(new LabelTextFieldEditor(canvas));
			case POPUP_MENU_MAIN:
				return createPopupMenuAction(new CanvasPopupMenu(canvas));
			case RECTANGULAR_SELECT:
				return createRectangularSelectAction(createDefaultRectangularSelectDecorator(canvas), canvas.getBackgroundLayer(), new CanvasRectangularSelectProvider(canvas));
			case MOUSE_CENTERED_ZOOM:
				return createMouseCenteredZoomAction(1.05);
			case PAN:
				return createPanAction();
			case CONN_SELECT:
				return canvas.createSelectAction();
			case FREE_MOVE_CP:
				return new MoveControlPointAction(new FreeMoveControl(canvas), RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED);
			case ADD_REMOVE_CP:
				return ActionFactory.createAddRemoveControlPointAction();
			case SELECT_LABEL:
				return createSelectAction(new LabelSelectProvider(canvas));
			case LABEL_HOVER:
				return createHoverAction(new LabelHoverProvider(canvas));
			case STATIC_MOVE_FREE:
				return createMoveAction();
			case DELETE:
				return createDeleteAction(new WidgetDeleteProvider(canvas));
			case COPY_PASTE:
				return createCopyPasteAction(new WidgetCopyPasteProvider(canvas));
			default:
				return null;

		}
	}

	public void setActiveMoveAction(String activeMoveAction)
	{
		this.activeMoveAction = activeMoveAction;
	}
}

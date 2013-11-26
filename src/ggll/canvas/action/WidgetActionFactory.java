package ggll.canvas.action;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.CanvasPopupMenu;
import ggll.canvas.provider.CanvasRectangularSelectProvider;
import ggll.canvas.provider.FreeMoveControl;
import ggll.canvas.provider.LabelHoverProvider;
import ggll.canvas.provider.LabelSelectProvider;
import ggll.canvas.provider.MultiMoveProvider;
import ggll.canvas.provider.NodeConnectProvider;
import ggll.canvas.provider.NodeHoverProvider;
import ggll.canvas.provider.NodeMultiSelectProvider;
import ggll.canvas.provider.NodeReconnectProvider;
import ggll.canvas.provider.NodeSelectProvider;
import ggll.canvas.provider.WidgetCopyPasteProvider;
import ggll.canvas.provider.WidgetDeleteProvider;
import ggll.canvas.widget.GridWidget;
import ggll.canvas.widget.LabelTextFieldEditor;
import ggll.resource.CanvasResource;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.modules.visual.action.MoveControlPointAction;
import org.netbeans.modules.visual.action.SingleLayerAlignWithWidgetCollector;

public class WidgetActionFactory
{
	public final static String ADD_REMOVE_CP = "AddRemoveCP";
	public final static String ALTERNATIVE = "Alternative";
	public final static String CONN_SELECT = "ConnSelect";
	public final static String COPY_PASTE = "CopyPaste";
	public final static String CREATE = "Create";
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

	private static String activeMoveAction = null;

	private WidgetActionFactory()
	{
	}


	public static WidgetAction getAction(String action, AbstractCanvas canvas)
	{	
		ActionFactory actionFactory = new ActionFactory();		
		switch (action)
		{
			case MOVE:
				if (activeMoveAction != null)
				{
					switch (activeMoveAction)
					{
						case CanvasResource.M_FREE:
							return getAction(MOVE_FREE, canvas);
						case CanvasResource.M_SNAP:
							return getAction(MOVE_SNAP, canvas);
						case CanvasResource.M_ALIGN:
							return getAction(MOVE_ALIGN, canvas);
						case CanvasResource.M_LINES:
							return getAction(MOVE_LINES, canvas);
					}
				}
				return getAction(MOVE_FREE, canvas);			
			case MOVE_FREE:
				return actionFactory.createMoveAction(actionFactory.createFreeMoveStrategy(), new MultiMoveProvider(canvas));
			case MOVE_SNAP:
				return actionFactory.createMoveAction(actionFactory.createSnapToGridMoveStrategy(GridWidget.GRID_SIZE, GridWidget.GRID_SIZE), new MultiMoveProvider(canvas));
			case MOVE_ALIGN:
				return actionFactory.createAlignWithMultiMoveAction(canvas, new SingleLayerAlignWithWidgetCollector(canvas.getMainLayer(), true), canvas.getInterractionLayer(), actionFactory.createDefaultAlignWithMoveDecorator(), true);
			case MOVE_LINES:
				return actionFactory.createMoveAction(actionFactory.createSnapToLineMoveStrategy(canvas), new MultiMoveProvider(canvas));
			case CREATE:
				return new NodeCreateAction(canvas);
			case SELECT:
				return actionFactory.createSelectAction(new NodeSelectProvider(canvas));
			case MULTI_SELECT:
				return actionFactory.createSelectAction(new NodeMultiSelectProvider(canvas));
			case NODE_HOVER:
				return actionFactory.createHoverAction(new NodeHoverProvider(canvas));
			case ALTERNATIVE:
				return actionFactory.createConnectAction(canvas.getCanvasDecorator().getConnDecoratorAlt(), canvas.getInterractionLayer(), new NodeConnectProvider(canvas));
			case SUCCESSOR:
				return actionFactory.createConnectAction(canvas.getCanvasDecorator().getConnDecoratorSuc(), canvas.getInterractionLayer(), new NodeConnectProvider(canvas));
			case RECONNECT:
				return actionFactory.createReconnectAction(new NodeReconnectProvider(canvas));
			case EDITOR:
				return actionFactory.createInplaceEditorAction(new LabelTextFieldEditor(canvas));
			case POPUP_MENU_MAIN:
				return actionFactory.createPopupMenuAction(new CanvasPopupMenu(canvas));
			case RECTANGULAR_SELECT:
				return actionFactory.createRectangularSelectAction(actionFactory.createDefaultRectangularSelectDecorator(canvas), canvas.getBackgroundLayer(), new CanvasRectangularSelectProvider(canvas));
			case MOUSE_CENTERED_ZOOM:
				return actionFactory.createMouseCenteredZoomAction(1.05);
			case PAN:
				return actionFactory.createPanAction();
			case CONN_SELECT:
				return canvas.createSelectAction();
			case FREE_MOVE_CP:
				return new MoveControlPointAction(new FreeMoveControl(canvas), RoutingPolicy.DISABLE_ROUTING_UNTIL_END_POINT_IS_MOVED);
			case ADD_REMOVE_CP:
				return org.netbeans.api.visual.action.ActionFactory.createAddRemoveControlPointAction();
			case SELECT_LABEL:
				return actionFactory.createSelectAction(new LabelSelectProvider(canvas));
			case LABEL_HOVER:
				return actionFactory.createHoverAction(new LabelHoverProvider(canvas));
			case STATIC_MOVE_FREE:
				return actionFactory.createMoveAction();
			case DELETE:
				return actionFactory.createDeleteAction(new WidgetDeleteProvider(canvas));
			case COPY_PASTE:
				return actionFactory.createCopyPasteAction(new WidgetCopyPasteProvider(canvas));
			default:
				return null;
				
		}
	}

	public static void setActiveMoveAction(String activeMoveAction)
	{
		WidgetActionFactory.activeMoveAction = activeMoveAction;
	}
}

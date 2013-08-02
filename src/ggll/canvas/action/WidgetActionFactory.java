package ggll.canvas.action;

import ggll.canvas.AbstractCanvas;
import ggll.canvas.CanvasPopupMenu;
import ggll.canvas.CanvasStrings;
import ggll.canvas.MoveTracker;
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

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.ConnectionWidget.RoutingPolicy;
import org.netbeans.modules.visual.action.MoveControlPointAction;
import org.netbeans.modules.visual.action.SingleLayerAlignWithWidgetCollector;

public class WidgetActionFactory implements Observer
{
	private static WidgetActionFactory instance = null;
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

	HashMap<String, WidgetAction> actions = new HashMap<String, WidgetAction>();

	private String activeMoveAction = null;

	private WidgetActionFactory()
	{
		actions.put(CREATE, null);
		actions.put(SELECT, null);
		actions.put(MULTI_SELECT, null);
		actions.put(NODE_HOVER, null);
		actions.put(ALTERNATIVE, null);
		actions.put(SUCCESSOR, null);
		actions.put(RECONNECT, null);
		actions.put(EDITOR, null);
		actions.put(MOVE, null);
		actions.put(MOVE_FREE, null);
		actions.put(MOVE_SNAP, null);
		actions.put(MOVE_ALIGN, null);
		actions.put(MOVE_LINES, null);
		actions.put(POPUP_MENU_MAIN, null);
		actions.put(RECTANGULAR_SELECT, null);
		actions.put(MOUSE_CENTERED_ZOOM, null);
		actions.put(PAN, null);
		actions.put(CONN_SELECT, null);
		actions.put(FREE_MOVE_CP, null);
		actions.put(ADD_REMOVE_CP, null);
		actions.put(SELECT_LABEL, null);
		actions.put(LABEL_HOVER, null);
		actions.put(STATIC_MOVE_FREE, null);
		actions.put(DELETE, null);
		actions.put(COPY_PASTE, null);
	}

	public static void dispose()
	{
		instance = null;
	}
	
	public static WidgetActionFactory getInstance()
	{
		if (instance == null)
		{
			instance = new WidgetActionFactory();
		}
		return instance;
	}

	public void clearAction(String action)
	{
		actions.put(action, null);
	}

	public WidgetAction getAction(String action, AbstractCanvas canvas)
	{
		if (action.equals(MOVE))
		{
			if (activeMoveAction != null)
			{
				WidgetAction moveAction = null;
				if (activeMoveAction.equals(CanvasStrings.M_FREE))
				{
					moveAction = getAction(MOVE_FREE, canvas);
				}
				else if (activeMoveAction.equals(CanvasStrings.M_SNAP))
				{
					moveAction = getAction(MOVE_SNAP, canvas);
				}
				else if (activeMoveAction.equals(CanvasStrings.M_ALIGN))
				{
					moveAction = getAction(MOVE_ALIGN, canvas);
				}
				else if (activeMoveAction.equals(CanvasStrings.M_LINES))
				{
					moveAction = getAction(MOVE_LINES, canvas);
				}
				return moveAction;
			}
			if (actions.get(MOVE) == null)
			{
				actions.put(MOVE, getAction(MOVE_FREE, canvas));
			}
			return actions.get(MOVE);
		}
		else if (action.equals(MOVE_FREE))
		{
			if (actions.get(MOVE_FREE) == null)
			{
				actions.put(MOVE_FREE, ActionFactory.createMoveAction(ActionFactory.createFreeMoveStrategy(), new MultiMoveProvider(canvas)));
			}
			return actions.get(MOVE_FREE);
		}
		else if (action.equals(MOVE_SNAP))
		{
			if (actions.get(MOVE_SNAP) == null)
			{
				actions.put(MOVE_SNAP, ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(GridWidget.GRID_SIZE, GridWidget.GRID_SIZE), new MultiMoveProvider(canvas)));
			}
			return actions.get(MOVE_SNAP);
		}
		else if (action.equals(MOVE_ALIGN))
		{
			if (actions.get(MOVE_ALIGN) == null)
			{
				actions.put(MOVE_ALIGN, ActionFactory.createAlignWithMultiMoveAction(canvas, new SingleLayerAlignWithWidgetCollector(canvas.getMainLayer(), true), canvas.getInterractionLayer(), ActionFactory.createDefaultAlignWithMoveDecorator(), true));
			}
			return actions.get(MOVE_ALIGN);
		}
		else if (action.equals(MOVE_LINES))
		{
			if (actions.get(MOVE_LINES) == null)
			{
				actions.put(MOVE_LINES, ActionFactory.createMoveAction(ActionFactory.createSnapToLineMoveStrategy(canvas), new MultiMoveProvider(canvas))); // true));
			}
			return actions.get(MOVE_LINES);
		}
		else if (action.equals(CREATE))
		{
			if (actions.get(CREATE) == null)
			{
				actions.put(CREATE, new NodeCreateAction(canvas));
			}
			return actions.get(CREATE);
		}
		else if (action.equals(SELECT))
		{
			if (actions.get(SELECT) == null)
			{
				actions.put(SELECT, ActionFactory.createSelectAction(new NodeSelectProvider(canvas)));
			}
			return actions.get(SELECT);
		}
		else if (action.equals(MULTI_SELECT))
		{
			if (actions.get(MULTI_SELECT) == null)
			{
				actions.put(MULTI_SELECT, ActionFactory.createSelectAction(new NodeMultiSelectProvider(canvas)));
			}
			return actions.get(MULTI_SELECT);
		}
		else if (action.equals(NODE_HOVER))
		{
			if (actions.get(NODE_HOVER) == null)
			{
				actions.put(NODE_HOVER, ActionFactory.createHoverAction(new NodeHoverProvider(canvas)));
			}
			return actions.get(NODE_HOVER);
		}
		else if (action.equals(ALTERNATIVE))
		{
			if (actions.get(ALTERNATIVE) == null)
			{
				actions.put(ALTERNATIVE, ActionFactory.createConnectAction(canvas.getCanvasDecorator().getConnDecoratorAlt(), canvas.getInterractionLayer(), new NodeConnectProvider(canvas)));
			}
			return actions.get(ALTERNATIVE);
		}
		else if (action.equals(SUCCESSOR))
		{
			if (actions.get(SUCCESSOR) == null)
			{
				actions.put(SUCCESSOR, ActionFactory.createConnectAction(canvas.getCanvasDecorator().getConnDecoratorSuc(), canvas.getInterractionLayer(), new NodeConnectProvider(canvas)));
			}
			return actions.get(SUCCESSOR);
		}
		else if (action.equals(RECONNECT))
		{
			if (actions.get(RECONNECT) == null)
			{
				actions.put(RECONNECT, ActionFactory.createReconnectAction(new NodeReconnectProvider(canvas)));
			}
			return actions.get(RECONNECT);
		}
		else if (action.equals(EDITOR))
		{
			if (actions.get(EDITOR) == null)
			{
				actions.put(EDITOR, ActionFactory.createInplaceEditorAction(new LabelTextFieldEditor(canvas)));
			}
			return actions.get(EDITOR);
		}
		else if (action.equals(POPUP_MENU_MAIN))
		{
			if (actions.get(POPUP_MENU_MAIN) == null)
			{
				actions.put(POPUP_MENU_MAIN, ActionFactory.createPopupMenuAction(new CanvasPopupMenu(canvas)));
			}
			return actions.get(POPUP_MENU_MAIN);
		}
		else if (action.equals(RECTANGULAR_SELECT))
		{
			if (actions.get(RECTANGULAR_SELECT) == null)
			{
				actions.put(RECTANGULAR_SELECT, ActionFactory.createRectangularSelectAction(ActionFactory.createDefaultRectangularSelectDecorator(canvas), canvas.getBackgroundLayer(), new CanvasRectangularSelectProvider(canvas)));
			}
			return actions.get(RECTANGULAR_SELECT);
		}
		else if (action.equals(MOUSE_CENTERED_ZOOM))
		{
			if (actions.get(MOUSE_CENTERED_ZOOM) == null)
			{
				actions.put(MOUSE_CENTERED_ZOOM, ActionFactory.createMouseCenteredZoomAction(1.05));
			}
			return actions.get(MOUSE_CENTERED_ZOOM);
		}
		else if (action.equals(PAN))
		{
			if (actions.get(PAN) == null)
			{
				actions.put(PAN, ActionFactory.createPanAction());
			}
			return actions.get(PAN);
		}
		else if (action.equals(CONN_SELECT))
		{
			if (actions.get(CONN_SELECT) == null)
			{
				actions.put(CONN_SELECT, canvas.createSelectAction());
			}
			return actions.get(CONN_SELECT);
		}
		else if (action.equals(FREE_MOVE_CP))
		{
			if (actions.get(FREE_MOVE_CP) == null)
			{
				actions.put(FREE_MOVE_CP, new MoveControlPointAction(new FreeMoveControl(), RoutingPolicy.DISABLE_ROUTING));
			}
			return actions.get(FREE_MOVE_CP);
		}
		else if (action.equals(ADD_REMOVE_CP))
		{
			if (actions.get(ADD_REMOVE_CP) == null)
			{
				actions.put(ADD_REMOVE_CP, org.netbeans.api.visual.action.ActionFactory.createAddRemoveControlPointAction());
			}
			return actions.get(ADD_REMOVE_CP);
		}
		else if (action.equals(SELECT_LABEL))
		{
			if (actions.get(SELECT_LABEL) == null)
			{
				actions.put(SELECT_LABEL, ActionFactory.createSelectAction(new LabelSelectProvider(canvas)));
			}
			return actions.get(SELECT_LABEL);
		}
		else if (action.equals(LABEL_HOVER))
		{
			if (actions.get(LABEL_HOVER) == null)
			{
				actions.put(LABEL_HOVER, ActionFactory.createHoverAction(new LabelHoverProvider(canvas)));
			}
			return actions.get(LABEL_HOVER);
		}
		else if (action.equals(STATIC_MOVE_FREE))
		{
			if (actions.get(STATIC_MOVE_FREE) == null)
			{
				actions.put(STATIC_MOVE_FREE, ActionFactory.createMoveAction());
			}
			return actions.get(STATIC_MOVE_FREE);
		}
		else if (action.equals(DELETE))
		{
			if (actions.get(DELETE) == null)
			{
				actions.put(DELETE, ActionFactory.createDeleteAction(new WidgetDeleteProvider(canvas)));
			}
			return actions.get(DELETE);
		}
		else if (action.equals(COPY_PASTE))
		{
			if (actions.get(COPY_PASTE) == null)
			{
				actions.put(COPY_PASTE, ActionFactory.createCopyPasteAction(new WidgetCopyPasteProvider(canvas)));
			}
			return actions.get(COPY_PASTE);
		}
		return null;
	}

	@Override
	public void update(Observable obs, Object obj)
	{
		if (obs instanceof MoveTracker)
		{
			activeMoveAction = (String) obj;
		}
	}
}
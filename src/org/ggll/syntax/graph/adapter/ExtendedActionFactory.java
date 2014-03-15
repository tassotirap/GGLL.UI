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
	
	private final BasicStroke STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]
	{ 6.0f, 3.0f }, 0.0f);
	
	public ExtendedActionFactory(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
	}
	
	private WidgetAction createAlignWithMultiMoveAction(final SyntaxGraph canvas, final AlignWithWidgetCollector collector, final LayerWidget interractionLayer, final AlignWithMoveDecorator decorator, final boolean outerBounds)
	{
		final AlignWithMultiMoveProvider alignWithMultiMoveProvidersp = new AlignWithMultiMoveProvider(canvas, collector, interractionLayer, decorator, outerBounds);
		return createMoveAction(alignWithMultiMoveProvidersp, alignWithMultiMoveProvidersp);
	}
	
	private ConnectAction createConnectAction(final ConnectDecorator decorator, final LayerWidget interractionLayer, final ConnectProvider provider)
	{
		return new ConnectAction(decorator != null ? decorator : createDefaultConnectDecorator(), interractionLayer, provider);
	}
	
	private AlignWithMoveDecorator createDefaultAlignWithMoveDecorator()
	{
		return new AlignWithMoveDecorator()
		{
			@Override
			public ConnectionWidget createLineWidget(final Scene scene)
			{
				final ConnectionWidget widget = new ConnectionWidget(scene);
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
			public ConnectionWidget createConnectionWidget(final Scene scene)
			{
				final ConnectionWidget widget = new ConnectionWidget(scene);
				widget.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
				return widget;
			}
			
			@Override
			public Anchor createFloatAnchor(final Point location)
			{
				return AnchorFactory.createFixedAnchor(location);
			}
			
			@Override
			public Anchor createSourceAnchor(final Widget sourceWidget)
			{
				return AnchorFactory.createCenterAnchor(sourceWidget);
			}
			
			@Override
			public Anchor createTargetAnchor(final Widget targetWidget)
			{
				return AnchorFactory.createCenterAnchor(targetWidget);
			}
		};
	}
	
	private DeleteAdapter createDeleteAction(final WidgetDeleteProvider wdp)
	{
		return new DeleteAdapter(wdp);
	}
	
	private WidgetAction createEditorAction(final TextFieldInplaceEditor editor)
	{
		return createInplaceEditorAction(editor, null);
	}
	
	private MoveStrategy createFreeMoveStrategy()
	{
		return new MoveStrategy()
		{
			@Override
			public Point locationSuggested(final Widget widget, final Point originalLocation, final Point suggestedLocation)
			{
				return suggestedLocation;
			}
		};
	}
	
	private WidgetAction createHoverAction(final TwoStateHoverProvider provider)
	{
		return new TwoStatedMouseHoverAction(provider);
	}
	
	private <T extends JComponent> WidgetAction createInplaceEditorAction(final InplaceEditorProvider<T> provider)
	{
		return new InplaceEditorAction<T>(provider);
	}
	
	private WidgetAction createInplaceEditorAction(final TextFieldInplaceEditor editor, final EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections)
	{
		return createInplaceEditorAction(new TextFieldInplaceEditorProvider(editor, expansionDirections));
	}
	
	private WidgetAction createMouseCenteredZoomAction(final double zoomMultiplier)
	{
		return new MouseCenteredZoomAction(zoomMultiplier);
	}
	
	private WidgetAction createMoveAction()
	{
		return createMoveAction(null, null);
	}
	
	private WidgetAction createMoveAction(final MoveStrategy strategy, final MoveProvider provider)
	{
		final MoveStrategy freeMoveStrategy = new MoveStrategy()
		{
			@Override
			public Point locationSuggested(final Widget widget, final Point originalLocation, final Point suggestedLocation)
			{
				return suggestedLocation;
			}
		};
		
		final MoveProvider defaultMoveProvider = new MoveProvider()
		{
			@Override
			public Point getOriginalLocation(final Widget widget)
			{
				return widget.getPreferredLocation();
			}
			
			@Override
			public void movementFinished(final Widget widget)
			{
			}
			
			@Override
			public void movementStarted(final Widget widget)
			{
			}
			
			@Override
			public void setNewLocation(final Widget widget, final Point location)
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
	
	private WidgetAction createReconnectAction(final ReconnectDecorator decorator, final ReconnectProvider provider)
	{
		final ReconnectDecorator defaultReconnectDecorator = new ReconnectDecorator()
		{
			@Override
			public Anchor createFloatAnchor(final Point location)
			{
				return AnchorFactory.createFixedAnchor(location);
			}
			
			@Override
			public Anchor createReplacementWidgetAnchor(final Widget replacementWidget)
			{
				return AnchorFactory.createCenterAnchor(replacementWidget);
			}
		};
		
		return new ReconnectAction(decorator != null ? decorator : defaultReconnectDecorator, provider);
	}
	
	private WidgetAction createReconnectAction(final ReconnectProvider provider)
	{
		return createReconnectAction(null, provider);
	}
	
	private WidgetAction createRectangularSelectAction(final RectangularSelectDecorator decorator, final LayerWidget interractionLayer, final RectangularSelectProvider provider)
	{
		return new RectangularSelectAction(decorator, interractionLayer, provider);
	}
	
	private WidgetAction createSelectAction(final SelectProvider provider)
	{
		return new SelectAction(provider);
	}
	
	private MoveStrategy createSnapToGridMoveStrategy(final int horizontalGridSize, final int verticalGridSize)
	{
		return new SnapToGridMoveStrategy(horizontalGridSize, verticalGridSize);
	}
	
	private MoveStrategy createSnapToLineMoveStrategy(final SyntaxGraph canvas)
	{
		return new SnapToLineMoveStrategy(LineProvider.getInstance(canvas));
	}
	
	public WidgetAction getAction(final String action)
	{
		switch (action)
		{
			case MOVE:
				if (activeMoveAction != null)
				{
					switch (activeMoveAction)
					{
						case CanvasResource.M_FREE:
							return getAction(ExtendedActionFactory.MOVE_FREE);
						case CanvasResource.M_SNAP:
							return getAction(ExtendedActionFactory.MOVE_SNAP);
						case CanvasResource.M_ALIGN:
							return getAction(ExtendedActionFactory.MOVE_ALIGN);
						case CanvasResource.M_LINES:
							return getAction(ExtendedActionFactory.MOVE_LINES);
					}
				}
				return getAction(ExtendedActionFactory.MOVE_FREE);
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
				return createConnectAction(canvas.getConnectorFactory().getConnector(CanvasResource.ALTERNATIVE), canvas.getInterractionLayer(), new NodeConnectProvider(canvas));
			case SUCCESSOR:
				return createConnectAction(canvas.getConnectorFactory().getConnector(CanvasResource.SUCCESSOR), canvas.getInterractionLayer(), new NodeConnectProvider(canvas));
			case RECONNECT:
				return createReconnectAction(new NodeReconnectProvider(canvas));
			case EDITOR:
				return createEditorAction(new LabelTextFieldEditor(canvas));
			case POPUP_MENU_MAIN:
				return createPopupMenuAction(new SyntaxGraphPopupMenu(canvas));
			case RECTANGULAR_SELECT:
				return createRectangularSelectAction(new DefaultRectangularSelectDecorator(canvas), canvas.getBackgroundLayer(), new CanvasRectangularSelectProvider(canvas));
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
			default:
				return null;
				
		}
	}
	
	public void setActiveMoveAction(final String activeMoveAction)
	{
		this.activeMoveAction = activeMoveAction;
	}
}

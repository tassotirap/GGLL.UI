package ggll.ui.canvas.action;

import ggll.ui.canvas.AbstractCanvas;
import ggll.ui.canvas.SnapToLineMoveStrategy;
import ggll.ui.canvas.provider.AlignWithMultiMoveProvider;
import ggll.ui.canvas.provider.FreeMoveControl;
import ggll.ui.canvas.provider.LineProvider;
import ggll.ui.canvas.provider.WidgetCopyPasteProvider;
import ggll.ui.canvas.provider.WidgetDeleteProvider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.util.EnumSet;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;

import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.CycleFocusProvider;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.HoverProvider;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.MoveControlPointProvider;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.ReconnectDecorator;
import org.netbeans.api.visual.action.ReconnectProvider;
import org.netbeans.api.visual.action.RectangularSelectDecorator;
import org.netbeans.api.visual.action.RectangularSelectProvider;
import org.netbeans.api.visual.action.ResizeControlPointResolver;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.TwoStateHoverProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.modules.visual.action.AcceptAction;
import org.netbeans.modules.visual.action.ActionMapAction;
import org.netbeans.modules.visual.action.AddRemoveControlPointAction;
import org.netbeans.modules.visual.action.AlignWithMoveStrategyProvider;
import org.netbeans.modules.visual.action.AlignWithResizeStrategyProvider;
import org.netbeans.modules.visual.action.CenteredZoomAction;
import org.netbeans.modules.visual.action.ConnectAction;
import org.netbeans.modules.visual.action.CycleFocusAction;
import org.netbeans.modules.visual.action.CycleObjectSceneFocusProvider;
import org.netbeans.modules.visual.action.DefaultRectangularSelectDecorator;
import org.netbeans.modules.visual.action.EditAction;
import org.netbeans.modules.visual.action.ExtendedConnectAction;
import org.netbeans.modules.visual.action.ForwardKeyEventsAction;
import org.netbeans.modules.visual.action.InplaceEditorAction;
import org.netbeans.modules.visual.action.MouseCenteredZoomAction;
import org.netbeans.modules.visual.action.MouseHoverAction;
import org.netbeans.modules.visual.action.MoveAction;
import org.netbeans.modules.visual.action.MoveControlPointAction;
import org.netbeans.modules.visual.action.ObjectSceneRectangularSelectProvider;
import org.netbeans.modules.visual.action.OrthogonalMoveControlPointProvider;
import org.netbeans.modules.visual.action.PanAction;
import org.netbeans.modules.visual.action.PopupMenuAction;
import org.netbeans.modules.visual.action.ReconnectAction;
import org.netbeans.modules.visual.action.RectangularSelectAction;
import org.netbeans.modules.visual.action.ResizeAction;
import org.netbeans.modules.visual.action.ResizeCornersControlPointResolver;
import org.netbeans.modules.visual.action.SelectAction;
import org.netbeans.modules.visual.action.SingleLayerAlignWithWidgetCollector;
import org.netbeans.modules.visual.action.SnapToGridMoveStrategy;
import org.netbeans.modules.visual.action.SwitchCardProvider;
import org.netbeans.modules.visual.action.TextFieldInplaceEditorProvider;
import org.netbeans.modules.visual.action.TwoStatedMouseHoverAction;
import org.netbeans.modules.visual.action.WheelPanAction;
import org.netbeans.modules.visual.action.ZoomAction;

public final class ActionFactory
{
	private final BasicStroke STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{ 6.0f, 3.0f }, 0.0f);

	public WidgetAction createAcceptAction(AcceptProvider provider)
	{
		assert provider != null;
		return new AcceptAction(provider);
	}

	public WidgetAction createActionMapAction()
	{
		return new ActionMapAction(null, null);
	}

	public WidgetAction createActionMapAction(InputMap inputMap, ActionMap actionMap)
	{
		assert inputMap != null && actionMap != null;
		return new ActionMapAction(inputMap, actionMap);
	}

	public WidgetAction createAddRemoveControlPointAction()
	{
		return createAddRemoveControlPointAction(3.0, 5.0);
	}

	public WidgetAction createAddRemoveControlPointAction(double createSensitivity, double deleteSensitivity)
	{
		return createAddRemoveControlPointAction(createSensitivity, deleteSensitivity, null);
	}

	public WidgetAction createAddRemoveControlPointAction(double createSensitivity, double deleteSensitivity, ConnectionWidget.RoutingPolicy routingPolicy)
	{
		return new AddRemoveControlPointAction(createSensitivity, deleteSensitivity, routingPolicy);
	}

	public WidgetAction createAlignWithMoveAction(AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator)
	{
		return createAlignWithMoveAction(collector, interractionLayer, decorator, true);
	}

	public WidgetAction createAlignWithMoveAction(AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		assert collector != null && interractionLayer != null && decorator != null;
		AlignWithMoveStrategyProvider sp = new AlignWithMoveStrategyProvider(collector, interractionLayer, decorator, outerBounds);
		return createMoveAction(sp, sp);
	}

	public WidgetAction createAlignWithMoveAction(LayerWidget collectionLayer, LayerWidget interractionLayer, AlignWithMoveDecorator decorator)
	{
		return createAlignWithMoveAction(collectionLayer, interractionLayer, decorator, true);
	}

	public WidgetAction createAlignWithMoveAction(LayerWidget collectionLayer, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		AlignWithMoveDecorator alignWithMoveDecoratorDefault = new AlignWithMoveDecorator()
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

		assert collectionLayer != null;
		return createAlignWithMoveAction(new SingleLayerAlignWithWidgetCollector(collectionLayer, outerBounds), interractionLayer, decorator != null ? decorator : alignWithMoveDecoratorDefault, outerBounds);
	}

	public WidgetAction createAlignWithMultiMoveAction(AbstractCanvas canvas, AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		assert collector != null && interractionLayer != null && decorator != null;
		AlignWithMultiMoveProvider alignWithMultiMoveProvidersp = new AlignWithMultiMoveProvider(canvas, collector, interractionLayer, decorator, outerBounds);
		return createMoveAction(alignWithMultiMoveProvidersp, alignWithMultiMoveProvidersp);
	}

	public WidgetAction createAlignWithResizeAction(AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator)
	{
		return createAlignWithResizeAction(collector, interractionLayer, decorator, true);
	}

	public WidgetAction createAlignWithResizeAction(AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		assert collector != null && interractionLayer != null && decorator != null;
		AlignWithResizeStrategyProvider sp = new AlignWithResizeStrategyProvider(collector, interractionLayer, decorator, outerBounds);
		return createResizeAction(sp, sp);
	}

	public WidgetAction createAlignWithResizeAction(LayerWidget collectionLayer, LayerWidget interractionLayer, AlignWithMoveDecorator decorator)
	{
		return createAlignWithResizeAction(collectionLayer, interractionLayer, decorator, true);
	}

	public WidgetAction createAlignWithResizeAction(LayerWidget collectionLayer, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds)
	{
		AlignWithMoveDecorator alignWithMoveDecoratorDefault = new AlignWithMoveDecorator()
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

		assert collectionLayer != null;
		return createAlignWithResizeAction(new SingleLayerAlignWithWidgetCollector(collectionLayer, outerBounds), interractionLayer, decorator != null ? decorator : alignWithMoveDecoratorDefault, outerBounds);
	}

	public WidgetAction createCenteredZoomAction(double zoomMultiplier)
	{
		return new CenteredZoomAction(zoomMultiplier);
	}

	public ConnectAction createConnectAction(ConnectDecorator decorator, LayerWidget interractionLayer, ConnectProvider provider)
	{
		assert interractionLayer != null && provider != null;
		return new ConnectAction(decorator != null ? decorator : createDefaultConnectDecorator(), interractionLayer, provider);
	}

	public ConnectAction createConnectAction(LayerWidget interractionLayer, ConnectProvider provider)
	{
		return createConnectAction(null, interractionLayer, provider);
	}

	public WidgetCopyPasteAction createCopyPasteAction(WidgetCopyPasteProvider wcpp)
	{
		return new WidgetCopyPasteAction(wcpp);
	}

	public WidgetAction createCycleFocusAction(CycleFocusProvider provider)
	{
		assert provider != null;
		return new CycleFocusAction(provider);
	}

	public WidgetAction createCycleObjectSceneFocusAction()
	{
		return createCycleFocusAction(new CycleObjectSceneFocusProvider());
	}

	public AlignWithMoveDecorator createDefaultAlignWithMoveDecorator()
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

	public ConnectDecorator createDefaultConnectDecorator()
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

	public MoveProvider createDefaultMoveProvider()
	{
		return new MoveProvider()
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
	}

	public ReconnectDecorator createDefaultReconnectDecorator()
	{
		return new ReconnectDecorator()
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
	}

	public RectangularSelectDecorator createDefaultRectangularSelectDecorator(Scene scene)
	{
		assert scene != null;
		return new DefaultRectangularSelectDecorator(scene);
	}

	public ResizeControlPointResolver createDefaultResizeControlPointResolver()
	{
		return new ResizeCornersControlPointResolver();
	}

	public WidgetDeleteAction createDeleteAction(WidgetDeleteProvider wdp)
	{
		return new WidgetDeleteAction(wdp);
	}

	public WidgetAction createEditAction(EditProvider provider)
	{
		assert provider != null;
		return new EditAction(provider);
	}

	public WidgetAction createExtendedConnectAction(ConnectDecorator decorator, LayerWidget interractionLayer, ConnectProvider provider)
	{
		ConnectDecorator defaultConnectDecorator = new ConnectDecorator()
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

		assert interractionLayer != null && provider != null;
		return new ExtendedConnectAction(decorator != null ? decorator : defaultConnectDecorator, interractionLayer, provider, InputEvent.CTRL_MASK);
	}

	public WidgetAction createExtendedConnectAction(ConnectDecorator decorator, LayerWidget interractionLayer, ConnectProvider provider, int modifiers)
	{
		ConnectDecorator defaultConnectDecorator = new ConnectDecorator()
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

		assert interractionLayer != null && provider != null && modifiers != 0;
		return new ExtendedConnectAction(decorator != null ? decorator : defaultConnectDecorator, interractionLayer, provider, modifiers);
	}

	public WidgetAction createExtendedConnectAction(LayerWidget interractionLayer, ConnectProvider provider)
	{
		return createExtendedConnectAction(null, interractionLayer, provider);
	}

	public WidgetAction createForwardKeyEventsAction(Widget forwardToWidget, String forwardToTool)
	{
		assert forwardToWidget != null;
		return new ForwardKeyEventsAction(forwardToWidget, forwardToTool);
	}

	public WidgetAction createFreeMoveControlPointAction(AbstractCanvas canvas)
	{
		return createMoveControlPointAction(new FreeMoveControl(canvas));
	}

	public MoveControlPointProvider createFreeMoveControlPointProvider(AbstractCanvas canvas)
	{
		return new FreeMoveControl(canvas);
	}

	public MoveStrategy createFreeMoveStrategy()
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

	public WidgetAction createHoverAction(HoverProvider provider)
	{
		assert provider != null;
		return new MouseHoverAction(provider);
	}

	public WidgetAction createHoverAction(TwoStateHoverProvider provider)
	{
		assert provider != null;
		return new TwoStatedMouseHoverAction(provider);
	}

	public <C extends JComponent> WidgetAction createInplaceEditorAction(InplaceEditorProvider<C> provider)
	{
		return new InplaceEditorAction<C>(provider);
	}

	public WidgetAction createInplaceEditorAction(TextFieldInplaceEditor editor)
	{
		return createInplaceEditorAction(editor, null);
	}

	public WidgetAction createInplaceEditorAction(TextFieldInplaceEditor editor, EnumSet<InplaceEditorProvider.ExpansionDirection> expansionDirections)
	{
		return createInplaceEditorAction(new TextFieldInplaceEditorProvider(editor, expansionDirections));
	}

	public WidgetAction createMouseCenteredZoomAction(double zoomMultiplier)
	{
		return new MouseCenteredZoomAction(zoomMultiplier);
	}

	public WidgetAction createMoveAction()
	{
		return createMoveAction(null, null);
	}

	public WidgetAction createMoveAction(MoveStrategy strategy, MoveProvider provider)
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

	public WidgetAction createMoveControlPointAction(MoveControlPointProvider provider)
	{
		return createMoveControlPointAction(provider, null);
	}

	public WidgetAction createMoveControlPointAction(MoveControlPointProvider provider, ConnectionWidget.RoutingPolicy routingPolicy)
	{
		assert provider != null;
		return new MoveControlPointAction(provider, routingPolicy);
	}

	public RectangularSelectProvider createObjectSceneRectangularSelectProvider(ObjectScene scene)
	{
		assert scene != null;
		return new ObjectSceneRectangularSelectProvider(scene);
	}

	public WidgetAction createOrthogonalMoveControlPointAction()
	{
		return createMoveControlPointAction(new OrthogonalMoveControlPointProvider());
	}

	public MoveControlPointProvider createOrthogonalMoveControlPointProvider()
	{
		return new OrthogonalMoveControlPointProvider();
	}

	public WidgetAction createPanAction()
	{
		return new PanAction();
	}

	public WidgetAction createPopupMenuAction(final PopupMenuProvider provider)
	{
		assert provider != null;
		return new PopupMenuAction(provider);
	}

	public WidgetAction createReconnectAction(ReconnectDecorator decorator, ReconnectProvider provider)
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

	public WidgetAction createReconnectAction(ReconnectProvider provider)
	{
		return createReconnectAction(null, provider);
	}

	public WidgetAction createRectangularSelectAction(ObjectScene scene, LayerWidget interractionLayer)
	{
		assert scene != null;
		return createRectangularSelectAction(createDefaultRectangularSelectDecorator(scene), interractionLayer, createObjectSceneRectangularSelectProvider(scene));
	}

	public WidgetAction createRectangularSelectAction(RectangularSelectDecorator decorator, LayerWidget interractionLayer, RectangularSelectProvider provider)
	{
		assert decorator != null && interractionLayer != null && provider != null;
		return new RectangularSelectAction(decorator, interractionLayer, provider);
	}

	public WidgetAction createResizeAction()
	{
		return createResizeAction(null, null);
	}

	public WidgetAction createResizeAction(ResizeStrategy strategy, ResizeControlPointResolver resolver, ResizeProvider provider)
	{
		ResizeProvider resizeProviderDefault = new ResizeProvider()
		{
			@Override
			public void resizingFinished(Widget widget)
			{
			}

			@Override
			public void resizingStarted(Widget widget)
			{
			}
		};

		ResizeStrategy resizeFreeStrategy = new ResizeStrategy()
		{
			@Override
			public Rectangle boundsSuggested(Widget widget, Rectangle originalBounds, Rectangle suggestedBounds, ResizeProvider.ControlPoint controlPoint)
			{
				return suggestedBounds;
			}
		};

		return new ResizeAction(strategy != null ? strategy : resizeFreeStrategy, resolver != null ? resolver : new ResizeCornersControlPointResolver(), provider != null ? provider : resizeProviderDefault);
	}

	public WidgetAction createResizeAction(ResizeStrategy strategy, ResizeProvider provider)
	{
		return createResizeAction(strategy, null, provider);
	}

	public WidgetAction createSelectAction(SelectProvider provider)
	{
		assert provider != null;
		return new SelectAction(provider);
	}

	public MoveStrategy createSnapToGridMoveStrategy(int horizontalGridSize, int verticalGridSize)
	{
		assert horizontalGridSize > 0 && verticalGridSize > 0;
		return new SnapToGridMoveStrategy(horizontalGridSize, verticalGridSize);
	}

	public MoveStrategy createSnapToLineMoveStrategy(AbstractCanvas canvas)
	{
		assert canvas != null;
		return new SnapToLineMoveStrategy(LineProvider.getInstance(canvas));
	}

	public WidgetAction createSwitchCardAction(Widget cardLayoutWidget)
	{
		assert cardLayoutWidget != null;
		return new SelectAction(new SwitchCardProvider(cardLayoutWidget));
	}

	public WidgetAction createWheelPanAction()
	{
		return new WheelPanAction();
	}

	public WidgetAction createZoomAction()
	{
		return createZoomAction(1.2, true);
	}

	public WidgetAction createZoomAction(double zoomMultiplier, boolean animated)
	{
		return new ZoomAction(zoomMultiplier, animated);
	}

	public InplaceEditorProvider.EditorController getInplaceEditorController(WidgetAction inplaceEditorAction)
	{
		return (InplaceEditorProvider.EditorController) inplaceEditorAction;
	}

}
package ggll.ui.canvas.widget;

import java.awt.Color;
import java.awt.Image;
import java.awt.Paint;

import org.netbeans.api.visual.laf.LookFeel;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

public class IconNodeWidgetExt extends Widget implements MarkedWidget, TypedWidget
{

	/**
	 * The text orientation specified relatively to the image
	 */
	public static enum TextOrientation
	{

		BOTTOM_CENTER, RIGHT_CENTER

	}

	private final ImageWidget imageWidget;

	private final LabelWidget labelWidget;
	private Paint markBackground;

	private Color markForeground;

	private String type;
	public Color DEFAULT_MARK_FOREGROUND = new Color(0, 0, 0);

	/**
	 * Creates an icon node widget with bottom-center orientation.
	 * 
	 * @param scene
	 *            the scene
	 */
	public IconNodeWidgetExt(Scene scene)
	{
		this(scene, TextOrientation.BOTTOM_CENTER);
	}

	/**
	 * Creates an icon node widget with a specified orientation.
	 * 
	 * @param scene
	 *            the scene
	 * @param orientation
	 *            the text orientation
	 */
	public IconNodeWidgetExt(Scene scene, TextOrientation orientation)
	{
		super(scene);
		final LookFeel lookFeel = getScene().getLookFeel();

		switch (orientation)
		{
			case BOTTOM_CENTER:
				setLayout(LayoutFactory.createVerticalFlowLayout(LayoutFactory.SerialAlignment.CENTER, -lookFeel.getMargin() + 1));
				break;
			case RIGHT_CENTER:
				setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, -lookFeel.getMargin() + 1));
				break;
		}

		this.imageWidget = new ImageWidget(scene);
		addChild(this.imageWidget);

		this.labelWidget = new LabelWidget(scene);
		this.labelWidget.setFont(scene.getDefaultFont().deriveFont(14.0f));
		addChild(this.labelWidget);

		setState(ObjectState.createNormal());
	}

	/**
	 * Returns the image widget part of the icon node widget.
	 * 
	 * @return the image widget
	 */
	public final ImageWidget getImageWidget()
	{
		return this.imageWidget;
	}

	/**
	 * Returns the label widget part of the icon node widget.
	 * 
	 * @return the label widget
	 */
	public final LabelWidget getLabelWidget()
	{
		return this.labelWidget;
	}

	@Override
	public String getMark()
	{
		return getLabelWidget().getLabel();
	}

	public Paint getMarkBackground()
	{
		// to make sure i'm telling the truth:
		getLabelWidget().setBackground(this.markBackground == null ? DEFAULT_MARK_BACKGROUND : this.markBackground);
		return getLabelWidget().getBackground();
	}

	public Color getMarkForeground()
	{
		getLabelWidget().setForeground(this.markForeground == null ? this.DEFAULT_MARK_FOREGROUND : this.markForeground);
		return getLabelWidget().getForeground();
	}

	@Override
	public String getType()
	{
		return this.type;
	}

	/**
	 * Implements the widget-state specific look of the widget.
	 * 
	 * @param previousState
	 *            the previous state
	 * @param state
	 *            the new state
	 */
	@Override
	public void notifyStateChanged(ObjectState previousState, ObjectState state)
	{
		final LookFeel lookFeel = getScene().getLookFeel();
		this.labelWidget.setBorder(lookFeel.getBorder(state));
		this.labelWidget.setForeground(lookFeel.getForeground(state));
	}

	/**
	 * Sets an image.
	 * 
	 * @param image
	 *            the image
	 */
	public final void setImage(Image image)
	{
		this.imageWidget.setImage(image);
	}

	/**
	 * Sets a label.
	 * 
	 * @param label
	 *            the label
	 */
	public final void setLabel(String label)
	{
		this.labelWidget.setLabel(label);
		this.labelWidget.setForeground(this.markForeground == null ? this.DEFAULT_MARK_FOREGROUND : this.markForeground);
	}

	@Override
	public void setMark(String mark)
	{
		setLabel(mark);
	}

	@Override
	public void setMarkBackground(Paint p)
	{
		this.markBackground = p;
		getLabelWidget().setBackground(this.markBackground);
	}

	@Override
	public void setMarkForeground(Color markForeground)
	{
		this.markForeground = markForeground;
		getLabelWidget().setForeground(markForeground);
	}

	@Override
	public void setType(String type)
	{
		this.type = type;
	}

}

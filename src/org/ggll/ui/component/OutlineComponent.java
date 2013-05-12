package org.ggll.ui.component;

import javax.swing.JComponent;

import org.ggll.canvas.Canvas;
import org.ggll.canvas.OutlineTopComponent;

public class OutlineComponent extends AbstractComponent
{

	/**
	 * Creates an outline component showing the canvas
	 * 
	 * @param an
	 *            instance of a Canvas is expected
	 */
	@Override
	public JComponent create(Object param) throws BadParameterException
	{
		if (param instanceof Canvas)
		{
			return new OutlineTopComponent((Canvas) param);
		}
		else
		{
			throw new BadParameterException("Can not intiate a outliner. Bad Parameter: " + param.toString());
		}
	}

	@Override
	public void fireContentChanged()
	{
	}

}

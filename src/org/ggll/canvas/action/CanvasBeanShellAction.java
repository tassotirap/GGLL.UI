package org.ggll.canvas.action;

import org.ggll.actions.AbstractEditAction;
import org.ggll.actions.AsinActionSet;
import org.ggll.actions.BeanShellFacade;
import org.ggll.canvas.Canvas;
import org.ggll.util.Log;

import bsh.BshMethod;
import bsh.NameSpace;
import bsh.UtilEvalError;

/**
 * An action that evaluates BeanShell code when invoked. BeanShell actions are
 * usually loaded from <code>actions.xml</code> and
 * <code>browser.actions.xml</code> files; see {@link AsinActionSet} for syntax
 * information.
 * 
 * @see jEdit#getAction(String)
 * @see jEdit#getActionNames()
 * @see AsinActionSet
 * @author Gustavo Braga
 */
public class CanvasBeanShellAction extends AbstractEditAction<Canvas>
{

	// {{{ MyBeanShellFacade class
	private static class MyBeanShellFacade extends BeanShellFacade<Canvas>
	{
		@Override
		protected void handleException(Canvas canvas, String path, Throwable t)
		{
			Log.log(Log.ERROR, this, t, t);
		}

		@Override
		protected void resetDefaultVariables(NameSpace namespace) throws UtilEvalError
		{
			namespace.setVariable("canvas", null, false);
		}

		@Override
		protected void setupDefaultVariables(NameSpace namespace, Canvas canvas) throws UtilEvalError
		{
			if (canvas != null)
			{
				namespace.setVariable("canvas", canvas, false);
			}
		}

		@Override
		public void init()
		{
			global.importClass("org.ggll.canvas.Canvas");
			global.importClass("org.ggll.canvas.CanvasFactory");
			global.importClass("org.ggll.canvas.state.VolatileStateManager");
			global.importClass("org.ggll.canvas.state.StaticStateManager");
			global.importClass("org.ggll.syntax.grammar.Controller");
			global.importClass("org.ggll.canvas.action.WidgetCopyPasteProvider");
			global.importClass("org.ggll.canvas.action.WidgetDeleteProvider");
			global.importClass("org.ggll.project.ProjectManager");
			global.importPackage("org.ggll.util");
		}
	} // }}}

	private static final BeanShellFacade<Canvas> bsh = new MyBeanShellFacade();

	private BshMethod cachedCode;

	private String code;

	private String sanitizedName;

	public CanvasBeanShellAction(String name, String code)
	{
		super(name);
		this.code = code;
		/*
		 * Some characters that we like to use in action names ('.', '-') are
		 * not allowed in BeanShell identifiers.
		 */
		sanitizedName = name.replace('.', '_').replace('-', '_');
	}

	// }}}

	@Override
	public void invoke(Canvas canvas)
	{
		try
		{
			if (cachedCode == null)
			{
				String cachedCodeName = "action_" + sanitizedName;
				cachedCode = bsh.cacheBlock(cachedCodeName, code, true);
			}

			bsh.runCachedBlock(cachedCode, canvas, new NameSpace(bsh.getNameSpace(), "BeanShellAction.invoke()"));
		}
		catch (Throwable e)
		{
			Log.log(Log.ERROR, this, e);
		}
	}

}

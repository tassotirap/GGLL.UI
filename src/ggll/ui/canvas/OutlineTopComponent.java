package ggll.ui.canvas;

import java.awt.BorderLayout;
import java.io.Serializable;

import org.netbeans.api.visual.graph.GraphScene;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
public class OutlineTopComponent extends TopComponent
{

	class ResolvableHelper implements Serializable
	{

		private static final long serialVersionUID = 1L;

		public Object readResolve()
		{
			return this;
		}
	}

	private static OutlineTopComponent instance;

	private static final String PREFERRED_ID = "janelaTopComponent";

	private OutlineTopComponent()
	{
		initComponents();
		setSize(900, 700);
	}

	public static OutlineTopComponent getInstance()
	{
		if (instance == null)
		{
			instance = new OutlineTopComponent();
		}
		return instance;
	}

	private void initComponents()
	{
		setLayout(new java.awt.BorderLayout());
	}

	@Override
	protected String preferredID()
	{
		return PREFERRED_ID;
	}

	@Override
	public void componentClosed()
	{
	}

	@Override
	public void componentOpened()
	{
	}

	@Override
	public int getPersistenceType()
	{
		return TopComponent.PERSISTENCE_NEVER;
	}

	public void setCanvas(GraphScene scene)
	{
		removeAll();
		add(scene.createSatelliteView(), BorderLayout.CENTER);
	}

	@Override
	public Object writeReplace()
	{
		return new ResolvableHelper();
	}
}

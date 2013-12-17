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

	private static OutlineTopComponent instance;

	class ResolvableHelper implements Serializable
	{

		private static final long serialVersionUID = 1L;

		public Object readResolve()
		{
			return this;
		}
	}

	private static final String PREFERRED_ID = "janelaTopComponent";

	private OutlineTopComponent()
	{
		initComponents();
		setSize(900, 700);
	}

	public void setCanvas(GraphScene scene)
	{
		removeAll();
		add(scene.createSatelliteView(), BorderLayout.CENTER);
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

	@Override
	public Object writeReplace()
	{
		return new ResolvableHelper();
	}

	public static OutlineTopComponent getInstance()
	{
		if (instance == null)
		{
			instance = new OutlineTopComponent();
		}
		return instance;
	}
}

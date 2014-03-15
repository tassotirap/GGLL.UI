package org.ggll.syntax.graph;

import java.awt.BorderLayout;
import java.io.Serializable;

import org.netbeans.api.visual.graph.GraphScene;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
public class SyntaxTopComponent extends TopComponent
{
	
	class ResolvableHelper implements Serializable
	{
		
		private static final long serialVersionUID = 1L;
		
		public Object readResolve()
		{
			return this;
		}
	}
	
	private static SyntaxTopComponent instance;
	
	private static final String PREFERRED_ID = "topWindowComponent";
	
	public static SyntaxTopComponent getInstance()
	{
		if (SyntaxTopComponent.instance == null)
		{
			SyntaxTopComponent.instance = new SyntaxTopComponent();
		}
		return SyntaxTopComponent.instance;
	}
	
	private SyntaxTopComponent()
	{
		initComponents();
		setSize(100, 700);
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
	
	private void initComponents()
	{
		setLayout(new java.awt.BorderLayout());
	}
	
	@Override
	protected String preferredID()
	{
		return SyntaxTopComponent.PREFERRED_ID;
	}
	
	public void setCanvas(final GraphScene<?, ?> scene)
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

package org.ggll.parser.syntax.grammar.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Properties;

abstract public class SyntaxElement extends Properties implements Serializable, Cloneable
{

	static final long serialVersionUID = 1;
	public final static String CHILDREN = "Children";
	public final static String INPUTS = "Inputs";
	public final static String OUTPUTS = "Outputs";
	transient protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		this.listeners = new PropertyChangeSupport(this);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue)
	{
		this.listeners.firePropertyChange(prop, old, newValue);
	}

	protected void fireStructureChange(String prop, Object child)
	{
		this.listeners.firePropertyChange(prop, null, child);
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		this.listeners.addPropertyChangeListener(l);
	}

	public abstract String getID();

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		this.listeners.removePropertyChangeListener(l);
	}

	public abstract void setID(String s);

	public void update()
	{
	}

}
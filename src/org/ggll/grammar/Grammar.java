package org.ggll.grammar;

import ggll.core.list.ExtendedList;

import java.util.HashMap;

/** This class is a container for a grammar **/
public class Grammar
{

	private final HashMap<GrammarComponent, ExtendedList<GrammarComponent>> alternatives;
	private final HashMap<GrammarComponent, ExtendedList<GrammarComponent>> antiAlternatives;
	private final HashMap<GrammarComponent, ExtendedList<GrammarComponent>> antiSuccessors;
	private final ExtendedList<GrammarComponent> components;
	private GrammarComponent current;
	private GrammarComponent head;
	private final ExtendedList<GrammarComponent> heads;
	private final ExtendedList<GrammarComponent> leftHands;
	private final HashMap<GrammarComponent, ExtendedList<GrammarComponent>> successors;

	public Grammar(GrammarComponent current)
	{
		this.alternatives = new HashMap<GrammarComponent, ExtendedList<GrammarComponent>>();
		this.antiAlternatives = new HashMap<GrammarComponent, ExtendedList<GrammarComponent>>();
		this.successors = new HashMap<GrammarComponent, ExtendedList<GrammarComponent>>();
		this.antiSuccessors = new HashMap<GrammarComponent, ExtendedList<GrammarComponent>>();
		this.leftHands = new ExtendedList<GrammarComponent>();
		this.heads = new ExtendedList<GrammarComponent>();
		this.components = new ExtendedList<GrammarComponent>();
		setCurrent(current);
	}

	private GrammarComponent addComp(GrammarComponent comp)
	{
		for (final GrammarComponent c : this.components.getAll())
		{
			if (c.getId().equals(comp.getId()))
			{
				return c;
			}
		}
		if (!this.alternatives.containsKey(comp))
		{
			this.alternatives.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!this.successors.containsKey(comp))
		{
			this.successors.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!this.antiAlternatives.containsKey(comp))
		{
			this.antiAlternatives.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!this.antiSuccessors.containsKey(comp))
		{
			this.antiSuccessors.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!this.components.contains(comp) && comp != this.head)
		{
			this.components.append(comp);
		}
		return comp;
	}

	public void addAlternative(GrammarComponent alternative)
	{
		alternative = addComp(alternative);
		this.alternatives.get(this.current).append(alternative);
		this.antiAlternatives.get(alternative).append(this.current);

	}

	public void addLeftHand(GrammarComponent lh)
	{
		addComp(lh);
		this.leftHands.append(lh);
	}

	public void addSuccessor(GrammarComponent successor)
	{
		successor = addComp(successor);
		this.successors.get(this.current).append(successor);
		this.antiSuccessors.get(successor).append(this.current);
	}

	@Override
	public void finalize()
	{
		this.current = null;
	}

	public ExtendedList<GrammarComponent> getAlternatives(GrammarComponent source)
	{
		if (this.alternatives.containsKey(source))
		{
			return this.alternatives.get(source);
		}
		return null;
	}

	public ExtendedList<GrammarComponent> getAntiAlternatives(GrammarComponent target)
	{
		if (this.antiAlternatives.containsKey(target))
		{
			return this.antiAlternatives.get(target);
		}
		return null;
	}

	public ExtendedList<GrammarComponent> getAntiSuccessors(GrammarComponent target)
	{
		if (this.antiSuccessors.containsKey(target))
		{
			return this.antiSuccessors.get(target);
		}
		return null;
	}

	/**
	 * @return the components
	 */
	public ExtendedList<GrammarComponent> getComponents()
	{
		return this.components;
	}

	public GrammarComponent getCurrent()
	{
		return this.current;
	}

	public GrammarComponent getHead()
	{
		return this.head;
	}

	public ExtendedList<GrammarComponent> getHeads()
	{
		return this.heads;
	}

	public ExtendedList<GrammarComponent> getLeftHands()
	{
		return this.leftHands;
	}

	public ExtendedList<GrammarComponent> getSucessors(GrammarComponent source)
	{
		if (this.successors.containsKey(source))
		{
			return this.successors.get(source);
		}
		return null;
	}

	public boolean hasCurrent()
	{
		return this.current != null;
	}

	public void setCurrent(GrammarComponent current)
	{
		current = addComp(current);
		this.current = current;
	}

	public void setHead(GrammarComponent head)
	{
		this.head = head;
		this.heads.append(head);
	}
}

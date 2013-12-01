package ggll.ui.core.syntax.grammar;

import ggll.core.list.ExtendedList;

import java.util.HashMap;

/** This class is a container for a grammar **/
public class Grammar
{

	private HashMap<GrammarComponent, ExtendedList<GrammarComponent>> alternatives;
	private HashMap<GrammarComponent, ExtendedList<GrammarComponent>> antiAlternatives;
	private HashMap<GrammarComponent, ExtendedList<GrammarComponent>> antiSuccessors;
	private ExtendedList<GrammarComponent> components;
	private GrammarComponent current;
	private GrammarComponent head;
	private ExtendedList<GrammarComponent> heads;
	private ExtendedList<GrammarComponent> leftHands;
	private HashMap<GrammarComponent, ExtendedList<GrammarComponent>> successors;

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
		for (GrammarComponent c : components.getAll())
		{
			if (c.getId().equals(comp.getId()))
			{
				return c;
			}
		}
		if (!alternatives.containsKey(comp))
		{
			alternatives.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!successors.containsKey(comp))
		{
			successors.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!antiAlternatives.containsKey(comp))
		{
			antiAlternatives.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!antiSuccessors.containsKey(comp))
		{
			antiSuccessors.put(comp, new ExtendedList<GrammarComponent>());
		}
		if (!components.contains(comp) && comp != head)
		{
			components.append(comp);
		}
		return comp;
	}

	public void addAlternative(GrammarComponent alternative)
	{
		alternative = addComp(alternative);
		alternatives.get(current).append(alternative);
		antiAlternatives.get(alternative).append(current);

	}

	public void addLeftHand(GrammarComponent lh)
	{
		addComp(lh);
		leftHands.append(lh);
	}

	public void addSuccessor(GrammarComponent successor)
	{
		successor = addComp(successor);
		successors.get(current).append(successor);
		antiSuccessors.get(successor).append(current);
	}

	@Override
	public void finalize()
	{
		this.current = null;
	}

	public ExtendedList<GrammarComponent> getAlternatives(GrammarComponent source)
	{
		if (alternatives.containsKey(source))
		{
			return alternatives.get(source);
		}
		return null;
	}

	public ExtendedList<GrammarComponent> getAntiAlternatives(GrammarComponent target)
	{
		if (antiAlternatives.containsKey(target))
		{
			return antiAlternatives.get(target);
		}
		return null;
	}

	public ExtendedList<GrammarComponent> getAntiSuccessors(GrammarComponent target)
	{
		if (antiSuccessors.containsKey(target))
		{
			return antiSuccessors.get(target);
		}
		return null;
	}

	/**
	 * @return the components
	 */
	public ExtendedList<GrammarComponent> getComponents()
	{
		return components;
	}

	public GrammarComponent getCurrent()
	{
		return this.current;
	}

	public GrammarComponent getHead()
	{
		return head;
	}

	public ExtendedList<GrammarComponent> getHeads()
	{
		return heads;
	}

	public ExtendedList<GrammarComponent> getLeftHands()
	{
		return leftHands;
	}

	public ExtendedList<GrammarComponent> getSucessors(GrammarComponent source)
	{
		if (successors.containsKey(source))
		{
			return successors.get(source);
		}
		return null;
	}

	public boolean hasCurrent()
	{
		return current != null;
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

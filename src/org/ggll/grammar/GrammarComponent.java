package org.ggll.grammar;

/** Just any component on a grammar; an abstract representation **/
public class GrammarComponent
{

	private Object contents;
	private boolean head;
	private Object id;
	private boolean lambda;
	private boolean leftHand;
	private boolean nonterminal;
	private boolean terminal;

	public GrammarComponent()
	{
	}

	public GrammarComponent(Object contents, Object id)
	{
		this.contents = contents;
		this.id = id;
	}

	public Object getContents()
	{
		return this.contents;
	}

	public Object getId()
	{
		return this.id;
	}

	public boolean isHead()
	{
		return this.head;
	}

	public boolean isLambda()
	{
		return this.lambda;
	}

	public boolean isLeftHand()
	{
		return this.leftHand;
	}

	public boolean isNonterminal()
	{
		return this.nonterminal;
	}

	public boolean isTerminal()
	{
		return this.terminal;
	}

	public void setContents(Object contents)
	{
		this.contents = contents;
	}

	public void setHead(boolean head)
	{
		this.head = head;
	}

	public void setId(Object id)
	{
		this.id = id;
	}

	public void setLambda(boolean lambda)
	{
		this.lambda = lambda;
	}

	public void setLeftHand(boolean leftHand)
	{
		this.leftHand = leftHand;
	}

	public void setNonterminal(boolean nonterminal)
	{
		this.nonterminal = nonterminal;
	}

	public void setTerminal(boolean terminal)
	{
		this.terminal = terminal;
	}
}

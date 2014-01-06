package org.ggll.parser.syntax.validation;

import org.ggll.parser.syntax.grammar.Grammar;
import org.ggll.parser.syntax.grammar.GrammarComponent;

import ggll.core.list.ExtendedList;

public class GSLL1Rules extends GrammarRule
{

	private final Grammar grammar;
	private final boolean onTheFly;

	public GSLL1Rules(Grammar grammar, boolean onTheFly)
	{
		super(grammar, onTheFly);
		this.onTheFly = onTheFly;
		this.grammar = grammar;
	}

	/** Is there a valid header **/
	public InvalidGrammarException r0()
	{
		if (!this.onTheFly && this.grammar.getHeads().count() == 0)
		{
			return new InvalidGrammarException("There must be an initial non-terminal.", "Any grammar must have exactly one start point: an initial non-terminal.", null);
		}
		if (this.grammar.getHeads().count() > 1)
		{
			return new InvalidGrammarException("There must only one initial non-terminal.", "Only one initial point by grammar is allowed. Only one grammar is allowed within each file.", this.grammar.getHeads().get(this.grammar.getHeads().count() - 1));
		}
		return null;
	}

	/**
	 * The header or a left side is someone's alternative or successor, or does
	 * not have a successor
	 **/
	public InvalidGrammarException r1()
	{
		GrammarComponent head = this.grammar.getHead();
		int index = -1;
		if (this.grammar.getLeftHands() != null && this.grammar.getLeftHands().count() > 0)
		{
			index = this.grammar.getLeftHands().count();
		}
		while (head != null)
		{
			if (this.grammar.getAlternatives(head).count() > 0)
			{
				return new InvalidGrammarException("A left non-terminal can only have an successor.", "A left non-terminal can not be immediately followed by an alternative node", head);
			}
			if (!this.onTheFly && head != this.grammar.getHead() && this.grammar.getSucessors(head).count() != 1)
			{
				return new InvalidGrammarException("A left non-terminal must have exactly one successor.", null, head);
			}
			if (this.grammar.getAntiAlternatives(head) != null && this.grammar.getAntiAlternatives(head).count() > 0 || this.grammar.getAntiSuccessors(head) != null && this.grammar.getAntiSuccessors(head).count() > 0)
			{
				return new InvalidGrammarException("A left non-terminal can not be alternative or successor of any other node", null, head);
			}
			if (index >= 1)
			{
				head = this.grammar.getLeftHands().get(--index);
			}
			else
			{
				head = null;
			}
		}
		return null;
	}

	/** avoid repeated left hands **/
	public InvalidGrammarException r2()
	{
		for (int i = 0; i < this.grammar.getLeftHands().count(); i++)
		{
			for (int j = 0; j < this.grammar.getLeftHands().count(); j++)
			{
				if (i != j)
				{
					if (this.grammar.getLeftHands().get(i).getContents().equals(this.grammar.getLeftHands().get(j).getContents()))
					{
						new InvalidGrammarException("Only one left hand by label is allowed", "You must join the two productions in one, through the use of alternative connections. (Ex. M -> a and M -> b is the same as M -> a | b", this.grammar.getLeftHands().get(j));
					}
				}
			}
		}
		return null;
	}

	/** only one successor and one alternative by node **/
	public InvalidGrammarException r3()
	{
		for (final GrammarComponent comp : this.grammar.getComponents().getAll())
		{
			if (this.grammar.getAntiAlternatives(comp).count() > 1 || this.grammar.getAntiSuccessors(comp).count() > 1 || this.grammar.getSucessors(comp).count() > 1 || this.grammar.getAlternatives(comp).count() > 1)
			{
				return new InvalidGrammarException("It is not allowed to have a node being simultaneously alternative or successor of two distinct nodes. ", null, comp);
			}
		}
		return null;
	}

	/** all nodes must be included in the graph **/
	public InvalidGrammarException r4()
	{
		if (!this.onTheFly)
		{
			for (final GrammarComponent comp : this.grammar.getComponents().getAll())
			{
				if (comp != this.grammar.getHead() && this.grammar.getAlternatives(comp).count() == 0 && this.grammar.getSucessors(comp).count() == 0 && this.grammar.getAntiAlternatives(comp).count() == 0 && this.grammar.getAntiSuccessors(comp).count() == 0)
				{
					return new InvalidGrammarException("There is node not connected to the graph.", "All terminal and non-terminal nodes present in the drawing area must be connected to the graph.", comp);
				}
			}
		}
		return null;
	}

	/**
	 * two different terminal nodes in sequence of alternatives can not have the
	 * same label. And if a non-terminal is in a sequence of alternatives, then
	 * the first terminal produced by this non-terminal can not be included in
	 * the sequence of alternatives
	 **/
	public InvalidGrammarException r5()
	{
		for (final GrammarComponent comp : this.grammar.getComponents().getAll())
		{
			if (comp.isTerminal() && this.grammar.getAlternatives(comp).count() > 0)
			{
				GrammarComponent alt = this.grammar.getAlternatives(comp).get(0);
				while (alt != null)
				{
					if (alt.isTerminal() && alt.getContents().equals(comp.getContents()) || alt == comp)
					{
						// return new
						// InvalidGrammarException("Two terminal nodes in a sequence of alternatives can not be same, or have the same label",
						// "This cases leads to a non-determinism.", alt);
					}
					if (alt.isNonterminal())
					{
						for (final GrammarComponent lh : this.grammar.getLeftHands().getAll())
						{
							if (lh.getContents().equals(alt.getContents()))
							{
								if (this.grammar.getSucessors(lh).count() > 0)
								{
									if (this.grammar.getSucessors(lh).get(0).getContents().equals(comp.getContents()))
									{
										return new InvalidGrammarException("The first symbol produced by non-terminal can not be in its sequence of alternatives.", "This cases leads to a non-determinism.", alt);
									}
								}
							}
						}
					}
					if (this.grammar.getAlternatives(alt).count() > 0)
					{
						alt = this.grammar.getAlternatives(alt).get(0);
					}
					else
					{
						alt = null;
					}
				}
			}
		}
		return null;
	}

	@Override
	public void validate() throws InvalidGrammarException
	{
		final ExtendedList<InvalidGrammarException> exs = new ExtendedList<InvalidGrammarException>();
		InvalidGrammarException ex;
		ex = r0();
		if (ex != null)
		{
			exs.append(ex);
		}
		ex = r1();
		if (ex != null)
		{
			exs.append(ex);
		}
		ex = r2();
		if (ex != null)
		{
			exs.append(ex);
		}
		/*
		 * ex = r3(); if (ex != null) exs.add(ex);
		 */
		ex = r4();
		if (ex != null)
		{
			exs.append(ex);
		}
		ex = r5();
		if (ex != null)
		{
			exs.append(ex);
		}
		if (exs.count() > 0)
		{
			int index = 1;
			final InvalidGrammarException result = exs.get(0);
			while (exs.count() > index)
			{
				result.insertMoreExceptions(exs.get(index++));
			}
			throw result;
		}

	}
}

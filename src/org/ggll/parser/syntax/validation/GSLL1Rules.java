//package org.ggll.parser.syntax.validation;
//
//import org.ggll.exceptions.InvalidGrammarException;
//import org.ggll.parser.syntax.grammar.Grammar;
//import org.ggll.parser.syntax.grammar.GrammarComponent;
//
//import ggll.core.list.ExtendedList;
//
//public class GSLL1Rules extends GrammarRule
//{
//
//	public GSLL1Rules(Grammar grammar, boolean onTheFly)
//	{
//		super(grammar, onTheFly);
//	}
//
//
//	/** avoid repeated left hands **/
//	public InvalidGrammarException r2()
//	{
//		for (int i = 0; i < this.grammar.getLeftHands().count(); i++)
//		{
//			for (int j = 0; j < this.grammar.getLeftHands().count(); j++)
//			{
//				if (i != j)
//				{
//					if (this.grammar.getLeftHands().get(i).getContents().equals(this.grammar.getLeftHands().get(j).getContents()))
//					{
//						new InvalidGrammarException("Only one left hand by label is allowed", "You must join the two productions in one, through the use of alternative connections. (Ex. M -> a and M -> b is the same as M -> a | b", this.grammar.getLeftHands().get(j));
//					}
//				}
//			}
//		}
//		return null;
//	}
//

//
//
//	/**
//	 * two different terminal nodes in sequence of alternatives can not have the
//	 * same label. And if a non-terminal is in a sequence of alternatives, then
//	 * the first terminal produced by this non-terminal can not be included in
//	 * the sequence of alternatives
//	 **/
//	public InvalidGrammarException r5()
//	{
//		for (final GrammarComponent comp : this.grammar.getComponents().getAll())
//		{
//			if (comp.isTerminal() && this.grammar.getAlternatives(comp).count() > 0)
//			{
//				GrammarComponent alt = this.grammar.getAlternatives(comp).get(0);
//				while (alt != null)
//				{
//					if (alt.isTerminal() && alt.getContents().equals(comp.getContents()) || alt == comp)
//					{
//						// return new
//						// InvalidGrammarException("Two terminal nodes in a sequence of alternatives can not be same, or have the same label",
//						// "This cases leads to a non-determinism.", alt);
//					}
//					if (alt.isNonterminal())
//					{
//						for (final GrammarComponent lh : this.grammar.getLeftHands().getAll())
//						{
//							if (lh.getContents().equals(alt.getContents()))
//							{
//								if (this.grammar.getSucessors(lh).count() > 0)
//								{
//									if (this.grammar.getSucessors(lh).get(0).getContents().equals(comp.getContents()))
//									{
//										return new InvalidGrammarException("The first symbol produced by non-terminal can not be in its sequence of alternatives.", "This cases leads to a non-determinism.", alt);
//									}
//								}
//							}
//						}
//					}
//					if (this.grammar.getAlternatives(alt).count() > 0)
//					{
//						alt = this.grammar.getAlternatives(alt).get(0);
//					}
//					else
//					{
//						alt = null;
//					}
//				}
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public void validate() throws InvalidGrammarException
//	{
//		final ExtendedList<InvalidGrammarException> exs = new ExtendedList<InvalidGrammarException>();
//		InvalidGrammarException ex;
//		ex = r0();
//		if (ex != null)
//		{
//			exs.append(ex);
//		}
//		ex = r1();
//		if (ex != null)
//		{
//			exs.append(ex);
//		}
//		ex = r2();
//		if (ex != null)
//		{
//			exs.append(ex);
//		}
//		/*
//		 * ex = r3(); if (ex != null) exs.add(ex);
//		 */
//		ex = r4();
//		if (ex != null)
//		{
//			exs.append(ex);
//		}
//		ex = r5();
//		if (ex != null)
//		{
//			exs.append(ex);
//		}
//		if (exs.count() > 0)
//		{
//			int index = 1;
//			final InvalidGrammarException result = exs.get(0);
//			while (exs.count() > index)
//			{
//				result.insertMoreExceptions(exs.get(index++));
//			}
//			throw result;
//		}
//
//	}
// }

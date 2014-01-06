package org.ggll.parser.syntax.validation;

import org.ggll.parser.syntax.grammar.GrammarComponent;

import ggll.core.list.ExtendedList;

public class InvalidGrammarException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2582912391213112321L;

	private final String description;
	private int iteratorIndex;

	private final ExtendedList<InvalidGrammarException> nextExceptions = new ExtendedList<InvalidGrammarException>();
	private GrammarComponent problem;

	public InvalidGrammarException(String message, String description, GrammarComponent problem)
	{
		super(message);
		this.description = description;
		this.problem = problem;
	}

	public String getDescription()
	{
		return this.description;
	}

	public boolean hasNext()
	{
		return this.iteratorIndex < this.nextExceptions.count();
	}

	public void insertMoreExceptions(InvalidGrammarException ex)
	{
		this.nextExceptions.append(ex);
	}

	public InvalidGrammarException nextException()
	{
		if (hasNext())
		{
			return this.nextExceptions.get(this.iteratorIndex++);
		}
		return null;
	}

	public void resetIterator()
	{
		this.iteratorIndex = 0;
	}

	public void setGrComp(GrammarComponent problem)
	{
		this.problem = problem;
	}

	public String whereId()
	{
		return this.problem.getId().toString();
	}

	public String whereLabel()
	{
		return this.problem.getContents().toString();
	}

}

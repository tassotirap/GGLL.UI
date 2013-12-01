package ggll.ui.core.syntax.validation;

import ggll.core.list.ExtendedList;
import ggll.ui.core.syntax.grammar.GrammarComponent;

public class InvalidGrammarException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2582912391213112321L;

	private String description;
	private int iteratorIndex;

	private ExtendedList<InvalidGrammarException> nextExceptions = new ExtendedList<InvalidGrammarException>();
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
		return iteratorIndex < nextExceptions.count();
	}

	public void insertMoreExceptions(InvalidGrammarException ex)
	{
		nextExceptions.append(ex);
	}

	public InvalidGrammarException nextException()
	{
		if (hasNext())
		{
			return nextExceptions.get(iteratorIndex++);
		}
		return null;
	}

	public void resetIterator()
	{
		iteratorIndex = 0;
	}

	public void setGrComp(GrammarComponent problem)
	{
		this.problem = problem;
	}

	public String whereId()
	{
		return problem.getId().toString();
	}

	public String whereLabel()
	{
		return problem.getContents().toString();
	}

}

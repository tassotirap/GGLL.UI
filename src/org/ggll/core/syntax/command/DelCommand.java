package org.ggll.core.syntax.command;

import org.ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class DelCommand extends Command
{

	private String targetType;

	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.DeleteCommand_Description;
	}

	public String getTargetType()
	{
		return targetType;
	}

	public void setTargetType(String type)
	{
		targetType = type;
	}

}

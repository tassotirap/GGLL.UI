package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

public class AddRoutineCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.AddRoutineCommand_Description;
	}
}

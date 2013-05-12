package org.ggll.core.syntax.command;

import org.ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class AddRoutineCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.AddRoutineCommand_Description;
	}
}

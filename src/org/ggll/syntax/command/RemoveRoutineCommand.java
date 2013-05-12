package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

public class RemoveRoutineCommand extends Command
{

	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.RemoveRoutineCommand_Description;
	}
}

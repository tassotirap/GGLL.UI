package ggll.core.syntax.command;

import ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class AddRoutineCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.AddRoutineCommand_Description;
	}
}

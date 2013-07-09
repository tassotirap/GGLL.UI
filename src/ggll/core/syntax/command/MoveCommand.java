package ggll.core.syntax.command;

import ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class MoveCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.MoveCommand_Description;
	}

	@Override
	public boolean undo()
	{
		return true;
	}

}

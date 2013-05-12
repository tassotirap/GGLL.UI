package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

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

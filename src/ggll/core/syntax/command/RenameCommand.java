package ggll.core.syntax.command;

import ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class RenameCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.RenameCommand_Description;
	}
}

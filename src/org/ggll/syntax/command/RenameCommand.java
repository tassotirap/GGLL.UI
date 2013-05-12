package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

public class RenameCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.RenameCommand_Description;
	}
}

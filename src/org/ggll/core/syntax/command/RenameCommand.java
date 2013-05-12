package org.ggll.core.syntax.command;

import org.ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class RenameCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.RenameCommand_Description;
	}
}

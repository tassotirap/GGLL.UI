package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

public class DisconnectCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.DisconnectionCommand_Description;
	}
}

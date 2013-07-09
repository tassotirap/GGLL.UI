package ggll.core.syntax.command;

import ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class DisconnectCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.DisconnectionCommand_Description;
	}
}

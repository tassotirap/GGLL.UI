package ggll.core.syntax.command;

import ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class ConnectCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.ConnectionCommand_Description;
	}

}

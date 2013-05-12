package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

public class ConnectCommand extends Command
{
	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.ConnectionCommand_Description;
	}

}

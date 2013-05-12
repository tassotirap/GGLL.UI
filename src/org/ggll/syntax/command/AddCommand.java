package org.ggll.syntax.command;

import org.ggll.syntax.grammar.model.SyntaxDefinitions;

public class AddCommand extends Command
{

	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.AddCommand_Description;
	}

}

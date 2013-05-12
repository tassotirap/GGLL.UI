package org.ggll.core.syntax.command;

import org.ggll.core.syntax.grammar.model.SyntaxDefinitions;

public class AddCommand extends Command
{

	@Override
	public String getDescription()
	{
		return SyntaxDefinitions.AddCommand_Description;
	}

}

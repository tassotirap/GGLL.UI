package org.ggll.grammar.validation;

import org.ggll.syntax.graph.SyntaxGraphRepository;
import org.ggll.syntax.graph.state.StateNode;

public class NTerminalValidation extends GrammarValidation
{

	@Override
	public void validate()
	{
		for (final StateNode nTerminal : SyntaxGraphRepository.getNTerminals().getAll())
		{
			if(SyntaxGraphRepository.findLeftSide(nTerminal) == null)
			{
				addError("Non terminal don't have a left-side terminal.",  nTerminal);
			}			
		}
	}

}

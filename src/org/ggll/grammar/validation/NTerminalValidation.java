package org.ggll.grammar.validation;

import org.ggll.syntax.graph.state.StateNode;
import org.ggll.syntax.graph.state.StateHelper;

public class NTerminalValidation extends GrammarValidation
{

	@Override
	public void validate()
	{
		for (final StateNode nTerminal : StateHelper.getNTerminals().getAll())
		{
			if(StateHelper.findLeftSide(nTerminal) == null)
			{
				addError("Non terminal doesn't have a initial node.",  nTerminal);
			}			
		}
	}

}

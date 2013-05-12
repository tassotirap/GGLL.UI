package org.ggll.core.semantics;

import org.ggll.core.lexical.Yytoken;

public interface TokenListener
{

	public void setCurrentToken(Yytoken currentToken);
}

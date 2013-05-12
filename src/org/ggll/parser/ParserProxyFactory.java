package org.ggll.parser;

import org.ggll.ioadapter.IOAdapter;

public abstract class ParserProxyFactory
{

	public static ParserProxy create(IOAdapter adapter)
	{
		return new ParserProxyImpl();
	}
}

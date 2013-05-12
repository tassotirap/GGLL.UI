package org.ggll.ioadapter;

import org.ggll.parser.ParserProxy;

public interface InputAdapterImpl
{

	public void init();

	public void setInput(ParserProxy input);

	public void setObject(Object obj);
}

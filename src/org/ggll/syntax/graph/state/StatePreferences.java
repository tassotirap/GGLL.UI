package org.ggll.syntax.graph.state;

import java.io.Serializable;

public class StatePreferences implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String connectionStrategy;
	private String moveStrategy;
	private boolean showGrid;
	private boolean showLines;
	
	/**
	 * 
	 * @return Connection Strategy
	 */
	public String getConnectionStrategy()
	{
		return connectionStrategy;
	}
	
	/**
	 * @return the Move Strategy
	 */
	public String getMoveStrategy()
	{
		return moveStrategy;
	}
	
	/**
	 * @return the Show Grid
	 */
	public boolean isShowGrid()
	{
		return showGrid;
	}
	
	/**
	 * @return the Show Lines
	 */
	public boolean isShowLines()
	{
		return showLines;
	}
	
	/**
	 * @param set
	 *            Connection Strategy
	 */
	public void setConnectionStrategy(final String connectionStrategy)
	{
		this.connectionStrategy = connectionStrategy;
	}
	
	/**
	 * @param set
	 *            Move Strategy
	 */
	public void setMoveStrategy(final String moveStrategy)
	{
		this.moveStrategy = moveStrategy;
	}
	
	/**
	 * @param set
	 *            Show Grid
	 */
	public void setShowGrid(final boolean showGrid)
	{
		this.showGrid = showGrid;
	}
	
	/**
	 * @param set
	 *            Show Lines
	 */
	public void setShowLines(final boolean showLines)
	{
		this.showLines = showLines;
	}
}

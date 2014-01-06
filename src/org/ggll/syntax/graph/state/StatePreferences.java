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
		return this.connectionStrategy;
	}

	/**
	 * @return the Move Strategy
	 */
	public String getMoveStrategy()
	{
		return this.moveStrategy;
	}

	/**
	 * @return the Show Grid
	 */
	public boolean isShowGrid()
	{
		return this.showGrid;
	}

	/**
	 * @return the Show Lines
	 */
	public boolean isShowLines()
	{
		return this.showLines;
	}

	/**
	 * @param set Connection Strategy
	 */
	public void setConnectionStrategy(String connectionStrategy)
	{
		this.connectionStrategy = connectionStrategy;
	}

	/**
	 * @param set Move Strategy
	 */
	public void setMoveStrategy(String moveStrategy)
	{
		this.moveStrategy = moveStrategy;
	}

	/**
	 * @param set Show Grid
	 */
	public void setShowGrid(boolean showGrid)
	{
		this.showGrid = showGrid;
	}

	/**
	 * @param set Show Lines
	 */
	public void setShowLines(boolean showLines)
	{
		this.showLines = showLines;
	}
}

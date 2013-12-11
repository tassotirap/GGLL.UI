package ggll.ui.canvas.state;

import java.io.Serializable;

public class Preferences implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String connectionStrategy;
	private String moveStrategy;
	private boolean showGrid;

	private boolean showLines;
	private int showStatus;

	/**
	 * @return the connectionStrategy
	 */
	public String getConnectionStrategy()
	{
		return connectionStrategy;
	}

	/**
	 * @return the moveStrategy
	 */
	public String getMoveStrategy()
	{
		return moveStrategy;
	}

	/**
	 * @return the showStatus
	 */
	public int getShowStatus()
	{
		return showStatus;
	}

	/**
	 * @return the showGrid
	 */
	public boolean isShowGrid()
	{
		return showGrid;
	}

	/**
	 * @return the showLines
	 */
	public boolean isShowLines()
	{
		return showLines;
	}

	/**
	 * @param connectionStrategy
	 *            the connectionStrategy to set
	 */
	public void setConnectionStrategy(String connectionStrategy)
	{
		this.connectionStrategy = connectionStrategy;
	}

	/**
	 * @param moveStrategy
	 *            the moveStrategy to set
	 */
	public void setMoveStrategy(String moveStrategy)
	{
		this.moveStrategy = moveStrategy;
	}

	/**
	 * @param showGrid
	 *            the showGrid to set
	 */
	public void setShowGrid(boolean showGrid)
	{
		this.showGrid = showGrid;
	}

	/**
	 * @param showLines
	 *            the showLines to set
	 */
	public void setShowLines(boolean showLines)
	{
		this.showLines = showLines;
	}

	/**
	 * @param showStatus
	 *            the showStatus to set
	 */
	public void setShowStatus(int showStatus)
	{
		this.showStatus = showStatus;
	}

}

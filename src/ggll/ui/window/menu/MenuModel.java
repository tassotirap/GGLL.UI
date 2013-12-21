package ggll.ui.window.menu;

public class MenuModel
{
	private boolean copy;
	private boolean cut;
	private boolean ebnfExport;
	private boolean find;
	private boolean paste;
	private boolean pngExport;
	private boolean print;
	private boolean redo;
	private boolean save;
	private boolean saveAll;
	private boolean saveAs;
	private boolean undo;
	private boolean zoomIn;
	private boolean zoomOut;

	public boolean isCopy()
	{
		return this.copy;
	}

	public boolean isCut()
	{
		return this.cut;
	}

	public boolean isEbnfExport()
	{
		return this.ebnfExport;
	}

	public boolean isFind()
	{
		return this.find;
	}

	public boolean isPaste()
	{
		return this.paste;
	}

	public boolean isPngExport()
	{
		return this.pngExport;
	}

	public boolean isPrint()
	{
		return this.print;
	}

	public boolean isRedo()
	{
		return this.redo;
	}

	public boolean isSave()
	{
		return this.save;
	}

	public boolean isSaveAll()
	{
		return this.saveAll;
	}

	public boolean isSaveAs()
	{
		return this.saveAs;
	}

	public boolean isUndo()
	{
		return this.undo;
	}

	public boolean isZoomIn()
	{
		return this.zoomIn;
	}

	public boolean isZoomOut()
	{
		return this.zoomOut;
	}

	public void setCopy(boolean copy)
	{
		this.copy = copy;
	}

	public void setCut(boolean cut)
	{
		this.cut = cut;
	}

	public void setEbnfExport(boolean ebnfExport)
	{
		this.ebnfExport = ebnfExport;
	}

	public void setFind(boolean find)
	{
		this.find = find;
	}

	public void setPaste(boolean paste)
	{
		this.paste = paste;
	}

	public void setPngExport(boolean pngExport)
	{
		this.pngExport = pngExport;
	}

	public void setPrint(boolean print)
	{
		this.print = print;
	}

	public void setRedo(boolean redo)
	{
		this.redo = redo;
	}

	public void setSave(boolean save)
	{
		this.save = save;
	}

	public void setSaveAll(boolean saveAll)
	{
		this.saveAll = saveAll;
	}

	public void setSaveAs(boolean saveAs)
	{
		this.saveAs = saveAs;
	}

	public void setUndo(boolean undo)
	{
		this.undo = undo;
	}

	public void setZoomIn(boolean zoomIn)
	{
		this.zoomIn = zoomIn;
	}

	public void setZoomOut(boolean zoomOut)
	{
		this.zoomOut = zoomOut;
	}
}

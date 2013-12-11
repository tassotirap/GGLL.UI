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
		return copy;
	}

	public void setCopy(boolean copy)
	{
		this.copy = copy;
	}

	public boolean isCut()
	{
		return cut;
	}

	public void setCut(boolean cut)
	{
		this.cut = cut;
	}

	public boolean isEbnfExport()
	{
		return ebnfExport;
	}

	public void setEbnfExport(boolean ebnfExport)
	{
		this.ebnfExport = ebnfExport;
	}

	public boolean isFind()
	{
		return find;
	}

	public void setFind(boolean find)
	{
		this.find = find;
	}

	public boolean isPaste()
	{
		return paste;
	}

	public void setPaste(boolean paste)
	{
		this.paste = paste;
	}

	public boolean isPngExport()
	{
		return pngExport;
	}

	public void setPngExport(boolean pngExport)
	{
		this.pngExport = pngExport;
	}

	public boolean isPrint()
	{
		return print;
	}

	public void setPrint(boolean print)
	{
		this.print = print;
	}

	public boolean isRedo()
	{
		return redo;
	}

	public void setRedo(boolean redo)
	{
		this.redo = redo;
	}

	public boolean isSave()
	{
		return save;
	}

	public void setSave(boolean save)
	{
		this.save = save;
	}

	public boolean isSaveAll()
	{
		return saveAll;
	}

	public void setSaveAll(boolean saveAll)
	{
		this.saveAll = saveAll;
	}

	public boolean isSaveAs()
	{
		return saveAs;
	}

	public void setSaveAs(boolean saveAs)
	{
		this.saveAs = saveAs;
	}

	public boolean isUndo()
	{
		return undo;
	}

	public void setUndo(boolean undo)
	{
		this.undo = undo;
	}

	public boolean isZoomIn()
	{
		return zoomIn;
	}

	public void setZoomIn(boolean zoomIn)
	{
		this.zoomIn = zoomIn;
	}

	public boolean isZoomOut()
	{
		return zoomOut;
	}

	public void setZoomOut(boolean zoomOut)
	{
		this.zoomOut = zoomOut;
	}
}

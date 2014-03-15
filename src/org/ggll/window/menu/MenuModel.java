package org.ggll.window.menu;

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
	
	public boolean isCut()
	{
		return cut;
	}
	
	public boolean isEbnfExport()
	{
		return ebnfExport;
	}
	
	public boolean isFind()
	{
		return find;
	}
	
	public boolean isPaste()
	{
		return paste;
	}
	
	public boolean isPngExport()
	{
		return pngExport;
	}
	
	public boolean isPrint()
	{
		return print;
	}
	
	public boolean isRedo()
	{
		return redo;
	}
	
	public boolean isSave()
	{
		return save;
	}
	
	public boolean isSaveAll()
	{
		return saveAll;
	}
	
	public boolean isSaveAs()
	{
		return saveAs;
	}
	
	public boolean isUndo()
	{
		return undo;
	}
	
	public boolean isZoomIn()
	{
		return zoomIn;
	}
	
	public boolean isZoomOut()
	{
		return zoomOut;
	}
	
	public void setCopy(final boolean copy)
	{
		this.copy = copy;
	}
	
	public void setCut(final boolean cut)
	{
		this.cut = cut;
	}
	
	public void setEbnfExport(final boolean ebnfExport)
	{
		this.ebnfExport = ebnfExport;
	}
	
	public void setFind(final boolean find)
	{
		this.find = find;
	}
	
	public void setPaste(final boolean paste)
	{
		this.paste = paste;
	}
	
	public void setPngExport(final boolean pngExport)
	{
		this.pngExport = pngExport;
	}
	
	public void setPrint(final boolean print)
	{
		this.print = print;
	}
	
	public void setRedo(final boolean redo)
	{
		this.redo = redo;
	}
	
	public void setSave(final boolean save)
	{
		this.save = save;
	}
	
	public void setSaveAll(final boolean saveAll)
	{
		this.saveAll = saveAll;
	}
	
	public void setSaveAs(final boolean saveAs)
	{
		this.saveAs = saveAs;
	}
	
	public void setUndo(final boolean undo)
	{
		this.undo = undo;
	}
	
	public void setZoomIn(final boolean zoomIn)
	{
		this.zoomIn = zoomIn;
	}
	
	public void setZoomOut(final boolean zoomOut)
	{
		this.zoomOut = zoomOut;
	}
}

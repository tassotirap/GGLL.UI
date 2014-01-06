package org.ggll.parser.syntax.grammar.model;

/** Defines a label for a node **/
public class NodeLabel extends SyntaxSubpart
{

	private static int count;

	private static final long serialVersionUID = 1L;

	private String text = SyntaxDefinitions.AsinPlugin_Tool_CreationTool_AsinLabel;

	public NodeLabel(String label)
	{
		super(getNewID());
		setLabelContents(label);
	}

	public static String getNewID()
	{
		return Integer.toString(count++);
	}

	@Override
	public String getID()
	{
		return "";
	}

	public String getLabelContents()
	{
		return this.text;
	}

	public void setLabelContents(String s)
	{
		this.text = s;
		firePropertyChange("labelContents", "", this.text);
	}

	@Override
	public String toString()
	{
		return SyntaxDefinitions.AsinPlugin_Tool_CreationTool_AsinLabel + " #" + getID() + " " + SyntaxDefinitions.PropertyDescriptor_Label_Text + "=" + getLabelContents();
	}

}
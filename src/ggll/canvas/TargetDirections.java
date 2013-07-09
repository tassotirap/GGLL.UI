package ggll.canvas;

import java.util.ArrayList;
import java.util.HashMap;

import org.netbeans.api.visual.anchor.Anchor.Direction;
import org.netbeans.api.visual.widget.ConnectionWidget;

public class TargetDirections
{
	private static HashMap<String, ArrayList<Direction>> map = new HashMap<String, ArrayList<Direction>>();
	private static ArrayList<String> connections = new ArrayList<String>();

	public static ArrayList<String> getConnections()
	{
		return connections;
	}

	public static HashMap<String, ArrayList<Direction>> getMaps()
	{
		return map;
	}

	public static void removeConnection(String cnnString, ConnectionWidget cnnObj)
	{
		if (cnnObj != null)
		{
			UnidirectionalAnchor anchor = (UnidirectionalAnchor) cnnObj.getTargetAnchor();
			if (anchor != null)
			{
				if (TargetDirections.getMaps().containsKey(anchor.getTarget()) && TargetDirections.getMaps().get(anchor.getTarget()).contains(anchor.getKind()))
				{
					TargetDirections.getMaps().get(anchor.getTarget()).remove(anchor.getKind());
				}
			}
		}
		if (TargetDirections.getConnections().contains(cnnString))
		{
			TargetDirections.getConnections().remove(cnnString);
		}
	}
}

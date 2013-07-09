/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2007 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package ggll.canvas.provider;

import ggll.canvas.Canvas;
import ggll.canvas.CanvasFactory;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.netbeans.api.visual.action.RectangularSelectProvider;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;

public final class CanvasRectangularSelectProvider implements RectangularSelectProvider
{

	public CanvasRectangularSelectProvider(Canvas canvas)
	{
	}

	@Override
	public void performSelection(Rectangle sceneSelection)
	{
		Canvas canvas = CanvasFactory.getCanvas();
		boolean entirely = sceneSelection.width > 0;
		int w = sceneSelection.width;
		int h = sceneSelection.height;
		Rectangle rect = new Rectangle(w >= 0 ? 0 : w, h >= 0 ? 0 : h, w >= 0 ? w : -w, h >= 0 ? h : -h);
		rect.translate(sceneSelection.x, sceneSelection.y);

		HashSet<Object> set = new HashSet<Object>();
		Set<?> objects = canvas.getObjects();
		for (Object object : objects)
		{
			Widget widget = canvas.findWidget(object);
			if (widget == null)
				continue;
			if (entirely)
			{
				Rectangle widgetRect = widget.convertLocalToScene(widget.getBounds());
				if (rect.contains(widgetRect))
					set.add(object);
			}
			else
			{
				if (widget instanceof ConnectionWidget)
				{
					ConnectionWidget conn = (ConnectionWidget) widget;
					java.util.List<Point> points = conn.getControlPoints();
					for (int i = points.size() - 2; i >= 0; i--)
					{
						Point p1 = widget.convertLocalToScene(points.get(i));
						Point p2 = widget.convertLocalToScene(points.get(i + 1));
						if (new Line2D.Float(p1, p2).intersects(rect))
							set.add(object);
					}
				}
				else
				{
					Rectangle widgetRect = widget.convertLocalToScene(widget.getBounds());
					if (rect.intersects(widgetRect))
						set.add(object);
				}
			}
		}
		Iterator<Object> iterator = set.iterator();
		canvas.setFocusedObject(iterator.hasNext() ? iterator.next() : null);
		canvas.userSelectionSuggested(set, false);
		for (Object o : canvas.getLabels())
		{
			LabelWidget lw = ((LabelWidget) canvas.findWidget(o));
			lw.setBorder(BorderFactory.createEmptyBorder());
		}
		for (Object o : canvas.getNodes())
		{
			Widget lw = canvas.findWidget(o);
			if (lw instanceof LabelWidget)
			{
				lw.setBackground(Color.WHITE);
				lw.setForeground(Color.BLACK);
			}
		}
		for (Object o : canvas.getSelectedObjects())
		{
			if (canvas.isNode(o) || canvas.isLabel(o))
			{
				Widget lw = CanvasFactory.getCanvas().findWidget(o);
				if (canvas.isLabel(o))
				{
					lw.setForeground(Color.BLUE);
					((LabelWidget) canvas.findWidget(o)).setBorder(BorderFactory.createLineBorder(1, Color.BLUE));
				}
				else if (lw instanceof LabelWidget)
				{
					lw.setBackground(Color.BLUE);
					lw.setForeground(Color.WHITE);
				}
			}
		}
	}

}
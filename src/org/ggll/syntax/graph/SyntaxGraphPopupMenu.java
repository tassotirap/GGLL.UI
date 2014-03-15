package org.ggll.syntax.graph;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import org.ggll.facade.GGLLFacade;
import org.ggll.grammar.GrammarParser;
import org.ggll.resource.CanvasResource;
import org.ggll.syntax.graph.provider.GridProvider;
import org.ggll.syntax.graph.provider.LineProvider;
import org.ggll.syntax.graph.provider.WidgetDeleteProvider;
import org.ggll.syntax.graph.state.StateHistory;
import org.ggll.syntax.graph.widget.MarkedWidget;
import org.ggll.window.RoutineWindow;
import org.ggll.window.view.AbstractView;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class SyntaxGraphPopupMenu extends WidgetAction.Adapter implements PopupMenuProvider
{
	
	final JPopupMenu popup = new JPopupMenu();
	private final SyntaxGraph canvas;
	private final GridProvider gridProvider;
	private final LineProvider lineProvider;
	
	private Widget widget;
	
	public SyntaxGraphPopupMenu(final SyntaxGraph canvas)
	{
		this.canvas = canvas;
		gridProvider = GridProvider.getInstance(canvas);
		lineProvider = LineProvider.getInstance(canvas);
	}
	
	private JMenuItem createBuildAndParseMenu()
	{
		final JMenuItem grammarMenu = new JMenuItem("Build and Parse");
		grammarMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				final Cursor oldCursor = canvas.getView().getCursor();
				canvas.getView().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				final GrammarParser syntaxParser = new GrammarParser();
				syntaxParser.parseGrammar();
				canvas.getView().setCursor(oldCursor);
			}
		});
		return grammarMenu;
	}
	
	private JMenuItem createDeleteMenu()
	{
		final JMenuItem deleteMenu = new JMenuItem("Delete");
		deleteMenu.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				final WidgetDeleteProvider wdp = new WidgetDeleteProvider(canvas);
				if (widget.getState().isSelected() && wdp.isDeletionAllowed())
				{
					wdp.deleteSelected();
				}
				else if (wdp.isDeletionAllowed(widget))
				{
					wdp.deleteThese(widget);
				}
			}
		});
		return deleteMenu;
	}
	
	private JMenu createMovingMenu()
	{
		final JMenu movingMenu = new JMenu("Move Policy");
		
		final JRadioButtonMenuItem freeMenuItem = new JRadioButtonMenuItem("Free Move");
		final JRadioButtonMenuItem snapMenuItem = new JRadioButtonMenuItem("Snap To Grid");
		final JRadioButtonMenuItem alignMenuItem = new JRadioButtonMenuItem("Auto Align");
		final JRadioButtonMenuItem linesMenuItem = new JRadioButtonMenuItem("Snap To Lines");
		
		freeMenuItem.setSelected(canvas.getCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		snapMenuItem.setSelected(canvas.getCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_SNAP));
		alignMenuItem.setSelected(canvas.getCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_ALIGN));
		linesMenuItem.setSelected(canvas.getCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_LINES));
		
		movingMenu.add(freeMenuItem);
		movingMenu.add(snapMenuItem);
		movingMenu.add(alignMenuItem);
		movingMenu.add(linesMenuItem);
		
		freeMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.removeAllLines();
				canvas.setMoveStrategy(CanvasResource.M_FREE);
			}
		});
		
		snapMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(true);
				lineProvider.removeAllLines();
				canvas.setMoveStrategy(CanvasResource.M_SNAP);
			}
		});
		
		alignMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.removeAllLines();
				canvas.setMoveStrategy(CanvasResource.M_ALIGN);
			}
		});
		
		linesMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.populateCanvas();
				canvas.setMoveStrategy(CanvasResource.M_LINES);
			}
		});
		return movingMenu;
	}
	
	private JMenuItem createRedoMenu()
	{
		final StateHistory volatileStateManager = canvas.getCanvasStateHistory();
		final JMenuItem redoMenu = new JMenuItem();
		redoMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if (volatileStateManager.hasNextRedo())
				{
					volatileStateManager.redo();
				}
			}
		});
		if (!volatileStateManager.hasNextRedo())
		{
			redoMenu.setEnabled(false);
			redoMenu.setText("Redo");
		}
		else
		{
			redoMenu.setText("Redo ");
		}
		return redoMenu;
	}
	
	private JMenu createRoutingMenu()
	{
		final JMenu routingMenu = new JMenu("Routing Policy");
		
		final JRadioButtonMenuItem ortoMenuItem = new JRadioButtonMenuItem("Ortogonal");
		final JRadioButtonMenuItem directMenuItem = new JRadioButtonMenuItem("Direct");
		final JRadioButtonMenuItem freeMenuItem = new JRadioButtonMenuItem("Free");
		
		ortoMenuItem.setSelected(canvas.getCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_ORTHOGONAL));
		directMenuItem.setSelected(canvas.getCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_DIRECT));
		freeMenuItem.setSelected(canvas.getCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_FREE));
		
		routingMenu.add(ortoMenuItem);
		routingMenu.add(directMenuItem);
		routingMenu.add(freeMenuItem);
		
		ortoMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				canvas.setConnectionStrategy(CanvasResource.R_ORTHOGONAL);
			}
		});
		
		directMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				canvas.setConnectionStrategy(CanvasResource.R_DIRECT);
			}
		});
		
		freeMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				canvas.setConnectionStrategy(CanvasResource.R_FREE);
			}
		});
		
		return routingMenu;
	}
	
	private JMenuItem createSemanticRoutinesMenu()
	{
		boolean hasSemanticRoutine = false;
		boolean isMarkedWidget = false;
		final MarkedWidget markedWidget = (MarkedWidget) widget;
		String mark = null;
		
		if (widget instanceof MarkedWidget)
		{
			isMarkedWidget = true;
			if ((mark = ((MarkedWidget) widget).getMark()) != null && !mark.equals(""))
			{
				hasSemanticRoutine = true;
			}
		}
		
		final JMenu semanticRoutinesMenu = new JMenu("Semantic Routines");
		final JMenuItem createSemanticRoutine = new JMenuItem("Create New...");
		final JMenuItem removeSemanticRoutine = new JMenuItem();
		final JMenuItem editSemanticRoutine = new JMenuItem();
		
		if (hasSemanticRoutine)
		{
			removeSemanticRoutine.setText("Remove " + mark);
		}
		else
		{
			removeSemanticRoutine.setText("Remove");
			removeSemanticRoutine.setEnabled(false);
		}
		
		if (hasSemanticRoutine)
		{
			editSemanticRoutine.setText("Edit " + mark + "...");
		}
		else
		{
			editSemanticRoutine.setText("Edit...");
			editSemanticRoutine.setEnabled(false);
		}
		
		semanticRoutinesMenu.add(createSemanticRoutine);
		semanticRoutinesMenu.add(removeSemanticRoutine);
		semanticRoutinesMenu.add(editSemanticRoutine);
		semanticRoutinesMenu.add(new JSeparator());
		
		final Set<String> semanticRoutinesNames = GGLLFacade.getInstance().getSemanticFile().getRegRoutines();
		for (final String semanticRoutineName : semanticRoutinesNames)
		{
			final JMenuItem semanticRoutinesNamesMenuItem = new JMenuItem("Use " + semanticRoutineName);
			semanticRoutinesNamesMenuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(final ActionEvent e)
				{
					markedWidget.setMark(semanticRoutineName);
					canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "AddRoutine"));
				}
			});
			semanticRoutinesMenu.add(semanticRoutinesNamesMenuItem);
		}
		
		if (!isMarkedWidget)
		{
			semanticRoutinesMenu.setEnabled(false);
		}
		
		createSemanticRoutine.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				String semFile = null;
				if (GGLLFacade.getInstance().getSemanticFile() != null)
				{
					semFile = GGLLFacade.getInstance().getSemanticFile().getAbsolutePath();
				}
				if (semFile != null)
				{
					final AbstractView abstractComponent = GGLLFacade.getInstance().getUnsavedView(semFile);
					if (abstractComponent != null)
					{
						final int option = JOptionPane.showConfirmDialog(popup, "A new semantic routine can not be created while the semantic routines file remains unsaved.\nWould you like to save it now?", "Can not create a new routine", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (option == JOptionPane.YES_OPTION)
						{
							GGLLFacade.getInstance().saveFile(abstractComponent.getComponentModel());
						}
						else
						{
							return;
						}
					}
				}
				if (semFile != null)
				{
					new RoutineWindow((String) canvas.findObject(widget), markedWidget, null, canvas);
				}
			}
		});
		
		removeSemanticRoutine.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				markedWidget.setMark(null);
				canvas.getCanvasStateHistory().propertyChange(new PropertyChangeEvent(this, "undoable", null, "RemoveRoutine"));
			}
		});
		
		editSemanticRoutine.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				String semFile = null;
				if (GGLLFacade.getInstance().getSemanticFile() != null)
				{
					semFile = GGLLFacade.getInstance().getSemanticFile().getAbsolutePath();
				}
				if (semFile != null)
				{
					final AbstractView abstractComponent = GGLLFacade.getInstance().getUnsavedView(semFile);
					if (abstractComponent != null)
					{
						final int option = JOptionPane.showConfirmDialog(popup, "A semantic routine can not be edited while the semantic routines file remains unsaved.\nWould you like to save it now?", "Can not create a new routine", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (option == JOptionPane.YES_OPTION)
						{
							GGLLFacade.getInstance().saveFile(abstractComponent.getComponentModel());
						}
						else
						{
							return;
						}
					}
					else
					{
						new RoutineWindow((String) canvas.findObject(widget), markedWidget, markedWidget.getMark(), canvas);
					}
				}
			}
		});
		return semanticRoutinesMenu;
	}
	
	private JMenu createShowMenu()
	{
		final JMenu showMenu = new JMenu("Show");
		
		final JRadioButtonMenuItem nothingMenuItem = new JRadioButtonMenuItem("Nothing");
		final JRadioButtonMenuItem gridMenuItem = new JRadioButtonMenuItem("Grid");
		final JRadioButtonMenuItem lineMenuItem = new JRadioButtonMenuItem("Lines");
		
		nothingMenuItem.setSelected(!canvas.isShowingGrid() && !canvas.isShowingLines());
		gridMenuItem.setEnabled(canvas.getCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		gridMenuItem.setSelected(canvas.isShowingGrid());
		lineMenuItem.setEnabled(canvas.getCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		lineMenuItem.setSelected(canvas.isShowingLines());
		
		showMenu.add(nothingMenuItem);
		showMenu.add(gridMenuItem);
		showMenu.add(lineMenuItem);
		
		nothingMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.removeAllLines();
			}
		});
		
		gridMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(true);
				lineProvider.removeAllLines();
			}
		});
		
		lineMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.populateCanvas();
			}
		});
		
		return showMenu;
	}
	
	private JMenuItem createUndoMenu()
	{
		final StateHistory volatileStateManager = canvas.getCanvasStateHistory();
		final JMenuItem undoMenu = new JMenuItem();
		undoMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if (volatileStateManager.hasNextUndo())
				{
					volatileStateManager.undo();
				}
			}
		});
		if (!volatileStateManager.hasNextUndo())
		{
			undoMenu.setEnabled(false);
			undoMenu.setText("Undo");
		}
		else
		{
			undoMenu.setText("Undo");
		}
		return undoMenu;
	}
	
	@Override
	public JPopupMenu getPopupMenu(final Widget widget, final Point localLocation)
	{
		Object focusedWidget = canvas.getFocusedObject();
		Widget activeWidget = canvas.findWidget(focusedWidget != widget ? focusedWidget : null);
		if (activeWidget == null)
		{
			for (final String string : canvas.getNodes())
			{
				
				final Widget tempWidget = canvas.findWidget(string);
				if (tempWidget.getPreferredLocation() != null && tempWidget.getPreferredBounds() != null)
				{
					final Rectangle area = new Rectangle();
					area.x = tempWidget.getPreferredLocation().x;
					area.y = tempWidget.getPreferredLocation().y;
					area.height = tempWidget.getPreferredBounds().height;
					area.width = tempWidget.getPreferredBounds().width;
					if (tempWidget.getBorder() != null)
					{
						area.height += tempWidget.getBorder().getInsets().bottom + tempWidget.getBorder().getInsets().top;
						area.width += tempWidget.getBorder().getInsets().left + tempWidget.getBorder().getInsets().right;
					}
					if (area.x <= localLocation.x && area.y >= localLocation.y && area.x + area.width >= localLocation.x && area.y - area.height <= localLocation.y)
					{
						activeWidget = tempWidget;
						focusedWidget = string;
						break;
					}
				}
			}
		}
		popup.removeAll();
		if (activeWidget != null && !canvas.isLabel(focusedWidget) && activeWidget instanceof MarkedWidget)
		{
			this.widget = activeWidget;
			popup.add(createDeleteMenu());
			popup.add(new JSeparator());
			
			popup.add(createSemanticRoutinesMenu());
			popup.add(new JSeparator());
		}
		popup.add(createUndoMenu());
		popup.add(createRedoMenu());
		popup.add(new JSeparator());
		popup.add(createBuildAndParseMenu());
		popup.add(new JSeparator());
		popup.add(createShowMenu());
		popup.add(createRoutingMenu());
		popup.add(createMovingMenu());
		
		return popup;
	}
	
}

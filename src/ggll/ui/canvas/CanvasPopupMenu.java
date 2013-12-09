package ggll.ui.canvas;

import ggll.ui.canvas.provider.GridProvider;
import ggll.ui.canvas.provider.LineProvider;
import ggll.ui.canvas.provider.WidgetCopyPasteProvider;
import ggll.ui.canvas.provider.WidgetDeleteProvider;
import ggll.ui.canvas.state.CanvasStateRepository;
import ggll.ui.canvas.widget.MarkedWidget;
import ggll.ui.core.syntax.grammar.Controller;
import ggll.ui.project.Context;
import ggll.ui.resource.CanvasResource;
import ggll.ui.view.AbstractView;
import ggll.ui.wizard.RoutineWizard;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;

import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class CanvasPopupMenu extends WidgetAction.Adapter implements PopupMenuProvider
{

	final JPopupMenu popup = new JPopupMenu();
	private Canvas canvas;
	private GridProvider gridProvider;
	private LineProvider lineProvider;
	private PropertyChangeSupport monitor;

	private Widget widget;

	public CanvasPopupMenu(Canvas canvas)
	{
		this.canvas = canvas;
		gridProvider = GridProvider.getInstance(canvas);
		lineProvider = LineProvider.getInstance(canvas);
		monitor = new PropertyChangeSupport(this);
		monitor.addPropertyChangeListener(canvas.getCanvasStateRepository());
	}

	private JMenuItem createBuildAndParseMenu()
	{
		JMenuItem grammarMenu = new JMenuItem("Build and Parse");
		grammarMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Cursor oldCursor = canvas.getView().getCursor();
				canvas.getView().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Controller.generateAndParseCurrentGrammar();
				canvas.getView().setCursor(oldCursor);
			}
		});
		return grammarMenu;
	}

	private JMenuItem createCopyMenu()
	{
		JMenuItem copyMenu = new JMenuItem("Copy");
		copyMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				WidgetCopyPasteProvider wcpp = new WidgetCopyPasteProvider(canvas);
				if (widget.getState().isSelected())
				{
					wcpp.copySelected();
				}
				else
				{
					wcpp.copyThese(widget);
				}
			}
		});
		return copyMenu;
	}

	private JMenuItem createCutMenu()
	{
		JMenuItem cutMenu = new JMenuItem("Cut");
		cutMenu.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				WidgetCopyPasteProvider wcpp = new WidgetCopyPasteProvider(canvas);
				WidgetDeleteProvider wdp = new WidgetDeleteProvider(canvas);
				if (widget.getState().isSelected() && wdp.isDeletionAllowed())
				{
					wcpp.cutSelected(wdp);
				}
				else if (wdp.isDeletionAllowed(widget))
				{
					wcpp.cutThese(wdp, widget);
				}
			}
		});
		return cutMenu;
	}

	private JMenuItem createDeleteMenu()
	{
		JMenuItem deleteMenu = new JMenuItem("Delete");
		deleteMenu.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				WidgetDeleteProvider wdp = new WidgetDeleteProvider(canvas);
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
		JMenu movingMenu = new JMenu("Move Policy");

		JRadioButtonMenuItem freeMenuItem = new JRadioButtonMenuItem("Free Move");
		JRadioButtonMenuItem snapMenuItem = new JRadioButtonMenuItem("Snap To Grid");
		JRadioButtonMenuItem alignMenuItem = new JRadioButtonMenuItem("Auto Align");
		JRadioButtonMenuItem linesMenuItem = new JRadioButtonMenuItem("Snap To Lines");

		freeMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		snapMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_SNAP));
		alignMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_ALIGN));
		linesMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_LINES));

		movingMenu.add(freeMenuItem);
		movingMenu.add(snapMenuItem);
		movingMenu.add(alignMenuItem);
		movingMenu.add(linesMenuItem);

		freeMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.removeAllLines();
				canvas.setMoveStrategy(CanvasResource.M_FREE);
			}
		});

		snapMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(true);
				lineProvider.removeAllLines();
				canvas.setMoveStrategy(CanvasResource.M_SNAP);
			}
		});

		alignMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.removeAllLines();
				canvas.setMoveStrategy(CanvasResource.M_ALIGN);
			}
		});

		linesMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.populateCanvas();
				canvas.setMoveStrategy(CanvasResource.M_LINES);
			}
		});
		return movingMenu;
	}

	private JMenuItem createPasteMenu(final Point localLocation)
	{
		JMenuItem pasteMenu = new JMenuItem("Paste");
		pasteMenu.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				WidgetCopyPasteProvider widgetCopyPasteProvider = new WidgetCopyPasteProvider(canvas);
				widgetCopyPasteProvider.paste(localLocation);
			}
		});
		return pasteMenu;
	}

	private JMenuItem createRedoMenu()
	{
		final CanvasStateRepository volatileStateManager = canvas.getCanvasStateRepository();
		JMenuItem redoMenu = new JMenuItem();
		redoMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
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
		JMenu routingMenu = new JMenu("Routing Policy");

		JRadioButtonMenuItem ortoMenuItem = new JRadioButtonMenuItem("Ortogonal");
		JRadioButtonMenuItem directMenuItem = new JRadioButtonMenuItem("Direct");
		JRadioButtonMenuItem freeMenuItem = new JRadioButtonMenuItem("Free");

		ortoMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_ORTHOGONAL));
		directMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_DIRECT));
		freeMenuItem.setSelected(canvas.getCurrentCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_FREE));

		routingMenu.add(ortoMenuItem);
		routingMenu.add(directMenuItem);
		routingMenu.add(freeMenuItem);

		ortoMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				canvas.setConnectionStrategy(CanvasResource.R_ORTHOGONAL);
			}
		});

		directMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				canvas.setConnectionStrategy(CanvasResource.R_DIRECT);
			}
		});

		freeMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
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

		if ((widget instanceof MarkedWidget))
		{
			isMarkedWidget = true;
			if ((mark = ((MarkedWidget) widget).getMark()) != null && !mark.equals(""))
			{
				hasSemanticRoutine = true;
			}
		}

		JMenu semanticRoutinesMenu = new JMenu("Semantic Routines");
		JMenuItem createSemanticRoutine = new JMenuItem("Create New...");
		JMenuItem removeSemanticRoutine = new JMenuItem();
		JMenuItem editSemanticRoutine = new JMenuItem();

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

		Set<String> semanticRoutinesNames = Context.getProject().getSemanticFile().getRegRoutines();
		for (final String semanticRoutineName : semanticRoutinesNames)
		{
			JMenuItem semanticRoutinesNamesMenuItem = new JMenuItem("Use " + semanticRoutineName);
			semanticRoutinesNamesMenuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					markedWidget.setMark(semanticRoutineName);
					monitor.firePropertyChange("undoable", null, "AddRoutine");
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
			public void actionPerformed(ActionEvent e)
			{
				String semFile = null;
				if (Context.getProject().getSemanticFile() != null)
				{
					semFile = Context.getProject().getSemanticFile().getAbsolutePath();
				}
				if (semFile != null)
				{
					AbstractView abstractComponent = Context.getUnsavedView(semFile);
					if (abstractComponent != null)
					{
						int option = JOptionPane.showConfirmDialog(popup, "A new semantic routine can not be created while the semantic routines file remains unsaved.\nWould you like to save it now?", "Can not create a new routine", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (option == JOptionPane.YES_OPTION)
						{
							Context.saveFile(abstractComponent.getComponentModel());
						}
						else
						{
							return;
						}
					}
				}
				if (semFile != null)
				{
					new RoutineWizard((String) canvas.findObject(widget), markedWidget, null, monitor);
				}
			}
		});

		removeSemanticRoutine.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				markedWidget.setMark(null);
				monitor.firePropertyChange("undoable", null, "RemoveRoutine");
			}
		});

		editSemanticRoutine.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (Context.getProject() != null)
				{
					String semFile = null;
					if (Context.getProject().getSemanticFile() != null)
					{
						semFile = Context.getProject().getSemanticFile().getAbsolutePath();
					}
					if (semFile != null)
					{
						AbstractView abstractComponent = Context.getUnsavedView(semFile);
						if (abstractComponent != null)
						{
							int option = JOptionPane.showConfirmDialog(popup, "A semantic routine can not be edited while the semantic routines file remains unsaved.\nWould you like to save it now?", "Can not create a new routine", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (option == JOptionPane.YES_OPTION)
							{
								Context.saveFile(abstractComponent.getComponentModel());
							}
							else
							{
								return;
							}
						}
						else
						{
							new RoutineWizard((String) canvas.findObject(widget), markedWidget, markedWidget.getMark(), monitor);
						}
					}
				}
			}
		});
		return semanticRoutinesMenu;
	}

	private JMenu createShowMenu()
	{
		JMenu showMenu = new JMenu("Show");

		JRadioButtonMenuItem nothingMenuItem = new JRadioButtonMenuItem("Nothing");
		JRadioButtonMenuItem gridMenuItem = new JRadioButtonMenuItem("Grid");
		JRadioButtonMenuItem lineMenuItem = new JRadioButtonMenuItem("Lines");

		nothingMenuItem.setSelected((!canvas.isShowingGrid() && !canvas.isShowingLines()));
		gridMenuItem.setEnabled(canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		gridMenuItem.setSelected(canvas.isShowingGrid());
		lineMenuItem.setEnabled(canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		lineMenuItem.setSelected(canvas.isShowingLines());

		showMenu.add(nothingMenuItem);
		showMenu.add(gridMenuItem);
		showMenu.add(lineMenuItem);

		nothingMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.removeAllLines();
			}
		});

		gridMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(true);
				lineProvider.removeAllLines();
			}
		});

		lineMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				gridProvider.setVisible(false);
				lineProvider.populateCanvas();
			}
		});

		return showMenu;
	}

	private JMenuItem createUndoMenu()
	{
		final CanvasStateRepository volatileStateManager = canvas.getCanvasStateRepository();
		JMenuItem undoMenu = new JMenuItem();
		undoMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
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
	public JPopupMenu getPopupMenu(Widget widget, Point localLocation)
	{
		Object focusedWidget = canvas.getFocusedObject();
		Widget activeWidget = canvas.findWidget((focusedWidget != widget) ? focusedWidget : null);
		if (activeWidget == null)
		{
			for (String string : canvas.getNodes())
			{

				Widget tempWidget = canvas.findWidget(string);
				if (tempWidget.getPreferredLocation() != null && tempWidget.getPreferredBounds() != null)
				{
					Rectangle area = new Rectangle();
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
			popup.add(createCopyMenu());
			popup.add(createCutMenu());
			popup.add(new JSeparator());
			popup.add(createDeleteMenu());
			popup.add(new JSeparator());

			popup.add(createSemanticRoutinesMenu());
			popup.add(new JSeparator());
		}
		popup.add(createUndoMenu());
		popup.add(createRedoMenu());
		popup.add(new JSeparator());
		popup.add(createPasteMenu(localLocation));
		popup.add(new JSeparator());
		popup.add(createBuildAndParseMenu());
		popup.add(new JSeparator());
		popup.add(createShowMenu());
		popup.add(createRoutingMenu());
		popup.add(createMovingMenu());

		return popup;
	}

}

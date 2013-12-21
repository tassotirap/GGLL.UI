package ggll.ui.canvas;

import ggll.ui.canvas.provider.GridProvider;
import ggll.ui.canvas.provider.LineProvider;
import ggll.ui.canvas.provider.WidgetDeleteProvider;
import ggll.ui.canvas.state.CanvasStateRepository;
import ggll.ui.canvas.widget.MarkedWidget;
import ggll.ui.core.syntax.grammar.Controller;
import ggll.ui.director.GGLLDirector;
import ggll.ui.resource.CanvasResource;
import ggll.ui.view.AbstractView;
import ggll.ui.wizard.RoutineWindow;

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

import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;

public class CanvasPopupMenu extends WidgetAction.Adapter implements PopupMenuProvider
{

	final JPopupMenu popup = new JPopupMenu();
	private final Canvas canvas;
	private final GridProvider gridProvider;
	private final LineProvider lineProvider;

	private Widget widget;

	public CanvasPopupMenu(Canvas canvas)
	{
		this.canvas = canvas;
		this.gridProvider = GridProvider.getInstance(canvas);
		this.lineProvider = LineProvider.getInstance(canvas);
	}

	private JMenuItem createBuildAndParseMenu()
	{
		final JMenuItem grammarMenu = new JMenuItem("Build and Parse");
		grammarMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				final Cursor oldCursor = CanvasPopupMenu.this.canvas.getView().getCursor();
				CanvasPopupMenu.this.canvas.getView().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Controller.generateAndParseCurrentGrammar();
				CanvasPopupMenu.this.canvas.getView().setCursor(oldCursor);
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
			public void actionPerformed(ActionEvent e)
			{
				final WidgetDeleteProvider wdp = new WidgetDeleteProvider(CanvasPopupMenu.this.canvas);
				if (CanvasPopupMenu.this.widget.getState().isSelected() && wdp.isDeletionAllowed())
				{
					wdp.deleteSelected();
				}
				else if (wdp.isDeletionAllowed(CanvasPopupMenu.this.widget))
				{
					wdp.deleteThese(CanvasPopupMenu.this.widget);
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

		freeMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		snapMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_SNAP));
		alignMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_ALIGN));
		linesMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_LINES));

		movingMenu.add(freeMenuItem);
		movingMenu.add(snapMenuItem);
		movingMenu.add(alignMenuItem);
		movingMenu.add(linesMenuItem);

		freeMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(false);
				CanvasPopupMenu.this.lineProvider.removeAllLines();
				CanvasPopupMenu.this.canvas.setMoveStrategy(CanvasResource.M_FREE);
			}
		});

		snapMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(true);
				CanvasPopupMenu.this.lineProvider.removeAllLines();
				CanvasPopupMenu.this.canvas.setMoveStrategy(CanvasResource.M_SNAP);
			}
		});

		alignMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(false);
				CanvasPopupMenu.this.lineProvider.removeAllLines();
				CanvasPopupMenu.this.canvas.setMoveStrategy(CanvasResource.M_ALIGN);
			}
		});

		linesMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(false);
				CanvasPopupMenu.this.lineProvider.populateCanvas();
				CanvasPopupMenu.this.canvas.setMoveStrategy(CanvasResource.M_LINES);
			}
		});
		return movingMenu;
	}

	private JMenuItem createRedoMenu()
	{
		final CanvasStateRepository volatileStateManager = this.canvas.getCanvasStateRepository();
		final JMenuItem redoMenu = new JMenuItem();
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
		final JMenu routingMenu = new JMenu("Routing Policy");

		final JRadioButtonMenuItem ortoMenuItem = new JRadioButtonMenuItem("Ortogonal");
		final JRadioButtonMenuItem directMenuItem = new JRadioButtonMenuItem("Direct");
		final JRadioButtonMenuItem freeMenuItem = new JRadioButtonMenuItem("Free");

		ortoMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_ORTHOGONAL));
		directMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_DIRECT));
		freeMenuItem.setSelected(this.canvas.getCurrentCanvasState().getPreferences().getConnectionStrategy().equals(CanvasResource.R_FREE));

		routingMenu.add(ortoMenuItem);
		routingMenu.add(directMenuItem);
		routingMenu.add(freeMenuItem);

		ortoMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.canvas.setConnectionStrategy(CanvasResource.R_ORTHOGONAL);
			}
		});

		directMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.canvas.setConnectionStrategy(CanvasResource.R_DIRECT);
			}
		});

		freeMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.canvas.setConnectionStrategy(CanvasResource.R_FREE);
			}
		});

		return routingMenu;
	}

	private JMenuItem createSemanticRoutinesMenu()
	{
		boolean hasSemanticRoutine = false;
		boolean isMarkedWidget = false;
		final MarkedWidget markedWidget = (MarkedWidget) this.widget;
		String mark = null;

		if (this.widget instanceof MarkedWidget)
		{
			isMarkedWidget = true;
			if ((mark = ((MarkedWidget) this.widget).getMark()) != null && !mark.equals(""))
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

		final Set<String> semanticRoutinesNames = GGLLDirector.getProject().getSemanticFile().getRegRoutines();
		for (final String semanticRoutineName : semanticRoutinesNames)
		{
			final JMenuItem semanticRoutinesNamesMenuItem = new JMenuItem("Use " + semanticRoutineName);
			semanticRoutinesNamesMenuItem.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					markedWidget.setMark(semanticRoutineName);
					CanvasPopupMenu.this.canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "AddRoutine"));
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
				if (GGLLDirector.getProject().getSemanticFile() != null)
				{
					semFile = GGLLDirector.getProject().getSemanticFile().getAbsolutePath();
				}
				if (semFile != null)
				{
					final AbstractView abstractComponent = GGLLDirector.getUnsavedView(semFile);
					if (abstractComponent != null)
					{
						final int option = JOptionPane.showConfirmDialog(CanvasPopupMenu.this.popup, "A new semantic routine can not be created while the semantic routines file remains unsaved.\nWould you like to save it now?", "Can not create a new routine", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
						if (option == JOptionPane.YES_OPTION)
						{
							GGLLDirector.saveFile(abstractComponent.getComponentModel());
						}
						else
						{
							return;
						}
					}
				}
				if (semFile != null)
				{
					new RoutineWindow((String) CanvasPopupMenu.this.canvas.findObject(CanvasPopupMenu.this.widget), markedWidget, null, CanvasPopupMenu.this.canvas);
				}
			}
		});

		removeSemanticRoutine.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				markedWidget.setMark(null);
				CanvasPopupMenu.this.canvas.getCanvasStateRepository().propertyChange(new PropertyChangeEvent(this, "undoable", null, "RemoveRoutine"));
			}
		});

		editSemanticRoutine.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (GGLLDirector.getProject() != null)
				{
					String semFile = null;
					if (GGLLDirector.getProject().getSemanticFile() != null)
					{
						semFile = GGLLDirector.getProject().getSemanticFile().getAbsolutePath();
					}
					if (semFile != null)
					{
						final AbstractView abstractComponent = GGLLDirector.getUnsavedView(semFile);
						if (abstractComponent != null)
						{
							final int option = JOptionPane.showConfirmDialog(CanvasPopupMenu.this.popup, "A semantic routine can not be edited while the semantic routines file remains unsaved.\nWould you like to save it now?", "Can not create a new routine", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
							if (option == JOptionPane.YES_OPTION)
							{
								GGLLDirector.saveFile(abstractComponent.getComponentModel());
							}
							else
							{
								return;
							}
						}
						else
						{
							new RoutineWindow((String) CanvasPopupMenu.this.canvas.findObject(CanvasPopupMenu.this.widget), markedWidget, markedWidget.getMark(), CanvasPopupMenu.this.canvas);
						}
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

		nothingMenuItem.setSelected(!this.canvas.isShowingGrid() && !this.canvas.isShowingLines());
		gridMenuItem.setEnabled(this.canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		gridMenuItem.setSelected(this.canvas.isShowingGrid());
		lineMenuItem.setEnabled(this.canvas.getCurrentCanvasState().getPreferences().getMoveStrategy().equals(CanvasResource.M_FREE));
		lineMenuItem.setSelected(this.canvas.isShowingLines());

		showMenu.add(nothingMenuItem);
		showMenu.add(gridMenuItem);
		showMenu.add(lineMenuItem);

		nothingMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(false);
				CanvasPopupMenu.this.lineProvider.removeAllLines();
			}
		});

		gridMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(true);
				CanvasPopupMenu.this.lineProvider.removeAllLines();
			}
		});

		lineMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CanvasPopupMenu.this.gridProvider.setVisible(false);
				CanvasPopupMenu.this.lineProvider.populateCanvas();
			}
		});

		return showMenu;
	}

	private JMenuItem createUndoMenu()
	{
		final CanvasStateRepository volatileStateManager = this.canvas.getCanvasStateRepository();
		final JMenuItem undoMenu = new JMenuItem();
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
		Object focusedWidget = this.canvas.getFocusedObject();
		Widget activeWidget = this.canvas.findWidget(focusedWidget != widget ? focusedWidget : null);
		if (activeWidget == null)
		{
			for (final String string : this.canvas.getNodes())
			{

				final Widget tempWidget = this.canvas.findWidget(string);
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
		this.popup.removeAll();
		if (activeWidget != null && !this.canvas.isLabel(focusedWidget) && activeWidget instanceof MarkedWidget)
		{
			this.widget = activeWidget;
			this.popup.add(createDeleteMenu());
			this.popup.add(new JSeparator());

			this.popup.add(createSemanticRoutinesMenu());
			this.popup.add(new JSeparator());
		}
		this.popup.add(createUndoMenu());
		this.popup.add(createRedoMenu());
		this.popup.add(new JSeparator());
		this.popup.add(createBuildAndParseMenu());
		this.popup.add(new JSeparator());
		this.popup.add(createShowMenu());
		this.popup.add(createRoutingMenu());
		this.popup.add(createMovingMenu());

		return this.popup;
	}

}

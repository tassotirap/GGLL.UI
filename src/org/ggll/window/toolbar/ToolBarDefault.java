package org.ggll.window.toolbar;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.ggll.director.GGLLDirector;
import org.ggll.resource.LangResource;
import org.ggll.syntax.graph.state.StateHistory;
import org.ggll.view.component.AbstractComponent;
import org.ggll.view.component.GrammarComponent;
import org.ggll.view.component.TextAreaComponent;

public class ToolBarDefault extends BaseToolBar implements PropertyChangeListener
{
	private static final long serialVersionUID = 1L;

	JButton[] buttons;
	String[] names;

	private JButton btnUndo, btnRedo, btnSave, btnSaveAll, btnPrint;

	public ToolBarDefault(Object context)
	{
		super(context);
		if (context instanceof GrammarComponent)
		{
			final GrammarComponent grammarComponent = (GrammarComponent) context;
			grammarComponent.getCanvas().getMonitor().addPropertyChangeListener("writing", this);
			grammarComponent.getCanvas().getMonitor().addPropertyChangeListener("object_state", this);
		}
		for (int i = 0; i < this.buttons.length; i++)
		{
			this.buttons[i].setName(this.names[i]);
		}
		this.add(this.btnSave);
		this.add(this.btnSaveAll);
		this.add(this.btnPrint);
		this.add(createJSeparator());
		this.add(this.btnUndo);
		this.add(this.btnRedo);
	}

	private JSeparator createJSeparator()
	{
		final JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		jSeparator.setMaximumSize(new Dimension(6, 100));
		return jSeparator;
	}

	private void setBaseActions()
	{
		this.btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				GGLLDirector.saveFile((AbstractComponent) ToolBarDefault.this.context);
			}

		});
		this.btnSaveAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				GGLLDirector.saveAllFiles();
			}

		});
		this.btnPrint.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				GGLLDirector.print(ToolBarDefault.this.context);
			}
		});
	}

	private void setCanvasActions(final GrammarComponent grammarComponent)
	{
		this.btnUndo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				final StateHistory volatileStateManager = grammarComponent.getCanvas().getCanvasStateHistory();
				if (volatileStateManager.hasNextUndo())
				{
					volatileStateManager.undo();
				}
			}

		});
		this.btnRedo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				final StateHistory vsm = grammarComponent.getCanvas().getCanvasStateHistory();
				if (vsm.hasNextRedo())
				{
					vsm.redo();
				}
			}

		});
	}

	@Override
	protected void initActions()
	{
		setBaseActions();

		if (this.context instanceof GrammarComponent)
		{
			setCanvasActions((GrammarComponent) this.context);
		}
		else if (this.context instanceof TextAreaComponent)
		{
			// setTextActions((TextAreaComponent) context);
		}
	}

	@Override
	protected void initComponets()
	{
		this.btnSave = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "document-save.png")));
		this.btnSaveAll = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "document-save-all.png")));
		this.btnPrint = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "document-print.png")));
		this.btnUndo = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "edit-undo.png")));
		this.btnUndo.setEnabled(false);
		this.btnRedo = new JButton(new ImageIcon(getClass().getResource(this.imgPath + "edit-redo.png")));
		this.btnRedo.setEnabled(false);
		this.buttons = new JButton[]{ this.btnSave, this.btnSaveAll, this.btnPrint, this.btnUndo, this.btnRedo };
		this.names = new String[]{ LangResource.save, LangResource.save_all, LangResource.print, LangResource.copy, LangResource.cut, LangResource.paste, LangResource.undo, LangResource.redo };
	}

	@Override
	protected void initLayout()
	{
		for (int i = 0; i < this.buttons.length; i++)
		{
			final JButton btn = this.buttons[i];
			btn.setOpaque(false);
			btn.setBorder(new EmptyBorder(5, 5, 5, 5));
			btn.setRolloverEnabled(true);
			btn.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) btn.getIcon()).getImage())));
			btn.setBackground(getBackground());
			btn.setToolTipText(this.names[i]);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		if (event.getSource() instanceof StateHistory)
		{
			final StateHistory canvas = (StateHistory) event.getSource();
			switch (event.getPropertyName())
			{
				case "writing":
					this.btnUndo.setEnabled(true);
					this.btnUndo.setToolTipText("Undo");
					break;
				case "object_state":
					this.btnRedo.setEnabled(canvas.hasNextRedo());
					this.btnUndo.setEnabled(canvas.hasNextUndo());
					break;
			}
		}
	}

}
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

import org.ggll.facade.GGLLFacade;
import org.ggll.images.ImageResource;
import org.ggll.resource.LangResource;
import org.ggll.syntax.graph.state.StateHistory;
import org.ggll.window.component.AbstractComponent;
import org.ggll.window.component.GrammarComponent;
import org.ggll.window.component.TextAreaComponent;

public class ToolBarDefault extends BaseToolBar implements PropertyChangeListener
{
	private static final long serialVersionUID = 1L;
	
	JButton[] buttons;
	String[] names;
	
	private JButton btnUndo, btnRedo, btnSave, btnSaveAll, btnPrint;
	
	public ToolBarDefault(final Object context)
	{
		super(context);
		if (context instanceof GrammarComponent)
		{
			final GrammarComponent grammarComponent = (GrammarComponent) context;
			grammarComponent.getCanvas().getMonitor().addPropertyChangeListener("writing", this);
			grammarComponent.getCanvas().getMonitor().addPropertyChangeListener("object_state", this);
		}
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i].setName(names[i]);
		}
		this.add(btnSave);
		this.add(btnSaveAll);
		this.add(btnPrint);
		this.add(createJSeparator());
		this.add(btnUndo);
		this.add(btnRedo);
	}
	
	private JSeparator createJSeparator()
	{
		final JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		jSeparator.setMaximumSize(new Dimension(6, 100));
		return jSeparator;
	}
	
	@Override
	protected void initActions()
	{
		setBaseActions();
		
		if (context instanceof GrammarComponent)
		{
			setCanvasActions((GrammarComponent) context);
		}
		else if (context instanceof TextAreaComponent)
		{
			// setTextActions((TextAreaComponent) context);
		}
	}
	
	@Override
	protected void initComponets()
	{
		btnSave = new JButton(new ImageIcon(ImageResource.imagePath + "document-save.png"));
		btnSaveAll = new JButton(new ImageIcon(ImageResource.imagePath + "document-save-all.png"));
		btnPrint = new JButton(new ImageIcon(ImageResource.imagePath + "document-print.png"));
		btnUndo = new JButton(new ImageIcon(ImageResource.imagePath + "edit-undo.png"));
		btnUndo.setEnabled(false);
		btnRedo = new JButton(new ImageIcon(ImageResource.imagePath + "edit-redo.png"));
		btnRedo.setEnabled(false);
		buttons = new JButton[]
		{ btnSave, btnSaveAll, btnPrint, btnUndo, btnRedo };
		names = new String[]
		{ LangResource.save, LangResource.save_all, LangResource.print, LangResource.copy, LangResource.cut, LangResource.paste, LangResource.undo, LangResource.redo };
	}
	
	@Override
	protected void initLayout()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			final JButton btn = buttons[i];
			btn.setOpaque(false);
			btn.setBorder(new EmptyBorder(5, 5, 5, 5));
			btn.setRolloverEnabled(true);
			btn.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) btn.getIcon()).getImage())));
			btn.setBackground(getBackground());
			btn.setToolTipText(names[i]);
		}
	}
	
	@Override
	public void propertyChange(final PropertyChangeEvent event)
	{
		if (event.getSource() instanceof StateHistory)
		{
			final StateHistory canvas = (StateHistory) event.getSource();
			switch (event.getPropertyName())
			{
				case "writing":
					btnUndo.setEnabled(true);
					btnUndo.setToolTipText("Undo");
					break;
				case "object_state":
					btnRedo.setEnabled(canvas.hasNextRedo());
					btnUndo.setEnabled(canvas.hasNextUndo());
					break;
			}
		}
	}
	
	private void setBaseActions()
	{
		btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				GGLLFacade.getInstance().saveFile((AbstractComponent) ToolBarDefault.this.context);
			}
			
		});
		btnSaveAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				GGLLFacade.getInstance().saveAllFiles();
			}
			
		});
		btnPrint.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				GGLLFacade.getInstance().print(ToolBarDefault.this.context);
			}
		});
	}
	
	private void setCanvasActions(final GrammarComponent grammarComponent)
	{
		btnUndo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				final StateHistory volatileStateManager = grammarComponent.getCanvas().getCanvasStateHistory();
				if (volatileStateManager.hasNextUndo())
				{
					volatileStateManager.undo();
				}
			}
			
		});
		btnRedo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent evt)
			{
				final StateHistory vsm = grammarComponent.getCanvas().getCanvasStateHistory();
				if (vsm.hasNextRedo())
				{
					vsm.redo();
				}
			}
			
		});
	}
	
}
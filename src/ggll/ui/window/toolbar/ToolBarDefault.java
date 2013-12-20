package ggll.ui.window.toolbar;

import ggll.ui.canvas.state.CanvasStateRepository;
import ggll.ui.director.GGLLDirector;
import ggll.ui.resource.LangResource;
import ggll.ui.view.component.AbstractComponent;
import ggll.ui.view.component.GrammarComponent;
import ggll.ui.view.component.TextAreaComponent;

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

import com.jidesoft.icons.ColorFilter;

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
			GrammarComponent grammarComponent = (GrammarComponent) context;
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
		this.add(createJSeparator());
		this.add(btnUndo);
		this.add(btnRedo);
	}

	private JSeparator createJSeparator()
	{
		JSeparator jSeparator = new JSeparator(SwingConstants.VERTICAL);
		jSeparator.setMaximumSize(new Dimension(6, 100));
		return jSeparator;
	}

	private void setBaseActions()
	{
		btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				GGLLDirector.saveFile((AbstractComponent) context);
			}

		});
		btnSaveAll.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				GGLLDirector.saveAllFiles();
			}

		});
		btnPrint.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				GGLLDirector.print(context);
			}
		});
	}

	private void setCanvasActions(final GrammarComponent grammarComponent)
	{
		btnUndo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				CanvasStateRepository volatileStateManager = grammarComponent.getCanvas().getCanvasStateRepository();
				if (volatileStateManager.hasNextUndo())
				{
					volatileStateManager.undo();
				}
			}

		});
		btnRedo.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				CanvasStateRepository vsm = grammarComponent.getCanvas().getCanvasStateRepository();
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
		btnSave = new JButton(new ImageIcon(getClass().getResource(imgPath + "document-save.png")));
		btnSaveAll = new JButton(new ImageIcon(getClass().getResource(imgPath + "document-save-all.png")));
		btnPrint = new JButton(new ImageIcon(getClass().getResource(imgPath + "document-print.png")));
		btnUndo = new JButton(new ImageIcon(getClass().getResource(imgPath + "edit-undo.png")));
		btnUndo.setEnabled(false);
		btnRedo = new JButton(new ImageIcon(getClass().getResource(imgPath + "edit-redo.png")));
		btnRedo.setEnabled(false);
		buttons = new JButton[]{ btnSave, btnSaveAll, btnPrint, btnUndo, btnRedo };
		names = new String[]{ LangResource.save, LangResource.save_all, LangResource.print, LangResource.copy, LangResource.cut, LangResource.paste, LangResource.undo, LangResource.redo };
	}

	@Override
	protected void initLayout()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			JButton btn = buttons[i];
			btn.setOpaque(false);
			btn.setBorder(new EmptyBorder(5, 5, 5, 5));
			btn.setRolloverEnabled(true);
			btn.setSelectedIcon(new ImageIcon(ColorFilter.createDarkerImage(((ImageIcon) btn.getIcon()).getImage())));
			btn.setRolloverIcon(new ImageIcon(ColorFilter.createBrighterImage(((ImageIcon) btn.getIcon()).getImage())));
			btn.setDisabledIcon(new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) btn.getIcon()).getImage())));
			btn.setBackground(this.getBackground());
			btn.setToolTipText(names[i]);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		if (event.getSource() instanceof CanvasStateRepository)
		{
			CanvasStateRepository canvas = (CanvasStateRepository) event.getSource();
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

}
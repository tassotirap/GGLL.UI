package org.ggll.ioadapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.ggll.parser.ParserProxy;
import org.ggll.parser.ParserProxyFactory;
import org.ggll.ui.component.BadParameterException;
import org.ggll.ui.component.InputAdapterComponent;
import org.ggll.ui.component.NewTextArea;
import org.ggll.ui.component.TextAreaRepo;
import org.ggll.ui.interfaces.IMainWindow;
import org.ggll.util.JarFileLoader;
import org.ggll.util.Log;

public class InputAdapter extends IOAdapter
{

	InputAdapterStub stub;
	/**
	 * A reference to the main class of the new component included in this
	 * adapter
	 */
	private Class<?> adapterClass;
	/**
	 * A reference to an instance of the new component's main class included in
	 * this adapter
	 */
	private Object adapterInstance;

	private boolean built;

	private NewTextArea codeAdvTextArea;
	private JTextArea codeTextArea;
	private JComponent codeView;

	private JComponent formView;
	private JComponent frameView;

	private InputAdapterComponent iaComponent;
	private JarFileLoader jarLoader;
	/**
	 * when you are navigating through classes, this object holds an instance of
	 * this last visited class
	 **/
	private Object lastInstance;
	private JFrame objectJFrame;

	private boolean started;

	private int stubInitPosition = -1;

	public InputAdapter(InputAdapterComponent iaComponent)
	{
		this.iaComponent = iaComponent;
		codeAdvTextArea = new NewTextArea();
		codeAdvTextArea.addComponentListener(iaComponent);
		codeTextArea = codeAdvTextArea.getTextArea();
		TextAreaRepo.remove(codeAdvTextArea);
		TextAreaRepo.register(iaComponent, codeAdvTextArea.getTextArea());
	}

	public boolean build()
	{
		try
		{
			InputAdapterForm iaf = (InputAdapterForm) formView;
			jarLoader = new JarFileLoader(new URL[]{});
			jarLoader.addFile(new File(iaf.getJarTextField().getText()));
			adapterClass = jarLoader.loadClass(iaf.getMainTextField().getText());
			built = true;
			iaComponent.setBuilt(true);
			return true;
		}
		catch (Exception e)
		{
			Log.log(Log.ERROR, this, "Could not built!", e);
		}
		return false;
	}

	/** Validates a new build **/
	public boolean canBuild()
	{
		// TODO decent validation, and rebuild
		InputAdapterForm iaf = (InputAdapterForm) formView;
		return new File(iaf.getJarTextField().getText()).exists() && !iaf.getMainTextField().getText().equals("");
	}

	/** Verifies if everything is ready to start, after a build **/
	public boolean canStart()
	{
		return built;
	}

	/** Generates a java file with the input adapter **/
	public boolean generate()
	{
		return true;
	}

	public Class<?> getAdapterClass()
	{
		return adapterClass;
	}

	public Object getAdapterInstance()
	{
		return adapterInstance;
	}

	public JTextArea getCodeTextArea()
	{
		return codeTextArea;
	}

	@Override
	public JComponent getCodeView()
	{
		if (codeView == null)
		{
			try
			{
				codeView = codeAdvTextArea.create(iaComponent.getPath());
				if (codeTextArea.getText().equals(""))
				{
					stub = new InputAdapterStub();
					stub.setClass("Input" + adapterClass.getSimpleName());
					stub.addImport(adapterInstance.getClass().getCanonicalName());
					stub.addImport(ParserProxyFactory.class.getCanonicalName());
					stub.addImport(ParserProxy.class.getCanonicalName());
					stub.addImport(InputAdapterImpl.class.getCanonicalName());
					stub.setObjectClass(adapterInstance.getClass().getName());
					stub.addInterface("InputAdapterImpl");
					codeTextArea.setText(stub.getStub());
					this.stubInitPosition = stub.getInitMethodPos();
				}
			}
			catch (BadParameterException e)
			{
			}
		}
		return codeView;
	}

	/**
	 * Gets the currently focused class
	 * 
	 * @return the last focused class
	 */
	public Class<?> getCurrentClass()
	{
		if (lastInstance == null)
		{
			return adapterClass;
		}
		else
		{
			return lastInstance.getClass();
		}
	}

	@Override
	public JComponent getFormView()
	{
		if (formView == null)
		{
			InputAdapterForm iaf = new InputAdapterForm(this);
			formView = iaf;
			File file = new File(iaComponent.getPath());
			try
			{
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String firstLine = br.readLine();
				br.close();
				fis.close();
				if (firstLine != null && firstLine.startsWith("///~"))
				{
					iaf.getMainTextField().setText(firstLine.replace("///~", ""));
					iaf.getMainTextField().setEditable(false);
				}
			}
			catch (IOException e)
			{
			}
		}
		return formView;
	}

	@Override
	public JComponent getFrameView()
	{
		return frameView;
	}

	public Object getLastInstance()
	{
		if (lastInstance == null)
		{
			return adapterInstance;
		}
		return lastInstance;
	}

	public int getStubInitPosition()
	{
		File file = new File(codeAdvTextArea.getPath());
		try
		{
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			int lnCnt = 0;
			while ((line = br.readLine()) != null)
			{
				if (line.replace("/t", "").replace(" ", "").matches("^publicvoidinit(){.*"))
				{
					stubInitPosition = lnCnt + 1;
					break;
				}
				lnCnt++;
			}
			br.close();
			fis.close();
		}
		catch (IOException e)
		{
			Log.log(Log.ERROR, this, "An internal error occurred", e);
			stubInitPosition = stub.getInitMethodPos();
		}
		;
		return stubInitPosition;
	}

	public boolean isBuilt()
	{
		return built;
	}

	public boolean isStarted()
	{
		return started;
	}

	public void resetLastInstance()
	{
		lastInstance = null;
	}

	public void save()
	{
		codeAdvTextArea.saveFile();
	}

	public void setLastInstance(Object instance)
	{
		this.lastInstance = instance;
	}

	public boolean start()
	{
		try
		{
			InputAdapterForm iaf = (InputAdapterForm) formView;
			String[] args = iaf.getArgsTextField().getText().split(" ");
			Object[] objArgs = new Object[]{ args };
			// someday my friend
			// adapterInstance = adapterClass.newInstance();
			adapterClass.getMethod("main", new Class[]{ String[].class }).invoke(null, objArgs);
			String frameClass = iaf.getJframeTextField().getText();
			String frameName = iaf.getJframeNameTextField().getText();
			for (java.awt.Window w : java.awt.Window.getOwnerlessWindows())
			{
				if (!frameClass.equals(""))
				{
					if (w.getClass().getName().equals(frameClass))
					{
						w.setVisible(false);
						if (w instanceof JFrame)
						{
							objectJFrame = (JFrame) w;
						}
					}
				}
				else if (!frameName.equals(""))
				{
					if (w.getName().equals(frameName))
					{
						w.setVisible(false);
						if (w instanceof JFrame)
						{
							objectJFrame = (JFrame) w;
						}
					}
				}
				else if (w instanceof JFrame && !w.getName().equals(IMainWindow.DEFAULT_NAME))
				{
					w.setVisible(false);
					// RISKY...Could get dialogs and other stuff
					objectJFrame = (JFrame) w;
				}
			}
			adapterInstance = objectJFrame;
			if (adapterInstance == null)
			{
				adapterInstance = adapterClass.newInstance();
			}
			started = true;
			iaComponent.setStarted(true);
			return true;
		}
		catch (Exception e)
		{
			Log.log(Log.ERROR, this, "Could not start!", e);
			/*
			 * InternalError error = new
			 * InternalError("Failed to invoke main method");
			 * error.initCause(e); throw error;
			 */
		}
		adapterClass = null;
		adapterInstance = null;
		return false;
	}

	public boolean stop()
	{
		// TODO Auto-generated method stub
		return false;
	}
}

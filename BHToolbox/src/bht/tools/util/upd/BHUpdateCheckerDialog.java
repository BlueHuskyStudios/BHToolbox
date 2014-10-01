package bht.tools.util.upd;

import java.awt.Window;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javax.swing.JDialog;

/**
 * BHUpdateCheckerDialog, made for BHToolbox, is copyright Blue Husky Programming ©2014 GPLv3 <hr/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 *		- 2014-09-30 (1.0.0) - Kyli created BHUpdateCheckerDialog
 * @since 2014-09-30
 */
public class BHUpdateCheckerDialog extends JDialog
{
	private JFXPanel interior;

	public BHUpdateCheckerDialog(Window owner)
	{
		super(owner);
		initGUI();
	}

	private ProgressBar progressBar;
	private void initGUI()
	{
		{
			interior = new JFXPanel();
			
		}
		{
			progressBar = new ProgressBar();
			progressBar.setProgress(-1);
		}
	}
	
	
}

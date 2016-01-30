package bht.tools.net.upd;

import bht.tools.Constants;
import bht.tools.comps.BHCompUtilities;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

/**
 * BHUpdateCheckerDialog, made for BHToolbox, is copyright Blue Husky Programming ©2014 BH-1-PS <hr/>
 * 
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 *		- 2014-09-30 (1.0.0) - Kyli created BHUpdateCheckerDialog
 * @since 2014-09-30
 */
public class BHUpdateCheckerDialog extends JDialog
{
	public BHUpdateCheckerDialog(Window owner)
	{
		super(owner);
		initGUI();
	}

	private JFXPanel holder;
	private Scene interior;
	private GridPane rootGrid;
	private ProgressBar progressBar;
	private Label statusLabel;
	private Button downloadButton;
    private void initGUI()
    {
		{
			setTitle("Checking for updates...");
			addWindowListener(new WindowAdapter()
			{
				@Override
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				}
			});
		}
		{
			holder = new JFXPanel();
			setContentPane(holder);
			Platform.runLater( () -> initJFXPanel(holder) );
		}
        pack();
    }

	@SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private void initJFXPanel(JFXPanel holder)
	{
		try
		{
			FXMLLoader.load(
				BHUpdateCheckerDialog.class.getResource(
					"UpdateChecker.fxml"
				)
			);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			{
				{
					rootGrid = new GridPane();
					GridPane.setHalignment(rootGrid, HPos.CENTER);
					rootGrid.setAlignment(Pos.CENTER);
					rootGrid.setPadding(new Insets(16));
					rootGrid.setHgap(16);
					rootGrid.setVgap(8);
				}

				interior = holder.getScene();
				if (interior == null)
					holder.setScene(interior = new Scene(rootGrid));
				interior.setRoot(rootGrid);

			}
			{
				statusLabel = new Label("Checking for Updates...");
//				statusLabel.setAlignment(Pos.CENTER);
//				statusLabel.setTextAlignment(TextAlignment.CENTER);
				rootGrid.add(statusLabel, 0, 0);
			}
			{
				progressBar = new ProgressBar();
				progressBar.setProgress(-1);
				progressBar.setPrefWidth(Constants.MAX_WIN_BOUNDS.width / 5d); // 1/5 the width of the screen
				rootGrid.add(progressBar, 0, 1);
			}
			{
				downloadButton = new Button("Get it!");
//				downloadButton.setAlignment(Pos.CENTER);
				rootGrid.add(downloadButton, 0, 2);
			}
			holder.setMinimumSize(new Dimension((int)(rootGrid.getPrefWidth() + .5), (int)(rootGrid.getPrefHeight() + .5)));
			setMinimumSize(holder.getMinimumSize());
		}
    }


	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> new BHUpdateCheckerDialog(null).setVisible(true));
	}
}

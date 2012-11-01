package org.syfsyf.warrunner;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTree;

public class TrayMenu {

	private EmbeddedServer embeddedServer;

	
	ActionListener exitListener = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Exiting...");
			try {
				embeddedServer.stopServer();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.exit(0);

		}
	};

	ActionListener startServer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			try {
				embeddedServer.startServer();
				updateStatusMenuItems();
			} catch (Exception e1) {
				e1.printStackTrace();
				embeddedServer.showError(e1.getMessage());
			}
		}
	};

	ActionListener stopServer = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			embeddedServer.stopServer();
			updateStatusMenuItems();
		}
	};

	ActionListener openInBrowser = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			embeddedServer.openInBrowser();
			updateStatusMenuItems();
		}
	};


	private MenuItem openItem;


	private MenuItem startItem;


	private MenuItem stopItem;


	private MenuItem exitItem;
	
	public TrayMenu(EmbeddedServer runner) {

		this.embeddedServer = runner;

		if (SystemTray.isSupported()) {
			createTray();
		}

	}
	void updateStatusMenuItems()
	{
		if(embeddedServer.server.isRunning()){
			stopItem.setEnabled(true);
			startItem.setEnabled(false);
		}
		else{
			stopItem.setEnabled(false);
			startItem.setEnabled(true);
		}
	}

	private void createTray() {
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().getImage(
				TrayMenu.class.getClassLoader().getResource(embeddedServer.properties.getProperty("app.icon", "app-icon.png")));
		/*
		 * MouseListener mouseListener = new MouseListener() {
		 * 
		 * public void mouseClicked(MouseEvent e) {
		 * System.out.println("Tray Icon - Mouse clicked!"); }
		 * 
		 * public void mouseEntered(MouseEvent e) {
		 * System.out.println("Tray Icon - Mouse entered!"); }
		 * 
		 * public void mouseExited(MouseEvent e) {
		 * System.out.println("Tray Icon - Mouse exited!"); }
		 * 
		 * public void mousePressed(MouseEvent e) {
		 * System.out.println("Tray Icon - Mouse pressed!"); }
		 * 
		 * public void mouseReleased(MouseEvent e) {
		 * System.out.println("Tray Icon - Mouse released!"); } };
		 */

		

		PopupMenu popup = new PopupMenu();

		this.openItem = new MenuItem("open");
		openItem.addActionListener(openInBrowser);
		popup.add(openItem);
	
		popup.add(new MenuItem("-"));
		
		this.startItem = new MenuItem("start server");
		startItem.addActionListener(startServer);
		popup.add(startItem);
		
		this. stopItem = new MenuItem("stop server");
		stopItem.addActionListener(stopServer);
		popup.add(stopItem);
		

		popup.add(new MenuItem("-"));
		
		this. exitItem = new MenuItem("exit");
		exitItem.addActionListener(exitListener);
		popup.add(exitItem);
		
		

		final TrayIcon trayIcon = new TrayIcon(image, embeddedServer.properties.getProperty("app.title", "Aplikacja"), popup);

		/*
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trayIcon.displayMessage("Action Event", "An Action Event Has Been Performed!", TrayIcon.MessageType.INFO);
			}
		};*/

		trayIcon.setImageAutoSize(true);
		
		updateStatusMenuItems();
		
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			// System.err.println("TrayIcon could not be added.");

		}

	}

}

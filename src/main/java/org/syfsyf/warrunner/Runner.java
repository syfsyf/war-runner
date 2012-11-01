package org.syfsyf.warrunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.URL;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class Runner {

	public static final String WARTORUNPROPS = "war_to_run.properties";
	Properties properties = new Properties();

	private String warDir = "war";
	private int port = 8080;
	boolean extractWar = false;
	boolean autoSelectPort = true;
	boolean showTrayMenu = true;
	boolean openInBrowser = true;
	String openUrl = "/";
	private boolean test = false;

	Server server;
	SocketConnector connector;

	public Runner(String[] args) {
		try {
			start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			showError(e.getMessage());
		}

	}
	void showError(String message){
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE, null);
		
	}

	void start() throws Exception {
		loadProperties();
		prepareServer();
		startServer();
		if (showTrayMenu) {
			showTrayMenu();
		}
		if (openInBrowser) {
			openInBrowser();
		}

	}
	void stopServer()
	{
		if(server!=null && server.isStarted()){
			try {
				server.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				showError(e.getMessage());
			}
		}
	}

	void openInBrowser() {
		
		String url=getUrlToOpen();
		BrowserControl.openUrl(url);

	}
	String getUrlToOpen()
	{
		return "http://localhost:"+connector.getPort()+""+openUrl;
	}

	private void showTrayMenu() {
		// TODO Auto-generated method stub
		//TrayMenu menu=new TrayMenu(this);

	}

	private void prepareServer() throws IOException {

		server = new Server();
		this.connector = new SocketConnector();

		// Set some timeout options to make debugging easier.
		connector.setMaxIdleTime(1000 * 60 * 60);
		connector.setSoLingerTime(-1);
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/");
		context.setExtractWAR(extractWar);

		if (test) {
			context.setWar(new File("d:/mat/eclipse_workspace/test_war/build/test.war").getCanonicalPath());
		} else {
			URL war = Runner.class.getClassLoader().getResource(warDir);
			if (war == null) {
				throw new RuntimeException("war not found:" + warDir);
			}
			context.setWar(Runner.class.getClassLoader().getResource(warDir).toExternalForm());
		}
		server.addHandler(context);

	}

	void startServer() throws Exception {

		if (autoSelectPort) {
			for (int p = connector.getPort(); p < 65000; p++) {
				connector.setPort(p);
				try {
					server.start();
					return;
				} catch (BindException bindEx) {
					if ("Address already in use: JVM_Bind".equals(bindEx.getMessage())) {

						continue;
					}
					throw new RuntimeException(bindEx);
				}
			}
		} else {
			server.start();
		}

	}

	private void loadProperties() throws IOException {

		InputStream propsStream = Runner.class.getClassLoader().getResourceAsStream(WARTORUNPROPS);
		if (propsStream == null) {
			// throw new RuntimeException("file not found:"+WARTORUNPROPS);
			return; // use defaults settings
		}

		properties.load(propsStream);
		propsStream.close();

		warDir = properties.getProperty("war.dir", warDir);
		if (properties.contains("port")) {
			port = Integer.valueOf(properties.getProperty("port"));
		}
		if (properties.contains("extract.war")) {
			extractWar = Boolean.valueOf(properties.getProperty("extract.war"));
		}
		if (properties.contains("auto.select.port")) {
			autoSelectPort = Boolean.valueOf(properties.getProperty("auto.select.port"));
		}
		if (properties.contains("open.in.browser")) {
			openInBrowser = Boolean.valueOf(properties.getProperty("open.in.browser"));
		}
		openUrl = properties.getProperty("open.url", openUrl);

	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Runner runner = new Runner(args);

		/*
		 * 
		 * InputStream propsStream =
		 * Runner.class.getClassLoader().getResourceAsStream
		 * ("war_to_run.properties"); if(propsStream==null){
		 * 
		 * 
		 * 
		 * } Properties properties=new Properties(); //properties.load()
		 * 
		 * 
		 * Server server = new Server(); SocketConnector connector = new
		 * SocketConnector();
		 * 
		 * // Set some timeout options to make debugging easier.
		 * connector.setMaxIdleTime(1000 * 60 * 60);
		 * connector.setSoLingerTime(-1); connector.setPort(80);
		 * server.setConnectors(new Connector[] { connector });
		 * 
		 * WebAppContext context = new WebAppContext();
		 * context.setServer(server); context.setContextPath("/");
		 * 
		 * ProtectionDomain protectionDomain =
		 * Runner.class.getProtectionDomain(); URL location =
		 * protectionDomain.getCodeSource().getLocation();
		 * System.out.println(location.toExternalForm()); File f = new
		 * File("d:/mat/eclipse_workspace/test_war/build/test.war");
		 * //context.setWar(location.toExternalForm());
		 * context.setExtractWAR(false);
		 * //context.setWar(Runner.class.getClassLoader
		 * ().getResource("test.war").toExternalForm());
		 * context.setWar(f.getCanonicalPath()); //context.setWar(war)
		 * 
		 * server.addHandler(context); try { for(int
		 * port=connector.getPort();port < 65000;port++){
		 * 
		 * try { connector.setPort(port); server.start(); } catch(BindException
		 * bindEx){
		 * if("Address already in use: JVM_Bind".equals(bindEx.getMessage())){
		 * 
		 * continue; } //System.out.println( bindEx.getMessage()); return; } }
		 * System.in.read(); server.stop(); server.join(); } catch (Exception e)
		 * { e.printStackTrace(); System.exit(100); }
		 */
	}

}

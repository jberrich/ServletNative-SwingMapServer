package ma.ensao.swingmap.server;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ma.ensao.swingmap.server.servlet.TimezoneDataServlet;
import ma.ensao.swingmap.server.servlet.WorldCitiesDataServlet;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import com.ezware.dialog.task.TaskDialogs;

@SuppressWarnings("serial")
public class SwingMapServerView extends JFrame implements ConsoleMessage {
	
	private JPanel         jContentPane          = null;
	private JPanel         consolePanel          = null;
	private JPanel         consoleButtonPanel    = null;
	private JButton        clearConsoleButton    = null;
	private JScrollPane    consoleScrollPane     = null;
	private Style          welcomeConsoleStyle   = null;
	private Style          defaultConsoleStyle   = null;
	private Style          serverConsoleStyle    = null;
	private Style          clientConsoleStyle    = null;
	private StyledDocument consoleStyledDocument = null;
	private JTextPane      consoleTextPane       = null;
	private JPanel         controlPanel          = null;
	private JButton        controlStartButton    = null;
	private JButton        controlStopButton     = null;
	private JButton        controlExitButton     = null;
	private MenuItem       startMenuItem         = null;
	private MenuItem       stopMenuItem          = null;
	private MenuItem       exitMenuiItem         = null;

	private Color  borderColor    = new Color(152, 9, 9);
	private String dateTimeFormat = "dd/MM/yyyy HH:mm:ss";
	
	private Properties conf = null;
	
	private int     port      = 0;
	private Tomcat  server    = null;
	private boolean isStopped = true;
	
	private TrayIcon tray = null;
	
	private WorldCitiesDataServlet worldCitiesDataServlet = null;
	private TimezoneDataServlet timezoneDataServlet = null;
	
	public SwingMapServerView() {
		super();
		init();
	}
	
	private void init() { 
		initConfig();
		initServer();
		initComponents();
		initStyle();
		initView();
	}
	
	private void initConfig() {
		try {
			conf = new Properties();
			FileInputStream in = new FileInputStream("./conf/config.ini");
			conf.load(in);
			in.close();
			
			this.port = Integer.parseInt(conf.getProperty("server.http.port"));
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

	public int getPort() {
		return port;
	}
	
	private void initComponents() {
		this.setContentPane(getJContentPane());		
	}

	private JPanel getJContentPane() {
		if(jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getConsolePanel(), BorderLayout.CENTER);
			jContentPane.add(getControlPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}
	
	private JPanel getConsolePanel() {
		if(consolePanel == null) {
			consolePanel = new JPanel();
			consolePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(borderColor, borderColor), 
					               "Console", 
					               TitledBorder.LEFT, 
					               TitledBorder.TOP,
					               consolePanel.getFont().deriveFont(Font.BOLD), 
					               borderColor));
			consolePanel.setLayout(new BorderLayout());
			consolePanel.add(getConsoleButtonPanel(), BorderLayout.NORTH);
			consolePanel.add(getConsoleScrollPane(), BorderLayout.CENTER);
		}
		return consolePanel;
	}
	
	private JPanel getConsoleButtonPanel() {
		if(consoleButtonPanel == null) {
			consoleButtonPanel = new JPanel();
			consoleButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			consoleButtonPanel.add(getClearConsoleButton());
		}
		return consoleButtonPanel;
	}
	
	private JButton getClearConsoleButton() {
		if(clearConsoleButton == null) {
			clearConsoleButton = new JButton(new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/clear.png")));
			clearConsoleButton.setOpaque(false);
			clearConsoleButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
			clearConsoleButton.addMouseListener(
					new MouseAdapter() {
					
						public void mouseEntered(MouseEvent e) {
							super.mouseEntered(e);
							clearConsoleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
						}
						
						public void mouseExited(MouseEvent e) {
							super.mouseExited(e);
							clearConsoleButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
						
					}
			);
			clearConsoleButton.addActionListener(
					new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							getConsoleTextPane().setText("");
						}
						
					}
			);
		}
		return clearConsoleButton;
	}
	
	private JScrollPane getConsoleScrollPane() {
		if(consoleScrollPane == null) {
			consoleScrollPane = new JScrollPane(getConsoleTextPane());
			consoleScrollPane.setOpaque(false);
		}
		return consoleScrollPane;		
	}
	
	private JTextPane getConsoleTextPane() {
		if(consoleTextPane == null) {
			consoleTextPane = new JTextPane();
			consoleTextPane.setEditable(false);
			consoleTextPane.setOpaque(false);
			consoleTextPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));		
		}
		return consoleTextPane;
	}
	
	private StyledDocument getConsoleStyledDocument() {
		if(consoleStyledDocument == null) {
			consoleStyledDocument = getConsoleTextPane().getStyledDocument();
		}
		return consoleStyledDocument;
	}
	
	private void initStyle() {
		welcomeConsoleStyle = getConsoleTextPane().getStyle("default");
		defaultConsoleStyle = getConsoleTextPane().addStyle("defaultConsoleStyle", welcomeConsoleStyle);
		serverConsoleStyle  = getConsoleTextPane().addStyle("serverConsoleStyle" , defaultConsoleStyle);
		clientConsoleStyle  = getConsoleTextPane().addStyle("clientConsoleStyle" , serverConsoleStyle);
		StyleConstants.setAlignment(welcomeConsoleStyle, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(defaultConsoleStyle, true);
		StyleConstants.setAlignment(defaultConsoleStyle, StyleConstants.ALIGN_LEFT);
		StyleConstants.setForeground(defaultConsoleStyle, new Color(33, 77, 115));
		StyleConstants.setForeground(serverConsoleStyle , new Color(198, 111, 9));
		StyleConstants.setForeground(clientConsoleStyle , new Color(58, 116, 32));
	}

	private JPanel getControlPanel() {
		if(controlPanel == null) {
			controlPanel = new JPanel();
			controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(borderColor, borderColor), 
					               "Contrôle", 
					               TitledBorder.LEFT, 
					               TitledBorder.TOP,
					               controlPanel.getFont().deriveFont(Font.BOLD), 
					               borderColor));
			controlPanel.setLayout(new GridBagLayout());
			controlPanel.add(getControlStartButton(),
		             new GridBagConstraints(0, 0, 1, 1, 0, 0, 
		            		 			    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
		            		                new Insets(5, 5, 5, 5), 0, 0));
			controlPanel.add(getControlStopButton(),
		             new GridBagConstraints(1, 0, 1, 1, 0, 0, 
		            		 			    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
		            		                new Insets(5, 0, 5, 5), 0, 0));
			controlPanel.add(getControlExitButton(),
		             new GridBagConstraints(0, 1, 2, 1, 0, 0, 
		            		 			    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 
		            		                new Insets(0, 5, 5, 5), 0, 0));
		}
		return controlPanel;
	}
	
	private JButton getControlStartButton() {
		if(controlStartButton == null) {
			controlStartButton = new JButton("Démarrer", new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/start.png")));
			controlStartButton.addMouseListener(
					new MouseAdapter() {
					
						public void mouseEntered(MouseEvent e) {
							super.mouseEntered(e);
							controlStartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
						}
						
						public void mouseExited(MouseEvent e) {
							super.mouseExited(e);
							controlStartButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
						
					}
			);
			controlStartButton.addActionListener(
					new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							startAction();
						}
						
					}
			);
		}
		return controlStartButton;
	}
	
	private void startAction() {
		try {
			getServer().start();
	        this.setIconImage(new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/start.png")).getImage());
			isStopped = false;
			String serverMSG = String.format("\n [%s] : Démarrage du serveur dans le port %d", new SimpleDateFormat(dateTimeFormat).format(new Date()), getPort());
			getConsoleStyledDocument().setParagraphAttributes(getConsoleStyledDocument().getLength()+1, serverMSG.length(), defaultConsoleStyle, false);
			getConsoleStyledDocument().insertString(getConsoleStyledDocument().getLength(), serverMSG, defaultConsoleStyle);
			this.setTitle(String.format("SwingMapServer : [Server : %s][IP : %s][Port : %d]", InetAddress.getLocalHost().getHostName(), 
					                                                                          InetAddress.getLocalHost().getHostAddress(), 
					                                                                          getPort()));
			getControlStartButton().setEnabled(false);
			getControlStopButton().setEnabled(true);
			getStartMenuItem().setEnabled(false);
			getStopMenuItem().setEnabled(true);
//			getServer().getServer().await();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}
	
	private void initServer() {
		try {
			getServer().setPort(getPort());
			initServlet();
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}
	
	private void initServlet() {
//		Context ctx = getServer().addContext("/", new File(".").getAbsolutePath());
		
		File docBase = new File(System.getProperty("java.io.tmpdir"));
		Context ctx = getServer().addContext("", docBase.getAbsolutePath());
		
		worldCitiesDataServlet = new WorldCitiesDataServlet();
		timezoneDataServlet = new TimezoneDataServlet(this);
		
		Tomcat.addServlet(ctx, "WorldCitiesData", worldCitiesDataServlet);
		Tomcat.addServlet(ctx, "TimezoneData", timezoneDataServlet);
		ctx.addServletMapping("/worldcities", "WorldCitiesData");
		ctx.addServletMapping("/timezone", "TimezoneData");
	}
	
	private Tomcat getServer() {
		if(server == null) {
			server = new Tomcat();
		}
		return server;
	}
	
	private JButton getControlStopButton() {
		if(controlStopButton == null) {
			controlStopButton = new JButton("Arrêter", new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/stop.png")));
			controlStopButton.setEnabled(false);
			controlStopButton.addMouseListener(
					new MouseAdapter() {
					
						public void mouseEntered(MouseEvent e) {
							super.mouseEntered(e);
							controlStopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
						}
						
						public void mouseExited(MouseEvent e) {
							super.mouseExited(e);
							controlStopButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
						
					}
			);
			controlStopButton.addActionListener(
					new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							stopAction();
						}
						
					}
			);
			
		}
		return controlStopButton;
	}
	
	private void stopAction() {
		try {
			getServer().stop();
			getControlStartButton().setEnabled(true);
			getControlStopButton().setEnabled(false);
			getStartMenuItem().setEnabled(true);
			getStopMenuItem().setEnabled(false);
			String serverMSG = String.format("\n [%s] : Arrêt du serveur", new SimpleDateFormat(dateTimeFormat).format(new Date()), getPort());
			getConsoleStyledDocument().setParagraphAttributes(getConsoleStyledDocument().getLength()+1, serverMSG.length(), defaultConsoleStyle, false);
			getConsoleStyledDocument().insertString(getConsoleStyledDocument().getLength(), serverMSG, defaultConsoleStyle);
			this.setTitle(String.format("SwingMapServer : [Server : %s][IP : %s]", InetAddress.getLocalHost().getHostName(), 
                                                                                   InetAddress.getLocalHost().getHostAddress()));
			this.setIconImage(new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/stop.png")).getImage());
			isStopped = true;
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}
	
	private JButton getControlExitButton() {
		if(controlExitButton == null) {
			controlExitButton = new JButton("Quitter", new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/exit.png")));
			controlExitButton.addMouseListener(
					new MouseAdapter() {
					
						public void mouseEntered(MouseEvent e) {
							super.mouseEntered(e);
							controlExitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
						}
						
						public void mouseExited(MouseEvent e) {
							super.mouseExited(e);
							controlExitButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
						}
						
					}
			);
			controlExitButton.addActionListener(
					new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							exitAction();
						}
						
					}
			);			
		}
		return controlExitButton;
	}
	
	private void exitAction() {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous quitter le serveur ?", "Quitter : Confirmation",dialogButton);
		if(dialogResult == 0) {
			if(!isStopped) {
				stopAction();	
			}
			SystemTray.getSystemTray().remove(getTray());
			System.exit(0);
		}
	}
	
	private void initView() {
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/view.png")).getImage());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(
				new WindowAdapter() {

					public void windowClosing(WindowEvent e) {
						setVisible(false);
					}
					
					public void windowIconified(WindowEvent e) {
						setVisible(false);
					}
			
				}
		);
		this.setTitle("ENSA Oujda : SwingMapServer");
		setSystemTray();
		try {
			String loadMSG = "\n" + 
					         "Démarrage de l’application : SwingMapServer - version Servlet" + "\n" +
	                         "Copyright ENSA Oujda : 2014-2015"                              + "\n" +
			                 "-----------------------------------------------------------------------------" + "\n";
			getConsoleStyledDocument().insertString(getConsoleStyledDocument().getLength(), loadMSG, welcomeConsoleStyle);
		} catch (BadLocationException e) {
			TaskDialogs.showException(e);
		}
		this.setVisible(true);
	}
	
	private void setSystemTray() {
		if (SystemTray.isSupported()) {
			try {
				SystemTray.getSystemTray().add(getTray());
			} catch (AWTException e) {
				TaskDialogs.showException(e);
			}
		}
	}
	
	private PopupMenu getPopup() {
		PopupMenu popup = new PopupMenu();	
		popup.add(getStartMenuItem());
		popup.add(getStopMenuItem());
		popup.addSeparator();
		popup.add(getExitMenuiItem());
		return popup;
	}
	
	private MenuItem getStartMenuItem() {
		if(startMenuItem == null) {
			startMenuItem = new MenuItem("Démarrer");
			startMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					startAction();
				}
				
			});
		}
		return startMenuItem;
	}
	
	private MenuItem getStopMenuItem() {
		if(stopMenuItem == null) {
			stopMenuItem  = new MenuItem("Arrêter");
			stopMenuItem.setEnabled(false);
			stopMenuItem.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					stopAction();
				}
				
			});
		}
		return stopMenuItem;
	}
	
	private MenuItem getExitMenuiItem() {
		if(exitMenuiItem == null) {
			exitMenuiItem = new MenuItem("Quitter");
			exitMenuiItem.addActionListener(
					new ActionListener() {
						
						public void actionPerformed(ActionEvent e) {
							exitAction();
						}
						
					}
			);
		}
		return exitMenuiItem;
	}
	
	private TrayIcon getTray() {
		if(tray == null) {
			Image icon = new ImageIcon(getClass().getResource("/ma/ensao/swingmap/server/resources/icons/view.png")).getImage();
			tray = new TrayIcon(icon, "ENSA Oujda : SwingMapServer", getPopup());
			tray.setImageAutoSize(true);
			tray.addMouseListener(
					new MouseAdapter() {
					
						public void mouseClicked(MouseEvent e) {
							if(e.getClickCount() == 2) {
								setVisible(true);
								toFront();
								setState(Frame.NORMAL);						
							}							
						};
						
					}					
			);
		}
		return tray;
	}

	@Override
	public void displayRequest(String host, double latitude, double longitude) {
		try {
			String serverRequestMSG = String.format("\n [%s] : Le client [%s] a envoyé une demande de récupération de la date système de la vile [%f, %f] ", new SimpleDateFormat(dateTimeFormat).format(new Date())
					                                                                                                                                       , host
					                                                                                                                                       , latitude
					                                                                                                                                       , longitude);
			getConsoleStyledDocument().setParagraphAttributes(getConsoleStyledDocument().getLength()+1, serverRequestMSG.length(), clientConsoleStyle, false);
			getConsoleStyledDocument().insertString(getConsoleStyledDocument().getLength(), serverRequestMSG, clientConsoleStyle);
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}

	@Override
	public void displayResponse(String host) {
		try {
			String serverResponseMSG = String.format("\n [%s] : Le serveur a répondu à la demande du client [%s]", new SimpleDateFormat(dateTimeFormat).format(new Date()), 
                                                                                                                   host);
			getConsoleStyledDocument().setParagraphAttributes(getConsoleStyledDocument().getLength()+1, serverResponseMSG.length(), serverConsoleStyle, false);
			getConsoleStyledDocument().insertString(getConsoleStyledDocument().getLength(), serverResponseMSG, serverConsoleStyle);
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
	}
	
}

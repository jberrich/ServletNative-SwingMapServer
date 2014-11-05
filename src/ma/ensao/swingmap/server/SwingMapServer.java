package ma.ensao.swingmap.server;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class SwingMapServer {

	private static void setDefaultLookAndFeel() {
		try {
			UIManager.put("RootPane.setupButtonVisible", false);
			org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(
				
				new Runnable() {
					
					public void run() {
						setDefaultLookAndFeel();
						new SwingMapServerView();
					}
					
				}
		
		);
	}

}

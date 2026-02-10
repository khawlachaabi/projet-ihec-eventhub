package Main.java;

import javax.swing.SwingUtilities;

import ui.LoginFrame;

/**
 * Point d'entrée pour l'interface graphique.
 * 
 * La logique "console" reste dans la classe Main,
 * tandis que cette classe sert uniquement à lancer l'UI Swing.
 */
public class MainUI {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			LoginFrame login = new LoginFrame();
			login.setVisible(true);
		});
	}
}

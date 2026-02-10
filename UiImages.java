package ui;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Utilitaire centralisé pour charger le logo IHEC
 * et l'afficher de manière cohérente dans toute l'UI.
 */
public final class UiImages {

	private UiImages() {
	}

	private static ImageIcon baseLogo;

	private static ImageIcon loadLogo() {
		if (baseLogo != null) {
			return baseLogo;
		}

		// On essaie plusieurs chemins possibles dans le classpath
		String[] paths = { "/ui/resources/ihec_logo.png", "/resources/ihec_logo.png", "/ihec_logo.png" };
		for (String path : paths) {
			java.net.URL url = UiImages.class.getResource(path);
			if (url != null) {
				baseLogo = new ImageIcon(url);
				return baseLogo;
			}
		}

		System.err.println(
				"[UiImages] Logo IHEC introuvable. Placez le fichier 'ihec_logo.png' dans src/ui/resources/ puis reconstruisez le projet.");
		return null;
	}

	public static ImageIcon logoSmall() {
		ImageIcon base = loadLogo();
		if (base == null) {
			return null;
		}
		Image scaled = base.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}

	public static ImageIcon logoMedium() {
		ImageIcon base = loadLogo();
		if (base == null) {
			return null;
		}
		Image scaled = base.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		return new ImageIcon(scaled);
	}
}



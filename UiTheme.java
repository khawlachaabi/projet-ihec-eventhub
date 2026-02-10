package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Classe utilitaire pour les styles et thèmes communs de l'interface
 */
public final class UiTheme {

	private UiTheme() {}

	// Palette de couleurs moderne
	public static final Color DARK_BG = new Color(15, 23, 42);
	public static final Color CARD_BG = new Color(30, 41, 59);
	public static final Color HEADER_BG = new Color(20, 30, 48);
	public static final Color PRIMARY_BLUE = new Color(59, 130, 246);
	public static final Color PRIMARY_BLUE_HOVER = new Color(37, 99, 235);
	public static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	public static final Color TEXT_SECONDARY = new Color(148, 163, 184);
	public static final Color ERROR_RED = new Color(239, 68, 68);
	public static final Color SUCCESS_GREEN = new Color(34, 197, 94);
	public static final Color BORDER_COLOR = new Color(51, 65, 85);

	// Polices
	public static Font getTitleFont() {
		return new Font("Segoe UI", Font.BOLD, 24);
	}

	public static Font getSubtitleFont() {
		return new Font("Segoe UI", Font.PLAIN, 16);
	}

	public static Font getBodyFont() {
		return new Font("Segoe UI", Font.PLAIN, 14);
	}

	public static Font getSmallFont() {
		return new Font("Segoe UI", Font.PLAIN, 13);
	}

	// Panel avec gradient de fond
	public static JPanel createGradientPanel() {
		return new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				java.awt.GradientPaint gradient = new java.awt.GradientPaint(
					0, 0, DARK_BG,
					0, getHeight(), CARD_BG
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
	}

	// Card moderne
	public static JPanel createCard() {
		JPanel card = new JPanel();
		card.setBackground(CARD_BG);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER_COLOR, 1),
			new EmptyBorder(0, 0, 0, 0)
		));
		return card;
	}

	// Champ de texte stylisé
	public static JTextField createStyledTextField() {
		JTextField field = new JTextField();
		field.setPreferredSize(new java.awt.Dimension(300, 40));
		field.setFont(getBodyFont());
		field.setBackground(DARK_BG);
		field.setForeground(TEXT_PRIMARY);
		field.setCaretColor(TEXT_PRIMARY);
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		return field;
	}

	// Champ de mot de passe stylisé
	public static JPasswordField createStyledPasswordField() {
		JPasswordField field = new JPasswordField();
		field.setPreferredSize(new java.awt.Dimension(300, 40));
		field.setFont(getBodyFont());
		field.setBackground(DARK_BG);
		field.setForeground(TEXT_PRIMARY);
		field.setCaretColor(TEXT_PRIMARY);
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		return field;
	}

	// Bouton primaire moderne
	public static JButton createPrimaryButton(String text) {
		JButton button = new JButton(text) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				if (getModel().isPressed()) {
					g2d.setColor(new Color(29, 78, 216));
				} else if (getModel().isRollover()) {
					g2d.setColor(PRIMARY_BLUE_HOVER);
				} else {
					g2d.setColor(PRIMARY_BLUE);
				}
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

				super.paintComponent(g);
			}
		};
		button.setPreferredSize(new java.awt.Dimension(300, 44));
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		return button;
	}

	// Bouton secondaire moderne
	public static JButton createSecondaryButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new java.awt.Dimension(300, 44));
		button.setFont(getBodyFont());
		button.setForeground(TEXT_SECONDARY);
		button.setBackground(CARD_BG);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(10, 16, 10, 16)
		));
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				button.setBackground(BORDER_COLOR);
			}

			@Override
			public void mouseExited(MouseEvent evt) {
				button.setBackground(CARD_BG);
			}
		});

		return button;
	}

	// Bouton succès (vert)
	public static JButton createSuccessButton(String text) {
		JButton button = new JButton(text) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(SUCCESS_GREEN);
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				super.paintComponent(g);
			}
		};
		button.setPreferredSize(new java.awt.Dimension(150, 40));
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		return button;
	}

	// Bouton danger (rouge)
	public static JButton createDangerButton(String text) {
		JButton button = new JButton(text) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(ERROR_RED);
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				super.paintComponent(g);
			}
		};
		button.setPreferredSize(new java.awt.Dimension(150, 40));
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		return button;
	}
}


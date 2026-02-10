package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import models.Admin;
import models.Etudiant;
import models.ResponsableClub;
import models.Utilisateur;
import service.GestionUtilisateurs;

/**
 * Écran de connexion moderne avec design professionnel
 */
public class LoginFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField emailField;
	private JPasswordField passwordField;
	private JLabel errorLabel;

	// Couleurs modernes
	private static final Color CARD_BG = new Color(30, 41, 59);
	private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
	private static final Color PRIMARY_BLUE_HOVER = new Color(37, 99, 235);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
	private static final Color ERROR_RED = new Color(239, 68, 68);

	public LoginFrame() {
		setTitle("IHEC EventHub - Connexion");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Icône de fenêtre
		if (UiImages.logoSmall() != null) {
			setIconImage(UiImages.logoSmall().getImage());
		}

		// Panel principal avec gradient
		JPanel mainPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				GradientPaint gradient = new GradientPaint(
					0, 0, new Color(15, 23, 42),
					0, getHeight(), new Color(30, 41, 59)
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		add(mainPanel, BorderLayout.CENTER);

		// Container central
		JPanel centerContainer = new JPanel(new GridBagLayout());
		centerContainer.setOpaque(false);
		mainPanel.add(centerContainer, BorderLayout.CENTER);

		// Card de connexion
		JPanel loginCard = createLoginCard();
		
		GridBagConstraints gbc = new GridBagConstraints();
		centerContainer.add(loginCard, gbc);

		setMinimumSize(new Dimension(800, 500));
	}

	private JPanel createLoginCard() {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout(0, 0));
		card.setBackground(CARD_BG);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
			new EmptyBorder(0, 0, 0, 0)
		));
		card.setPreferredSize(new Dimension(750, 450));

		// Panel gauche - Logo et branding
		JPanel leftPanel = createBrandingPanel();
		card.add(leftPanel, BorderLayout.WEST);

		// Panel droit - Formulaire
		JPanel rightPanel = createFormPanel();
		card.add(rightPanel, BorderLayout.CENTER);

		return card;
	}

	private JPanel createBrandingPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(20, 30, 48));
		panel.setPreferredSize(new Dimension(320, 450));
		panel.setBorder(new EmptyBorder(50, 30, 50, 30));

		// Logo centré en haut
		if (UiImages.logoMedium() != null) {
			JLabel logoLabel = new JLabel(UiImages.logoMedium());
			logoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			panel.add(logoLabel);
			panel.add(Box.createVerticalStrut(30));
		}

		// Titre principal
		JLabel titleLabel = new JLabel("IHEC EventHub");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(titleLabel);

		panel.add(Box.createVerticalStrut(15));

		// Sous-titre
		JLabel subtitleLabel = new JLabel("Plateforme de Gestion");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		subtitleLabel.setForeground(TEXT_SECONDARY);
		subtitleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(subtitleLabel);

		JLabel subtitleLabel2 = new JLabel("des Événements");
		subtitleLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		subtitleLabel2.setForeground(TEXT_SECONDARY);
		subtitleLabel2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(subtitleLabel2);

		panel.add(Box.createVerticalGlue());

		// Points décoratifs
		JPanel dotsPanel = new JPanel();
		dotsPanel.setOpaque(false);
		dotsPanel.setLayout(new BoxLayout(dotsPanel, BoxLayout.X_AXIS));
		dotsPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		for (int i = 0; i < 3; i++) {
			JPanel dot = new JPanel();
			dot.setBackground(PRIMARY_BLUE);
			dot.setPreferredSize(new Dimension(8, 8));
			dot.setMaximumSize(new Dimension(8, 8));
			dotsPanel.add(dot);
			if (i < 2) dotsPanel.add(Box.createHorizontalStrut(8));
		}
		panel.add(dotsPanel);

		return panel;
	}

	private JPanel createFormPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(CARD_BG);
		panel.setBorder(new EmptyBorder(50, 50, 50, 50));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 0;

		// Titre du formulaire
		JLabel formTitle = new JLabel("Connexion");
		formTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
		formTitle.setForeground(TEXT_PRIMARY);
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(formTitle, gbc);

		JLabel welcomeLabel = new JLabel("Bienvenue ! Connectez-vous à votre compte");
		welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		welcomeLabel.setForeground(TEXT_SECONDARY);
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 30, 0);
		panel.add(welcomeLabel, gbc);

		// Email
		JLabel emailLabel = new JLabel("Adresse email");
		emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		emailLabel.setForeground(TEXT_SECONDARY);
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(emailLabel, gbc);

		emailField = createStyledTextField();
		gbc.gridy = 3;
		gbc.insets = new Insets(0, 0, 20, 0);
		panel.add(emailField, gbc);

		// Mot de passe
		JLabel pwdLabel = new JLabel("Mot de passe");
		pwdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		pwdLabel.setForeground(TEXT_SECONDARY);
		gbc.gridy = 4;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(pwdLabel, gbc);

		passwordField = createStyledPasswordField();
		gbc.gridy = 5;
		gbc.insets = new Insets(0, 0, 25, 0);
		panel.add(passwordField, gbc);

		// Message d'erreur
		errorLabel = new JLabel(" ");
		errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		errorLabel.setForeground(ERROR_RED);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridy = 6;
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(errorLabel, gbc);

		// Bouton de connexion
		JButton loginButton = createPrimaryButton("Se connecter");
		loginButton.addActionListener(e -> attemptLogin());
		gbc.gridy = 7;
		gbc.insets = new Insets(0, 0, 12, 0);
		panel.add(loginButton, gbc);

		// Bouton d'inscription
		JButton signupButton = createSecondaryButton("Créer un compte");
		signupButton.addActionListener(e -> new InscriptionFrame().setVisible(true));
		gbc.gridy = 8;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(signupButton, gbc);

		return panel;
	}

	private JTextField createStyledTextField() {
		JTextField field = new JTextField();
		field.setPreferredSize(new Dimension(300, 40));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBackground(new Color(15, 23, 42));
		field.setForeground(TEXT_PRIMARY);
		field.setCaretColor(TEXT_PRIMARY);
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		return field;
	}

	private JPasswordField createStyledPasswordField() {
		JPasswordField field = new JPasswordField();
		field.setPreferredSize(new Dimension(300, 40));
		field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		field.setBackground(new Color(15, 23, 42));
		field.setForeground(TEXT_PRIMARY);
		field.setCaretColor(TEXT_PRIMARY);
		field.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		return field;
	}

	private JButton createPrimaryButton(String text) {
		JButton button = new JButton(text) {
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
		button.setPreferredSize(new Dimension(300, 44));
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		return button;
	}

	private JButton createSecondaryButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(300, 44));
		button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		button.setForeground(TEXT_SECONDARY);
		button.setBackground(CARD_BG);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
			BorderFactory.createEmptyBorder(10, 16, 10, 16)
		));
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(new Color(51, 65, 85));
			}
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(CARD_BG);
			}
		});
		
		return button;
	}

	private void attemptLogin() {
		try {
			String email = emailField.getText().trim();
			String password = new String(passwordField.getPassword());
	
			if (email.isEmpty() || password.isEmpty()) {
				errorLabel.setText("Veuillez saisir l'email et le mot de passe.");
				return;
			}
	
			GestionUtilisateurs gestionUsers = GestionUtilisateurs.getInstance();
			initialiserUtilisateursTestSiNecessaire(gestionUsers);
	
			Utilisateur user = gestionUsers.authentifier(email, password);
	
			if (user == null) {
				if (gestionUsers.rechercherDansDemandesParEmail(email) != null) {
					errorLabel.setText("Votre compte est en attente de validation par l'admin.");
				} else {
					errorLabel.setText("Email ou mot de passe incorrect.");
				}
				return;
			}
	
			AppSession.setCurrentUser(user);
	
			String role = user.getRole();
			SwingUtilities.invokeLater(() -> {
				try {
					if ("Admin".equalsIgnoreCase(role)) {
						new AdminDashboard().setVisible(true);
					} else if ("ResponsableClub".equalsIgnoreCase(role)) {
						new ResponsableDashboard().setVisible(true);
					} else if ("Etudiant".equalsIgnoreCase(role) || "Étudiant".equalsIgnoreCase(role)) {
						new EtudiantDashboard().setVisible(true);
					} else {
						new AdminDashboard().setVisible(true);
					}
					dispose();
				} catch (Exception ex) {
					ex.printStackTrace();
					errorLabel.setText("Erreur lors de l'ouverture du tableau de bord.");
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
			errorLabel.setText("Une erreur inattendue est survenue.");
		}
	}

	private void initialiserUtilisateursTestSiNecessaire(GestionUtilisateurs gestionUsers) {
		// On initialise si l'admin par défaut n'existe pas encore
		if (gestionUsers.rechercherParEmail("admin@ihec.tn") != null) {
			return;
		}

		Admin admin = new Admin("Dupont", "Jean", "admin@ihec.tn", "admin123", "Administration");
		gestionUsers.inscrireUtilisateur(admin);

		ResponsableClub resp = new ResponsableClub("Ben Ali", "Sara", "sara@ihec.tn", "sara123", "IEEE");
		gestionUsers.inscrireUtilisateur(resp);

		Etudiant etudiant = new Etudiant("Trabelsi", "Mohamed", "mohamed@ihec.tn", "mohamed123", "L3", "Informatique");
		gestionUsers.inscrireUtilisateur(etudiant);
	}
}
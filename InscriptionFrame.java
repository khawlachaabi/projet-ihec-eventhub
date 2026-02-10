package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import models.Etudiant;
import models.ResponsableClub;
import service.GestionUtilisateurs;

/**
 * Fenêtre d'inscription moderne avec design professionnel
 */
public class InscriptionFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField nomField;
	private JTextField prenomField;
	private JTextField emailField;
	private JTextField telephoneField;
	private JPasswordField passwordField;
	private JComboBox<String> roleCombo;

	public InscriptionFrame() {
		setTitle("Créer un compte - IHEC EventHub");
		setSize(900, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		if (UiImages.logoSmall() != null) {
			setIconImage(UiImages.logoSmall().getImage());
		}

		// Panel principal avec gradient
		JPanel mainPanel = UiTheme.createGradientPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);

		// Container central
		JPanel centerContainer = new JPanel(new GridBagLayout());
		centerContainer.setOpaque(false);
		mainPanel.add(centerContainer, BorderLayout.CENTER);

		// Card d'inscription
		JPanel signupCard = createSignupCard();

		GridBagConstraints gbc = new GridBagConstraints();
		centerContainer.add(signupCard, gbc);

		setMinimumSize(new Dimension(800, 500));
	}

	private JPanel createSignupCard() {
		JPanel card = UiTheme.createCard();
		card.setLayout(new BorderLayout(0, 0));
		card.setPreferredSize(new Dimension(750, 550));

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
		panel.setBackground(UiTheme.HEADER_BG);
		panel.setPreferredSize(new Dimension(320, 550));
		panel.setBorder(new EmptyBorder(50, 30, 50, 30));

		// Logo centré en haut
		if (UiImages.logoMedium() != null) {
			JLabel logoLabel = new JLabel(UiImages.logoMedium());
			logoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			panel.add(logoLabel);
			panel.add(Box.createVerticalStrut(30));
		}

		// Titre principal
		JLabel titleLabel = new JLabel("Rejoignez IHEC");
		titleLabel.setFont(UiTheme.getTitleFont());
		titleLabel.setForeground(UiTheme.TEXT_PRIMARY);
		titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(titleLabel);

		panel.add(Box.createVerticalStrut(15));

		// Sous-titre
		JLabel subtitleLabel = new JLabel("Créez votre compte");
		subtitleLabel.setFont(UiTheme.getSubtitleFont());
		subtitleLabel.setForeground(UiTheme.TEXT_SECONDARY);
		subtitleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		panel.add(subtitleLabel);

		JLabel subtitleLabel2 = new JLabel("en quelques clics");
		subtitleLabel2.setFont(UiTheme.getSubtitleFont());
		subtitleLabel2.setForeground(UiTheme.TEXT_SECONDARY);
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
			dot.setBackground(UiTheme.PRIMARY_BLUE);
			dot.setPreferredSize(new Dimension(8, 8));
			dot.setMaximumSize(new Dimension(8, 8));
			dotsPanel.add(dot);
			if (i < 2)
				dotsPanel.add(Box.createHorizontalStrut(8));
		}
		panel.add(dotsPanel);

		return panel;
	}

	private JPanel createFormPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(UiTheme.CARD_BG);
		panel.setBorder(new EmptyBorder(50, 50, 50, 50));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 0;

		// Titre du formulaire
		JLabel formTitle = new JLabel("Créer un compte");
		formTitle.setFont(UiTheme.getTitleFont());
		formTitle.setForeground(UiTheme.TEXT_PRIMARY);
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 10, 0);
		panel.add(formTitle, gbc);

		JLabel welcomeLabel = new JLabel("Remplissez le formulaire ci-dessous");
		welcomeLabel.setFont(UiTheme.getSmallFont());
		welcomeLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 25, 0);
		panel.add(welcomeLabel, gbc);

		// Nom
		JLabel nomLabel = new JLabel("Nom");
		nomLabel.setFont(UiTheme.getSmallFont());
		nomLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(nomLabel, gbc);

		nomField = UiTheme.createStyledTextField();
		gbc.gridy = 3;
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(nomField, gbc);

		// Prénom
		JLabel prenomLabel = new JLabel("Prénom");
		prenomLabel.setFont(UiTheme.getSmallFont());
		prenomLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 4;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(prenomLabel, gbc);

		prenomField = UiTheme.createStyledTextField();
		gbc.gridy = 5;
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(prenomField, gbc);

		// Email
		JLabel emailLabel = new JLabel("Adresse email");
		emailLabel.setFont(UiTheme.getSmallFont());
		emailLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 6;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(emailLabel, gbc);

		emailField = UiTheme.createStyledTextField();
		gbc.gridy = 7;
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(emailField, gbc);

		// Téléphone
		JLabel telLabel = new JLabel("Téléphone");
		telLabel.setFont(UiTheme.getSmallFont());
		telLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 8;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(telLabel, gbc);

		telephoneField = UiTheme.createStyledTextField();
		gbc.gridy = 9;
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(telephoneField, gbc);

		// Mot de passe
		JLabel pwdLabel = new JLabel("Mot de passe");
		pwdLabel.setFont(UiTheme.getSmallFont());
		pwdLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 10;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(pwdLabel, gbc);

		passwordField = UiTheme.createStyledPasswordField();
		gbc.gridy = 11;
		gbc.insets = new Insets(0, 0, 15, 0);
		panel.add(passwordField, gbc);

		// Rôle
		JLabel roleLabel = new JLabel("Rôle");
		roleLabel.setFont(UiTheme.getSmallFont());
		roleLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 12;
		gbc.insets = new Insets(0, 0, 6, 0);
		panel.add(roleLabel, gbc);

		roleCombo = new JComboBox<>(new String[] { "Étudiant", "ResponsableClub" });
		roleCombo.setPreferredSize(new Dimension(300, 40));
		roleCombo.setFont(UiTheme.getBodyFont());
		roleCombo.setBackground(UiTheme.DARK_BG);
		roleCombo.setForeground(UiTheme.TEXT_PRIMARY);
		roleCombo.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
		gbc.gridy = 13;
		gbc.insets = new Insets(0, 0, 25, 0);
		panel.add(roleCombo, gbc);

		// Bouton d'inscription
		JButton signupButton = UiTheme.createPrimaryButton("Envoyer la demande");
		signupButton.addActionListener(e -> handleSignup());
		gbc.gridy = 14;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(signupButton, gbc);

		return panel;
	}

	private void handleSignup() {
		String nom = nomField.getText().trim();
		String prenom = prenomField.getText().trim();
		String email = emailField.getText().trim();
		String telephone = telephoneField.getText().trim();
		String mdp = new String(passwordField.getPassword());
		String role = (String) roleCombo.getSelectedItem();

		if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || mdp.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champs manquants",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		GestionUtilisateurs gestionUsers = GestionUtilisateurs.getInstance();

		boolean ok;
		if ("Étudiant".equalsIgnoreCase(role)) {
			Etudiant etu = new Etudiant(nom, prenom, email, mdp, "L3", "Informatique");
			etu.setTelephone(telephone);
			ok = gestionUsers.creerDemandeInscription(etu);
		} else {
			ResponsableClub resp = new ResponsableClub(nom, prenom, email, mdp, "Club étudiant");
			resp.setTelephone(telephone);
			ok = gestionUsers.creerDemandeInscription(resp);
		}

		if (ok) {
			JOptionPane.showMessageDialog(this,
					"Votre demande d'inscription a été envoyée à l'administrateur.\nVous serez notifié(e) après validation.",
					"Demande envoyée", JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this,
					"Cet email est déjà utilisé par un compte ou une demande en attente.\nEssayez avec un autre email.",
					"Email déjà utilisé", JOptionPane.ERROR_MESSAGE);
		}
	}
}

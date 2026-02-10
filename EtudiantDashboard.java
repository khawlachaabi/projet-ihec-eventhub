package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Tableau de bord moderne pour les étudiants
 */
public class EtudiantDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	public EtudiantDashboard() {
		setTitle("IHEC EventHub - Étudiant");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 650);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		if (UiImages.logoMedium() != null) {
			setIconImage(UiImages.logoMedium().getImage());
		}

		// Panel principal avec gradient
		JPanel mainPanel = UiTheme.createGradientPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);

		// Header
		JPanel header = createHeader();
		mainPanel.add(header, BorderLayout.NORTH);

		// Container central
		JPanel centerContainer = new JPanel(new GridBagLayout());
		centerContainer.setOpaque(false);
		centerContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
		mainPanel.add(centerContainer, BorderLayout.CENTER);

		// Card avec boutons
		JPanel card = UiTheme.createCard();
		card.setLayout(new GridBagLayout());
		card.setPreferredSize(new Dimension(600, 300));
		card.setBorder(new EmptyBorder(40, 40, 40, 40));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 0;

		JLabel welcomeLabel = new JLabel("Bienvenue dans votre espace étudiant");
		welcomeLabel.setFont(UiTheme.getTitleFont());
		welcomeLabel.setForeground(UiTheme.TEXT_PRIMARY);
		gbc.gridy = 0;
		gbc.insets = new java.awt.Insets(0, 0, 20, 0);
		card.add(welcomeLabel, gbc);

		JLabel descLabel = new JLabel("Consultez les événements disponibles à l'IHEC");
		descLabel.setFont(UiTheme.getSmallFont());
		descLabel.setForeground(UiTheme.TEXT_SECONDARY);
		gbc.gridy = 1;
		gbc.insets = new java.awt.Insets(0, 0, 30, 0);
		card.add(descLabel, gbc);

		JButton btnEvenements = UiTheme.createPrimaryButton("Voir les événements");
		btnEvenements.setPreferredSize(new Dimension(500, 50));
		btnEvenements.addActionListener(e -> new EvenementsEtudiantFrame().setVisible(true));
		gbc.gridy = 2;
		gbc.insets = new java.awt.Insets(0, 0, 0, 0);
		card.add(btnEvenements, gbc);

		centerContainer.add(card, gbc);

		setMinimumSize(new Dimension(900, 600));
	}

	private JPanel createHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(UiTheme.HEADER_BG);
		header.setBorder(new EmptyBorder(20, 30, 20, 30));

		JLabel logo = new JLabel(UiImages.logoMedium());
		JLabel title = new JLabel("Espace étudiant");
		title.setFont(UiTheme.getTitleFont());
		title.setForeground(UiTheme.TEXT_PRIMARY);

		JPanel left = new JPanel(new BorderLayout());
		left.setOpaque(false);
		left.add(logo, BorderLayout.WEST);
		left.add(Box.createHorizontalStrut(15), BorderLayout.CENTER);
		left.add(title, BorderLayout.EAST);

		JButton logout = UiTheme.createSecondaryButton("Se déconnecter");
		logout.setPreferredSize(new Dimension(150, 40));
		logout.addActionListener(e -> {
			new LoginFrame().setVisible(true);
			dispose();
		});

		header.add(left, BorderLayout.WEST);
		header.add(logout, BorderLayout.EAST);

		return header;
	}
}

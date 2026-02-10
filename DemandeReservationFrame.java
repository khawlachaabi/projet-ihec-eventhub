package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import models.Evenement;
import models.Reservation;
import models.Salle;
import models.Utilisateur;
import service.GestionEvenements;
import service.GestionReservations;
import service.GestionSalles;

/**
 * Formulaire moderne de demande de réservation avec sidebar
 */
public class DemandeReservationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> eventCombo;
	private JComboBox<String> salleCombo;
	private JTextArea commentaireArea;

	public DemandeReservationFrame() {
		initFrame();
		initContent();
	}

	private void initFrame() {
		setTitle("IHEC EventHub - Nouvelle Réservation");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1200, 750);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		if (UiImages.logoMedium() != null) {
			setIconImage(UiImages.logoMedium().getImage());
		}
		setMinimumSize(new Dimension(1000, 650));
	}

	private void initContent() {
		// Panel principal avec gradient
		JPanel root = UiTheme.createGradientPanel();
		root.setLayout(new BorderLayout());
		add(root, BorderLayout.CENTER);

		// Sidebar moderne
		JPanel sideBar = buildSideBar();
		root.add(sideBar, BorderLayout.WEST);

		// Zone centrale
		JPanel center = buildCenterPanel();
		root.add(center, BorderLayout.CENTER);
	}

	private JPanel buildSideBar() {
		JPanel side = new JPanel();
		side.setPreferredSize(new Dimension(280, 0));
		side.setBackground(UiTheme.HEADER_BG);
		side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
		side.setBorder(new EmptyBorder(30, 30, 30, 30));

		// Header avec logo
		JLabel logoLabel = new JLabel(UiImages.logoMedium());

		JLabel title = new JLabel("IHEC EventHub");
		title.setFont(UiTheme.getTitleFont());
		title.setForeground(UiTheme.TEXT_PRIMARY);

		JLabel subtitle = new JLabel("Gestion Réservations");
		subtitle.setFont(UiTheme.getSmallFont());
		subtitle.setForeground(UiTheme.TEXT_SECONDARY);

		JPanel textHeader = new JPanel();
		textHeader.setOpaque(false);
		textHeader.setLayout(new BoxLayout(textHeader, BoxLayout.Y_AXIS));
		textHeader.add(title);
		textHeader.add(Box.createVerticalStrut(4));
		textHeader.add(subtitle);

		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		header.setBorder(new EmptyBorder(0, 0, 30, 0));
		header.add(logoLabel, BorderLayout.WEST);
		header.add(textHeader, BorderLayout.CENTER);

		side.add(header);
		side.add(Box.createVerticalStrut(20));

		// Boutons de navigation
		boolean isAdmin = AppSession.isAdmin();
		
		if (isAdmin) {
			side.add(createNavItem("Tableau de bord", () -> {
				new AdminDashboard().setVisible(true);
				dispose();
			}, false));
			side.add(Box.createVerticalStrut(8));
			side.add(createNavItem("Réservations", () -> {
				new AdminReservationsFrame().setVisible(true);
				dispose();
			}, false));
		} else {
			Utilisateur user = AppSession.getCurrentUser();
			if (user != null && "ResponsableClub".equalsIgnoreCase(user.getRole())) {
				side.add(createNavItem("Tableau de bord", () -> {
					new ResponsableDashboard().setVisible(true);
					dispose();
				}, false));
				side.add(Box.createVerticalStrut(8));
				side.add(createNavItem("Créer événement", () -> {
					new CreerEvenementReservationFrame().setVisible(true);
					dispose();
				}, false));
			} else {
				side.add(createNavItem("Tableau de bord", () -> {
					new EtudiantDashboard().setVisible(true);
					dispose();
				}, false));
			}
			side.add(Box.createVerticalStrut(8));
		}
		
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Nouvelle réservation", null, true));
		side.add(Box.createVerticalStrut(20));
		side.add(createNavItem("Retour", () -> dispose(), false));

		side.add(Box.createVerticalGlue());

		JLabel footer = new JLabel("IHEC • 2025");
		footer.setForeground(UiTheme.TEXT_SECONDARY);
		footer.setFont(UiTheme.getSmallFont());
		side.add(footer);

		return side;
	}

	private JPanel buildCenterPanel() {
		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(false);
		center.setBorder(new EmptyBorder(40, 40, 40, 40));

		// Header
		JLabel title = new JLabel("Demander une réservation");
		title.setForeground(UiTheme.TEXT_PRIMARY);
		title.setFont(UiTheme.getTitleFont());

		JLabel subtitle = new JLabel("Sélectionnez un événement existant et la salle souhaitée pour votre réservation.");
		subtitle.setForeground(UiTheme.TEXT_SECONDARY);
		subtitle.setFont(UiTheme.getSmallFont());

		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.add(title);
		headerPanel.add(Box.createVerticalStrut(8));
		headerPanel.add(subtitle);

		center.add(headerPanel, BorderLayout.NORTH);

		// Formulaire
		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setOpaque(false);
		formContainer.setBorder(new EmptyBorder(30, 0, 0, 0));

		JPanel formCard = UiTheme.createCard();
		formCard.setLayout(new BorderLayout());
		formCard.setPreferredSize(new Dimension(650, 500));
		formCard.setBorder(new EmptyBorder(40, 40, 40, 40));

		JPanel form = buildForm();
		formCard.add(form, BorderLayout.CENTER);

		GridBagConstraints gbc = new GridBagConstraints();
		formContainer.add(formCard, gbc);

		center.add(formContainer, BorderLayout.CENTER);

		return center;
	}

	private JPanel buildForm() {
		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		// Initialiser les champs
		eventCombo = new JComboBox<>();
		styleComboBox(eventCombo);
		chargerEvenements();

		salleCombo = new JComboBox<>();
		styleComboBox(salleCombo);
		chargerSalles();

		commentaireArea = new JTextArea(4, 30);
		commentaireArea.setFont(UiTheme.getBodyFont());
		commentaireArea.setBackground(UiTheme.DARK_BG);
		commentaireArea.setForeground(UiTheme.TEXT_PRIMARY);
		commentaireArea.setCaretColor(UiTheme.TEXT_PRIMARY);
		commentaireArea.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(12, 12, 12, 12)
		));
		commentaireArea.setLineWrap(true);
		commentaireArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(commentaireArea);
		scrollPane.setPreferredSize(new Dimension(500, 120));
		scrollPane.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1));

		// Ajouter les champs
		int row = 0;
		addLabeledField("Événement associé", eventCombo, form, gbc, row++);
		addLabeledField("Salle souhaitée", salleCombo, form, gbc, row++);
		addLabeledField("Commentaire (optionnel)", scrollPane, form, gbc, row++);

		// Bouton de validation
		JButton envoyer = UiTheme.createPrimaryButton("Envoyer la demande");
		envoyer.setPreferredSize(new Dimension(400, 44));
		envoyer.addActionListener(e -> handleSubmit());

		gbc.gridx = 0;
		gbc.gridy = row * 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(30, 0, 0, 0);
		form.add(envoyer, gbc);

		return form;
	}

	private void styleComboBox(JComboBox<?> combo) {
		combo.setPreferredSize(new Dimension(500, 40));
		combo.setFont(UiTheme.getBodyFont());
		combo.setBackground(UiTheme.DARK_BG);
		combo.setForeground(UiTheme.TEXT_PRIMARY);
		combo.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(8, 12, 8, 12)
		));
	}

	private void addLabeledField(String labelText, java.awt.Component comp, JPanel panel, GridBagConstraints gbc, int row) {
		JLabel label = new JLabel(labelText);
		label.setForeground(UiTheme.TEXT_SECONDARY);
		label.setFont(UiTheme.getSmallFont());
		
		gbc.gridx = 0;
		gbc.gridy = row * 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(10, 10, 0, 10);
		panel.add(label, gbc);

		gbc.gridy = row * 2 + 1;
		gbc.insets = new Insets(5, 10, 15, 10);
		panel.add(comp, gbc);
		gbc.insets = new Insets(10, 10, 10, 10);
	}

	private void chargerEvenements() {
		Utilisateur currentUser = AppSession.getCurrentUser();
		if (currentUser == null) {
			return;
		}

		GestionEvenements gestionEvents = GestionEvenements.getInstance();
		List<Evenement> events;

		if (AppSession.isAdmin()) {
			events = gestionEvents.listerEvenementsActifs();
		} else {
			// Pour les non-admins, lister les événements de l'organisateur ou tous les événements actifs
			Utilisateur user = AppSession.getCurrentUser();
			if (user != null) {
				events = gestionEvents.listerParOrganisateur(user.getId());
				// Si aucun événement créé par l'utilisateur, afficher tous les événements actifs
				if (events.isEmpty()) {
					events = gestionEvents.listerEvenementsActifs();
				}
			} else {
				events = gestionEvents.listerEvenementsActifs();
			}
		}

		eventCombo.removeAllItems();
		
		if (events.isEmpty()) {
			eventCombo.addItem("Aucun événement disponible");
		} else {
			for (Evenement e : events) {
				eventCombo.addItem(e.getTitre());
			}
		}
	}

	private void chargerSalles() {
		GestionSalles gestionSalles = GestionSalles.getInstance();
		List<Salle> salles = gestionSalles.listerToutesSalles();

		salleCombo.removeAllItems();
		
		if (salles.isEmpty()) {
			salleCombo.addItem("Aucune salle disponible");
		} else {
			for (Salle s : salles) {
				salleCombo.addItem(s.getNom() + " - Capacité: " + s.getCapacite());
			}
		}
	}

	private void handleSubmit() {
		Utilisateur currentUser = AppSession.getCurrentUser();
		if (currentUser == null) {
			JOptionPane.showMessageDialog(this, "Aucun utilisateur connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String eventTitle = (String) eventCombo.getSelectedItem();
		String salleInfo = (String) salleCombo.getSelectedItem();
		String commentaire = commentaireArea.getText().trim();

		if (eventTitle == null || eventTitle.equals("Aucun événement disponible")) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un événement valide.", "Événement requis", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (salleInfo == null || salleInfo.equals("Aucune salle disponible")) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner une salle valide.", "Salle requise", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Extraire le nom de la salle (avant " - Capacité:")
		String salleNom = salleInfo.split(" - ")[0];

		// Récupérer l'événement et la salle
		GestionEvenements gestionEvents = GestionEvenements.getInstance();
		Evenement event = null;
		
		// Chercher l'événement par titre dans la liste
		List<Evenement> allEvents = gestionEvents.listerEvenementsActifs();
		for (Evenement e : allEvents) {
			if (e.getTitre().equals(eventTitle)) {
				event = e;
				break;
			}
		}
		
		if (event == null) {
			JOptionPane.showMessageDialog(this, "Événement introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		GestionSalles gestionSalles = GestionSalles.getInstance();
		Salle salle = gestionSalles.rechercherParNom(salleNom);
		
		if (salle == null) {
			JOptionPane.showMessageDialog(this, "Salle introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Créer la réservation
		Reservation reservation = new Reservation(event, salle);
		
		GestionReservations gestionRes = GestionReservations.getInstance();
		if (!gestionRes.creerReservation(reservation)) {
			JOptionPane.showMessageDialog(this, 
				"La salle n'est pas disponible pour cette période.\nVeuillez choisir une autre salle ou modifier les dates de l'événement.", 
				"Conflit de réservation", 
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		String message = "✓ Demande de réservation envoyée avec succès !\n\n" +
			"Événement: " + eventTitle + "\n" +
			"Salle: " + salleNom + "\n" +
			"Statut: EN ATTENTE de validation par l'administrateur";
		
		if (!commentaire.isEmpty()) {
			message += "\nCommentaire: " + commentaire;
		}

		JOptionPane.showMessageDialog(this, message, "Succès", JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}

	private JPanel createNavItem(String text, Runnable onClick, boolean active) {
		JLabel label = new JLabel(text);
		label.setForeground(active ? UiTheme.TEXT_PRIMARY : UiTheme.TEXT_SECONDARY);
		label.setFont(UiTheme.getBodyFont());

		class NavPanel extends JPanel {
			private static final long serialVersionUID = 1L;
			private boolean hovered = false;

			NavPanel() {
				super(new BorderLayout());
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (active || hovered) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					if (active) {
						g2d.setColor(UiTheme.PRIMARY_BLUE);
					} else {
						g2d.setColor(new Color(UiTheme.PRIMARY_BLUE.getRed(), UiTheme.PRIMARY_BLUE.getGreen(),
							UiTheme.PRIMARY_BLUE.getBlue(), 100));
					}
					g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
				}
			}

			void setHovered(boolean h) {
				hovered = h;
				repaint();
			}
		}

		NavPanel item = new NavPanel();
		item.setOpaque(false);
		item.setPreferredSize(new Dimension(Integer.MAX_VALUE, 45));
		item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
		item.setBorder(new EmptyBorder(10, 15, 10, 15));
		item.add(label, BorderLayout.CENTER);

		if (onClick != null) {
			item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			item.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (!active) {
						label.setForeground(UiTheme.TEXT_PRIMARY);
						item.setHovered(true);
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (!active) {
						label.setForeground(UiTheme.TEXT_SECONDARY);
						item.setHovered(false);
					}
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					onClick.run();
				}
			});
		}

		return item;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			DemandeReservationFrame frame = new DemandeReservationFrame();
			frame.setVisible(true);
		});
	}
}
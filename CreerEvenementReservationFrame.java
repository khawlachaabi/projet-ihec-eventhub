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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import models.Evenement;
import models.Reservation;
import models.Salle;
import models.TypeEvenement;
import models.Utilisateur;
import service.GestionEvenements;
import service.GestionReservations;
import service.GestionSalles;

/**
 * Formulaire moderne pour créer un événement et réserver une salle
 */
public class CreerEvenementReservationFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTextField titreField;
	private JTextField descriptionField;
	private JComboBox<TypeEvenement> typeCombo;
	private JSpinner dateSpinner;
	private JTextField heureField;
	private JTextField dureeField;
	private JComboBox<String> salleCombo;

	public CreerEvenementReservationFrame() {
		initFrame();
		initContent();
	}

	private void initFrame() {
		setTitle("IHEC EventHub - Nouvel Événement");
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

		JLabel subtitle = new JLabel("Gestion Événements");
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
		} else {
			Utilisateur user = AppSession.getCurrentUser();
			if (user != null && "ResponsableClub".equalsIgnoreCase(user.getRole())) {
				side.add(createNavItem("Tableau de bord", () -> {
					new ResponsableDashboard().setVisible(true);
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
		
		side.add(createNavItem("Créer événement", null, true));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Mes réservations", () -> {
			// Navigation vers la liste des réservations
		}, false));
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
		JLabel title = new JLabel("Créer un événement");
		title.setForeground(UiTheme.TEXT_PRIMARY);
		title.setFont(UiTheme.getTitleFont());

		JLabel subtitle = new JLabel("Remplissez les informations pour créer votre événement et réserver une salle.");
		subtitle.setForeground(UiTheme.TEXT_SECONDARY);
		subtitle.setFont(UiTheme.getSmallFont());

		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.add(title);
		headerPanel.add(Box.createVerticalStrut(8));
		headerPanel.add(subtitle);

		center.add(headerPanel, BorderLayout.NORTH);

		// Formulaire dans un scroll pane
		JPanel formContainer = new JPanel(new GridBagLayout());
		formContainer.setOpaque(false);
		formContainer.setBorder(new EmptyBorder(30, 0, 0, 0));

		JPanel formCard = UiTheme.createCard();
		formCard.setLayout(new BorderLayout());
		formCard.setPreferredSize(new Dimension(650, 550));
		formCard.setBorder(new EmptyBorder(40, 40, 40, 40));

		JPanel form = buildForm();
		
		JScrollPane scrollPane = new JScrollPane(form);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		formCard.add(scrollPane, BorderLayout.CENTER);

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
		titreField = UiTheme.createStyledTextField();
		descriptionField = UiTheme.createStyledTextField();
		
		typeCombo = new JComboBox<>(TypeEvenement.values());
		styleComboBox(typeCombo);

		// Date par défaut = aujourd'hui
		Date today = new Date();
		SpinnerDateModel dateModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
		dateSpinner = new JSpinner(dateModel);
		JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
		dateSpinner.setEditor(editor);
		dateSpinner.setPreferredSize(new Dimension(300, 40));
		dateSpinner.setFont(UiTheme.getBodyFont());

		heureField = UiTheme.createStyledTextField();
		heureField.setText("10:00");
		
		dureeField = UiTheme.createStyledTextField();
		dureeField.setText("120");
		
		salleCombo = new JComboBox<>();
		styleComboBox(salleCombo);
		
		chargerSallesDisponibles();

		// Ajouter les champs au formulaire
		int row = 0;
		addLabeledField("Titre de l'événement", titreField, form, gbc, row++);
		addLabeledField("Description", descriptionField, form, gbc, row++);
		addLabeledField("Type d'événement", typeCombo, form, gbc, row++);
		addLabeledField("Date (AAAA-MM-JJ)", dateSpinner, form, gbc, row++);
		addLabeledField("Heure de début (HH:mm)", heureField, form, gbc, row++);
		addLabeledField("Durée (minutes)", dureeField, form, gbc, row++);
		addLabeledField("Salle", salleCombo, form, gbc, row++);

		// Bouton de validation
		JButton valider = UiTheme.createPrimaryButton("Créer l'événement et réserver");
		valider.setPreferredSize(new Dimension(400, 44));
		valider.addActionListener(e -> handleCreate());

		gbc.gridx = 0;
		gbc.gridy = row * 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(30, 0, 0, 0);
		form.add(valider, gbc);

		return form;
	}

	private void styleComboBox(JComboBox<?> combo) {
		combo.setPreferredSize(new Dimension(300, 40));
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

	private void chargerSallesDisponibles() {
		GestionSalles gestionSalles = GestionSalles.getInstance();
		List<Salle> salles = gestionSalles.listerToutesSalles();

		if (salles.isEmpty()) {
			Salle amphi = new Salle("Amphi A", 300, models.TypeSalle.AMPHITHEATRE, "Rez-de-chaussée", "Bâtiment A");
			gestionSalles.ajouterSalle(amphi);

			Salle td1 = new Salle("TD1", 40, models.TypeSalle.SALLE_TD, "1er étage", "Bâtiment B");
			gestionSalles.ajouterSalle(td1);

			salles = gestionSalles.listerToutesSalles();
		}

		salleCombo.removeAllItems();
		for (Salle s : salles) {
			salleCombo.addItem(s.getNom());
		}
	}

	private void handleCreate() {
		Utilisateur organisateur = AppSession.getCurrentUser();
		if (organisateur == null) {
			JOptionPane.showMessageDialog(this, "Aucun utilisateur connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String titre = titreField.getText().trim();
		String desc = descriptionField.getText().trim();
		String heureTxt = heureField.getText().trim();
		String dureeTxt = dureeField.getText().trim();
		TypeEvenement type = (TypeEvenement) typeCombo.getSelectedItem();
		String salleNom = (String) salleCombo.getSelectedItem();

		if (titre.isEmpty() || desc.isEmpty() || heureTxt.isEmpty() || dureeTxt.isEmpty() || salleNom == null) {
			JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int dureeMinutes;
		try {
			dureeMinutes = Integer.parseInt(dureeTxt);
			if (dureeMinutes <= 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Durée invalide. Veuillez entrer un nombre positif de minutes.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dateDebut;
		try {
			Date selectedDate = (Date) dateSpinner.getValue();
			String dateTxt = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
			dateDebut = sdf.parse(dateTxt + " " + heureTxt);
		} catch (ParseException ex) {
			JOptionPane.showMessageDialog(this, "Format de date/heure invalide. Utilisez le format HH:mm pour l'heure.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Date dateFin = new Date(dateDebut.getTime() + dureeMinutes * 60000L);

		// Créer l'événement
		Evenement event = new Evenement(titre, desc, dateDebut, dateFin, type, organisateur);
		GestionEvenements gestionEvents = GestionEvenements.getInstance();
		if (!gestionEvents.creerEvenement(event)) {
			JOptionPane.showMessageDialog(this, "Impossible de créer l'événement. Vérifiez que la date n'est pas dans le passé.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Créer la réservation
		GestionSalles gestionSalles = GestionSalles.getInstance();
		Salle salle = gestionSalles.rechercherParNom(salleNom);
		if (salle == null) {
			JOptionPane.showMessageDialog(this, "Salle introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		Reservation reservation = new Reservation(event, salle);
		GestionReservations gestionRes = GestionReservations.getInstance();
		if (!gestionRes.creerReservation(reservation)) {
			JOptionPane.showMessageDialog(this, "La salle n'est pas disponible pour cette période.", "Conflit de réservation", JOptionPane.WARNING_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this,
			"✓ Événement créé avec succès !\n\nVotre demande de réservation a été envoyée à l'administrateur.\nStatut: EN ATTENTE de validation.",
			"Succès", JOptionPane.INFORMATION_MESSAGE);
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
			CreerEvenementReservationFrame frame = new CreerEvenementReservationFrame();
			frame.setVisible(true);
		});
	}
}
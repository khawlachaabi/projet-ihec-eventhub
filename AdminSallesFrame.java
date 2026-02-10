package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import models.Salle;
import models.TypeSalle;
import service.GestionSalles;

/**
 * Vue de gestion des salles avec design moderne
 */
public class AdminSallesFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JTable table;
	private DefaultTableModel model;

	private JTextField nomField;
	private JTextField capaciteField;
	private JComboBox<TypeSalle> typeCombo;
	private JTextField localisationField;
	private JTextField batimentField;

	public AdminSallesFrame() {
		initFrame();
		initContent();
	}

	private void initFrame() {
		setTitle("IHEC EventHub - Gestion des salles");
		setSize(1200, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

		JLabel subtitle = new JLabel("Panneau Admin");
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
		side.add(createNavItem("Tableau de bord", () -> {
			new AdminDashboard().setVisible(true);
			dispose();
		}, false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Salles", null, true));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Réservations", () -> {
			new AdminReservationsFrame().setVisible(true);
			dispose();
		}, false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Utilisateurs", () -> {
			new AdminUtilisateursFrame().setVisible(true);
			dispose();
		}, false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Statistiques", () -> {
			new AdminStatsFrame().setVisible(true);
			dispose();
		}, false));
		side.add(Box.createVerticalStrut(20));
		side.add(createNavItem("Se déconnecter", () -> {
			new LoginFrame().setVisible(true);
			dispose();
		}, false));

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

		// Header moderne
		JPanel header = createHeader();
		center.add(header, BorderLayout.NORTH);

		// Container central
		JPanel centerContainer = new JPanel(new BorderLayout());
		centerContainer.setOpaque(false);
		centerContainer.setBorder(new EmptyBorder(30, 0, 0, 0));
		center.add(centerContainer, BorderLayout.CENTER);

		// Card avec tableau
		JPanel tableCard = UiTheme.createCard();
		tableCard.setLayout(new BorderLayout());
		tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));

		// Tableau des salles
		String[] columns = { "ID", "Nom", "Capacité", "Type", "Disponibilité" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.setRowHeight(32);
		table.setFillsViewportHeight(true);
		styleTable(table);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		scrollPane.getViewport().setBackground(UiTheme.CARD_BG);

		tableCard.add(scrollPane, BorderLayout.CENTER);
		centerContainer.add(tableCard, BorderLayout.CENTER);

		// Card formulaire d'ajout
		JPanel formCard = UiTheme.createCard();
		formCard.setLayout(new BorderLayout());
		formCard.setBorder(new EmptyBorder(20, 20, 20, 20));
		formCard.setPreferredSize(new Dimension(350, 0));

		JLabel formTitle = new JLabel("Ajouter une salle");
		formTitle.setFont(UiTheme.getTitleFont());
		formTitle.setForeground(UiTheme.TEXT_PRIMARY);
		formTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
		formCard.add(formTitle, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.gridx = 0;

		nomField = UiTheme.createStyledTextField();
		capaciteField = UiTheme.createStyledTextField();
		typeCombo = new JComboBox<>(TypeSalle.values());
		typeCombo.setPreferredSize(new Dimension(300, 40));
		typeCombo.setFont(UiTheme.getBodyFont());
		typeCombo.setBackground(UiTheme.DARK_BG);
		typeCombo.setForeground(UiTheme.TEXT_PRIMARY);
		typeCombo.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1),
				BorderFactory.createEmptyBorder(8, 12, 8, 12)));
		localisationField = UiTheme.createStyledTextField();
		batimentField = UiTheme.createStyledTextField();

		int row = 0;
		addLabeledField("Nom", nomField, form, gbc, row++);
		addLabeledField("Capacité", capaciteField, form, gbc, row++);
		addLabeledField("Type", typeCombo, form, gbc, row++);
		addLabeledField("Localisation", localisationField, form, gbc, row++);
		addLabeledField("Bâtiment", batimentField, form, gbc, row++);

		JButton ajouterBtn = UiTheme.createPrimaryButton("Ajouter la salle");
		ajouterBtn.setPreferredSize(new Dimension(300, 44));
		ajouterBtn.addActionListener(e -> handleAddSalle());

		gbc.gridx = 0;
		gbc.gridy = row * 2;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(15, 0, 0, 0);
		form.add(ajouterBtn, gbc);

		formCard.add(form, BorderLayout.CENTER);
		centerContainer.add(formCard, BorderLayout.EAST);

		reloadSalles();

		return center;
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
			item.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			item.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent e) {
					if (!active) {
						label.setForeground(UiTheme.TEXT_PRIMARY);
						item.setHovered(true);
					}
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent e) {
					if (!active) {
						label.setForeground(UiTheme.TEXT_SECONDARY);
						item.setHovered(false);
					}
				}

				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					onClick.run();
				}
			});
		}

		return item;
	}

	private JPanel createHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(UiTheme.HEADER_BG);
		header.setBorder(new EmptyBorder(20, 30, 20, 30));

		JLabel logo = new JLabel(UiImages.logoMedium());
		JLabel title = new JLabel("Gestion des salles");
		title.setFont(UiTheme.getTitleFont());
		title.setForeground(UiTheme.TEXT_PRIMARY);

		JPanel left = new JPanel(new BorderLayout());
		left.setOpaque(false);
		left.add(logo, BorderLayout.WEST);
		left.add(new javax.swing.Box.Filler(new Dimension(15, 0), new Dimension(15, 0), new Dimension(15, 0)), BorderLayout.CENTER);
		left.add(title, BorderLayout.EAST);

		header.add(left, BorderLayout.WEST);
		return header;
	}

	private void styleTable(JTable table) {
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setBackground(UiTheme.PRIMARY_BLUE);
		table.getTableHeader().setForeground(UiTheme.TEXT_PRIMARY);
		table.getTableHeader().setFont(UiTheme.getBodyFont());

		table.setBackground(UiTheme.CARD_BG);
		table.setForeground(UiTheme.TEXT_PRIMARY);
		table.setSelectionBackground(UiTheme.PRIMARY_BLUE);
		table.setSelectionForeground(UiTheme.TEXT_PRIMARY);
	}

	private void addLabeledField(String labelText, java.awt.Component comp, JPanel panel, GridBagConstraints gbc,
			int row) {
		JLabel label = new JLabel(labelText);
		label.setForeground(UiTheme.TEXT_SECONDARY);
		label.setFont(UiTheme.getSmallFont());
		gbc.gridx = 0;
		gbc.gridy = row * 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 10, 0, 10);
		panel.add(label, gbc);

		gbc.gridy = row * 2 + 1;
		gbc.insets = new Insets(5, 10, 15, 10);
		panel.add(comp, gbc);
		gbc.insets = new Insets(8, 8, 8, 8);
	}

	private void reloadSalles() {
		model.setRowCount(0);
		GestionSalles gestionSalles = GestionSalles.getInstance();
		List<Salle> salles = gestionSalles.listerToutesSalles();
		for (Salle s : salles) {
			model.addRow(new Object[] { s.getId(), s.getNom(), s.getCapacite(), s.getType().getLibelle(),
					s.isDisponible() ? "Disponible" : "Réservée" });
		}
	}

	private void handleAddSalle() {
		String nom = nomField.getText().trim();
		String capaciteTxt = capaciteField.getText().trim();
		TypeSalle type = (TypeSalle) typeCombo.getSelectedItem();
		String loc = localisationField.getText().trim();
		String bat = batimentField.getText().trim();

		if (nom.isEmpty() || capaciteTxt.isEmpty() || type == null || loc.isEmpty() || bat.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Champs manquants",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int capacite;
		try {
			capacite = Integer.parseInt(capaciteTxt);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Capacité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		GestionSalles gestionSalles = GestionSalles.getInstance();
		Salle salle = new Salle(nom, capacite, type, loc, bat);
		if (!gestionSalles.ajouterSalle(salle)) {
			JOptionPane.showMessageDialog(this, "Une salle avec ce nom existe déjà.", "Doublon",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Réinitialiser les champs
		nomField.setText("");
		capaciteField.setText("");
		localisationField.setText("");
		batimentField.setText("");

		reloadSalles();
		JOptionPane.showMessageDialog(this, "Salle ajoutée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
	}
}

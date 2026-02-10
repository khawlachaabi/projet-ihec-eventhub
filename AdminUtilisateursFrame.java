package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import service.GestionUtilisateurs;
import models.Utilisateur;

/**
 * Vue moderne de gestion des utilisateurs
 */
public class AdminUtilisateursFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// Couleurs modernes
	private static final Color CARD_BG = new Color(30, 41, 59);
	private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
	private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
	private static final Color DANGER_RED = new Color(239, 68, 68);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
	private static final Color BORDER_COLOR = new Color(51, 65, 85);

	public AdminUtilisateursFrame() {
		initFrame();
		initContent();
	}

	private void initFrame() {
		setTitle("IHEC EventHub - Gestion des utilisateurs");
		setSize(1200, 750);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		if (UiImages.logoSmall() != null) {
			setIconImage(UiImages.logoSmall().getImage());
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
		side.add(createNavItem("Salles", () -> {
			new AdminSallesFrame().setVisible(true);
			dispose();
		}, false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Réservations", () -> {
			new AdminReservationsFrame().setVisible(true);
			dispose();
		}, false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Utilisateurs", null, true));
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

		// Header
		JPanel header = createModernHeader();
		center.add(header, BorderLayout.NORTH);

		// Container principal
		JPanel contentContainer = new JPanel(new BorderLayout(0, 20));
		contentContainer.setOpaque(false);
		contentContainer.setBorder(new EmptyBorder(20, 0, 0, 0));
		center.add(contentContainer, BorderLayout.CENTER);

		// Tables et données
		String[] columns = { "ID", "Prénom", "Nom", "Email", "Rôle", "Téléphone" };
		DefaultTableModel usersModel = new DefaultTableModel(columns, 0);
		DefaultTableModel demandesModel = new DefaultTableModel(columns, 0);

		GestionUtilisateurs gestionUsers = GestionUtilisateurs.getInstance();
		chargerUtilisateurs(gestionUsers, usersModel);
		chargerDemandes(gestionUsers, demandesModel);

		// Section utilisateurs actifs
		JPanel usersSection = createUsersSection(usersModel);
		contentContainer.add(usersSection, BorderLayout.CENTER);

		// Section demandes
		JPanel demandesSection = createDemandesSection(demandesModel, gestionUsers, usersModel);
		contentContainer.add(demandesSection, BorderLayout.SOUTH);

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
						g2d.setColor(PRIMARY_BLUE);
					} else {
						g2d.setColor(new Color(PRIMARY_BLUE.getRed(), PRIMARY_BLUE.getGreen(),
								PRIMARY_BLUE.getBlue(), 100));
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
						label.setForeground(TEXT_PRIMARY);
						item.setHovered(true);
					}
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent e) {
					if (!active) {
						label.setForeground(TEXT_SECONDARY);
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

	private JPanel createModernHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setOpaque(false);
		header.setBorder(new EmptyBorder(0, 0, 20, 0));

		// Panneau gauche avec logo
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
		leftPanel.setOpaque(false);

		if (UiImages.logoMedium() != null) {
			JLabel logoLabel = new JLabel(UiImages.logoMedium());
			leftPanel.add(logoLabel);
			leftPanel.add(Box.createHorizontalStrut(16));
		}

		// Titres
		JPanel titlesPanel = new JPanel();
		titlesPanel.setLayout(new BoxLayout(titlesPanel, BoxLayout.Y_AXIS));
		titlesPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("Gestion des Utilisateurs");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		titlesPanel.add(titleLabel);

		titlesPanel.add(Box.createVerticalStrut(4));

		JLabel subtitleLabel = new JLabel("Gérez les comptes actifs et validez les demandes d'inscription");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(TEXT_SECONDARY);
		subtitleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		titlesPanel.add(subtitleLabel);

		leftPanel.add(titlesPanel);
		header.add(leftPanel, BorderLayout.WEST);

		return header;
	}

	private JPanel createUsersSection(DefaultTableModel usersModel) {
		JPanel section = new JPanel(new BorderLayout());
		section.setOpaque(false);

		// En-tête de section avec badge
		JPanel sectionHeader = new JPanel(new BorderLayout());
		sectionHeader.setOpaque(false);
		sectionHeader.setBorder(new EmptyBorder(0, 0, 12, 0));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.setOpaque(false);

		JLabel sectionTitle = new JLabel("Utilisateurs Actifs");
		sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		sectionTitle.setForeground(TEXT_PRIMARY);
		titlePanel.add(sectionTitle);

		titlePanel.add(Box.createHorizontalStrut(12));

		// Badge de comptage
		JLabel countBadge = new JLabel(String.valueOf(usersModel.getRowCount()));
		countBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
		countBadge.setForeground(Color.WHITE);
		countBadge.setOpaque(true);
		countBadge.setBackground(PRIMARY_BLUE);
		countBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
		titlePanel.add(countBadge);

		sectionHeader.add(titlePanel, BorderLayout.WEST);
		section.add(sectionHeader, BorderLayout.NORTH);

		// Table
		JTable usersTable = new JTable(usersModel);
		styleModernTable(usersTable);

		JScrollPane scrollPane = new JScrollPane(usersTable);
		styleScrollPane(scrollPane);
		scrollPane.setPreferredSize(new Dimension(0, 300));

		section.add(scrollPane, BorderLayout.CENTER);

		return section;
	}

	private JPanel createDemandesSection(DefaultTableModel demandesModel, GestionUtilisateurs gestionUsers,
			DefaultTableModel usersModel) {
		JPanel section = new JPanel(new BorderLayout());
		section.setOpaque(false);
		section.setPreferredSize(new Dimension(0, 280));

		// En-tête de section
		JPanel sectionHeader = new JPanel(new BorderLayout());
		sectionHeader.setOpaque(false);
		sectionHeader.setBorder(new EmptyBorder(0, 0, 12, 0));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.setOpaque(false);

		JLabel sectionTitle = new JLabel("Demandes d'Inscription");
		sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		sectionTitle.setForeground(TEXT_PRIMARY);
		titlePanel.add(sectionTitle);

		titlePanel.add(Box.createHorizontalStrut(12));

		// Badge de comptage
		JLabel countBadge = new JLabel(String.valueOf(demandesModel.getRowCount()));
		countBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));
		countBadge.setForeground(Color.WHITE);
		countBadge.setOpaque(true);
		countBadge.setBackground(new Color(234, 179, 8)); // Amber pour les demandes
		countBadge.setBorder(new EmptyBorder(4, 10, 4, 10));
		titlePanel.add(countBadge);

		sectionHeader.add(titlePanel, BorderLayout.WEST);
		section.add(sectionHeader, BorderLayout.NORTH);

		// Table
		JTable demandesTable = new JTable(demandesModel);
		styleModernTable(demandesTable);

		JScrollPane scrollPane = new JScrollPane(demandesTable);
		styleScrollPane(scrollPane);

		section.add(scrollPane, BorderLayout.CENTER);

		// Boutons d'action
		JPanel actionsPanel = new JPanel();
		actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.X_AXIS));
		actionsPanel.setOpaque(false);
		actionsPanel.setBorder(new EmptyBorder(12, 0, 0, 0));

		JButton accepterBtn = createModernButton("✓ Accepter", SUCCESS_GREEN);
		JButton refuserBtn = createModernButton("✕ Refuser", DANGER_RED);

		accepterBtn.addActionListener(e -> {
			int row = demandesTable.getSelectedRow();
			if (row == -1) {
				showModernDialog("Veuillez sélectionner une demande", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int id = (int) demandesModel.getValueAt(row, 0);
			String nom = (String) demandesModel.getValueAt(row, 2);
			String prenom = (String) demandesModel.getValueAt(row, 1);
			
			if (gestionUsers.accepterDemande(id)) {
				rechargerTables(gestionUsers, usersModel, demandesModel, countBadge);
				showModernDialog("La demande de " + prenom + " " + nom + " a été acceptée avec succès.", 
					"Demande acceptée", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		refuserBtn.addActionListener(e -> {
			int row = demandesTable.getSelectedRow();
			if (row == -1) {
				showModernDialog("Veuillez sélectionner une demande", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
				return;
			}
			int id = (int) demandesModel.getValueAt(row, 0);
			String nom = (String) demandesModel.getValueAt(row, 2);
			String prenom = (String) demandesModel.getValueAt(row, 1);
			
			if (gestionUsers.refuserDemande(id)) {
				rechargerTables(gestionUsers, usersModel, demandesModel, countBadge);
				showModernDialog("La demande de " + prenom + " " + nom + " a été refusée.", 
					"Demande refusée", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		actionsPanel.add(accepterBtn);
		actionsPanel.add(Box.createHorizontalStrut(12));
		actionsPanel.add(refuserBtn);
		actionsPanel.add(Box.createHorizontalGlue());

		section.add(actionsPanel, BorderLayout.SOUTH);

		return section;
	}

	private void styleModernTable(JTable table) {
		table.setRowHeight(36);
		table.setFillsViewportHeight(true);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		// Header
		JTableHeader header = table.getTableHeader();
		header.setReorderingAllowed(false);
		header.setBackground(CARD_BG);
		header.setForeground(TEXT_SECONDARY);
		header.setFont(new Font("Segoe UI", Font.BOLD, 12));
		header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
		header.setPreferredSize(new Dimension(header.getWidth(), 40));

		// Renderer personnalisé
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				setFont(new Font("Segoe UI", Font.PLAIN, 13));
				setBorder(new EmptyBorder(8, 12, 8, 12));
				
				if (isSelected) {
					setBackground(new Color(51, 65, 85));
					setForeground(TEXT_PRIMARY);
				} else {
					setBackground(row % 2 == 0 ? CARD_BG : new Color(37, 48, 66));
					setForeground(TEXT_PRIMARY);
				}
				
				// Coloration spéciale pour la colonne rôle
				if (column == 4 && value != null) {
					String role = value.toString();
					if ("Admin".equalsIgnoreCase(role)) {
						setForeground(new Color(239, 68, 68));
					} else if ("ResponsableClub".equalsIgnoreCase(role)) {
						setForeground(new Color(59, 130, 246));
					} else if (role.toLowerCase().contains("etudiant")) {
						setForeground(new Color(34, 197, 94));
					}
				}
				
				return this;
			}
		};

		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		table.setBackground(CARD_BG);
		table.setForeground(TEXT_PRIMARY);
		table.setSelectionBackground(new Color(51, 65, 85));
		table.setSelectionForeground(TEXT_PRIMARY);
	}

	private void styleScrollPane(JScrollPane scrollPane) {
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBackground(CARD_BG);
		scrollPane.getViewport().setBackground(CARD_BG);
		scrollPane.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(BORDER_COLOR, 1),
			BorderFactory.createEmptyBorder(0, 0, 0, 0)
		));
	}

	private JButton createModernButton(String text, Color bgColor) {
		JButton button = new JButton(text) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				Color color = bgColor;
				if (getModel().isPressed()) {
					color = color.darker();
				} else if (getModel().isRollover()) {
					color = color.brighter();
				}
				
				g2d.setColor(color);
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
				
				super.paintComponent(g);
			}
		};
		
		button.setFont(new Font("Segoe UI", Font.BOLD, 13));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setPreferredSize(new Dimension(130, 38));
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		return button;
	}

	private void showModernDialog(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	private void chargerUtilisateurs(GestionUtilisateurs gestionUsers, DefaultTableModel model) {
		for (Utilisateur u : gestionUsers.listerTousUtilisateurs()) {
			model.addRow(new Object[] { 
				u.getId(), 
				u.getPrenom(), 
				u.getNom(), 
				u.getEmail(), 
				u.getRole(),
				u.getTelephone() == null ? "-" : u.getTelephone() 
			});
		}
	}

	private void chargerDemandes(GestionUtilisateurs gestionUsers, DefaultTableModel model) {
		for (Utilisateur u : gestionUsers.listerDemandesInscription()) {
			model.addRow(new Object[] { 
				u.getId(), 
				u.getPrenom(), 
				u.getNom(), 
				u.getEmail(), 
				u.getRole(),
				u.getTelephone() == null ? "-" : u.getTelephone() 
			});
		}
	}

	private void rechargerTables(GestionUtilisateurs gestionUsers, DefaultTableModel usersModel,
			DefaultTableModel demandesModel, JLabel countBadge) {
		usersModel.setRowCount(0);
		chargerUtilisateurs(gestionUsers, usersModel);

		demandesModel.setRowCount(0);
		chargerDemandes(gestionUsers, demandesModel);
		
		// Mettre à jour le badge
		countBadge.setText(String.valueOf(demandesModel.getRowCount()));
	}
}
package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.List;

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

import models.Admin;
import models.Reservation;
import service.GestionReservations;

/**
 * Vue moderne de gestion des réservations (admin)
 */
public class AdminReservationsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// Couleurs modernes
	private static final Color DARK_BG = new Color(15, 23, 42);
	private static final Color CARD_BG = new Color(30, 41, 59);
	private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
	private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
	private static final Color DANGER_RED = new Color(239, 68, 68);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
	private static final Color BORDER_COLOR = new Color(51, 65, 85);

	private JTable table;
	private DefaultTableModel model;

	public AdminReservationsFrame() {
		setTitle("IHEC EventHub - Gestion des réservations");
		setSize(1200, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

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
					0, 0, DARK_BG,
					0, getHeight(), new Color(30, 41, 59)
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		add(mainPanel, BorderLayout.CENTER);

		// Header moderne
		JPanel header = createModernHeader();
		mainPanel.add(header, BorderLayout.NORTH);

		// Container de contenu
		JPanel contentContainer = new JPanel(new BorderLayout());
		contentContainer.setOpaque(false);
		contentContainer.setBorder(new EmptyBorder(20, 0, 0, 0));
		mainPanel.add(contentContainer, BorderLayout.CENTER);

		// Table
		String[] columns = { "ID", "Événement", "Salle", "Date création", "Statut", "Validée par" };
		model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(model);
		styleModernTable(table);

		JScrollPane scrollPane = new JScrollPane(table);
		styleScrollPane(scrollPane);

		contentContainer.add(scrollPane, BorderLayout.CENTER);

		// Barre d'actions moderne
		JPanel actionsPanel = createActionsPanel();
		contentContainer.add(actionsPanel, BorderLayout.SOUTH);

		setMinimumSize(new Dimension(900, 600));

		reloadReservations();
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

		JLabel titleLabel = new JLabel("Gestion des Réservations");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		titlesPanel.add(titleLabel);

		titlesPanel.add(Box.createVerticalStrut(4));

		JLabel subtitleLabel = new JLabel("Validez ou refusez les demandes de réservation");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(TEXT_SECONDARY);
		subtitleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		titlesPanel.add(subtitleLabel);

		leftPanel.add(titlesPanel);
		header.add(leftPanel, BorderLayout.WEST);

		// Badge de stats
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
		statsPanel.setOpaque(false);

		GestionReservations gestionRes = GestionReservations.getInstance();
		int totalReservations = gestionRes.compterReservations();
		int enAttente = gestionRes.listerReservationsEnAttente().size();

		JPanel badgesPanel = new JPanel();
		badgesPanel.setLayout(new BoxLayout(badgesPanel, BoxLayout.X_AXIS));
		badgesPanel.setOpaque(false);

		badgesPanel.add(createStatsBadge("Total", String.valueOf(totalReservations), PRIMARY_BLUE));
		badgesPanel.add(Box.createHorizontalStrut(8));
		badgesPanel.add(createStatsBadge("En attente", String.valueOf(enAttente), new Color(234, 179, 8)));

		statsPanel.add(badgesPanel);
		header.add(statsPanel, BorderLayout.EAST);

		return header;
	}

	private JPanel createStatsBadge(String label, String value, Color color) {
		JPanel badge = new JPanel();
		badge.setLayout(new BoxLayout(badge, BoxLayout.Y_AXIS));
		badge.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 30));
		badge.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(color, 1),
			new EmptyBorder(8, 16, 8, 16)
		));

		JLabel valueLabel = new JLabel(value);
		valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		valueLabel.setForeground(color);
		valueLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		badge.add(valueLabel);

		JLabel labelComp = new JLabel(label);
		labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		labelComp.setForeground(TEXT_SECONDARY);
		labelComp.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		badge.add(labelComp);

		return badge;
	}

	private void styleModernTable(JTable table) {
		table.setRowHeight(40);
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
		header.setPreferredSize(new Dimension(header.getWidth(), 42));

		// Renderer personnalisé
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
					boolean isSelected, boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				
				setFont(new Font("Segoe UI", Font.PLAIN, 13));
				setBorder(new EmptyBorder(10, 12, 10, 12));
				
				if (isSelected) {
					setBackground(new Color(51, 65, 85));
					setForeground(TEXT_PRIMARY);
				} else {
					setBackground(row % 2 == 0 ? CARD_BG : new Color(37, 48, 66));
					setForeground(TEXT_PRIMARY);
				}
				
				// Coloration spéciale pour la colonne Statut
				if (column == 4 && value != null) {
					String statut = value.toString();
					JLabel statusLabel = new JLabel(statut);
					statusLabel.setOpaque(true);
					statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
					statusLabel.setBorder(new EmptyBorder(4, 10, 4, 10));
					
					if ("VALIDEE".equalsIgnoreCase(statut)) {
						statusLabel.setBackground(new Color(34, 197, 94, 50));
						statusLabel.setForeground(SUCCESS_GREEN);
						setText("✓ Validée");
						setForeground(SUCCESS_GREEN);
					} else if ("REFUSEE".equalsIgnoreCase(statut)) {
						statusLabel.setBackground(new Color(239, 68, 68, 50));
						statusLabel.setForeground(DANGER_RED);
						setText("✕ Refusée");
						setForeground(DANGER_RED);
					} else if ("EN_ATTENTE".equalsIgnoreCase(statut)) {
						statusLabel.setBackground(new Color(234, 179, 8, 50));
						statusLabel.setForeground(new Color(234, 179, 8));
						setText("⏳ En attente");
						setForeground(new Color(234, 179, 8));
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

	private JPanel createActionsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(16, 0, 0, 0));

		JButton validerBtn = createModernButton("✓ Valider", SUCCESS_GREEN);
		JButton refuserBtn = createModernButton("✕ Refuser", DANGER_RED);
		JButton rafraichirBtn = createModernButton("🔄 Rafraîchir", PRIMARY_BLUE);

		validerBtn.addActionListener(e -> handleValidate());
		refuserBtn.addActionListener(e -> handleRefuse());
		rafraichirBtn.addActionListener(e -> reloadReservations());

		panel.add(validerBtn);
		panel.add(Box.createHorizontalStrut(12));
		panel.add(refuserBtn);
		panel.add(Box.createHorizontalStrut(12));
		panel.add(rafraichirBtn);
		panel.add(Box.createHorizontalGlue());

		// Info label
		JLabel infoLabel = new JLabel("Sélectionnez une réservation pour la gérer");
		infoLabel.setForeground(TEXT_SECONDARY);
		infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
		panel.add(infoLabel);

		return panel;
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
		button.setPreferredSize(new Dimension(140, 40));
		button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		
		return button;
	}

	private void reloadReservations() {
		model.setRowCount(0);
		GestionReservations gestionRes = GestionReservations.getInstance();
		List<Reservation> reservations = gestionRes.listerToutesReservations();

		for (Reservation r : reservations) {
			String statut = r.getStatut().name();
			String validePar = (r.getValidePar() != null)
					? r.getValidePar().getPrenom() + " " + r.getValidePar().getNom()
					: "-";
			model.addRow(new Object[] { 
				r.getId(), 
				r.getEvenement().getTitre(), 
				r.getSalle().getNom(),
				r.getDateCreation(), 
				statut, 
				validePar 
			});
		}
	}

	private Admin getCurrentAdmin() {
		if (!(AppSession.getCurrentUser() instanceof Admin admin)) {
			showModernDialog("Aucun administrateur connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return admin;
	}

	private void handleValidate() {
		int row = table.getSelectedRow();
		if (row == -1) {
			showModernDialog("Veuillez sélectionner une réservation dans la liste.", 
				"Aucune sélection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int id = (int) model.getValueAt(row, 0);
		String evenement = (String) model.getValueAt(row, 1);
		
		Admin admin = getCurrentAdmin();
		if (admin == null) {
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(this, 
			"Confirmer la validation de la réservation pour l'événement :\n" + evenement,
			"Confirmer la validation", 
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		
		if (confirm != JOptionPane.YES_OPTION) {
			return;
		}

		GestionReservations gestionRes = GestionReservations.getInstance();
		boolean ok = gestionRes.validerReservation(id, admin);
		if (!ok) {
			showModernDialog("Impossible de valider cette réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		reloadReservations();
		showModernDialog("La réservation pour \"" + evenement + "\" a été validée avec succès.", 
			"Validation réussie", JOptionPane.INFORMATION_MESSAGE);
	}

	private void handleRefuse() {
		int row = table.getSelectedRow();
		if (row == -1) {
			showModernDialog("Veuillez sélectionner une réservation dans la liste.", 
				"Aucune sélection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int id = (int) model.getValueAt(row, 0);
		String evenement = (String) model.getValueAt(row, 1);
		
		String motif = JOptionPane.showInputDialog(this, 
			"Motif du refus pour l'événement \"" + evenement + "\" :", 
			"Refuser la réservation",
			JOptionPane.QUESTION_MESSAGE);
		
		if (motif == null || motif.trim().isEmpty()) {
			return;
		}

		Admin admin = getCurrentAdmin();
		if (admin == null) {
			return;
		}

		GestionReservations gestionRes = GestionReservations.getInstance();
		boolean ok = gestionRes.refuserReservation(id, admin, motif);
		if (!ok) {
			showModernDialog("Impossible de refuser cette réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}

		reloadReservations();
		showModernDialog("La réservation pour \"" + evenement + "\" a été refusée.\nMotif : " + motif, 
			"Réservation refusée", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showModernDialog(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
	}
}
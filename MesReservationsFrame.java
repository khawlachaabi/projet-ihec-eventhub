package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import models.Reservation;
import models.Utilisateur;
import service.GestionReservations;

/**
 * Liste moderne des réservations de l'utilisateur connecté
 */
public class MesReservationsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	// Couleurs modernes
	private static final Color DARK_BG = new Color(15, 23, 42);
	private static final Color CARD_BG = new Color(30, 41, 59);
	private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
	private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
	private static final Color DANGER_RED = new Color(239, 68, 68);
	private static final Color WARNING_AMBER = new Color(234, 179, 8);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
	private static final Color BORDER_COLOR = new Color(51, 65, 85);

	public MesReservationsFrame() {
		setTitle("IHEC EventHub - Mes réservations");
		setSize(1200, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		if (UiImages.logoMedium() != null) {
			setIconImage(UiImages.logoMedium().getImage());
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

		// Container central
		JPanel centerContainer = new JPanel(new BorderLayout());
		centerContainer.setOpaque(false);
		centerContainer.setBorder(new EmptyBorder(20, 0, 0, 0));
		mainPanel.add(centerContainer, BorderLayout.CENTER);

		// Card avec tableau
		String[] columns = { "ID", "Événement", "Salle", "Date création", "Statut" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Charger les réservations
		Utilisateur current = AppSession.getCurrentUser();
		int totalRes = 0;
		int enAttente = 0;
		int validees = 0;
		
		if (current != null) {
			GestionReservations gestionRes = GestionReservations.getInstance();
			List<Reservation> reservations = gestionRes.listerParUtilisateur(current.getId());
			totalRes = reservations.size();
			
			for (Reservation r : reservations) {
				String statut = r.getStatut().name();
				if ("EN_ATTENTE".equals(statut)) enAttente++;
				if ("VALIDEE".equals(statut)) validees++;
				
				model.addRow(new Object[] { 
					r.getId(), 
					r.getEvenement().getTitre(), 
					r.getSalle().getNom(),
					r.getDateCreation(), 
					statut 
				});
			}
		}

		// Ajouter les badges de stats au header
		addStatsBadges(header, totalRes, enAttente, validees);

		JTable table = new JTable(model);
		styleModernTable(table);

		JScrollPane scroll = new JScrollPane(table);
		styleScrollPane(scroll);

		centerContainer.add(scroll, BorderLayout.CENTER);

		setMinimumSize(new Dimension(900, 600));
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

		JLabel titleLabel = new JLabel("Mes Réservations");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		titlesPanel.add(titleLabel);

		titlesPanel.add(Box.createVerticalStrut(4));

		JLabel subtitleLabel = new JLabel("Suivez l'état de vos demandes de réservation");
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitleLabel.setForeground(TEXT_SECONDARY);
		subtitleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		titlesPanel.add(subtitleLabel);

		leftPanel.add(titlesPanel);
		header.add(leftPanel, BorderLayout.WEST);

		return header;
	}

	private void addStatsBadges(JPanel header, int total, int enAttente, int validees) {
		// Panel pour les badges
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.X_AXIS));
		statsPanel.setOpaque(false);

		statsPanel.add(createStatsBadge("Total", String.valueOf(total), PRIMARY_BLUE));
		statsPanel.add(Box.createHorizontalStrut(8));
		statsPanel.add(createStatsBadge("En attente", String.valueOf(enAttente), WARNING_AMBER));
		statsPanel.add(Box.createHorizontalStrut(8));
		statsPanel.add(createStatsBadge("Validées", String.valueOf(validees), SUCCESS_GREEN));

		header.add(statsPanel, BorderLayout.EAST);
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
					
					if ("VALIDEE".equalsIgnoreCase(statut)) {
						setText("✓ Validée");
						setForeground(SUCCESS_GREEN);
					} else if ("REFUSEE".equalsIgnoreCase(statut)) {
						setText("✕ Refusée");
						setForeground(DANGER_RED);
					} else if ("EN_ATTENTE".equalsIgnoreCase(statut)) {
						setText("⏳ En attente");
						setForeground(WARNING_AMBER);
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
}
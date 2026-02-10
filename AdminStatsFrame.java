package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import service.GestionEvenements;
import service.GestionReservations;
import service.GestionSalles;
import service.GestionUtilisateurs;

/**
 * Vue statistiques avec design moderne et navigation cohérente
 */
public class AdminStatsFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public AdminStatsFrame() {
		initFrame();
		initContent();
	}

	private void initFrame() {
		setTitle("IHEC EventHub - Statistiques");
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
		side.add(createNavItem("Salles", () -> new AdminSallesFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Réservations", () -> new AdminReservationsFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Utilisateurs", () -> new AdminUtilisateursFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Statistiques", null, true));
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
		JLabel title = new JLabel("Statistiques globales");
		title.setForeground(UiTheme.TEXT_PRIMARY);
		title.setFont(UiTheme.getTitleFont());

		JLabel subtitle = new JLabel("Vue d'ensemble complète des données de la plateforme.");
		subtitle.setForeground(UiTheme.TEXT_SECONDARY);
		subtitle.setFont(UiTheme.getSmallFont());

		JPanel header = new JPanel();
		header.setOpaque(false);
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.add(title);
		header.add(Box.createVerticalStrut(8));
		header.add(subtitle);

		center.add(header, BorderLayout.NORTH);

		// Grille de statistiques
		JPanel statsGrid = new JPanel(new GridBagLayout());
		statsGrid.setOpaque(false);
		statsGrid.setBorder(new EmptyBorder(30, 0, 0, 0));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(10, 10, 10, 10);

		// Récupérer les stats
		int nbEvenements = GestionEvenements.getInstance().compterEvenements();
		int nbReservations = GestionReservations.getInstance().compterReservations();
		int nbSalles = GestionSalles.getInstance().compterSalles();
		int nbUtilisateurs = GestionUtilisateurs.getInstance().compterUtilisateurs();
		int nbReservationsEnAttente = GestionReservations.getInstance().listerReservationsEnAttente().size();
		int nbReservationsValidees = GestionReservations.getInstance().listerReservationsValidees().size();

		// Première ligne
		gbc.gridx = 0;
		gbc.gridy = 0;
		statsGrid.add(buildStatCard("Événements", String.valueOf(nbEvenements), UiTheme.PRIMARY_BLUE), gbc);

		gbc.gridx = 1;
		statsGrid.add(buildStatCard("Réservations totales", String.valueOf(nbReservations), UiTheme.SUCCESS_GREEN), gbc);

		gbc.gridx = 2;
		statsGrid.add(buildStatCard("Salles", String.valueOf(nbSalles), new Color(147, 51, 234)), gbc);

		// Deuxième ligne
		gbc.gridx = 0;
		gbc.gridy = 1;
		statsGrid.add(buildStatCard("Utilisateurs", String.valueOf(nbUtilisateurs), UiTheme.ERROR_RED), gbc);

		gbc.gridx = 1;
		statsGrid.add(buildStatCard("Réservations en attente", String.valueOf(nbReservationsEnAttente), new Color(251, 146, 60)), gbc);

		gbc.gridx = 2;
		statsGrid.add(buildStatCard("Réservations validées", String.valueOf(nbReservationsValidees), UiTheme.SUCCESS_GREEN), gbc);

		center.add(statsGrid, BorderLayout.CENTER);

		return center;
	}

	private JPanel buildStatCard(String label, String value, Color accentColor) {
		JPanel card = UiTheme.createCard();
		card.setLayout(new BorderLayout());
		card.setPreferredSize(new Dimension(240, 180));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(accentColor, 2),
			new EmptyBorder(25, 25, 25, 25)
		));

		JLabel labelComp = new JLabel(label);
		labelComp.setForeground(UiTheme.TEXT_SECONDARY);
		labelComp.setFont(UiTheme.getSmallFont());

		JLabel valueComp = new JLabel(value);
		valueComp.setForeground(UiTheme.TEXT_PRIMARY);
		valueComp.setFont(new Font("Segoe UI", Font.BOLD, 36));

		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(labelComp);
		content.add(Box.createVerticalStrut(15));
		content.add(valueComp);

		card.add(content, BorderLayout.CENTER);

		return card;
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
			AdminStatsFrame frame = new AdminStatsFrame();
			frame.setVisible(true);
		});
	}
}
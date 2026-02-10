package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import service.GestionEvenements;
import service.GestionReservations;
import service.GestionUtilisateurs;

/**
 * Dashboard principal pour l'administrateur avec design moderne
 */
public class AdminDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	public AdminDashboard() {
		initFrame();
		initContent();
	}

	private void initFrame() {
		setTitle("IHEC EventHub - Administration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		side.add(createNavItem("Tableau de bord", null, true));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Salles", () -> new AdminSallesFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Réservations", () -> new AdminReservationsFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Utilisateurs", () -> new AdminUtilisateursFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(8));
		side.add(createNavItem("Statistiques", () -> new AdminStatsFrame().setVisible(true), false));
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
		JLabel title = new JLabel("Vue d'ensemble");
		title.setForeground(UiTheme.TEXT_PRIMARY);
		title.setFont(UiTheme.getTitleFont());

		JLabel subtitle = new JLabel("Suivez rapidement l'activité sur les réservations et les salles.");
		subtitle.setForeground(UiTheme.TEXT_SECONDARY);
		subtitle.setFont(UiTheme.getSmallFont());

		JPanel header = new JPanel();
		header.setOpaque(false);
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		header.add(title);
		header.add(Box.createVerticalStrut(8));
		header.add(subtitle);

		center.add(header, BorderLayout.NORTH);

		// Cartes de stats
		JPanel cardsContainer = new JPanel();
		cardsContainer.setOpaque(false);
		cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.X_AXIS));
		cardsContainer.setBorder(new EmptyBorder(30, 0, 0, 0));

		int nbEvenementsActifs = GestionEvenements.getInstance().listerEvenementsActifs().size();
		int nbReservationsEnAttente = GestionReservations.getInstance().listerReservationsEnAttente().size();
		int nbReservationsTotal = GestionReservations.getInstance().compterReservations();
		int nbUtilisateurs = GestionUtilisateurs.getInstance().compterUtilisateurs();

		cardsContainer.add(buildStatCard("Événements actifs", String.valueOf(nbEvenementsActifs), UiTheme.PRIMARY_BLUE));
		cardsContainer.add(Box.createHorizontalStrut(20));
		cardsContainer.add(buildStatCard("Réservations en attente", String.valueOf(nbReservationsEnAttente), UiTheme.ERROR_RED));
		cardsContainer.add(Box.createHorizontalStrut(20));
		cardsContainer.add(buildStatCard("Réservations totales", String.valueOf(nbReservationsTotal), UiTheme.SUCCESS_GREEN));
		cardsContainer.add(Box.createHorizontalStrut(20));
		cardsContainer.add(buildStatCard("Utilisateurs", String.valueOf(nbUtilisateurs), new Color(147, 51, 234)));

		center.add(cardsContainer, BorderLayout.CENTER);

		return center;
	}

	private JPanel buildStatCard(String label, String value, Color accentColor) {
		JPanel card = UiTheme.createCard();
		card.setLayout(new BorderLayout());
		card.setPreferredSize(new Dimension(220, 140));
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(accentColor, 2),
			new EmptyBorder(20, 20, 20, 20)
		));

		JLabel labelComp = new JLabel(label);
		labelComp.setForeground(UiTheme.TEXT_SECONDARY);
		labelComp.setFont(UiTheme.getSmallFont());

		JLabel valueComp = new JLabel(value, SwingConstants.LEFT);
		valueComp.setForeground(UiTheme.TEXT_PRIMARY);
		valueComp.setFont(new Font("Segoe UI", Font.BOLD, 32));

		card.add(labelComp, BorderLayout.NORTH);
		card.add(valueComp, BorderLayout.CENTER);

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
			AdminDashboard dash = new AdminDashboard();
			dash.setVisible(true);
		});
	}
}

package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Tableau de bord moderne premium pour les responsables de club
 */
public class ResponsableDashboard extends JFrame {

	private static final long serialVersionUID = 1L;

	// Couleurs modernes
	private static final Color DARK_BG = new Color(15, 23, 42);
	private static final Color SIDEBAR_BG = new Color(20, 30, 48);
	private static final Color CARD_BG = new Color(30, 41, 59);
	private static final Color PRIMARY_BLUE = new Color(59, 130, 246);
	private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
	private static final Color PURPLE = new Color(147, 51, 234);
	private static final Color TEXT_PRIMARY = new Color(248, 250, 252);
	private static final Color TEXT_SECONDARY = new Color(148, 163, 184);
	private static final Color BORDER_COLOR = new Color(51, 65, 85);

	public ResponsableDashboard() {
		setTitle("IHEC EventHub - Responsable de club");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1400, 820);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		if (UiImages.logoMedium() != null) {
			setIconImage(UiImages.logoMedium().getImage());
		}

		// Panel principal avec gradient sophistiqué
		JPanel mainPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				GradientPaint gradient = new GradientPaint(
					0, 0, DARK_BG,
					getWidth(), getHeight(), new Color(30, 41, 59)
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		add(mainPanel, BorderLayout.CENTER);

		// Sidebar premium
		JPanel sidebar = buildModernSidebar();
		mainPanel.add(sidebar, BorderLayout.WEST);

		// Zone centrale
		JPanel centerPanel = buildCenterPanel();
		mainPanel.add(centerPanel, BorderLayout.CENTER);

		setMinimumSize(new Dimension(1200, 700));
	}

	private JPanel buildModernSidebar() {
		JPanel side = new JPanel();
		side.setPreferredSize(new Dimension(300, 0));
		side.setBackground(SIDEBAR_BG);
		side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
		side.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR),
			new EmptyBorder(30, 25, 30, 25)
		));

		// Header avec logo centré
		JPanel logoContainer = new JPanel();
		logoContainer.setOpaque(false);
		logoContainer.setLayout(new BoxLayout(logoContainer, BoxLayout.Y_AXIS));
		
		if (UiImages.logoMedium() != null) {
			JLabel logoLabel = new JLabel(UiImages.logoMedium());
			logoLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			logoContainer.add(logoLabel);
			logoContainer.add(Box.createVerticalStrut(16));
		}

		JLabel title = new JLabel("IHEC EventHub");
		title.setFont(new Font("Segoe UI", Font.BOLD, 22));
		title.setForeground(TEXT_PRIMARY);
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		logoContainer.add(title);

		logoContainer.add(Box.createVerticalStrut(6));

		JLabel subtitle = new JLabel("Responsable Club");
		subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		subtitle.setForeground(TEXT_SECONDARY);
		subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		logoContainer.add(subtitle);

		logoContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
		side.add(logoContainer);
		side.add(Box.createVerticalStrut(30));

		// Séparateur
		JPanel divider = new JPanel();
		divider.setBackground(BORDER_COLOR);
		divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		side.add(divider);
		side.add(Box.createVerticalStrut(20));

		// Section Navigation
		JLabel navTitle = new JLabel("ACTIONS RAPIDES");
		navTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
		navTitle.setForeground(new Color(100, 116, 139));
		navTitle.setBorder(new EmptyBorder(0, 15, 10, 0));
		side.add(navTitle);

		// Navigation items
		side.add(createNavItem("📊 Tableau de bord", null, true));
		side.add(Box.createVerticalStrut(6));
		side.add(createNavItem("➕ Créer un événement", () -> new CreerEvenementReservationFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(6));
		side.add(createNavItem("📋 Mes réservations", () -> new MesReservationsFrame().setVisible(true), false));
		side.add(Box.createVerticalStrut(6));
		side.add(createNavItem("📅 Calendrier", () -> new EvenementsEtudiantFrame().setVisible(true), false));

		side.add(Box.createVerticalStrut(30));

		// Séparateur
		JPanel divider2 = new JPanel();
		divider2.setBackground(BORDER_COLOR);
		divider2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		side.add(divider2);
		side.add(Box.createVerticalStrut(20));

		// Bouton de déconnexion
		side.add(createNavItem("🚪 Se déconnecter", () -> {
			new LoginFrame().setVisible(true);
			dispose();
		}, false));

		side.add(Box.createVerticalGlue());

		// Footer
		JPanel footerPanel = new JPanel();
		footerPanel.setOpaque(false);
		footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
		
		JLabel footer = new JLabel("© 2025 IHEC");
		footer.setForeground(new Color(100, 116, 139));
		footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		footer.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		footerPanel.add(footer);
		
		side.add(footerPanel);

		return side;
	}

	private JPanel buildCenterPanel() {
		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(false);
		center.setBorder(new EmptyBorder(40, 40, 40, 40));

		// Header élégant
		JPanel headerPanel = new JPanel();
		headerPanel.setOpaque(false);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(new EmptyBorder(0, 0, 35, 0));

		JLabel titleLabel = new JLabel("Bienvenue dans votre espace");
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
		titleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		headerPanel.add(titleLabel);

		headerPanel.add(Box.createVerticalStrut(8));

		JLabel subtitleLabel = new JLabel("Gérez vos événements et réservations facilement");
		subtitleLabel.setForeground(TEXT_SECONDARY);
		subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
		subtitleLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		headerPanel.add(subtitleLabel);

		center.add(headerPanel, BorderLayout.NORTH);

		// Grille de cartes d'action
		JPanel cardsGrid = new JPanel(new GridLayout(2, 2, 24, 24));
		cardsGrid.setOpaque(false);

		cardsGrid.add(createPremiumActionCard(
			"Créer un Événement",
			"Créez un événement et réservez une salle en quelques clics",
			"➕",
			PRIMARY_BLUE,
			() -> new CreerEvenementReservationFrame().setVisible(true)
		));

		cardsGrid.add(createPremiumActionCard(
			"Mes Réservations",
			"Consultez l'état de vos demandes de réservation",
			"📋",
			SUCCESS_GREEN,
			() -> new MesReservationsFrame().setVisible(true)
		));

		cardsGrid.add(createPremiumActionCard(
			"Calendrier des Événements",
			"Découvrez tous les événements à venir sur le campus",
			"📅",
			PURPLE,
			() -> new EvenementsEtudiantFrame().setVisible(true)
		));

		cardsGrid.add(createInfoCard(
			"Statut du Club",
			"Votre club est actif",
			"✓",
			new Color(34, 197, 94)
		));

		center.add(cardsGrid, BorderLayout.CENTER);

		return center;
	}

	private JPanel createPremiumActionCard(String title, String description, String icon, Color accentColor, Runnable onClick) {
		JPanel card = new JPanel() {
			private boolean hovered = false;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Fond avec effet hover
				if (hovered) {
					g2d.setColor(new Color(CARD_BG.getRed() + 15, CARD_BG.getGreen() + 15, CARD_BG.getBlue() + 15));
				} else {
					g2d.setColor(CARD_BG);
				}
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
				
				// Bordure avec accent color
				g2d.setColor(accentColor);
				g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
				
				// Effet de glow en haut
				GradientPaint glow = new GradientPaint(
					0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40),
					0, 60, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 0)
				);
				g2d.setPaint(glow);
				g2d.fillRoundRect(1, 1, getWidth() - 2, 60, 16, 16);
			}

			{
				addMouseListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseEntered(java.awt.event.MouseEvent e) {
						hovered = true;
						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						repaint();
					}

					@Override
					public void mouseExited(java.awt.event.MouseEvent e) {
						hovered = false;
						setCursor(Cursor.getDefaultCursor());
						repaint();
					}

					@Override
					public void mouseClicked(java.awt.event.MouseEvent e) {
						onClick.run();
					}
				});
			}
		};

		card.setLayout(new BorderLayout());
		card.setOpaque(false);
		card.setBorder(new EmptyBorder(28, 28, 28, 28));

		// Icône et titre en haut
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);

		JLabel iconLabel = new JLabel(icon);
		iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
		iconLabel.setForeground(accentColor);
		topPanel.add(iconLabel, BorderLayout.WEST);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		titleLabel.setBorder(new EmptyBorder(8, 16, 0, 0));
		topPanel.add(titleLabel, BorderLayout.CENTER);

		card.add(topPanel, BorderLayout.NORTH);

		// Description
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(new EmptyBorder(16, 0, 0, 0));

		JLabel descLabel = new JLabel("<html><div style='width:220px'>" + description + "</div></html>");
		descLabel.setForeground(TEXT_SECONDARY);
		descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		descLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		centerPanel.add(descLabel);

		card.add(centerPanel, BorderLayout.CENTER);

		// Arrow indicator
		JLabel arrow = new JLabel("→");
		arrow.setForeground(accentColor);
		arrow.setFont(new Font("Segoe UI", Font.BOLD, 24));
		card.add(arrow, BorderLayout.SOUTH);

		return card;
	}

	private JPanel createInfoCard(String title, String status, String icon, Color accentColor) {
		JPanel card = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2d.setColor(CARD_BG);
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
				
				g2d.setColor(accentColor);
				g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
				
				GradientPaint glow = new GradientPaint(
					0, 0, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 40),
					0, 60, new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 0)
				);
				g2d.setPaint(glow);
				g2d.fillRoundRect(1, 1, getWidth() - 2, 60, 16, 16);
			}
		};

		card.setLayout(new BorderLayout());
		card.setOpaque(false);
		card.setBorder(new EmptyBorder(28, 28, 28, 28));

		// Icône et titre
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setOpaque(false);

		JLabel iconLabel = new JLabel(icon);
		iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
		iconLabel.setForeground(accentColor);
		topPanel.add(iconLabel, BorderLayout.WEST);

		JLabel titleLabel = new JLabel(title);
		titleLabel.setForeground(TEXT_PRIMARY);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		titleLabel.setBorder(new EmptyBorder(8, 16, 0, 0));
		topPanel.add(titleLabel, BorderLayout.CENTER);

		card.add(topPanel, BorderLayout.NORTH);

		// Status au centre
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setBorder(new EmptyBorder(24, 0, 0, 0));

		JLabel statusLabel = new JLabel(status);
		statusLabel.setForeground(accentColor);
		statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		statusLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		centerPanel.add(statusLabel);

		card.add(centerPanel, BorderLayout.CENTER);

		return card;
	}

	private JPanel createNavItem(String text, Runnable onClick, boolean active) {
		JLabel label = new JLabel(text);
		label.setForeground(active ? TEXT_PRIMARY : TEXT_SECONDARY);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		class NavPanel extends JPanel {
			private static final long serialVersionUID = 1L;
			private boolean hovered = false;

			NavPanel() {
				super(new BorderLayout());
			}

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				if (active) {
					g2d.setColor(new Color(PRIMARY_BLUE.getRed(), PRIMARY_BLUE.getGreen(), 
						PRIMARY_BLUE.getBlue(), 120));
					g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
					
					g2d.setColor(PRIMARY_BLUE);
					g2d.fillRoundRect(0, 8, 4, getHeight() - 16, 2, 2);
				} else if (hovered) {
					g2d.setColor(new Color(71, 85, 105, 60));
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
		item.setPreferredSize(new Dimension(Integer.MAX_VALUE, 44));
		item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
		item.setBorder(new EmptyBorder(10, 16, 10, 16));
		item.add(label, BorderLayout.CENTER);

		if (onClick != null) {
			item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
}
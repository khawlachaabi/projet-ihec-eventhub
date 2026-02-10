package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import models.Evenement;
import service.GestionEvenements;

/**
 * Liste des événements avec design moderne
 */
public class EvenementsEtudiantFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public EvenementsEtudiantFrame() {
		setTitle("Événements IHEC");
		setSize(1000, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		if (UiImages.logoMedium() != null) {
			setIconImage(UiImages.logoMedium().getImage());
		}

		// Panel principal avec gradient
		JPanel mainPanel = UiTheme.createGradientPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);

		// Header moderne
		JPanel header = createHeader();
		mainPanel.add(header, BorderLayout.NORTH);

		// Container central
		JPanel centerContainer = new JPanel(new BorderLayout());
		centerContainer.setOpaque(false);
		centerContainer.setBorder(new EmptyBorder(30, 30, 30, 30));
		mainPanel.add(centerContainer, BorderLayout.CENTER);

		// Card avec tableau
		JPanel tableCard = UiTheme.createCard();
		tableCard.setLayout(new BorderLayout());
		tableCard.setBorder(new EmptyBorder(20, 20, 20, 20));

		String[] columns = { "ID", "Titre", "Type", "Organisateur", "Date début", "Salle" };
		DefaultTableModel model = new DefaultTableModel(columns, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Charger les événements actifs
		GestionEvenements gestionEvents = GestionEvenements.getInstance();
		service.GestionReservations gestionRes = service.GestionReservations.getInstance();
		List<Evenement> evenements = gestionEvents.listerEvenementsActifs();
		List<models.Reservation> reservations = gestionRes.listerToutesReservations();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		for (Evenement e : evenements) {
			String salleNom = "-";
			// Essayer de trouver la salle associée via les réservations
			for (models.Reservation r : reservations) {
				if (r.getEvenement().getId() == e.getId()) {
					salleNom = r.getSalle().getNom();
					break;
				}
			}
			
			model.addRow(new Object[] {
				e.getId(),
				e.getTitre(),
				e.getType().getLibelle(),
				e.getOrganisateur().getPrenom() + " " + e.getOrganisateur().getNom(),
				sdf.format(e.getDateDebut()),
				salleNom
			});
		}

		JTable table = new JTable(model);
		table.setRowHeight(32);
		table.setFillsViewportHeight(true);
		styleTable(table);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		scroll.getViewport().setBackground(UiTheme.CARD_BG);

		tableCard.add(scroll, BorderLayout.CENTER);
		centerContainer.add(tableCard, BorderLayout.CENTER);

		setMinimumSize(new Dimension(900, 600));
	}

	private JPanel createHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(UiTheme.HEADER_BG);
		header.setBorder(new EmptyBorder(20, 30, 20, 30));

		JLabel logo = new JLabel(UiImages.logoMedium());
		JLabel title = new JLabel("Événements à venir");
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
}

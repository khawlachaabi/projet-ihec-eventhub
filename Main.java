package Main.java;
import models.*;
import service.*;
import java.util.Scanner;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * Classe principale - Menu interactif du système de gestion d'événements IHEC
 */
public class Main {
    
    private static Scanner scanner = new Scanner(System.in);
    private static Utilisateur utilisateurConnecte = null;
    
    // Services (Singleton)
    private static GestionUtilisateurs gestionUsers = GestionUtilisateurs.getInstance();
    private static GestionSalles gestionSalles = GestionSalles.getInstance();
    private static GestionEvenements gestionEvents = GestionEvenements.getInstance();
    private static GestionReservations gestionReservations = GestionReservations.getInstance();
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE GESTION D'ÉVÉNEMENTS - IHEC          ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
        
        // Initialiser des données de test
        initialiserDonneesTest();
        
        // Menu principal
        boolean continuer = true;
        while (continuer) {
            if (utilisateurConnecte == null) {
                continuer = menuNonConnecte();
            } else {
                switch (utilisateurConnecte.getRole()) {
                    case "Admin":
                        continuer = menuAdmin();
                        break;
                    case "ResponsableClub":
                        continuer = menuResponsableClub();
                        break;
                    case "Etudiant":
                        continuer = menuEtudiant();
                        break;
                }
            }
        }
        
        System.out.println("\n✓ Merci d'avoir utilisé le système. Au revoir !");
        scanner.close();
    }
    
    // ==================== MENU NON CONNECTÉ ====================
    
    private static boolean menuNonConnecte() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("         MENU PRINCIPAL");
        System.out.println("═══════════════════════════════════════");
        System.out.println("1. Se connecter");
        System.out.println("2. S'inscrire (Étudiant/Responsable Club)");
        System.out.println("3. Quitter");
        System.out.print("\nVotre choix : ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                seConnecter();
                break;
            case 2:
                sInscrire();
                break;
            case 3:
                return false;
            default:
                System.out.println("✗ Choix invalide.");
        }
        return true;
    }
    
    // ==================== MENU ADMIN ====================
    
    private static boolean menuAdmin() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("    MENU ADMINISTRATEUR - " + utilisateurConnecte.getPrenom());
        System.out.println("═══════════════════════════════════════");
        System.out.println("1. Gérer les salles");
        System.out.println("2. Valider/Refuser réservations");
        System.out.println("3. Créer réservation directe");
        System.out.println("4. Consulter calendrier événements");
        System.out.println("5. Gérer les utilisateurs");
        System.out.println("6. Générer rapport");
        System.out.println("7. Se déconnecter");
        System.out.print("\nVotre choix : ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                menuGererSalles();
                break;
            case 2:
                validerRefuserReservations();
                break;
            case 3:
                creerReservationDirecte();
                break;
            case 4:
                consulterCalendrierEvenements();
                break;
            case 5:
                menuGererUtilisateurs();
                break;
            case 6:
                genererRapport();
                break;
            case 7:
                seDeconnecter();
                break;
            default:
                System.out.println("✗ Choix invalide.");
        }
        return true;
    }
    
    // ==================== MENU RESPONSABLE CLUB ====================
    
    private static boolean menuResponsableClub() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("    MENU RESPONSABLE CLUB - " + utilisateurConnecte.getPrenom());
        System.out.println("═══════════════════════════════════════");
        System.out.println("1. Créer un événement");
        System.out.println("2. Demander une réservation");
        System.out.println("3. Consulter mes demandes");
        System.out.println("4. Annuler une demande");
        System.out.println("5. Consulter salles disponibles");
        System.out.println("6. Consulter calendrier événements");
        System.out.println("7. Se déconnecter");
        System.out.print("\nVotre choix : ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                creerEvenement();
                break;
            case 2:
                demanderReservation();
                break;
            case 3:
                consulterMesDemandes();
                break;
            case 4:
                annulerDemande();
                break;
            case 5:
                consulterSallesDisponibles();
                break;
            case 6:
                consulterCalendrierEvenements();
                break;
            case 7:
                seDeconnecter();
                break;
            default:
                System.out.println("✗ Choix invalide.");
        }
        return true;
    }
    
    // ==================== MENU ÉTUDIANT ====================
    
    private static boolean menuEtudiant() {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("    MENU ÉTUDIANT - " + utilisateurConnecte.getPrenom());
        System.out.println("═══════════════════════════════════════");
        System.out.println("1. Consulter salles disponibles");
        System.out.println("2. Consulter calendrier événements");
        System.out.println("3. Se déconnecter");
        System.out.print("\nVotre choix : ");
        
        int choix = lireEntier();
        
        switch (choix) {
            case 1:
                consulterSallesDisponibles();
                break;
            case 2:
                consulterCalendrierEvenements();
                break;
            case 3:
                seDeconnecter();
                break;
            default:
                System.out.println("✗ Choix invalide.");
        }
        return true;
    }
    
    // ==================== FONCTIONNALITÉS ====================
    
    private static void seConnecter() {
        System.out.println("\n--- CONNEXION ---");
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();
        
        utilisateurConnecte = gestionUsers.authentifier(email, mdp);
    }
    
    private static void sInscrire() {
        System.out.println("\n--- INSCRIPTION ---");
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String mdp = scanner.nextLine();
        System.out.println("Type de compte : 1. Étudiant | 2. Responsable Club");
        int type = lireEntier();
        
        if (type == 1) {
            System.out.print("Niveau (ex: L3, M1) : ");
            String niveau = scanner.nextLine();
            System.out.print("Filière : ");
            String filiere = scanner.nextLine();
            Etudiant etudiant = new Etudiant(nom, prenom, email, mdp, niveau, filiere);
            gestionUsers.inscrireUtilisateur(etudiant);
        } else if (type == 2) {
            System.out.print("Nom du club : ");
            String nomClub = scanner.nextLine();
            ResponsableClub resp = new ResponsableClub(nom, prenom, email, mdp, nomClub);
            gestionUsers.inscrireUtilisateur(resp);
        }
    }
    
    private static void seDeconnecter() {
        utilisateurConnecte.seDeconnecter();
        utilisateurConnecte = null;
    }
    
    private static void menuGererSalles() {
        System.out.println("\n--- GESTION DES SALLES ---");
        System.out.println("1. Ajouter une salle");
        System.out.println("2. Afficher toutes les salles");
        System.out.println("3. Supprimer une salle");
        System.out.print("Choix : ");
        int choix = lireEntier();
        
        if (choix == 1) {
            System.out.print("Nom de la salle : ");
            String nom = scanner.nextLine();
            System.out.print("Capacité : ");
            int capacite = lireEntier();
            System.out.println("Type : 1.Amphi 2.TD 3.TP 4.Réunion 5.Club 6.Labo");
            int typeChoix = lireEntier();
            TypeSalle type = TypeSalle.values()[typeChoix - 1];
            System.out.print("Localisation : ");
            String loc = scanner.nextLine();
            System.out.print("Bâtiment : ");
            String bat = scanner.nextLine();
            
            Salle salle = new Salle(nom, capacite, type, loc, bat);
            gestionSalles.ajouterSalle(salle);
        } else if (choix == 2) {
            gestionSalles.afficherToutesSalles();
        } else if (choix == 3) {
            System.out.print("ID de la salle à supprimer : ");
            int id = lireEntier();
            gestionSalles.supprimerSalle(id);
        }
    }
    
    private static void creerEvenement() {
        System.out.println("\n--- CRÉER UN ÉVÉNEMENT ---");
        System.out.print("Titre : ");
        String titre = scanner.nextLine();
        System.out.print("Description : ");
        String desc = scanner.nextLine();
        System.out.println("Type : 1.Conf 2.Formation 3.Club 4.Soutenance 5.Réunion 6.Cérémonie 7.Workshop");
        int typeChoix = lireEntier();
        TypeEvenement type = TypeEvenement.values()[typeChoix - 1];
        
        // Créer une date simple (demain à 10h)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 10);
        Date debut = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date fin = cal.getTime();
        
        Evenement event = new Evenement(titre, desc, debut, fin, type, utilisateurConnecte);
        gestionEvents.creerEvenement(event);
    }
    
    private static void demanderReservation() {
        System.out.println("\n--- DEMANDER UNE RÉSERVATION ---");
        
        // Afficher les événements de l'utilisateur
        List<Evenement> mesEvents = gestionEvents.listerParOrganisateur(utilisateurConnecte.getId());
        if (mesEvents.isEmpty()) {
            System.out.println("Vous devez d'abord créer un événement.");
            return;
        }
        
        System.out.println("Vos événements :");
        for (int i = 0; i < mesEvents.size(); i++) {
            System.out.println((i + 1) + ". " + mesEvents.get(i).getTitre());
        }
        System.out.print("Choisir un événement : ");
        int choixEvent = lireEntier() - 1;
        Evenement event = mesEvents.get(choixEvent);
        
        // Afficher les salles disponibles
        gestionSalles.afficherToutesSalles();
        System.out.print("ID de la salle : ");
        int idSalle = lireEntier();
        Salle salle = gestionSalles.rechercherSalle(idSalle);
        
        if (salle != null) {
            Reservation reservation = new Reservation(event, salle);
            gestionReservations.creerReservation(reservation);
        }
    }
    
    private static void consulterMesDemandes() {
        List<Reservation> mesReservations = gestionReservations.listerParUtilisateur(utilisateurConnecte.getId());
        if (mesReservations.isEmpty()) {
            System.out.println("Aucune demande de réservation.");
        } else {
            System.out.println("\n=== MES DEMANDES ===");
            for (Reservation res : mesReservations) {
                System.out.println(res);
            }
        }
    }
    
    private static void annulerDemande() {
        consulterMesDemandes();
        System.out.print("ID de la réservation à annuler : ");
        int id = lireEntier();
        gestionReservations.annulerReservation(id);
    }
    
    private static void validerRefuserReservations() {
        List<Reservation> enAttente = gestionReservations.listerReservationsEnAttente();
        if (enAttente.isEmpty()) {
            System.out.println("Aucune demande en attente.");
            return;
        }
        
        System.out.println("\n=== RÉSERVATIONS EN ATTENTE ===");
        for (Reservation res : enAttente) {
            System.out.println(res);
        }
        
        System.out.print("ID de la réservation : ");
        int id = lireEntier();
        System.out.println("1. Valider | 2. Refuser");
        int choix = lireEntier();
        
        Admin admin = (Admin) utilisateurConnecte;
        if (choix == 1) {
            gestionReservations.validerReservation(id, admin);
        } else if (choix == 2) {
            System.out.print("Motif du refus : ");
            String motif = scanner.nextLine();
            gestionReservations.refuserReservation(id, admin, motif);
        }
    }
    
    private static void creerReservationDirecte() {
        System.out.println("\n--- RÉSERVATION DIRECTE (ADMIN) ---");
        System.out.println("Fonctionnalité pour réserver au nom d'un enseignant.");
        // À implémenter si nécessaire
    }
    
    private static void consulterSallesDisponibles() {
        System.out.println("\n=== SALLES DISPONIBLES ===");
        List<Salle> salles = gestionSalles.listerSallesDisponibles();
        for (Salle salle : salles) {
            System.out.println(salle);
        }
    }
    
    private static void consulterCalendrierEvenements() {
        gestionEvents.afficherTousEvenements();
    }
    
    private static void menuGererUtilisateurs() {
        System.out.println("\n--- GESTION DES UTILISATEURS ---");
        System.out.println("1. Afficher tous les utilisateurs");
        System.out.println("2. Supprimer un utilisateur");
        System.out.print("Choix : ");
        int choix = lireEntier();
        
        if (choix == 1) {
            gestionUsers.afficherTousUtilisateurs();
        } else if (choix == 2) {
            System.out.print("ID de l'utilisateur : ");
            int id = lireEntier();
            gestionUsers.supprimerUtilisateur(id);
        }
    }
    
    private static void genererRapport() {
        System.out.println("\n=== RAPPORT STATISTIQUE ===");
        System.out.println("Nombre d'utilisateurs : " + gestionUsers.compterUtilisateurs());
        System.out.println("Nombre de salles : " + gestionSalles.compterSalles());
        System.out.println("Nombre d'événements : " + gestionEvents.compterEvenements());
        System.out.println("Nombre de réservations : " + gestionReservations.compterReservations());
        System.out.println("  - En attente : " + gestionReservations.listerReservationsEnAttente().size());
        System.out.println("  - Validées : " + gestionReservations.listerReservationsValidees().size());
        System.out.println("  - Refusées : " + gestionReservations.listerReservationsRefusees().size());
    }
    
    // ==================== DONNÉES DE TEST ====================
    
    private static void initialiserDonneesTest() {
        // Créer un admin
        Admin admin = new Admin("Dupont", "Jean", "admin@ihec.tn", "admin123", "Administration");
        gestionUsers.inscrireUtilisateur(admin);
        
        // Créer un responsable club
        ResponsableClub resp = new ResponsableClub("Ben Ali", "Sara", "sara@ihec.tn", "sara123", "IEEE");
        gestionUsers.inscrireUtilisateur(resp);
        
        // Créer un étudiant
        Etudiant etudiant = new Etudiant("Trabelsi", "Mohamed", "mohamed@ihec.tn", "mohamed123", "L3", "Informatique");
        gestionUsers.inscrireUtilisateur(etudiant);
        
        // Créer des salles
        Salle amphi = new Salle("Amphi A", 300, TypeSalle.AMPHITHEATRE, "Rez-de-chaussée", "Bâtiment A");
        amphi.ajouterEquipement(new Equipement("Projecteur", 1));
        amphi.ajouterEquipement(new Equipement("Microphone", 2));
        gestionSalles.ajouterSalle(amphi);
        
        Salle salleTD = new Salle("TD1", 40, TypeSalle.SALLE_TD, "1er étage", "Bâtiment B");
        gestionSalles.ajouterSalle(salleTD);
        
        System.out.println("\n✓ Données de test initialisées :");
        System.out.println("  Admin : admin@ihec.tn / admin123");
        System.out.println("  Resp Club : sara@ihec.tn / sara123");
        System.out.println("  Étudiant : mohamed@ihec.tn / mohamed123");
    }
    
    // ==================== UTILITAIRES ====================
    
    private static int lireEntier() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Veuillez entrer un nombre : ");
        }
        int nombre = scanner.nextInt();
        scanner.nextLine(); // Consommer le retour à la ligne
        return nombre;
    }
}
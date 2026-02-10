package service;

import models.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de gestion des réservations (Pattern Singleton)
 */
public class GestionReservations {
    
    private List<Reservation> reservations;
    private static GestionReservations instance = null;
    
    /**
     * Constructeur privé (Singleton)
     */
    private GestionReservations() {
        this.reservations = new ArrayList<>();
    }
    
    /**
     * Récupère l'instance unique (Singleton)
     */
    public static GestionReservations getInstance() {
        if (instance == null) {
            instance = new GestionReservations();
        }
        return instance;
    }
    
    /**
     * Retourne une copie de toutes les réservations
     */
    public List<Reservation> listerToutesReservations() {
        return new ArrayList<>(reservations);
    }
    
    /**
     * Crée une nouvelle réservation (demande)
     */
    public boolean creerReservation(Reservation reservation) {
        // Vérifier la disponibilité de la salle
        if (!verifierDisponibilite(reservation.getSalle(), 
                                    reservation.getEvenement().getDateDebut(), 
                                    reservation.getEvenement().getDuree())) {
            System.out.println("Erreur: La salle n'est pas disponible pour cette période.");
            return false;
        }
        
        reservations.add(reservation);
        System.out.println("Demande de réservation créée avec succès (Statut: EN_ATTENTE)");
        return true;
    }
    
    /**
     * Valide une réservation (Admin)
     */
    public boolean validerReservation(int id, Admin admin) {
        Reservation reservation = rechercherReservation(id);
        if (reservation != null) {
            boolean ok = reservation.valider(admin);
            if (ok) {
                // Marquer la salle comme occupée (indisponible)
                Salle salle = reservation.getSalle();
                salle.setDisponible(false);
            }
            return ok;
        }
        System.out.println("Erreur: Réservation #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Refuse une réservation (Admin)
     */
    public boolean refuserReservation(int id, Admin admin, String motif) {
        Reservation reservation = rechercherReservation(id);
        if (reservation != null) {
            return reservation.refuser(admin, motif);
        }
        System.out.println("Erreur: Réservation #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Annule une réservation
     */
    public boolean annulerReservation(int id) {
        Reservation reservation = rechercherReservation(id);
        if (reservation != null) {
            return reservation.annuler();
        }
        System.out.println("Erreur: Réservation #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Vérifie la disponibilité d'une salle
     */
    public boolean verifierDisponibilite(Salle salle, Date dateDebut, int duree) {
        Date dateFin = new Date(dateDebut.getTime() + duree * 60000L);
        
        for (Reservation res : reservations) {
            // Ignorer les réservations refusées ou annulées
            if (res.getStatut() == StatutReservation.REFUSEE || 
                res.getStatut() == StatutReservation.ANNULEE) {
                continue;
            }
            
            // Vérifier si c'est la même salle
            if (res.getSalle().getId() == salle.getId()) {
                Date resDebut = res.getEvenement().getDateDebut();
                Date resFin = res.getEvenement().getDateFin();
                
                // Vérifier le chevauchement
                if (!(dateFin.before(resDebut) || dateDebut.after(resFin))) {
                    return false; // Conflit détecté
                }
            }
        }
        return true; // Pas de conflit
    }
    
    /**
     * Détecte les conflits pour une réservation
     */
    public List<Reservation> detecterConflits(Reservation reservation) {
        List<Reservation> conflits = new ArrayList<>();
        Date dateDebut = reservation.getEvenement().getDateDebut();
        Date dateFin = reservation.getEvenement().getDateFin();
        
        for (Reservation res : reservations) {
            if (res.getId() == reservation.getId()) continue;
            if (res.getStatut() == StatutReservation.REFUSEE || 
                res.getStatut() == StatutReservation.ANNULEE) continue;
            
            if (res.getSalle().getId() == reservation.getSalle().getId()) {
                Date resDebut = res.getEvenement().getDateDebut();
                Date resFin = res.getEvenement().getDateFin();
                
                if (!(dateFin.before(resDebut) || dateDebut.after(resFin))) {
                    conflits.add(res);
                }
            }
        }
        return conflits;
    }
    
    /**
     * Recherche une réservation par ID
     */
    public Reservation rechercherReservation(int id) {
        for (Reservation res : reservations) {
            if (res.getId() == id) {
                return res;
            }
        }
        return null;
    }
    
    /**
     * Liste les réservations en attente
     */
    public List<Reservation> listerReservationsEnAttente() {
        return reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.EN_ATTENTE)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les réservations validées
     */
    public List<Reservation> listerReservationsValidees() {
        return reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.VALIDEE)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les réservations refusées
     */
    public List<Reservation> listerReservationsRefusees() {
        return reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.REFUSEE)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les réservations par utilisateur (organisateur)
     */
    public List<Reservation> listerParUtilisateur(int idUtilisateur) {
        return reservations.stream()
                .filter(r -> r.getEvenement().getOrganisateur().getId() == idUtilisateur)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les réservations par salle
     */
    public List<Reservation> listerParSalle(int idSalle) {
        return reservations.stream()
                .filter(r -> r.getSalle().getId() == idSalle)
                .collect(Collectors.toList());
    }
    
    /**
     * Compte le nombre de réservations
     */
    public int compterReservations() {
        return reservations.size();
    }
    
    /**
     * Affiche toutes les réservations
     */
    public void afficherToutesReservations() {
        if (reservations.isEmpty()) {
            System.out.println("Aucune réservation enregistrée.");
            return;
        }
        System.out.println("\n=== LISTE DES RÉSERVATIONS ===");
        for (Reservation res : reservations) {
            System.out.println(res);
            if (res.getStatut() == StatutReservation.REFUSEE) {
                System.out.println("  Motif refus: " + res.getMotifRefus());
            }
            if (res.getValidePar() != null) {
                System.out.println("  Validée par: " + res.getValidePar().getPrenom() + " " + res.getValidePar().getNom());
            }
            System.out.println("---");
        }
    }
}

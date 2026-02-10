package models;

import java.util.Date;

/**
 * Classe représentant une réservation de salle pour un événement
 */
public class Reservation {
    
    private int id;
    private Evenement evenement;
    private Salle salle;
    private Date dateCreation;
    private StatutReservation statut;
    private String commentaire;
    private String motifRefus;
    private Admin validePar;
    
    private static int compteurId = 1;
    
    /**
     * Constructeur
     */
    public Reservation(Evenement evenement, Salle salle) {
        this.id = compteurId++;
        this.evenement = evenement;
        this.salle = salle;
        this.dateCreation = new Date();
        this.statut = StatutReservation.EN_ATTENTE;
        this.commentaire = "";
        this.motifRefus = "";
        this.validePar = null;
    }
    
    /**
     * Valide la réservation
     */
    public boolean valider(Admin admin) {
        if (this.statut == StatutReservation.EN_ATTENTE) {
            this.statut = StatutReservation.VALIDEE;
            this.validePar = admin;
            System.out.println("Réservation #" + id + " validée par " + admin.getPrenom() + " " + admin.getNom());
            return true;
        }
        System.out.println("Impossible de valider une réservation avec le statut " + statut);
        return false;
    }
    
    /**
     * Refuse la réservation
     */
    public boolean refuser(Admin admin, String motif) {
        if (this.statut == StatutReservation.EN_ATTENTE) {
            this.statut = StatutReservation.REFUSEE;
            this.motifRefus = motif;
            this.validePar = admin;
            System.out.println("Réservation #" + id + " refusée par " + admin.getPrenom() + " " + admin.getNom());
            System.out.println("Motif: " + motif);
            return true;
        }
        System.out.println("Impossible de refuser une réservation avec le statut " + statut);
        return false;
    }
    
    /**
     * Annule la réservation
     */
    public boolean annuler() {
        if (this.statut == StatutReservation.EN_ATTENTE || this.statut == StatutReservation.VALIDEE) {
            this.statut = StatutReservation.ANNULEE;
            System.out.println("Réservation #" + id + " annulée.");
            return true;
        }
        System.out.println("Impossible d'annuler une réservation avec le statut " + statut);
        return false;
    }
    
    /**
     * Modifie le commentaire
     */
    public void modifierCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public Evenement getEvenement() {
        return evenement;
    }
    
    public Salle getSalle() {
        return salle;
    }
    
    public Date getDateCreation() {
        return dateCreation;
    }
    
    public StatutReservation getStatut() {
        return statut;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    public String getMotifRefus() {
        return motifRefus;
    }
    
    public Admin getValidePar() {
        return validePar;
    }
    
    @Override
    public String toString() {
        return "Réservation #" + id + " | " + statut + " | Événement: " + evenement.getTitre() + " | Salle: " + salle.getNom();
    }
}
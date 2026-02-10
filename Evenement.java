package models;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Classe représentant un événement
 */
public class Evenement {
    
    private int id;
    private String titre;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private int duree; // en minutes
    private TypeEvenement type;
    private Utilisateur organisateur;
    private int nombreParticipants;
    private boolean annule;
    
    private static int compteurId = 1;
    
    /**
     * Constructeur
     */
    public Evenement(String titre, String description, Date dateDebut, Date dateFin, TypeEvenement type, Utilisateur organisateur) {
        this.id = compteurId++;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
        this.organisateur = organisateur;
        this.annule = false;
        this.nombreParticipants = 0;
        calculerDuree();
    }
    
    /**
     * Calcule la durée en minutes
     */
    private void calculerDuree() {
        long diffInMillis = dateFin.getTime() - dateDebut.getTime();
        this.duree = (int) TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
    }
    
    /**
     * Modifie les détails de l'événement
     */
    public void modifierDetails(String titre, String description) {
        this.titre = titre;
        this.description = description;
        System.out.println("Détails de l'événement modifiés.");
    }
    
    /**
     * Modifie les dates de l'événement
     */
    public void modifierDates(Date dateDebut, Date dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        calculerDuree();
        System.out.println("Dates de l'événement modifiées.");
    }
    
    /**
     * Annule l'événement
     */
    public void annuler() {
        this.annule = true;
        System.out.println("L'événement " + titre + " a été annulé.");
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public String getTitre() {
        return titre;
    }
    
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
        calculerDuree();
    }
    
    public Date getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
        calculerDuree();
    }
    
    public int getDuree() {
        return duree;
    }
    
    public TypeEvenement getType() {
        return type;
    }
    
    public void setType(TypeEvenement type) {
        this.type = type;
    }
    
    public Utilisateur getOrganisateur() {
        return organisateur;
    }
    
    public int getNombreParticipants() {
        return nombreParticipants;
    }
    
    public void setNombreParticipants(int nombreParticipants) {
        this.nombreParticipants = nombreParticipants;
    }
    
    public boolean isAnnule() {
        return annule;
    }
    
    @Override
    public String toString() {
        return "Événement: " + titre + " (" + type + ") - Organisé par: " + organisateur.getPrenom() + " " + organisateur.getNom() + " - Durée: " + duree + " min";
    }
}
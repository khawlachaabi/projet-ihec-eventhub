package service;

import models.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de gestion des événements (Pattern Singleton)
 */
public class GestionEvenements {
    
    private List<Evenement> evenements;
    private static GestionEvenements instance = null;
    
    /**
     * Constructeur privé (Singleton)
     */
    private GestionEvenements() {
        this.evenements = new ArrayList<>();
    }
    
    /**
     * Récupère l'instance unique (Singleton)
     */
    public static GestionEvenements getInstance() {
        if (instance == null) {
            instance = new GestionEvenements();
        }
        return instance;
    }
    
    /**
     * Crée un nouvel événement
     */
    public boolean creerEvenement(Evenement evenement) {
        // Vérifier que la date de début est dans le futur
        if (evenement.getDateDebut().before(new Date())) {
            System.out.println("Erreur: La date de début doit être dans le futur.");
            return false;
        }
        evenements.add(evenement);
        System.out.println("Événement '" + evenement.getTitre() + "' créé avec succès.");
        return true;
    }
    
    /**
     * Modifie un événement existant
     */
    public boolean modifierEvenement(int id, Evenement evenementModifie) {
        Evenement event = rechercherEvenement(id);
        if (event != null) {
            event.modifierDetails(evenementModifie.getTitre(), evenementModifie.getDescription());
            event.modifierDates(evenementModifie.getDateDebut(), evenementModifie.getDateFin());
            event.setType(evenementModifie.getType());
            System.out.println("Événement #" + id + " modifié avec succès.");
            return true;
        }
        System.out.println("Erreur: Événement #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Supprime un événement
     */
    public boolean supprimerEvenement(int id) {
        Evenement event = rechercherEvenement(id);
        if (event != null) {
            evenements.remove(event);
            System.out.println("Événement #" + id + " supprimé avec succès.");
            return true;
        }
        System.out.println("Erreur: Événement #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Annule un événement
     */
    public boolean annulerEvenement(int id) {
        Evenement event = rechercherEvenement(id);
        if (event != null) {
            event.annuler();
            return true;
        }
        System.out.println("Erreur: Événement #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Recherche un événement par ID
     */
    public Evenement rechercherEvenement(int id) {
        for (Evenement event : evenements) {
            if (event.getId() == id) {
                return event;
            }
        }
        return null;
    }
    
    /**
     * Liste les événements actifs (non annulés)
     */
    public List<Evenement> listerEvenementsActifs() {
        return evenements.stream()
                .filter(e -> !e.isAnnule())
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les événements par organisateur
     */
    public List<Evenement> listerParOrganisateur(int idUtilisateur) {
        return evenements.stream()
                .filter(e -> e.getOrganisateur().getId() == idUtilisateur)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les événements par type
     */
    public List<Evenement> listerParType(TypeEvenement type) {
        return evenements.stream()
                .filter(e -> e.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les événements dans une période donnée
     */
    public List<Evenement> listerParPeriode(Date debut, Date fin) {
        return evenements.stream()
                .filter(e -> !e.getDateDebut().before(debut) && !e.getDateDebut().after(fin))
                .collect(Collectors.toList());
    }
    
    /**
     * Compte le nombre d'événements
     */
    public int compterEvenements() {
        return evenements.size();
    }
    
    /**
     * Affiche tous les événements
     */
    public void afficherTousEvenements() {
        if (evenements.isEmpty()) {
            System.out.println("Aucun événement enregistré.");
            return;
        }
        System.out.println("\n=== LISTE DES ÉVÉNEMENTS ===");
        for (Evenement event : evenements) {
            System.out.println(event);
            if (event.isAnnule()) {
                System.out.println("  [ANNULÉ]");
            }
            System.out.println("---");
        }
    }
}

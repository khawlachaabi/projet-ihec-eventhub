package service;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de gestion des salles (Pattern Singleton)
 */
public class GestionSalles {
    
    private List<Salle> salles;
    private static GestionSalles instance = null;
    
    /**
     * Constructeur privé (Singleton)
     */
    private GestionSalles() {
        this.salles = new ArrayList<>();
    }
    
    /**
     * Récupère l'instance unique (Singleton)
     */
    public static GestionSalles getInstance() {
        if (instance == null) {
            instance = new GestionSalles();
        }
        return instance;
    }
    
    /**
     * Ajoute une nouvelle salle
     */
    public boolean ajouterSalle(Salle salle) {
        // Vérifier si le nom existe déjà
        if (rechercherParNom(salle.getNom()) != null) {
            System.out.println("Erreur: Une salle avec ce nom existe déjà.");
            return false;
        }
        salles.add(salle);
        System.out.println("Salle " + salle.getNom() + " ajoutée avec succès.");
        return true;
    }
    
    /**
     * Modifie une salle existante
     */
    public boolean modifierSalle(int id, Salle salleModifiee) {
        Salle salle = rechercherSalle(id);
        if (salle != null) {
            salle.setNom(salleModifiee.getNom());
            salle.setCapacite(salleModifiee.getCapacite());
            salle.setType(salleModifiee.getType());
            salle.setLocalisation(salleModifiee.getLocalisation());
            salle.setBatiment(salleModifiee.getBatiment());
            System.out.println("Salle #" + id + " modifiée avec succès.");
            return true;
        }
        System.out.println("Erreur: Salle #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Supprime une salle
     */
    public boolean supprimerSalle(int id) {
        Salle salle = rechercherSalle(id);
        if (salle != null) {
            salles.remove(salle);
            System.out.println("Salle #" + id + " supprimée avec succès.");
            return true;
        }
        System.out.println("Erreur: Salle #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Recherche une salle par ID
     */
    public Salle rechercherSalle(int id) {
        for (Salle salle : salles) {
            if (salle.getId() == id) {
                return salle;
            }
        }
        return null;
    }
    
    /**
     * Recherche une salle par nom
     */
    public Salle rechercherParNom(String nom) {
        for (Salle salle : salles) {
            if (salle.getNom().equalsIgnoreCase(nom)) {
                return salle;
            }
        }
        return null;
    }
    
    /**
     * Liste les salles disponibles (simplifiée)
     */
    public List<Salle> listerSallesDisponibles() {
        return salles.stream()
                .filter(Salle::isDisponible)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les salles par type
     */
    public List<Salle> listerParType(TypeSalle type) {
        return salles.stream()
                .filter(s -> s.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste les salles par capacité minimale
     */
    public List<Salle> listerParCapacite(int capaciteMin) {
        return salles.stream()
                .filter(s -> s.getCapacite() >= capaciteMin)
                .collect(Collectors.toList());
    }
    
    /**
     * Liste toutes les salles
     */
    public List<Salle> listerToutesSalles() {
        return new ArrayList<>(salles);
    }
    
    /**
     * Compte le nombre de salles
     */
    public int compterSalles() {
        return salles.size();
    }
    
    /**
     * Affiche toutes les salles
     */
    public void afficherToutesSalles() {
        if (salles.isEmpty()) {
            System.out.println("Aucune salle enregistrée.");
            return;
        }
        System.out.println("\n=== LISTE DES SALLES ===");
        for (Salle salle : salles) {
            System.out.println(salle);
            if (!salle.getEquipements().isEmpty()) {
                System.out.println("  Équipements:");
                for (Equipement eq : salle.getEquipements()) {
                    System.out.println("    - " + eq);
                }
            }
            System.out.println("---");
        }
    }
}
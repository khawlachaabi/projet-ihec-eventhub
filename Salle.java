package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant une salle de l'IHEC
 */
public class Salle {
    
    private int id;
    private String nom;
    private int capacite;
    private TypeSalle type;
    private String localisation;
    private String batiment;
    private int etage;
    private List<Equipement> equipements;
    private boolean disponible;
    
    private static int compteurId = 1;
    
    /**
     * Constructeur
     */
    public Salle(String nom, int capacite, TypeSalle type, String localisation, String batiment) {
        this.id = compteurId++;
        this.nom = nom;
        this.capacite = capacite;
        this.type = type;
        this.localisation = localisation;
        this.batiment = batiment;
        this.equipements = new ArrayList<>();
        this.disponible = true;
        this.etage = 0;
    }
    
    /**
     * Ajoute un équipement à la salle
     */
    public void ajouterEquipement(Equipement equipement) {
        equipements.add(equipement);
        System.out.println("Équipement " + equipement.getNom() + " ajouté à la salle " + nom);
    }
    
    /**
     * Retire un équipement de la salle
     */
    public boolean retirerEquipement(int idEquipement) {
        return equipements.removeIf(eq -> eq.getId() == idEquipement);
    }
    
    /**
     * Modifie la capacité de la salle
     */
    public void modifierCapacite(int nouvelleCapacite) {
        this.capacite = nouvelleCapacite;
        System.out.println("Capacité de " + nom + " modifiée à " + nouvelleCapacite + " places.");
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public int getCapacite() {
        return capacite;
    }
    
    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
    
    public TypeSalle getType() {
        return type;
    }
    
    public void setType(TypeSalle type) {
        this.type = type;
    }
    
    public String getLocalisation() {
        return localisation;
    }
    
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    
    public String getBatiment() {
        return batiment;
    }
    
    public void setBatiment(String batiment) {
        this.batiment = batiment;
    }
    
    public int getEtage() {
        return etage;
    }
    
    public void setEtage(int etage) {
        this.etage = etage;
    }
    
    public List<Equipement> getEquipements() {
        return equipements;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        return "Salle " + nom + " (" + type + ") - Capacité: " + capacite + " - " + batiment + " - " + localisation;
    }
}
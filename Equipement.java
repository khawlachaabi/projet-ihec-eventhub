package models;

/**
 * Classe représentant un équipement d'une salle
 */
public class Equipement {
    
    private int id;
    private String nom;
    private String description;
    private int quantite;
    private boolean fonctionnel;
    
    private static int compteurId = 1;
    
    /**
     * Constructeur
     */
    public Equipement(String nom, int quantite) {
        this.id = compteurId++;
        this.nom = nom;
        this.quantite = quantite;
        this.fonctionnel = true;
        this.description = "";
    }
    
    /**
     * Constructeur avec description
     */
    public Equipement(String nom, String description, int quantite) {
        this(nom, quantite);
        this.description = description;
    }
    
    /**
     * Vérifie l'état de l'équipement
     */
    public boolean verifierEtat() {
        return fonctionnel;
    }
    
    /**
     * Marque l'équipement comme défectueux
     */
    public void marquerDefectueux() {
        this.fonctionnel = false;
        System.out.println("L'équipement " + nom + " a été marqué comme défectueux.");
    }
    
    /**
     * Répare l'équipement
     */
    public void reparer() {
        this.fonctionnel = true;
        System.out.println("L'équipement " + nom + " a été réparé.");
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public boolean isFonctionnel() {
        return fonctionnel;
    }
    
    @Override
    public String toString() {
        return nom + " (x" + quantite + ") - " + (fonctionnel ? "Fonctionnel" : "Défectueux");
    }
}
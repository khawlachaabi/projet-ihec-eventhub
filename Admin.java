package models;

/**
 * Classe représentant un Administrateur du système
 * Hérite de Utilisateur
 */
public class Admin extends Utilisateur {
    
    private String departement;
    
    /**
     * Constructeur
     */
    public Admin(String nom, String prenom, String email, String motDePasse, String departement) {
        super(nom, prenom, email, motDePasse, "Admin");
        this.departement = departement;
    }
    
    /**
     * Redéfinition de la méthode abstraite afficherProfil
     */
    @Override
    public String afficherProfil() {
        return super.toString() + "\nDépartement: " + departement;
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public String getDepartement() {
        return departement;
    }
    
    public void setDepartement(String departement) {
        this.departement = departement;
    }
}
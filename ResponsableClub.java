package models;

/**
 * Classe représentant un Responsable de Club étudiant
 * Hérite de Utilisateur
 */
public class ResponsableClub extends Utilisateur {
    
    private String nomClub;
    private String fonction;
    
    /**
     * Constructeur
     */
    public ResponsableClub(String nom, String prenom, String email, String motDePasse, String nomClub) {
        super(nom, prenom, email, motDePasse, "ResponsableClub");
        this.nomClub = nomClub;
        this.fonction = "Responsable";
    }
    
    /**
     * Redéfinition de la méthode abstraite afficherProfil
     */
    @Override
    public String afficherProfil() {
        return super.toString() + "\nClub: " + nomClub + "\nFonction: " + fonction;
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public String getNomClub() {
        return nomClub;
    }
    
    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }
    
    public String getFonction() {
        return fonction;
    }
    
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }
}
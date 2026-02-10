package models;

/**
 * Classe représentant un Étudiant
 * Hérite de Utilisateur
 */
public class Etudiant extends Utilisateur {
    
    private String niveau;
    private String filiere;
    private String matricule;
    
    /**
     * Constructeur
     */
    public Etudiant(String nom, String prenom, String email, String motDePasse, String niveau, String filiere) {
        super(nom, prenom, email, motDePasse, "Etudiant");
        this.niveau = niveau;
        this.filiere = filiere;
        this.matricule = genererMatricule();
    }
    
    /**
     * Génère un matricule unique
     */
    private String genererMatricule() {
        return "ETU" + String.format("%04d", getId());
    }
    
    /**
     * Redéfinition de la méthode abstraite afficherProfil
     */
    @Override
    public String afficherProfil() {
        return super.toString() + "\nMatricule: " + matricule + "\nNiveau: " + niveau + "\nFilière: " + filiere;
    }
    
    // ========== GETTERS & SETTERS ==========
    
    public String getNiveau() {
        return niveau;
    }
    
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    
    public String getFiliere() {
        return filiere;
    }
    
    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }
    
    public String getMatricule() {
        return matricule;
    }
}

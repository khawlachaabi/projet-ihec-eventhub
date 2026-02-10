package models;

import java.util.Date;

/**
 * Classe abstraite représentant un utilisateur du système
 * Utilise le principe d'héritage pour Admin, ResponsableClub et Etudiant
 */
public abstract class Utilisateur {
    
    // Attributs protégés (accessibles aux classes filles)
    protected int id;
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse;
    protected String telephone;
    protected String role;
    protected Date dateInscription;
    
    // Compteur statique pour générer les IDs
    private static int compteurId = 1;
    
    /**
     * Constructeur de la classe Utilisateur
     */
    public Utilisateur(String nom, String prenom, String email, String motDePasse, String role) {
        this.id = compteurId++;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.dateInscription = new Date();
    }
    
    /**
     * Méthode pour se connecter au système
     */
    public boolean seConnecter(String email, String motDePasse) {
        if (email == null || motDePasse == null || this.email == null) return false;
        return this.email.equalsIgnoreCase(email.trim()) && this.motDePasse.equals(motDePasse);
    }
    
    /**
     * Méthode pour se déconnecter
     */
    public void seDeconnecter() {
        System.out.println(this.prenom + " " + this.nom + " s'est déconnecté.");
    }
    
    /**
     * Méthode pour modifier le profil
     */
    public void modifierProfil(String telephone) {
        this.telephone = telephone;
        System.out.println("Profil modifié avec succès.");
    }
    
    /**
      Méthode abstraite - chaque classe fille la redéfinit
     */
    public abstract String afficherProfil();
    
    // ========== GETTERS ==========
    
    public int getId() {
        return id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public Date getDateInscription() {
        return dateInscription;
    }
    
    // ========== SETTERS ==========
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + " | " + prenom + " " + nom + " | Email: " + email + " | Rôle: " + role;
    }
}
package service;

import models.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe de gestion des utilisateurs (Pattern Singleton)
 */
public class GestionUtilisateurs {
    
    private List<Utilisateur> utilisateurs;
    // Nouveaux comptes en attente de validation par l'admin
    private List<Utilisateur> demandesInscription;
    private static GestionUtilisateurs instance = null;
    
    /**
     * Constructeur privé (Singleton)
     */
    private GestionUtilisateurs() {
        this.utilisateurs = new ArrayList<>();
        this.demandesInscription = new ArrayList<>();
    }
    
    /**
     * Récupère l'instance unique (Singleton)
     */
    public static GestionUtilisateurs getInstance() {
        if (instance == null) {
            instance = new GestionUtilisateurs();
        }
        return instance;
    }
    
    /**
     * Inscrit un nouvel utilisateur
     */
    public boolean inscrireUtilisateur(Utilisateur utilisateur) {
        // Vérifier si l'email existe déjà (actif ou en attente)
        if (rechercherParEmail(utilisateur.getEmail()) != null || rechercherDansDemandesParEmail(utilisateur.getEmail()) != null) {
            System.out.println("Erreur: Un utilisateur avec cet email existe déjà.");
            return false;
        }
        utilisateurs.add(utilisateur);
        System.out.println("Utilisateur " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " inscrit avec succès.");
        return true;
    }

    /**
     * Crée une nouvelle demande d'inscription (compte en attente)
     */
    public boolean creerDemandeInscription(Utilisateur utilisateur) {
        if (rechercherParEmail(utilisateur.getEmail()) != null || rechercherDansDemandesParEmail(utilisateur.getEmail()) != null) {
            System.out.println("Erreur: Une demande ou un compte avec cet email existe déjà.");
            return false;
        }
        demandesInscription.add(utilisateur);
        System.out.println("Demande d'inscription créée pour " + utilisateur.getEmail());
        return true;
    }
    
    /**
     * Authentifie un utilisateur
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        if (email == null || motDePasse == null) return null;
        
        System.out.println("Tentative d'authentification pour : " + email);
        for (Utilisateur user : utilisateurs) {
            if (user.seConnecter(email, motDePasse)) {
                System.out.println("Connexion réussie: " + user.getPrenom() + " " + user.getNom() + " (" + user.getRole() + ")");
                return user;
            }
        }
        System.out.println("Échec de connexion: Email ou mot de passe incorrect pour " + email);
        return null;
    }
    
    /**
     * Modifie un utilisateur
     */
    public boolean modifierUtilisateur(int id, Utilisateur utilisateurModifie) {
        Utilisateur user = rechercherUtilisateur(id);
        if (user != null) {
            user.setTelephone(utilisateurModifie.getTelephone());
            System.out.println("Utilisateur #" + id + " modifié avec succès.");
            return true;
        }
        System.out.println("Erreur: Utilisateur #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Supprime un utilisateur
     */
    public boolean supprimerUtilisateur(int id) {
        Utilisateur user = rechercherUtilisateur(id);
        if (user != null) {
            utilisateurs.remove(user);
            System.out.println("Utilisateur #" + id + " supprimé avec succès.");
            return true;
        }
        System.out.println("Erreur: Utilisateur #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Recherche un utilisateur par ID
     */
    public Utilisateur rechercherUtilisateur(int id) {
        for (Utilisateur user : utilisateurs) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Recherche un utilisateur par email
     */
    public Utilisateur rechercherParEmail(String email) {
        if (email == null) return null;
        for (Utilisateur user : utilisateurs) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Recherche un utilisateur dans les demandes par email
     */
    public Utilisateur rechercherDansDemandesParEmail(String email) {
        if (email == null) return null;
        for (Utilisateur user : demandesInscription) {
            if (user.getEmail() != null && user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Liste les utilisateurs par rôle
     */
    public List<Utilisateur> listerParRole(String role) {
        return utilisateurs.stream()
                .filter(u -> u.getRole().equalsIgnoreCase(role))
                .collect(Collectors.toList());
    }
    
    /**
     * Liste tous les utilisateurs
     */
    public List<Utilisateur> listerTousUtilisateurs() {
        return new ArrayList<>(utilisateurs);
    }

    /**
     * Liste toutes les demandes d'inscription en attente
     */
    public List<Utilisateur> listerDemandesInscription() {
        return new ArrayList<>(demandesInscription);
    }

    /**
     * Accepte une demande (l'admin valide le compte)
     */
    public boolean accepterDemande(int id) {
        Utilisateur demande = null;
        for (Utilisateur u : demandesInscription) {
            if (u.getId() == id) {
                demande = u;
                break;
            }
        }
        if (demande == null) {
            System.out.println("Erreur: Demande d'inscription #" + id + " introuvable.");
            return false;
        }
        demandesInscription.remove(demande);
        return inscrireUtilisateur(demande);
    }

    /**
     * Refuse une demande d'inscription
     */
    public boolean refuserDemande(int id) {
        for (Utilisateur u : demandesInscription) {
            if (u.getId() == id) {
                demandesInscription.remove(u);
                System.out.println("Demande d'inscription #" + id + " refusée / supprimée.");
                return true;
            }
        }
        System.out.println("Erreur: Demande d'inscription #" + id + " introuvable.");
        return false;
    }
    
    /**
     * Compte le nombre d'utilisateurs
     */
    public int compterUtilisateurs() {
        return utilisateurs.size();
    }
    
    /**
     * Affiche tous les utilisateurs
     */
    public void afficherTousUtilisateurs() {
        if (utilisateurs.isEmpty()) {
            System.out.println("Aucun utilisateur enregistré.");
            return;
        }
        System.out.println("\n=== LISTE DES UTILISATEURS ===");
        for (Utilisateur user : utilisateurs) {
            System.out.println(user.afficherProfil());
            System.out.println("---");
        }
    }
}
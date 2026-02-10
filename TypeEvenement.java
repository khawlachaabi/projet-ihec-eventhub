package models;

/**
 * Énumération des types d'événements
 */
public enum TypeEvenement {
    CONFERENCE("Conférence"),
    FORMATION("Formation"),
    ACTIVITE_CLUB("Activité Club"),
    SOUTENANCE("Soutenance"),
    REUNION("Réunion"),
    CEREMONIE("Cérémonie"),
    WORKSHOP("Workshop");
    
    private String libelle;
    
    /**
     * Constructeur de l'enum
     */
    TypeEvenement(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Retourne le libellé du type d'événement
     */
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
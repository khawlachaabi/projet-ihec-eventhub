package models;

/**
 * Énumération des types de salles disponibles
 */
public enum TypeSalle {
    AMPHITHEATRE("Amphithéâtre"),
    SALLE_TD("Salle de TD"),
    SALLE_TP("Salle de TP"),
    SALLE_REUNION("Salle de réunion"),
    ESPACE_CLUB("Espace Club"),
    LABORATOIRE("Laboratoire");
    
    private String libelle;
    
    /**
     * Constructeur de l'enum
     */
    TypeSalle(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Retourne le libellé du type de salle
     */
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
package models;

/**
 * Énumération des statuts d'une réservation
 */
public enum StatutReservation {
    EN_ATTENTE("En attente de validation"),
    VALIDEE("Validée"),
    REFUSEE("Refusée"),
    ANNULEE("Annulée");
    
    private String libelle;
    
    /**
     * Constructeur de l'enum
     */
    StatutReservation(String libelle) {
        this.libelle = libelle;
    }
    
    /**
     * Retourne le libellé du statut
     */
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
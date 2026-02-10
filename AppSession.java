package ui;

import models.Utilisateur;

/**
 * Classe utilitaire singleton pour gérer la session utilisateur
 * de l'application IHEC EventHub.
 * 
 * Cette classe permet de :
 * - Garder en mémoire l'utilisateur actuellement connecté
 * - Vérifier l'état de connexion
 * - Vérifier les rôles et permissions
 * - Gérer la déconnexion
 */
public final class AppSession {

	private static Utilisateur currentUser;
	private static long sessionStartTime;

	/**
	 * Constructeur privé pour empêcher l'instanciation
	 */
	private AppSession() {
		throw new AssertionError("Cette classe ne peut pas être instanciée");
	}

	/**
	 * Récupère l'utilisateur actuellement connecté
	 * 
	 * @return l'utilisateur connecté ou null si aucun utilisateur n'est connecté
	 */
	public static Utilisateur getCurrentUser() {
		return currentUser;
	}

	/**
	 * Définit l'utilisateur actuellement connecté et démarre la session
	 * 
	 * @param user l'utilisateur à connecter
	 */
	public static void setCurrentUser(Utilisateur user) {
		currentUser = user;
		if (user != null) {
			sessionStartTime = System.currentTimeMillis();
		}
	}

	/**
	 * Vérifie si un utilisateur est actuellement connecté
	 * 
	 * @return true si un utilisateur est connecté, false sinon
	 */
	public static boolean isLoggedIn() {
		return currentUser != null;
	}

	/**
	 * Vérifie si l'utilisateur connecté est un administrateur
	 * 
	 * @return true si l'utilisateur est admin, false sinon
	 */
	public static boolean isAdmin() {
		return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
	}

	/**
	 * Vérifie si l'utilisateur connecté est un étudiant
	 * 
	 * @return true si l'utilisateur est étudiant, false sinon
	 */
	public static boolean isStudent() {
		return currentUser != null && "etudiant".equalsIgnoreCase(currentUser.getRole());
	}

	/**
	 * Vérifie si l'utilisateur connecté est un responsable de club
	 * 
	 * @return true si l'utilisateur est responsable, false sinon
	 */
	public static boolean isResponsable() {
		return currentUser != null && "ResponsableClub".equalsIgnoreCase(currentUser.getRole());
	}

	/**
	 * Récupère l'ID de l'utilisateur connecté
	 * 
	 * @return l'ID de l'utilisateur ou -1 si non connecté
	 */
	public static int getCurrentUserId() {
		return currentUser != null ? currentUser.getId() : -1;
	}

	/**
	 * Récupère le nom complet de l'utilisateur connecté
	 * 
	 * @return le nom complet ou "Invité" si non connecté
	 */
	public static String getCurrentUserName() {
		if (currentUser == null) {
			return "Invité";
		}
		return currentUser.getNom() + " " + currentUser.getPrenom();
	}

	/**
	 * Récupère le rôle de l'utilisateur connecté
	 * 
	 * @return le rôle ou "guest" si non connecté
	 */
	public static String getCurrentUserRole() {
		return currentUser != null ? currentUser.getRole() : "guest";
	}

	/**
	 * Déconnecte l'utilisateur actuel et nettoie la session
	 */
	public static void logout() {
		currentUser = null;
		sessionStartTime = 0;
	}

	/**
	 * Récupère la durée de la session en millisecondes
	 * 
	 * @return la durée de la session ou 0 si non connecté
	 */
	public static long getSessionDuration() {
		if (currentUser == null || sessionStartTime == 0) {
			return 0;
		}
		return System.currentTimeMillis() - sessionStartTime;
	}

	/**
	 * Récupère la durée de la session formatée (HH:MM:SS)
	 * 
	 * @return la durée formatée ou "00:00:00" si non connecté
	 */
	public static String getFormattedSessionDuration() {
		long duration = getSessionDuration();
		if (duration == 0) {
			return "00:00:00";
		}
		
		long seconds = (duration / 1000) % 60;
		long minutes = (duration / (1000 * 60)) % 60;
		long hours = (duration / (1000 * 60 * 60)) % 24;
		
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	/**
	 * Vérifie si l'utilisateur a un rôle spécifique
	 * 
	 * @param role le rôle à vérifier
	 * @return true si l'utilisateur a ce rôle, false sinon
	 */
	public static boolean hasRole(String role) {
		return currentUser != null && role != null 
			&& role.equalsIgnoreCase(currentUser.getRole());
	}

	/**
	 * Affiche les informations de session (pour le débogage)
	 * 
	 * @return une chaîne avec les infos de session
	 */
	public static String getSessionInfo() {
		if (currentUser == null) {
			return "Aucune session active";
		}
		return String.format(
			"Session active - Utilisateur: %s (%s) - Durée: %s",
			getCurrentUserName(),
			getCurrentUserRole(),
			getFormattedSessionDuration()
		);
	}
}

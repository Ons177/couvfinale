package Controllers;

import entities.Utilisateur;

public class UserSession {
    private static Utilisateur currentUser;
    private static boolean isAdmin;

    // Méthode pour définir l'utilisateur connecté et mettre à jour isAdmin
    public static void setCurrentUser(Utilisateur user) {
        currentUser = user;
        updateIsAdmin();
    }

    // Méthode pour obtenir l'utilisateur connecté
    public static Utilisateur getCurrentUser() {
        return currentUser;
    }
    public static boolean isAdmin() {
        return isAdmin;
    }

    public static void setIsAdmin(boolean isAdmin) {
        UserSession.isAdmin = isAdmin;
    }

    // Méthode pour mettre à jour l'état isAdmin en fonction du rôle de l'utilisateur
    private static void updateIsAdmin() {
        if (currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            isAdmin = true;
        } else {
            isAdmin = false;
        }
    }
}

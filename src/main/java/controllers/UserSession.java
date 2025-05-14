package controllers;

import entities.Utilisateur;

public class UserSession {
    public static Utilisateur currentUser;
    public static Utilisateur getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Utilisateur user) {
        currentUser = user;
    }
}

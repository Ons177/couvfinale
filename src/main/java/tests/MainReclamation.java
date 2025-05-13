package tests;
import entities.*;
import services.*;

import java.sql.SQLException;
public class MainReclamation {
    public static void main(String[] args) {
        ServiceReclamation serviceReclamation = new ServiceReclamation();
        try {
            serviceReclamation.ajouter(new Reclamation(1, "trajet", "Problème de retard", "2025-04-21", "en attente", "moyenne"));
            serviceReclamation.modifier(new Reclamation(1, 1, "trajet", "Problème résolu", "2025-04-21", "résolue", "moyenne"));
            serviceReclamation.supprimer(new Reclamation(1, 1, "trajet", "Problème résolu", "2025-04-21", "résolue", "moyenne"));
            System.out.println(serviceReclamation.recuperer());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        ServiceReponse serviceReponse = new ServiceReponse();
        try {
            serviceReponse.ajouter(new Reponse(1, 1, "Votre réclamation a été prise en compte.", "2025-04-21"));
            serviceReponse.modifier(new Reponse(1, 1, 1, "Votre réclamation est résolue.", "2025-04-21"));
            serviceReponse.supprimer(new Reponse(1, 1, 1, "Votre réclamation est résolue.", "2025-04-21"));
            System.out.println(serviceReponse.recuperer());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

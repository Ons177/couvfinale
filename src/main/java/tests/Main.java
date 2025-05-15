package tests;

import entities.*;
import services.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;



public class Main {
    public static void main(String[] args) {
        ServiceEvenement se = new ServiceEvenement();
        ServiceReservationEvenement sr = new ServiceReservationEvenement();
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

        try {
            // Création et ajout d'un événement
            Evenement ev = new Evenement(
                    "Tech Meetup", "Conférence sur la technologie", "Sousse",
                    LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 11),
                    LocalDateTime.of(2025, 6, 10, 14, 0),
                    1, "Conférence");
            se.ajouter(ev);

            System.out.println("Événements :");
            se.getAll().forEach(System.out::println);

            // Création d'une réservation avec le prix (ex: 75.0)
            ReservationEvenement res = new ReservationEvenement(1, 2, 3, LocalDate.now(), "Confirmée", 75.0);
            sr.reserver(res);

            System.out.println("Réservations :");
            sr.recuperer().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserService service = new UserService();
        VehicleService serviceVehicule = new VehicleService();
        // 1. Ajouter un utilisateur
        Utilisateur conducteur1 = new Utilisateur("Doe", "John", "john.doe@example.com", "password123", "123456789", "conducteur", Date.valueOf("1990-05-15"));
        Utilisateur conducteur2 = new Utilisateur("Ons", "Smati", "onssmati@gmail.com", "ons12345", "25896325", "conducteur", Date.valueOf("2003-09-17"));
        Utilisateur passager = new Utilisateur("Ali", "Ben Ali", "ali@example.com", "ali123", "987654321", "passager", Date.valueOf("1995-03-25"));

        service.ajouter(conducteur1);
        service.ajouter(conducteur2);
        service.ajouter(passager);
        System.out.println("Utilisateur ajouté.\n");

        // 2. Récupérer tous les utilisateurs
        List<Utilisateur> utilisateurs = service.recuperer();
        System.out.println("Liste des utilisateurs:");
        for (Utilisateur u : utilisateurs) {
            System.out.println(u);
        }
        System.out.println();

        // 3. Modifier le premier utilisateur trouvé
        if (!utilisateurs.isEmpty()) {
            Utilisateur utilisateurAModifier = utilisateurs.get(0);
            utilisateurAModifier.setNom("Smith");
            utilisateurAModifier.setPrenom("Jane");
            utilisateurAModifier.setEmail("jane.smith@example.com");
            utilisateurAModifier.setMdp("newpassword456");
            utilisateurAModifier.setTelephone("987654321");
            utilisateurAModifier.setRole("conducteur");
            utilisateurAModifier.setDate_naissance(Date.valueOf("1995-10-20"));

            service.modifier(utilisateurAModifier);
            System.out.println("Utilisateur modifié.\n");
        }

        // 4. Afficher encore tous les utilisateurs après modification
        utilisateurs = service.recuperer();
        System.out.println("Liste des utilisateurs après modification:");
        for (Utilisateur u : utilisateurs) {
            System.out.println(u);
        }
        System.out.println();

            /*/// 5. Supprimer le premier utilisateur trouvé
            if (!utilisateurs.isEmpty()) {
                Utilisateur utilisateurASupprimer = utilisateurs.get(0);
                service.supprimer(utilisateurASupprimer);
                System.out.println("Utilisateur supprimé.\n");
            }*/

        // 6. Afficher les utilisateurs restants après suppression
        utilisateurs = service.recuperer();
        System.out.println("Liste des utilisateurs après suppression:");
        for (Utilisateur u : utilisateurs) {
            System.out.println(u);
        }
        VehicleService service2 = new VehicleService();
        for (Utilisateur u : utilisateurs) {
            if (u.getRole().equalsIgnoreCase("conducteur")) {
                // Ajouter 2 véhicules par conducteur (exemple)
                Vehicule v1 = new Vehicule(u, "Toyota", "Corolla", "Rouge", "1234TU" + u.getId_utilisateur(), 5);
                Vehicule v2 = new Vehicule(u, "Mercedes", "Classe A", "Noir", "5678TU" + u.getId_utilisateur(), 4);

                serviceVehicule.addVehicle(v1);
                serviceVehicule.addVehicle(v2);

                System.out.println("Deux véhicules ajoutés pour conducteur : " + u.getNom() + " " + u.getPrenom());}

            else {
                System.out.println("Aucun conducteur trouvé pour associer un véhicule.");   }
        }

        // 6. Récupérer et afficher tous les véhicules
        List<Vehicule> v2 = serviceVehicule.recuperer();
        System.out.println("Liste des vehicules:");
        for (Vehicule ve : v2) {
            System.out.println(ve);
        }


        // 5. Modifier un véhicule (ici on modifie juste le 1er véhicule pour tester)
        List<Vehicule> v3 = serviceVehicule.recuperer();
        if (!v2.isEmpty()) {
            Vehicule vToUpdate = v2.get(0);
            vToUpdate.setCouleur("Bleu"); // changer la couleur
            serviceVehicule.modifier(vToUpdate);
            System.out.println("Véhicule modifié !");
        }

        // 6. Supprimer un véhicule (toujours pour tester)
                /*if (!v2.isEmpty()) {
                    Vehicule vToDelete = v2.get(0);
                    serviceVehicule.supprimer(vToDelete);
                    System.out.println("Véhicule supprimé !");
                }*/


    }

    }





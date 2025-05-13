package tests;

import entities.Evenement;
import entities.ReservationEvenement;
import services.ServiceEvenement;
import services.ServiceReservationEvenement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;




public class Main {
    public static void main(String[] args) {
        ServiceEvenement se = new ServiceEvenement();
        ServiceReservationEvenement sr = new ServiceReservationEvenement();

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
    }
}





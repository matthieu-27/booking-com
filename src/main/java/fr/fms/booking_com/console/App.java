package fr.fms.booking_com.console;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.fms.booking_com.dao.BookingRepository;
import fr.fms.booking_com.dao.RoomRepository;
import fr.fms.booking_com.entities.Room;

@Component
public class App {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Scanner scanner = new Scanner(System.in);

    public void start() {
        boolean running = true;
        while (running) {
            printmenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createRoom();
                    break;
                case "2":
                    modifyRoom();
                    break;
                case "3":
                    deleteRoom();
                    break;
                case "4":
                    createBooking();
                    break;
                case "5":
                    deleteBooking();
                    break;
                case "6":
                    verifyConflicts();
                    break;
                case "7":
                    displayBookings();
                    break;
                case "8":
                    displayRooms();
                    break;
                case "9":
                    running = false;
                    break;
                default:
                    break;
            }
        }
    }

    private void displayRooms() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayRooms'");
    }

    private void displayBookings() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayBookings'");
    }

    private void verifyConflicts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyConflicts'");
    }

    private void deleteBooking() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBooking'");
    }

    private void createBooking() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createBooking'");
    }

    private void deleteRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRoom'");
    }

    private void modifyRoom() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyRoom'");
    }

    private void createRoom() {
        System.out.println("Nom de la salle: ");
        String name = scanner.nextLine();
        System.out.println("Capacité de la salle:");
        int capacity = scanner.nextInt();
        Room room = new Room(name, capacity);
        roomRepository.save(room);
        System.out.println("Salle ajoutée avec succès");
    }

    private void printmenu() {
        System.out.println("Bienvenue dans booking.com booking.Yeah !");
        System.out.println("1: Créer une salle");
        System.out.println("2: Modifier une salle");
        System.out.println("3: Supprimer un salle");
        System.out.println("4: Créer une réservations");
        System.out.println("5: Supprimer une réservations");
        System.out.println("6: Vérifier les conflits");
        System.out.println("7: Afficher les réservations");
        System.out.println("8: Afficher les salles disponibles");
        System.out.println("9: Quitter le programme");
    }

}

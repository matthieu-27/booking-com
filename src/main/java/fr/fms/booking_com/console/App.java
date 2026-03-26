package fr.fms.booking_com.console;

import java.util.Scanner;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

    private final Validator validator;

    public App() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void start() {
        boolean running = true;
        while (running) {
            printmenu();
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    try {
                        createRoom();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
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

    private void createRoom() throws Exception {
        String name = App.readInput("Nom de la salle:");
        System.out.println("Capacité de la salle:");
        int capacity = Integer.parseInt(App.readInput("Capacité de la salle:"));
        Room room;

        try {
            room = new Room(name, capacity);
            this.validateRoom(room);
            System.out.println("Données valides ! Salle crée, " + name);
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            printmenu();
        } finally {
            room = new Room(name, capacity);
            roomRepository.save(room);
            System.out.println("Salle ajoutée avec succès");
        }

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

    public void validateRoom(Room userInput) {
        Set<ConstraintViolation<Room>> violations = validator.validate(userInput);
        if (!violations.isEmpty()) {
            violations.forEach(violation -> System.err.println(violation.getMessage()));
            throw new IllegalArgumentException("Les données saisies sont invalides.");
        }
    }

    public static String readInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}

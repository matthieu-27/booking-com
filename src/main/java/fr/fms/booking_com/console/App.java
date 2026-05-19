package fr.fms.booking_com.console;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.fms.booking_com.dao.BookingRepository;
import fr.fms.booking_com.dao.RoomRepository;
import fr.fms.booking_com.entities.Booking;
import fr.fms.booking_com.entities.Room;
import fr.fms.booking_com.utils.UserBookingInputValidator;
import fr.fms.booking_com.utils.UserRoomInputValidator;

@Component
public class App {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private UserRoomInputValidator roomValidator;

    @Autowired
    private UserBookingInputValidator bookingValidator;

    public void start() throws Exception {
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

    public List<Room> displayRooms() {
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            System.out.println("Pas de salles créées.");
        } else {
            rooms.forEach(System.out::println);
        }
        return rooms;
    }

    public List<Booking> displayBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) {
            System.out.println("Pas de réunions créées.");
        } else {
            bookings.forEach(System.out::println);
        }
        return bookings;
    }

    private void verifyConflicts() {
        List<Booking> conflicts = bookingRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        b -> b.getRoom().getId() + "-" + b.getDesiredAt() + "-" + b.getScheduledAt()))
                .values().stream()
                .filter(group -> group.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        if (conflicts.isEmpty()) {
            System.out.println("Aucun conflit détecté.");
        } else {
            System.out.println("Conflits détectés :");
            conflicts.forEach(System.out::println);
        }
    }

    private void deleteBooking() {
        Long id = Long.valueOf(UserRoomInputValidator.readInput("ID de la réservation: "));
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        if (bookingOpt.isPresent()) {
            System.out.println(bookingOpt.get());
            bookingRepository.deleteById(id);
            System.out.println("Réservation supprimée avec succès");
        } else {
            System.out.println("Réservation introuvable");
        }
    }

    private void createBooking() {
        Long id = Long.valueOf(UserRoomInputValidator.readInput("ID de la Salle: "));
        Room room = null;
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            room = roomOpt.get();
        } else {
            System.out.println("Salle introuvable");
            printmenu();
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Booking booking;
        try {
            LocalDate desiredAt = LocalDate.parse(
                    UserBookingInputValidator.readInput("Jour désire (ex: 03/03/2026) : "),
                    dateFormatter);
            LocalTime scheduledAt = LocalTime.parse(UserRoomInputValidator.readInput("Heure désiré (ex: 02:00:00) : "),
                    timeFormatter);
            booking = new Booking(desiredAt, scheduledAt, room);
            bookingRepository.save(booking);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void deleteRoom() {
        Long id = Long.valueOf(UserRoomInputValidator.readInput("ID de la Salle: "));

        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            System.out.println(room);
            roomRepository.delete(room);
            System.out.println("Salle supprimé avec succès");
        } else {
            System.out.println("Salle introuvable");
        }
    }

    private void modifyRoom() {

        Long id = Long.valueOf(UserRoomInputValidator.readInput("ID de la Salle: "));

        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();

            System.out.println(room.toString());

            String name = UserRoomInputValidator.readInput("Nouveau nom (" + room.getName() + "): ");
            int capacity = Integer
                    .parseInt(UserRoomInputValidator.readInput("Nouvelle capacité (" + room.getCapacity() + "): "));

            room.setName(name.isEmpty() ? room.getName() : name);
            room.setCapacity(capacity > 0 ? room.getCapacity() : capacity);
            try {
                roomValidator.validate(room);
            } catch (Exception e) {
                System.err.println("Erreur : " + e.getMessage());
                printmenu();
            } finally {
                roomRepository.save(room);
                System.out.println("Données valides ! Salle modifié, " + room);
            }

        } else {
            System.out.println("Salle introuvable");
        }
    }

    private void createRoom() throws Exception {
        String name = UserRoomInputValidator.readInput("Nom de la salle:");
        int capacity = Integer.parseInt(UserRoomInputValidator.readInput("Capacité de la salle:"));
        Room room;

        try {
            room = new Room(name, capacity);
            roomValidator.validate(room);
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            printmenu();
        } finally {
            room = new Room(name, capacity);
            roomRepository.save(room);
            System.out.println("Données valides ! Salle crée, " + room);
        }

    }

    private void printmenu() {
        System.out.println("Bienvenue dans booking.com booking.Yeah !");
        System.out.println("1: Créer une salle");
        System.out.println("2: Modifier une salle");
        System.out.println("3: Supprimer un salle");
        System.out.println("4: Créer une réservation");
        System.out.println("5: Supprimer une réservation");
        System.out.println("6: Vérifier les conflits");
        System.out.println("7: Afficher les réservations");
        System.out.println("8: Afficher les salles disponibles");
        System.out.println("9: Quitter le programme");
    }

}

package fr.fms.booking_com.console;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                    validateBooking();
                    break;
                case "6":
                    deleteBooking();
                    break;
                case "7":
                    verifyConflicts();
                    break;
                case "8":
                    displayBookings();
                    break;
                case "9":
                    displayRooms();
                    break;
                case "10":
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

    public void verifyConflicts() { // check for conflicts that already are in database.
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

    public void validateBooking(){
        Long id = Long.valueOf(UserRoomInputValidator.readInput("ID de la réservation: "));
        Optional<Booking> bookingOpt = bookingRepository.findById(id);

        if (bookingOpt.isPresent()){
            Booking booking = bookingOpt.get();
            try {
                String endedAt = UserBookingInputValidator.readInput("Réservation terminée ? (o/n)");
                if(endedAt.equalsIgnoreCase("o")){
                    booking.setEndedAt(LocalDateTime.now());
                    bookingRepository.save(booking);
                } else {
                    start();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("Réservation introuvable");
        }
    }

    public void deleteBooking() {
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

    public void createBooking() {
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
        // check for conflict between T and T+1h BEFORE saving in database
        try {
            LocalDate desiredAt = LocalDate.parse(
                    UserBookingInputValidator.readInput("Jour désire (ex: 03/03/2026) : "),
                    dateFormatter);
            LocalTime scheduledAt = LocalTime.parse(UserRoomInputValidator.readInput("Heure désiré (ex: 02:00:00) : "),
                    timeFormatter);

            if (hasConflict(room.getId(), desiredAt, scheduledAt)) {
                System.out.println("Créneau indisponible : une réservation active occupe déjà ce créneau.");
                return;
            }

            booking = new Booking(desiredAt, scheduledAt, room);
            bookingRepository.save(booking);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public boolean hasConflict(Long roomId, LocalDate desiredAt, LocalTime scheduledAt) { // helper for the T + T1h logic
        LocalTime newEnd = scheduledAt.plusHours(1);
        return bookingRepository
                .findByRoomIdAndDesiredAtAndEndedAtIsNull(roomId, desiredAt)
                .stream()
                .anyMatch(b -> scheduledAt.isBefore(b.getScheduledAt().plusHours(1)) // here is the hour
                        && b.getScheduledAt().isBefore(newEnd));
    }

    public void deleteRoom() {
        Long id = Long.valueOf(UserRoomInputValidator.readInput("ID de la Salle: "));
        if (deleteRoomById(id)) {
            System.out.println("Salle supprimée avec succès");
        } else {
            System.out.println("Salle introuvable");
        }
    }

    public boolean deleteRoomById(Long id) {
        Optional<Room> roomOpt = roomRepository.findById(id);
        if (roomOpt.isPresent()) {
            roomRepository.delete(roomOpt.get());
            return true;
        }
        return false;
    }

    public void modifyRoom() {
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

    public void createRoom() throws Exception {
        String name = UserRoomInputValidator.readInput("Nom de la salle:");
        int capacity = Integer.parseInt(UserRoomInputValidator.readInput("Capacité de la salle:"));
        Room room;

        if (isRoomNameTaken(name)) {
            System.out.println("Erreur : une salle avec le nom \"" + name + "\" existe déjà.");
            return;
        }

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

    public void printmenu() {
        System.out.println("Bienvenue dans booking.com booking.Yeah !");
        System.out.println("1: Créer une salle");
        System.out.println("2: Modifier une salle");
        System.out.println("3: Supprimer un salle");
        System.out.println("4: Créer une réservation");
        System.out.println("5: Valider une réservation");
        System.out.println("6: Supprimer une réservation");
        System.out.println("7: Vérifier les conflits");
        System.out.println("8: Afficher les réservations");
        System.out.println("9: Afficher les salles disponibles");
        System.out.println("10: Quitter le programme");
    }

    public boolean isRoomNameTaken(String name) {
        return roomRepository.findByName(name).isPresent();
    }
}

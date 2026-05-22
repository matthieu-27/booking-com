package fr.fms.booking_com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fms.booking_com.console.App;
import fr.fms.booking_com.dao.BookingRepository;
import fr.fms.booking_com.dao.RoomRepository;
import fr.fms.booking_com.entities.Booking;
import fr.fms.booking_com.entities.Room;

@ExtendWith(MockitoExtension.class)
class AppTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private App app;

    @Test
    void displayRooms_shouldReturnEmptyList_whenNoRoomsExist() {
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        List<Room> result = app.displayRooms();

        assertTrue(result.isEmpty());
    }

    @Test
    void displayRooms_shouldReturnRooms_whenRoomsExist() {
        List<Room> fakeRooms = Arrays.asList(new Room("Salle A", 10), new Room("Salle B", 20));
        when(roomRepository.findAll()).thenReturn(fakeRooms);

        List<Room> result = app.displayRooms();

        assertEquals(2, result.size());
        assertEquals("Salle A", result.get(0).getName());
    }

    @Test
    void displayBookings_shouldReturnBookings_whenBookingExist() {
        Room room = new Room("Salle de test");
        // stocking time in a variable so nanoseconds can be ignored
        LocalDate today = LocalDate.now();
        List<Booking> bookings = Arrays.asList(new Booking(today, LocalTime.of(9, 0), room));
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = app.displayBookings();

        assertEquals(1, result.size());
        assertEquals(today, result.get(0).getDesiredAt());
    }

    @Test
    void displayBookings_shouldReturnEmptyList_whenNoBookingsExist() {
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        List<Booking> result = app.displayBookings();

        assertTrue(result.isEmpty());
    }

    @Test
    void hasConflict_shouldReturnFalse_whenNoActiveBookingsExist() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        LocalTime time = LocalTime.of(10, 0);

        when(bookingRepository.findByRoomIdAndDesiredAtAndEndedAtIsNull(1L, date)).thenReturn(Collections.emptyList());

        assertFalse(app.hasConflict(1L, date, time));
    }

    @Test
    void hasConflict_shouldReturnTrue_whenScheduledBookingExists() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Room room = new Room("Salle A", 10);
        // Existing booking 10h00 → occupe 10h00–11h00
        Booking existing = new Booking(date, LocalTime.of(10, 0), room);

        when(bookingRepository.findByRoomIdAndDesiredAtAndEndedAtIsNull(1L, date)).thenReturn(Arrays.asList(existing));

        // Nouvelle demande à 10h30 → dans la fenêtre → conflit
        assertTrue(app.hasConflict(1L, date, LocalTime.of(10, 30)));
    }

    @Test
    void hasConflict_shouldReturnFalse_whenScheduledBookingDoesNotOverlap() {
        LocalDate date = LocalDate.of(2026, 6, 1);
        Room room = new Room("Salle A", 10);
        // Existing booking 10h00 → occupe 10h00–11h00
        Booking existing = new Booking(date, LocalTime.of(10, 0), room);

        when(bookingRepository.findByRoomIdAndDesiredAtAndEndedAtIsNull(1L, date)).thenReturn(Arrays.asList(existing));

        // Nouvelle demande à 11h00 → hors fenêtre → pas de conflit
        assertFalse(app.hasConflict(1L, date, LocalTime.of(11, 0)));
    }

    @Test
    void isRoomNameTaken_shouldReturnFalse_whenNameIsUnique() {
        when(roomRepository.findByName("Salle C")).thenReturn(Optional.empty());

        assertFalse(app.isRoomNameTaken("Salle C"));
    }

    @Test
    void deleteRoomById_shouldReturnTrue_whenRoomExists() {
        Room room = new Room("To delete", 33);
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        assertTrue(app.deleteRoomById(1L));
        verify(roomRepository).delete(room);
    }

    @Test
    void deleteRoomById_shouldReturnFalse_whenRoomNotFound() {
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());

        assertFalse(app.deleteRoomById(99L));
        verify(roomRepository, never()).delete(any());
    }


}

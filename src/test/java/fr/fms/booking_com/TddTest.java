package fr.fms.booking_com;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fms.booking_com.console.App;
import fr.fms.booking_com.dao.BookingRepository;
import fr.fms.booking_com.dao.RoomRepository;
import fr.fms.booking_com.entities.Room;

@ExtendWith(MockitoExtension.class)
public class TddTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private App app;

    @Test
    void createRoom_shouldRaiseAnError_whenNameIsNotUnique() {
        when(roomRepository.findByName("Salle A"))
                .thenReturn(Optional.of(new Room("Salle A", 10)));

        assertTrue(app.isRoomNameTaken("Salle A"));
    }

    @Test
    void createRoom_shouldNotRaiseAnError_whenNameIsUnique() {
        when(roomRepository.findByName("Salle B"))
                .thenReturn(Optional.empty());

        assertFalse(app.isRoomNameTaken("Salle B"));
    }
    
}

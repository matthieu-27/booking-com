package fr.fms.booking_com;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.fms.booking_com.console.App;
import fr.fms.booking_com.dao.RoomRepository;
import fr.fms.booking_com.entities.Room;

@ExtendWith(MockitoExtension.class)
class AppTest {

    @Mock
    private RoomRepository roomRepository;

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
        List<Room> fakeSalles = Arrays.asList(
            new Room("Salle A", 10),
            new Room("Salle B", 20)
        );
        when(roomRepository.findAll()).thenReturn(fakeSalles);

        List<Room> result = app.displayRooms();

        assertEquals(2, result.size());
        assertEquals("Salle A", result.get(0).getName());
    }
}

package fr.fms.booking_com.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fms.booking_com.entities.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
  Optional<Room> findByName(String name);
}

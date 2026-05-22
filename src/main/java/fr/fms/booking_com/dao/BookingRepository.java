package fr.fms.booking_com.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fms.booking_com.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomIdAndDesiredAtAndEndedAtIsNull(Long roomId, LocalDate desiredAt);

    
    void deleteById(Long bookingId);
}

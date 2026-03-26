package fr.fms.booking_com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.fms.booking_com.entities.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}

package fr.fms.booking_com.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale.Category;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent(message = "La réservation ne peux pas être dans le passé")
    private LocalDate desiredAt;
    @FutureOrPresent(message = "La réservation ne peux pas être dans le passé")
    private LocalTime scheduledAt;
    @PastOrPresent(message = "La fin de la réservation ne peux pas être dans le futur")
    private LocalDateTime endedAt;

    @ManyToOne
    private Room room;

    public Booking(LocalDate desiredAt, LocalTime scheduledAt, Room room) {
        this.desiredAt = desiredAt;
        this.scheduledAt = scheduledAt;
        this.room = room;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", desiredAt='" + getDesiredAt() + "'" +
                ", scheduledAt='" + getScheduledAt() + "'" +
                ", endedAt='" + getEndedAt() + "'" +
                ", room='" + getRoom() + "'" +
                "}";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    public LocalDate getDesiredAt() {
        return this.desiredAt;
    }

    public void setDesiredAt(LocalDate desiredAt) {
        this.desiredAt = desiredAt;
    }

    public LocalTime getScheduledAt() {
        return this.scheduledAt;
    }

    public void setScheduledAt(LocalTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getEndedAt() {
        return this.endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

}

package com.shareitserver.booking.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.shareitserver.booking.enums.BookingState;
import com.shareitserver.item.model.Item;
import com.shareitserver.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User user;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingState bookingState;

    public Booking(Long id, LocalDateTime start, LocalDateTime end, BookingState bookingState) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.bookingState = bookingState;
    }

    public Booking(Long id, LocalDateTime start, LocalDateTime end, Item item, User user, BookingState bookingState) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.user = user;
        this.bookingState = bookingState;
    }

    public Booking(Long id, Item item, User user) {
        this.id = id;
        this.item = item;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return Objects.equals(getId(), booking.getId()) && Objects.equals(getStart(), booking.getStart()) && Objects.equals(getEnd(), booking.getEnd()) && Objects.equals(getItem(), booking.getItem()) && Objects.equals(getUser(), booking.getUser()) && getBookingState() == booking.getBookingState();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStart(), getEnd(), getItem(), getUser(), getBookingState());
    }
}

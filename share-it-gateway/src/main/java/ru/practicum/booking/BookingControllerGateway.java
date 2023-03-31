package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingControllerGateway {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto dto,
                                                            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return bookingClient.createBooking(dto, userId);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<BookingResponseDto> patchBooking(@PathVariable Long bookingId,
                                                           @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                           @RequestParam Boolean approved) {
        return bookingClient.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long bookingId,
                                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getBookingsByBooker(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                                                        @RequestParam(required = false,
                                                                                defaultValue = "ALL") String state,
                                                                        @Valid BookingPaginationParams params) {
        return bookingClient.getBookingsByBooker(bookerId, state, params);
    }

    @GetMapping("/owner")
    public ResponseEntity<?> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                                                       @RequestParam(required = false,
                                                                               defaultValue = "ALL") String state,
                                                                       @Valid BookingPaginationParams params) {
        return bookingClient.getBookingsByOwner(ownerId, state, params);
    }
}
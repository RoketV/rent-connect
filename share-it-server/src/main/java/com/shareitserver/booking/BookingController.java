package com.shareitserver.booking;

import com.shareitserver.booking.dto.BookingRequestDto;
import com.shareitserver.booking.dto.BookingResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto dto,
                                                            @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(bookingService.createBooking(dto, userId));
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<BookingResponseDto> patchBooking(@PathVariable Long bookingId,
                                                           @RequestHeader("X-Sharer-User-Id") @NotNull Long userId,
                                                           @RequestParam Boolean approved) {
        return ResponseEntity.ok(bookingService.approveBooking(bookingId, userId, approved));
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long bookingId,
                                                         @RequestHeader("X-Sharer-User-Id") @NotNull Long userId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId, userId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getBookingsByBooker(@RequestHeader("X-Sharer-User-Id") @NotNull Long bookerId,
                                                                        @RequestParam(required = false,
                                                                                defaultValue = "ALL") String state,
                                                                        @Valid BookingPaginationParams params) {
        return ResponseEntity.ok(bookingService.getBookingsByBooker(bookerId, state, params));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") @NotNull Long ownerId,
                                                                       @RequestParam(required = false,
                                                                               defaultValue = "ALL") String state,
                                                                       @Valid BookingPaginationParams params) {
        return ResponseEntity.ok(bookingService.getBookingsByOwner(ownerId, state, params));
    }
}

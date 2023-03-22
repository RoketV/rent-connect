package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.client.BaseClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;


import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<BookingResponseDto> createBooking(BookingRequestDto dto, Long userId) {
        ResponseEntity<Object> response = post("/", userId, dto);
        BookingResponseDto bookingResponseDto = (BookingResponseDto) response.getBody();
        return ResponseEntity.status(response.getStatusCode()).body(bookingResponseDto);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<BookingResponseDto> approveBooking(Long bookingId, Long userId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        ResponseEntity<Object> response = patch("{bookingId}", userId, parameters);
        BookingResponseDto dto = (BookingResponseDto) response.getBody();
        return ResponseEntity.status(response.getStatusCode()).body(dto);
    }


//    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
//        return post("", userId, requestDto);
//    }

    public ResponseEntity<BookingResponseDto> getBooking(long userId, Long bookingId) {
        ResponseEntity<Object> response = get("/" + bookingId, userId);
        BookingResponseDto dto = (BookingResponseDto) response.getBody();
        return ResponseEntity.status(response.getStatusCode()).body(dto);
    }
}

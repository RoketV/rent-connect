package ru.practicum.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.client.BaseClient;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(SimpleClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<BookingResponseDto> createBooking(BookingRequestDto dto, Long userId) {
        ResponseEntity<Object> response = post("/", userId, dto);
        BookingResponseDto bookingResponseDto = (BookingResponseDto) response.getBody();
        return ResponseEntity.status(response.getStatusCode()).body(bookingResponseDto);
    }

    public ResponseEntity<BookingResponseDto> approveBooking(Long bookingId, Long userId, Boolean approved) {
                return patchBooking(bookingId, userId, approved);
    }

    public ResponseEntity<List<BookingResponseDto>> getBookingsByBooker(Long bookerId, String state, BookingPaginationParams params) {
        ResponseEntity<Object[]> response = getListOfBookings("", bookerId, state, params);
        List<BookingResponseDto> dtoList = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(obj -> objectMapper.convertValue(obj, BookingResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(response.getStatusCode()).body(dtoList);
    }

    public ResponseEntity<List<BookingResponseDto>> getBookingsByOwner(Long bookerId, String state, BookingPaginationParams params) {
        ResponseEntity<Object[]> response = getListOfBookings("/owner", bookerId, state, params);
        List<BookingResponseDto> dtoList = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(obj -> objectMapper.convertValue(obj, BookingResponseDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(response.getStatusCode()).body(dtoList);
    }

    public ResponseEntity<BookingResponseDto> getBooking(long userId, Long bookingId) {
        ResponseEntity<Object> response = get("/" + bookingId, userId);
        BookingResponseDto dto = (BookingResponseDto) response.getBody();
        return ResponseEntity.status(response.getStatusCode()).body(dto);
    }

    protected ResponseEntity<Object[]> getListOfBookings(String path, Long userId, String state, BookingPaginationParams params) {
        HttpEntity<?> entity = new HttpEntity<>(defaultHeaders(userId));
        return rest.exchange(
                "/bookings" + path + "?state={state}&page={page}&size={size}",
                HttpMethod.GET,
                entity,
                Object[].class,
                state,
                params.getFrom(),
                params.getSize()
                );
    }

    protected ResponseEntity<BookingResponseDto> patchBooking(Long bookingId, Long userId, Boolean approved) {
        HttpEntity<?> entity = new HttpEntity<>(defaultHeaders(userId));
        Map<Object, Object> parameters = Map.of(
                "approved", approved
        );
        return rest.exchange("/bookings/{bookingId}", HttpMethod.PATCH, entity,
                BookingResponseDto.class, bookingId, parameters);
    }
}

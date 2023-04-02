package ru.practicum.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.MainClient;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.booking.dto.BookingState;
import ru.practicum.exceptions.UnsupportedBookingState;
import ru.practicum.util.ResponseMaker;
import ru.practicum.util.UriPathBuilderWithParams;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BookingClient extends MainClient {
    private static final String API_PREFIX = "/bookings";

    private final String serverUrl;

    private final ResponseMaker responseMaker;

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                         ResponseMaker responseMaker) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
        this.serverUrl = serverUrl;
        this.responseMaker = responseMaker;
    }

    public ResponseEntity<BookingResponseDto> createBooking(BookingRequestDto dto, Long userId) {
        ResponseEntity<Object> response = post("", userId, dto);
        return responseMaker.makeResponse(response, BookingResponseDto.class);

    }

    public ResponseEntity<BookingResponseDto> approveBooking(Long bookingId, Long userId, Boolean approved) {
        ResponseEntity<Object> response = patchBooking(bookingId, userId, approved);
        return responseMaker.makeResponse(response, BookingResponseDto.class);
    }

    public ResponseEntity<List<BookingResponseDto>> getBookingsByBooker(Long bookerId, String state, BookingPaginationParams params) {
        ResponseEntity<Object[]> response = getListOfBookings("", bookerId, state, params);
        return responseMaker.makeListResponse(response, BookingResponseDto.class);
    }

    public ResponseEntity<List<BookingResponseDto>> getBookingsByOwner(Long ownerId, String state, BookingPaginationParams params) {
        ResponseEntity<Object[]> response = getListOfBookings("/owner", ownerId, state, params);
        return responseMaker.makeListResponse(response, BookingResponseDto.class);
    }

    public ResponseEntity<BookingResponseDto> getBooking(Long userId, Long bookingId) {
        ResponseEntity<Object> response = get("/" + bookingId, userId);
        return responseMaker.makeResponse(response, BookingResponseDto.class);
    }

    protected ResponseEntity<Object[]> getListOfBookings(String path, Long userId, String stateParam, BookingPaginationParams params) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnsupportedBookingState("Unknown state: " + stateParam));
        HttpEntity<?> entity = new HttpEntity<>(defaultHeaders(userId));
        Map<String, String> parameters = Map.of(
                "state", state.toString(),
                "from", params.from().toString(),
                "size", params.size().toString()
        );
        ResponseEntity<Object[]> response;
        try {
            URI uri = UriPathBuilderWithParams.buildUri(serverUrl, API_PREFIX, path, parameters);
           response = rest.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    Object[].class
            );
        }  catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
        return prepareGatewayResponse(response);
    }

    protected ResponseEntity<Object> patchBooking(Long bookingId, Long userId, Boolean approved) {
        HttpEntity<?> entity = new HttpEntity<>(defaultHeaders(userId));
        Map<String, String> parameters = Map.of(
                "approved", approved.toString()
        );
        ResponseEntity<Object> response;
        try {
            Map<String, String> pathVariable = Collections.singletonMap("bookingId", bookingId.toString());
            URI uri = UriPathBuilderWithParams.buildUri(serverUrl, API_PREFIX,
                    "/" + bookingId, parameters, pathVariable);
            response = rest.exchange(uri,
                    HttpMethod.PATCH,
                    entity,
                    Object.class);
        }  catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(response);
    }
}

package com.shareitserver.bookingTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shareitserver.booking.BookingController;
import com.shareitserver.booking.BookingService;
import com.shareitserver.booking.dto.BookingRequestDto;
import com.shareitserver.booking.dto.BookingResponseDto;
import com.shareitserver.booking.enums.BookingState;
import com.shareitserver.item.model.Item;
import com.shareitserver.user.model.User;
import com.shareitserver.util.PaginationParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @MockBean
    private BookingService bookingService;

    @BeforeEach
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setDateFormat(dateFormat);
    }

    @Test
    public void createBooking_shouldReturnBookingResponseDto() throws Exception {
        BookingRequestDto requestDto = new BookingRequestDto();
        requestDto.setItemId(2L);
        requestDto.setStart(LocalDateTime.now().plusHours(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(1));

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(1L);
        responseDto.setUser(new User(1L, "name", "email@email.com"));
        responseDto.setStart(LocalDateTime.now());
        responseDto.setEnd(LocalDateTime.now().plusDays(1));
        responseDto.setBookingState(BookingState.APPROVED);

        when(bookingService.createBooking(any(BookingRequestDto.class), anyLong())).thenReturn(responseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.bookingState").value(responseDto.getBookingState().toString()));
    }

    @Test
    public void patchBooking_shouldReturnBookingResponseDto() throws Exception {
        Long bookingId = 1L;
        Long userId = 2L;
        Boolean approved = true;

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(bookingId);
        responseDto.setUser(new User(userId, "name", "email@email.com"));
        responseDto.setStart(LocalDateTime.now());
        responseDto.setEnd(LocalDateTime.now().plusDays(1));
        responseDto.setBookingState(BookingState.APPROVED);

        when(bookingService.approveBooking(eq(bookingId), eq(userId), eq(approved))).thenReturn(responseDto);

        mockMvc.perform(patch("/bookings/" + bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", approved.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.user.id").value(responseDto.getUser().getId()))
                .andExpect(jsonPath("$.bookingState").value(responseDto.getBookingState().toString()));
    }

    @Test
    public void getBooking_shouldReturnBookingResponseDto() throws Exception {
        Long bookingId = 1L;
        Long userId = 2L;

        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setId(bookingId);
        responseDto.setUser(new User(userId, "name", "email@email.com"));
        responseDto.setStart(LocalDateTime.now());
        responseDto.setEnd(LocalDateTime.now().plusDays(1));
        responseDto.setBookingState(BookingState.APPROVED);

        when(bookingService.getBooking(eq(bookingId), eq(userId))).thenReturn(responseDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.getId()))
                .andExpect(jsonPath("$.user.id").value(responseDto.getUser().getId()))
                .andExpect(jsonPath("$.bookingState").value(responseDto.getBookingState().toString()));
    }

    @Test
    public void getBookingsByBooker_shouldReturnBookingResponseDto() throws Exception {
        Long bookingId1 = 1L;
        Long bookingId2 = 2L;
        Long userId = 2L;
        String state = "ALL";

        User user = new User(userId, "name", "email@email.com");


        BookingResponseDto responseDto1 = new BookingResponseDto();
        responseDto1.setId(bookingId1);
        responseDto1.setUser(user);
        responseDto1.setStart(LocalDateTime.now());
        responseDto1.setEnd(LocalDateTime.now().plusDays(1));
        responseDto1.setBookingState(BookingState.APPROVED);

        BookingResponseDto responseDto2 = new BookingResponseDto();
        responseDto2.setId(bookingId2);
        responseDto2.setUser(user);
        responseDto2.setStart(LocalDateTime.now());
        responseDto2.setEnd(LocalDateTime.now().plusDays(1));
        responseDto2.setBookingState(BookingState.APPROVED);

        List<BookingResponseDto> responseDtos = Arrays.asList(responseDto1, responseDto2);
        PaginationParams params = new PaginationParams(0, 20);

        when(bookingService.getBookingsByBooker(
                eq(userId), eq(state), eq(params))).thenReturn(responseDtos);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDto1.getId()))
                .andExpect(jsonPath("$[0].user.id").value(responseDto1.getUser().getId()))
                .andExpect(jsonPath("$[0].bookingState").value(responseDto1.getBookingState().toString()))
                .andExpect(jsonPath("$[1].id").value(responseDto2.getId()))
                .andExpect(jsonPath("$[1].user.id").value(responseDto2.getUser().getId()))
                .andExpect(jsonPath("$[1].bookingState").value(responseDto2.getBookingState().toString()));
    }

    @Test
    public void getBookingsByOwner_shouldReturnBookingResponseDto() throws Exception {
        Long bookingId1 = 1L;
        Long bookingId2 = 2L;
        Long userId = 2L;
        Long ownerId = 3L;
        String state = "ALL";

        User user = new User(userId, "name", "email@email.com");
        User owner = new User(ownerId, "owner name", "owneremail@email.com");

        Item item = new Item(1L, "item name", "item description", true, owner);

        BookingResponseDto responseDto1 = new BookingResponseDto();
        responseDto1.setId(bookingId1);
        responseDto1.setItem(item);
        responseDto1.setUser(user);
        responseDto1.setStart(LocalDateTime.now());
        responseDto1.setEnd(LocalDateTime.now().plusDays(1));
        responseDto1.setBookingState(BookingState.APPROVED);

        BookingResponseDto responseDto2 = new BookingResponseDto();
        responseDto2.setId(bookingId2);
        responseDto2.setItem(item);
        responseDto2.setUser(user);
        responseDto2.setStart(LocalDateTime.now());
        responseDto2.setEnd(LocalDateTime.now().plusDays(1));
        responseDto2.setBookingState(BookingState.APPROVED);

        List<BookingResponseDto> responseDtos = Arrays.asList(responseDto1, responseDto2);

        when(bookingService.getBookingsByOwner(
                eq(userId), eq(state), any(PaginationParams.class))).thenReturn(responseDtos);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", state)
                        .param("from", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(responseDto1.getId()))
                .andExpect(jsonPath("$[0].user.name").value(responseDto1.getUser().getName()))
                .andExpect(jsonPath("$[0].user.email").value(responseDto1.getUser().getEmail()))
                .andExpect(jsonPath("$[0].bookingState").value(responseDto1.getBookingState().toString()))

                .andExpect(jsonPath("$[1].id").value(responseDto2.getId()))
                .andExpect(jsonPath("$[1].user.name").value(responseDto2.getUser().getName()))
                .andExpect(jsonPath("$[1].user.email").value(responseDto2.getUser().getEmail()))
                .andExpect(jsonPath("$[1].bookingState").value(responseDto2.getBookingState().toString()));

    }
}
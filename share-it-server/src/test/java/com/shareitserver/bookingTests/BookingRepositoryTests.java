package com.shareitserver.bookingTests;

import com.shareitserver.booking.BookingPaginationParams;
import com.shareitserver.booking.BookingRepository;
import com.shareitserver.booking.enums.BookingState;
import com.shareitserver.booking.model.Booking;
import com.shareitserver.item.ItemRepository;
import com.shareitserver.item.model.Item;
import com.shareitserver.user.UserRepositoryServer;
import com.shareitserver.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingRepositoryTests {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepositoryServer userRepositoryServer;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testFindByIdAndItem_User_IdOrUser_Id() {
        User user = new User(1L, "user name", "email@email.com");
        userRepositoryServer.save(user);

        Item item = new Item("Item 1", "Description 1", true, user);
        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setUser(user);
        bookingRepository.save(booking);

        Optional<Booking> result = bookingRepository.findByIdAndItem_User_IdOrUser_Id(1L, 1L);
        assertTrue(result.isPresent());
        assertEquals(booking, result.get());

        result = bookingRepository.findByIdAndItem_User_IdOrUser_Id(2L, 1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAllByUser_Id() {
        User user = new User(1L, "user name", "email@email.com");
        userRepositoryServer.save(user);

        Item item = new Item("Item 1", "Description 1", true, user);
        itemRepository.save(item);

        Booking booking1 = new Booking(1L, item, user);
        Booking booking2 = new Booking(2L, item, user);
        bookingRepository.saveAll(List.of(booking1, booking2));

        List<Booking> result = bookingRepository.findAllByUser_Id(user.getId());
        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
    }

    @Test
    public void testFindAllByItem_Id() {
        User user = new User(1L, "user name", "email@email.com");
        userRepositoryServer.save(user);

        Item item = new Item("Item 1", "Description 1", true, user);
        itemRepository.save(item);

        Booking booking1 = new Booking(1L, item, user);
        Booking booking2 = new Booking(2L, item, user);
        bookingRepository.saveAll(List.of(booking1, booking2));

        List<Booking> result = bookingRepository.findAllByItem_IdAndBookingState(item.getId());
        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
    }

    @Test
    public void testFindAllByItem_User_Id() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        Booking booking2 = new Booking(2L, item1, user);
        Booking booking3 = new Booking(3L, item2, user);
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        List<Booking> result = bookingRepository.findAllByItem_User_Id(item1.getUser().getId());
        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
        assertFalse(result.contains(booking3));
    }

    @Test
    public void testFindAllByOwner() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        Booking booking2 = new Booking(2L, item1, user);
        Booking booking3 = new Booking(3L, item2, user);
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));
        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findAllByOwner(item1.getUser().getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    public void testFindAllFutureBookingsByBooker() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().plusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().plusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().minusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));
        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findFutureBookingsByBooker(user.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    public void testFindAllPastBookingsByBooker() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().minusHours(3));
        booking1.setEnd(LocalDateTime.now().minusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().plusHours(1));
        booking3.setEnd(LocalDateTime.now().plusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));
        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findPastBookingsByBooker(user.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    public void testFindAllPastBookingsByBookerAndItem() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().minusHours(3));
        booking1.setEnd(LocalDateTime.now().minusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().plusHours(1));
        booking3.setEnd(LocalDateTime.now().plusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        List<Booking> result = bookingRepository.findPastBookingsByBookerAndItem(user.getId(), item1.getId());
        assertEquals(2, result.size());
        assertTrue(result.contains(booking1));
        assertTrue(result.contains(booking2));
        assertFalse(result.contains(booking3));
    }

    @Test
    public void testFindCurrentBookingsByBooker() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().minusHours(3));
        booking1.setEnd(LocalDateTime.now().plusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().plusHours(1));
        booking3.setEnd(LocalDateTime.now().plusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findCurrentBookingsByBooker(user.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @ParameterizedTest
    @EnumSource(BookingState.class)
    public void testFindByStatusByBooker(BookingState bookingState) {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setBookingState(bookingState);
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setBookingState(bookingState);
        Booking booking3 = new Booking(3L, item2, user);

        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findByStatusByBooker(bookingState, user.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    public void testFindAllFutureBookingsByOwner() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().plusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().plusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().minusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));
        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findFutureBookingsByOwner(itemOwner.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    public void testFindAllPastBookingsByOwner() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().minusHours(3));
        booking1.setEnd(LocalDateTime.now().minusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().minusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().plusHours(1));
        booking3.setEnd(LocalDateTime.now().plusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findPastBookingsByOwner(itemOwner.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    public void testFindCurrentBookingsByOwner() {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setStart(LocalDateTime.now().minusHours(3));
        booking1.setEnd(LocalDateTime.now().plusHours(2));
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setStart(LocalDateTime.now().minusHours(3));
        booking2.setEnd(LocalDateTime.now().plusHours(2));
        Booking booking3 = new Booking(3L, item2, user);
        booking3.setStart(LocalDateTime.now().plusHours(1));
        booking3.setEnd(LocalDateTime.now().plusHours(2));
        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findCurrentBookingsByOwner(itemOwner.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @ParameterizedTest
    @EnumSource(BookingState.class)
    public void testFindByStatusByOwner(BookingState bookingState) {
        User user = new User(1L, "user name", "email@email.com");
        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.saveAll(List.of(user, itemOwner));

        Item item1 = new Item("Item 1", "Description 1", true, itemOwner);
        Item item2 = new Item("Item 2", "Description 2", true, user);
        itemRepository.saveAll(List.of(item1, item2));

        Booking booking1 = new Booking(1L, item1, user);
        booking1.setBookingState(bookingState);
        Booking booking2 = new Booking(2L, item1, user);
        booking2.setBookingState(bookingState);
        Booking booking3 = new Booking(3L, item2, user);

        bookingRepository.saveAll(List.of(booking1, booking2, booking3));

        BookingPaginationParams params = new BookingPaginationParams(0, 20);

        Page<Booking> result = bookingRepository.findByStatusByOwner(bookingState, itemOwner.getId(),
                PageRequest.of(params.from(), params.size()));
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(booking1));
        assertTrue(result.getContent().contains(booking2));
        assertFalse(result.getContent().contains(booking3));
    }

    @Test
    void testUpdateBookingStatus() {
        User user = new User(1L, "user name", "email@email.com");
        userRepositoryServer.save(user);

        User itemOwner = new User(2L, "itemOwner name", "email@email2.com");
        userRepositoryServer.save(itemOwner);

        Item item = new Item("Item", "Description", true, itemOwner);
        itemRepository.save(item);

        Booking booking = new Booking(1L, item, user);
        booking.setBookingState(BookingState.WAITING);
        bookingRepository.save(booking);
        Booking getBooking = bookingRepository.findById(booking.getId()).get();
        getBooking.setBookingState(BookingState.APPROVED);

        bookingRepository.updateBookingStatus(BookingState.REJECTED, booking.getId());

        Optional<Booking> optionalBooking = bookingRepository.findById(booking.getId());
        assertTrue(optionalBooking.isPresent());
        assertEquals(item, optionalBooking.get().getItem());
        assertEquals(BookingState.APPROVED, optionalBooking.get().getBookingState());
    }
}

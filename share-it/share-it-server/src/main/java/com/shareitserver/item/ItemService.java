package com.shareitserver.item;

import com.shareitserver.booking.BookingRepository;
import com.shareitserver.booking.dto.BookingInItemResponseDto;
import com.shareitserver.booking.dto.BookingMapper;
import com.shareitserver.booking.enums.BookingState;
import com.shareitserver.booking.model.Booking;
import com.shareitserver.comments.Comment;
import com.shareitserver.comments.CommentRepository;
import com.shareitserver.comments.dto.CommentInputDto;
import com.shareitserver.comments.dto.CommentMapper;
import com.shareitserver.comments.dto.CommentOutputDto;
import com.shareitserver.exceptions.CommentConsistencyException;
import com.shareitserver.exceptions.DifferentUsersException;
import com.shareitserver.exceptions.EntityNotFoundException;
import com.shareitserver.item.dto.ItemInputDto;
import com.shareitserver.item.dto.ItemMapper;
import com.shareitserver.item.dto.ItemOutputDto;
import com.shareitserver.item.model.Item;
import com.shareitserver.request.ItemRequest;
import com.shareitserver.request.ItemRequestRepository;
import com.shareitserver.user.UserRepository;
import com.shareitserver.user.model.User;
import com.shareitserver.util.PaginationParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository requestRepository;


    @Transactional
    public ItemOutputDto addItem(ItemInputDto dto, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item has to belong to user." +
                        "User with id %d not found", userId)));
        dto.setUser(user);
        Item item = ItemMapper.ITEM_MAPPER.toItem(dto);
        Long requestId = dto.getRequestId();
        if (requestId != null) {
            ItemRequest request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("request with id %d not found", requestId)));
            item.setRequest(request);
        }
        log.info("item with name {} added", item.getName());
        return ItemMapper.ITEM_MAPPER.toDto(itemRepository.save(item));
    }

    @Transactional
    public ItemOutputDto updateItem(ItemInputDto dto, Long userId, Long itemId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException(String.format("there is no user with id %d for new item", userId));
        }
        if (!userIsTheSame(itemId, userId)) {
            throw new DifferentUsersException("cannot update item's user");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("item with %d id not found", itemId)));
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getUser() != null) {
            item.setUser(dto.getUser());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        log.info("item with id {} updated", itemId);
        return ItemMapper.ITEM_MAPPER.toDto(itemRepository.save(item));
    }

    public ItemOutputDto getItem(Long ownerId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("item with id %s not found", itemId)));
        List<Booking> bookings = bookingRepository.findAllByItem_IdAndBookingState(itemId, BookingState.APPROVED);
        ItemOutputDto dto = ItemMapper.ITEM_MAPPER.toDto(item);
        List<CommentOutputDto> comments = commentRepository.findAllByItem_Id(itemId)
                .stream()
                .map(CommentMapper.COMMENT_MAPPER::toDto)
                .collect(Collectors.toList());
        dto.setComments(comments);
        if (!item.getUser().getId().equals(ownerId)) {
            return dto;
        }
        LocalDateTime now = LocalDateTime.now();
        for (Booking booking : bookings) {
            if (booking.getStart().isBefore(now)) {
                dto.setLastBooking(BookingMapper.BOOKING_MAPPER.toBookingInItemDto(booking));
            }
            if (booking.getStart().isAfter(now)) {
                dto.setNextBooking(BookingMapper.BOOKING_MAPPER.toBookingInItemDto(booking));
            }
        }
        return dto;
    }

    public List<ItemOutputDto> getItems(Long ownerId, PaginationParams params) {
        Page<Item> page = itemRepository.findAllByUser_IdOrderByIdAsc(ownerId, PageRequest.of(params.from(), params.size()));
        List<ItemOutputDto> items = page.getContent()
                .stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
        List<BookingInItemResponseDto> bookings = bookingRepository.findAllByItem_User_Id(ownerId)
                .stream()
                .map(BookingMapper.BOOKING_MAPPER::toBookingInItemDto).toList();

        for (ItemOutputDto item : items) {
            item.setLastBooking(bookings
                    .stream()
                    .filter(b -> b.getItemId().equals(item.getId()))
                    .filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(BookingInItemResponseDto::getEnd))
                    .orElse(null));
            item.setNextBooking(bookings
                    .stream()
                    .filter(b -> b.getItemId().equals(item.getId()))
                    .filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(BookingInItemResponseDto::getStart))
                    .orElse(null));
        }
        return items;
    }

    @Transactional
    public ItemOutputDto deleteItem(Long id) {
        Optional<Item> item = itemRepository.findById(id);
        item.ifPresent(itemRepository::delete);
        log.info("item with id {} deleted", id);
        return ItemMapper.ITEM_MAPPER.toDto(item
                .orElseThrow(() -> new EntityNotFoundException("there is no such item to delete")));
    }

    public List<ItemOutputDto> searchItem(String text, PaginationParams params) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        Page<Item> page = itemRepository.findAll(PageRequest.of(params.from(), params.size()));
        List<ItemOutputDto> items = page.getContent()
                .stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text.toLowerCase())
                                || item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
        if (items.isEmpty()) {
            throw new EntityNotFoundException("there is no item which satisfies your search");
        }
        return items;
    }

    public CommentOutputDto addComment(CommentInputDto dto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("item with id %s not found", itemId)));
        List<Booking> pastBookings = bookingRepository
                .findPastBookingsByBookerAndItem(userId, itemId);
        if (pastBookings.isEmpty()) {
            throw new CommentConsistencyException(String.format("user with id %d cannot leave comment for booking " +
                    "which is still current or in future", userId));
        }
        Comment comment = CommentMapper.COMMENT_MAPPER.toComment(dto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Item has to belong to user." +
                        "User with id %d not found", userId)));
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        log.info("comment added be user with id {}", userId);
        return CommentMapper.COMMENT_MAPPER.toDto(commentRepository.save(comment));
    }

    private boolean userIsTheSame(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("cannot find Item with %s id", itemId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("cannot find User with %s id", userId)));
        return item.getUser().equals(user);
    }
}
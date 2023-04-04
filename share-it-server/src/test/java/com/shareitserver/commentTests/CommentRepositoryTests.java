package com.shareitserver.commentTests;


import com.shareitserver.comments.Comment;
import com.shareitserver.comments.CommentRepository;
import com.shareitserver.item.ItemRepository;
import com.shareitserver.item.model.Item;
import com.shareitserver.user.UserRepository;
import com.shareitserver.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findAllByItem_IdTest() {
        User user = new User("userName", "email@email.com");

        userRepository.save(user);

        Item item1 = new Item("name Item1", "description Item1", true, user);
        Item item2 = new Item("name Item2", "description Item2", true, user);

        itemRepository.saveAll(List.of(item1, item2));

        Comment comment1 = new Comment("text1", user, item1, LocalDateTime.now());
        Comment comment2 = new Comment("text2", user, item1, LocalDateTime.now());
        Comment comment3 = new Comment("text3", user, item2, LocalDateTime.now());

        commentRepository.saveAll(List.of(comment1, comment2, comment3));

        List<Comment> comments = commentRepository.findAllByItem_Id(1L);

        Assertions.assertAll(
                () -> assertFalse(comments.isEmpty()),
                () -> assertEquals(2, comments.size()),
                () -> assertEquals("text1", comments.get(0).getText()),
                () -> assertEquals("text2", comments.get(1).getText()),
                () -> assertEquals(user, comments.get(0).getAuthor()),
                () -> assertEquals(user, comments.get(1).getAuthor()),
                () -> assertEquals(item1, comments.get(0).getItem()),
                () -> assertEquals(item1, comments.get(1).getItem())
        );
    }
}

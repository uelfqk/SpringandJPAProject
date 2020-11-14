package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    @Transactional
    void saveItem() {
        // given
        Book book = new Book();
        book.setName("가나다라");
        book.setPrice(10000);
        book.setStockQuantity(100);
        book.setAuthor("나나");
        book.setIsbn("isbn");

        // when
        Long findItemId = itemService.saveItem(book);

        // then
        Assertions.assertThat(book).isEqualTo(itemRepository.findOne(findItemId));
    }

    @Test
    @Transactional
    void findItem() {
        // given
        Book book = new Book();
        book.setName("가나다라");
        book.setPrice(10000);
        book.setStockQuantity(100);
        book.setAuthor("나나");
        book.setIsbn("isbn");
        Long saveItemId = itemService.saveItem(book);

        // when
        Item resultService = itemService.findOne(saveItemId);
        Item resultRepository = itemRepository.findOne(saveItemId);

        // then
        Assertions.assertThat(resultService).isEqualTo(resultRepository);
    }
}
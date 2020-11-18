package jpabook.jpashop.service;

import jpabook.jpashop.domain.dto.BookUpdateDto;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public void updateBook(Long itemId, BookUpdateDto dto) {
        Book findItem = (Book)findOne(itemId);
        findItem.setName(dto.getName());
        findItem.setPrice(dto.getPrice());
        findItem.setStockQuantity(dto.getStockQuantity());
        findItem.setAuthor(dto.getAuthor());
        findItem.setIsbn(dto.getIsbn());
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}

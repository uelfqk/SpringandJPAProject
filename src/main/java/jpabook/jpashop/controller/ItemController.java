package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.ItemForm;
import jpabook.jpashop.domain.dto.BookUpdateDto;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ItemForm());
        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(ItemForm form) {
        Book book = Book.createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        Long saveItemId = itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping(value = "/items")
    public String showItems(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping(value = "/items/{id}/edit")
    public String updateItemForm(@PathVariable(name = "id") Long id, Model model) {
        Book book = (Book)itemService.findOne(id);
        ItemForm form = ItemForm.createItemForm(id, book.getName(), book.getPrice(), book.getStockQuantity(), book.getAuthor(), book.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping(value = "/items/{id}/edit")
    public String updateItem(ItemForm form) {
        BookUpdateDto dto = BookUpdateDto.createDto(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.updateBook(form.getId(), dto);
        return "redirect:/items";
    }
}

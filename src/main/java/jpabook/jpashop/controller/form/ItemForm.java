package jpabook.jpashop.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public static ItemForm createItemForm(Long id, String name, int price, int stockQuantity, String author, String isbn) {
        ItemForm form = new ItemForm();
        form.setId(id);
        form.setName(name);
        form.setPrice(price);
        form.setStockQuantity(stockQuantity);
        form.setAuthor(author);
        form.setIsbn(isbn);
        return form;
    }
}

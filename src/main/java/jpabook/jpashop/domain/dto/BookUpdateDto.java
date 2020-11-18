package jpabook.jpashop.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class BookUpdateDto {

    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public static BookUpdateDto createDto(String name, int price, int stockQuantity, String author, String isbn) {
        BookUpdateDto dto = new BookUpdateDto();
        dto.setName(name);
        dto.setPrice(price);
        dto.setStockQuantity(stockQuantity);
        dto.setAuthor(author);
        dto.setIsbn(isbn);
        return dto;
    }
}

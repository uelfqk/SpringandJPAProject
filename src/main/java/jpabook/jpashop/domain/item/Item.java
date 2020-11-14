package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.CategoryItem;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categoryItems = new ArrayList<>();

    //==양방향 연관관계에서 편의 메소드==//
    public void setCategoryItem(CategoryItem categoryItem) {
        this.categoryItems.add(categoryItem);
        categoryItem.setItem(this);
    }

    //==비즈니스 로직==//
    // 보통 개발할때 아이템 서비스에서 StockQuantity 를 가져와서 연산을 하고
    // setStockQuantity(result); 이런 식으로 했을 것이다.
    // 객체지향적으로 생각해보면 데이터를 가지고 있는 객체에 비즈니스 로직을 만드는게 가장 좋다.
    // 그래서 아래와 같은 핵심 비즈니스 로직을 넣은 것이다.

    /*
    * stock 증가
    * */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /*
    * stock 감소
    * */
    public void removeStock(int quantity) {
        int resultStock = this.stockQuantity -= quantity;
        if(resultStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
    }
}

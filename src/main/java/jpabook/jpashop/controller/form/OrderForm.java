package jpabook.jpashop.controller.form;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter(value = AccessLevel.PRIVATE)
public class OrderForm {

    private Long id;
    private List<Member> members = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private int orderCount;

    public static OrderForm createForm(Long orderId, List<Member> members, List<Item> items, int orderCount) {
        OrderForm form = new OrderForm();
        form.setId(orderId);
        form.setMembers(members);
        form.setItems(items);
        form.setOrderCount(orderCount);
        return form;
    }

    public static OrderForm createForm(List<Member> members, List<Item> items) {
        OrderForm form = new OrderForm();
        form.setMembers(members);
        form.setItems(items);
        return form;
    }
}

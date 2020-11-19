package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.ItemForm;
import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.controller.form.OrderForm;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    @GetMapping(value = "/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    // 컨트롤러에서 Member 와 Item 을 찾아서 넘겨도 되지 않나요?
    // 그렇게 해도 됩니다. 다만 Controller Layer 가 지저분해진다.
    // 또 Service Layer 는 더 많은 엔티티를 의존함으로 각 Id(식별자) 를 Service Layer 로 넘겨 (Transaction 안에서 -> 영속 상태 혜택)
    // 엔티티를 조회하고 변경하는 로직을 처리하는 것이 더 유리하다.
    // Controller Layer 에서 Member 를 조회해서 order 를 Service Layer 로 넘기게 되면 영속성 컨텍스트가 종료된 상태에서 넘어가기 때문에
    // Service Layer 에서 해당 엔티티를 변경하는것이 굉장히 난해해진다.
    @PostMapping(value = "/order")
    public String create(@RequestParam(name = "memberId") Long memberId,
                         @RequestParam(name = "itemId") Long itemId,
                         @RequestParam(name = "count") int count) {

        Long saveOrderId = orderService.order(memberId, itemId, count);

        return "redirect:/";
    }

    // 단순 화면 출력 (단순히 값을 위임하는것) 로직은 Controller Layer 에서 처리해도 괜찮다.
    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping(value = "/orders/{id}/cancel")
    public String cancel(@PathVariable(name = "id") Long id) {
        orderService.cancelOrder(id);
        return "redirect:/orders";
    }
}

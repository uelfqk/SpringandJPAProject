package jpabook.jpashop.controller;

import jpabook.jpashop.controller.form.MemberForm;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    // 35 line : @Valid 를 사용하면 MemberForm 이 Validation 을 사용하는것으로 인식한다.
    // form.MemberFrom 11 line : MemberFrom.name -> @NotEmpty 사용 가능
    // 35 line : @Valid object, BindingResult result 가 있으면 오류 내용이 담겨서 해당 메소드가 동작하게된다.
    //           spring-thymeleaf 를 받으면 thymeleaf-spring 의존성을 같이 받아오게 되며
    //           spring 과 thymeleaf 가 인티그레이션이 좋다.
    // Member Entity 를 그대로 넣지 않고 MemberForm 을 사용하는 이유
    // Entity 에는 실제 화면의 데이터에 필요하지 않은 데이터도 있고 Entity 에 Validation 을 적용하면
    // Entity 객체가 많이 지저분해지며 실무에서는 현재보다 더 복잡하다.
    // 차라리 MemberForm 과 같이 Form 을 만들어 사용하는것이 좋다.
    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) {
        // 36 - 38 line 에러가 있으면 다시 members/createMemberForm 으로 보낸다.
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
}

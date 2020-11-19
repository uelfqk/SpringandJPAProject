package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

// @Controller 와 @ResponseBody 를 합쳐 놓은 애노테이션
// @ResponseBody 반환되는 데이터를 바로 Json 또는 xml 로 변환
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // @RequestBody -> json 으로 수신된 데이터를 Member 객체에 반영한다.
    // @Valid -> @NotEmpty 등의 Validation 을 Entity 에서 사용하면 추후에 Entity 에 필드를 변경하는 경우
    // API 스팩이 변하는 심각한 상황이 발생한다.
    // 혼자 개발하는 경우는 관계 없지만 협업을 하는 상황에서는 API 가 깨지는 상황 발생
    // Ex). 기존 Entity 의 필드 name -> username 으로 변경하면 API 스팩 변화
    //      Validation 을 Entity 에 사용 -> A 라는 클라이언트에서는 name 필드가 Null 값이 있어도 괜찮지만
    //                                     B 라는 클라이언트에서는 name 필드가 Null 이면 안되는 경우
    //  -. 이 모든것을 Entity 객체 하나로 처리할 수 없다.
    // 또한 Entity 를 외부에 노출하는 상황이 발생할 수 있다.
    // Ex). Entity 에 user password 가 있을때 Entity 자체로 처리하게 되면 password 가 노출 된다.
    // 최대한 Entity 객체를 순수하게 유지하는 방향으로 처리해야한다.
    @PostMapping(value = "/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    // API 스팩에서 요구하는 Dto 객체를 만들어서 사용
    // Entity 를 변경하더라도 API 스팩이 변화하지 않아 안정적으로 운영이 가능하다.
    // Entity 를 사용하는 경우 개발자 입장에서는 API 스팩 문서를 확인하지 않으면 어떤 값을 받는지 확인할 수 없다.
    // Dto 객체를 만들어 사용하게되면 해당 객체만 보고도 API 스팩을 확인 할 수 있다.
    // Validation 을 사용하더라도 Entity 에 영향을 주지 않아 여러 클라이언트에 대해 대응할 수 있다.
    @PostMapping(value = "/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long saveMemberId = memberService.join(member);
        return new CreateMemberResponse(saveMemberId);
    }

    @Data @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable(name = "id") Long id,
                                               @RequestBody UpdateMemberRequest request) {
        // 변경감지를 위해 MemberService 에 @Transaction 을 걸고 변경감지를 활용
        memberService.updateMember(id, request.getName());
        // API 가 복잡하지 않고 지금처럼 PK를 단건을 찍어 조회하는 경우 부하가 많이 걸리지 않아 이와 같이 처리해도 괜찮다. - 김영한님
        Member findMember = memberService.findMember(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    // 반환되는 컬렉션을 이용하여 json 으로 변환
//    [
//        "Count":2 ---> 불가능
//        {
//            "id": 1,
//                "name": "member1",
//                "orders": [],
//            "address": null
//        },
//        {
//            "id": 2,
//                "name": "member2",
//                "orders": [],
//            "address": null
//        }
//    ]
    // 위와 같이 반환
    // Entity 의 모든 정보가 노출 되는 상황이 발생함
    // Entity 에서 노출되지 않을 정보는 @JsonIgnore 애노테이션을 사용하면 되나
    // 클라이언트에서 요구하는 데이터를 Entity 객체 하나로 처리할 수 없는 상황이 발생한다. -> 프레젠테이션 로직이 Entity 로 들어오게된다.
    // 컬랙션을 반환하면 json 배열 형태로 변환되어 다른 값을 넣을 수가 없다.
    @GetMapping(value = "/api/v1/members")
    public List<Member> selectMemberV1() {
        return memberService.findMembers();
    }

//    {
//        "count": 2,
//        "data": [
//            {
//                "name": "member1"
//            },
//            {
//                "name": "member2"
//            }
//        ]
//    }
    // Result 로 한번 감싸서 넣게 되면 json 배열 [] 로 시작하는 것이 아니라 {} 로 시작하게 되며
    // 컬랙션 이외의 데이터도 넣을 수 있게 된다.
    @GetMapping(value = "/api/v2/members")
    public Result selectMemberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}

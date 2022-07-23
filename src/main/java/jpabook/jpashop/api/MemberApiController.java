package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {
    private final MemberService memberService;

    /** 실무에서 사용 X
     * ->   1) 엔티티 정보 전체가 조회되므로 api상으로 보여질 것만 정의해서 호출해야한다.
     *      2) 각 API 별로 원하는 정보가 다를 수 있음. 이름만 호출 or 이름, 전화번호 호출 등등 여러가지 타입 존재.*/
    @GetMapping("api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))   //stream을 통해 generic을 MemberDto로 변경.
                .collect(Collectors.toList());
        return new Result(collect.size(),collect); //fix : collect.size 추가함.
    }

    /** 단순 List로 반환하게 되면 유연성이 저하되므로 Generic 타입으로 한번 캡슐화 해서 처리.
     * => JSON상에서 List로 반환되게 되면 겉에 '[ ]' 기호로 한번 감싸지는데, 이렇게 되면 유연성이 저하된다. (사용자가 다른 스팩을 요구할 경우 난해...)
     * */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }


    @PostMapping("/api/v1/members")

    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {   //json 데이터를 Member 변수에 자동 입력해준다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) { //DTO 장점 : 엔티티의 변수가 변동되도, API에는 영향을 안줌.
        log.info("create Members");
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV3(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request) {
        log.info("update Members");
        memberService.update(id, request.getName());
        Member one = memberService.findOne(id); //영속성 컨텍스트를 보존하기 위해서 따로 findOne 메소드를 호출해서 처리해준다. (리턴 값만 오가면 데이터가 변경되는 위험이 있을 수 있다)
        return new UpdateMemberResponse(one.getId(), one.getName());
    }

    /**
     * inner class - 해당 클래스 이외에서 쓸 일이 없을 경우 사용. 일종의 전역변수처럼 사용하므로 static class 로 선언한다.
     */
    @Data
    static class CreateMemberRequest {
        private String name;    //param key 값에 "nameTest" 라고 명시해야한다. 즉, Entity<->DTO 의 변수명을 독립적으로 갖는다.
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

}

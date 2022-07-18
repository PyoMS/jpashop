package jpabook.jpashop.controller;

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

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "/members/createMemberForm";

    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult result) {    //MemberForm의 필수값을
        /**
         * @Valid 데이터가 존재하지 않으면 BindingResult 에 데이터가 하나 들어온다.
         * 에러가 있으면 다시 "members/createMemberForm"; 로 리턴해주는데
         * BindingResult 파라미터도 같이 리턴해준다.
         * 그리고 해당 파라미터는 Thymleaf에서 처리해준다.
         * */
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }


        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        memberForm.setName(memberForm.getName());
        member.setAddress(address);
        memberService.join(member);
        return "redirect:/";
    }


}

package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //readOnly 설정을 해주면 단순 find 시 영속성컨텍스트의 속도를 최적화할 수 있다.
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    /**
     * 회원 가입
     * 쓰기 동작이므로
     * @Transactional(readOnly = true) 일 경우 동작 x
     * */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        /**
         * 시나리오 1 ) join 메소드에 memberA 라는 파라미터로 2명 이상의 유저가 등록할 경우 => 문제 발생할 수 있음.
         * */
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findMember(Long memberId){
        return memberRepository.findOne(memberId);
    }

}

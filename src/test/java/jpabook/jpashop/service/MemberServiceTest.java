package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)    //Junit 실행시 Spring 이랑 같이 실행.
@SpringBootTest     //SpringBoot를 띄운 상태에서 테스트 -> 이거 없으면 Autowired 실패함.
@Transactional      //Transactional을 걸어 놓은 상태에서 테스트 하고 종료 후 기본적으로 RollBack함
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

//    @Autowired
//    EntityManager em;

    /**
     * 기본적으로 테스트 환경에서의 트랜젝셔널은 수행 후 롤백 처리한다 -> 따라서 DB에 insert가 반영되지 않는다.
     * 더 정확히는 EntityManager에서 flush를 처리하지 않는다. (단, flush 한다고 해서 DB 자체에 commit 이 되는것은 아니다.
     */
    @Test
    @Rollback(value = false)
    public void signUp() throws Exception{
        //given
        Member member = new Member();
        member.setName("kin");
        //when
        Long saveID = memberService.join(member);
//        em.flush();   //테스트 console에는 insert 문 log 찍힘.
        //then
        assertEquals(member, memberRepository.findOne(saveID));
//        assertEquals(saveID, (Long)13L); // assertEquals test
    }


    @Test(expected = IllegalStateException.class)   //해당 예외사항이 발생되면 알아서 catch 후 return;
    public void duplicate_signUp () throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kin");

        Member member2 = new Member();
        member2.setName("kin");
        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다.");   //테스트 케이스 자체를 잘못 기재할 수 있기때문에 해당 쿼리를 만나면 일부러 발생시킨 오류가 발생되지 않았다는 의미.
    }
}
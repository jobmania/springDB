package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * 예외 누수문제 해결
 * SQLException 제거
 * MemberRepoisitory 인터페이스 의존
 */

@Slf4j
public class MemberServiceV4 {
    private final MemberRepository memberRepository;


    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional  // 있을시 스프링이 프록시를 만들어서 처리해준다!@
    public void accountTransfer(String fromId, String toId, int money)  {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money)  {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);


        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        validation(fromMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 에외 발생");
        }
    }


}

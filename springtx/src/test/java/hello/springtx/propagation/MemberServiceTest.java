package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;


    /**
    * memberServcie  @Transactional : off
     * memberRepository   @Transactional : on
     * logRepository  @Transactional : ON
    * */
    @Test
    void outerTxOff_success(){
        // given
        String username = "outerTxOff_success";

        // when
        memberService.joinV1(username);


        // then : 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }



    /**
     * memberServcie  @Transactional : off
     * memberRepository   @Transactional : on
     * logRepository  @Transactional : ON Exception
     * */
    @Test
    void outerTxOff_fail(){
        // given
        String username = "로그예외_outerTxOff_success";

        // when
        org.assertj.core.api.Assertions.assertThatThrownBy(() ->  memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);


        // then : 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());

    }


    /**
     * memberServcie  @Transactional : ON
     * memberRepository   @Transactional : off
     * logRepository  @Transactional : off
     * */
    @Test
    void singleTx(){
        // given
        String username = "outerTxOff_success";

        // when
        memberService.joinV1(username);


        // then : 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());

    }
}
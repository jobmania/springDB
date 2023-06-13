package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션, 파라미터 연동, 풀을  고려한 종료!!
 * */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {
    private final DataSource dataSource; // 데이터소스에서 커넥션을 들고 와야되기때문~
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // START!
        Connection con = dataSource.getConnection();

        try {
            con.setAutoCommit(false); // 트랜잭션 시작!
            // 비즈니스로직
            bizLogic(con, fromId, toId, money);
            // 커밋 , OR 롤백
            con.commit();  // 성공시 커밋!
        }catch (Exception e){
            con.rollback(); // 실패!
            throw new IllegalStateException(e);
        }finally {
            release(con);

        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(con, fromId);
        Member toMember = memberRepository.findById(con, toId);


        memberRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        validation(fromMember);
        memberRepository.update(con, toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체 중 에외 발생");
        }
    }

    private static void release(Connection con) {
        if(con !=null){
            try {
                con.setAutoCommit(true); // 커넥션 풀 고려
                con.close();
            }catch (Exception e){
                log.info("error",e);
            }

        }
    }
}

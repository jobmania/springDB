package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;


/**
 * JDBC - DriverManager 사용
 * */
@Slf4j
public class MemberRepositoryV0 {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error",e);
            throw e;
        } finally {
            //  pstmt.close();  // Exception 발생시 아래 코드 호출안됨ㅇㅇ.
            // 항상 반납하도록 finally에 수행
            close(con, pstmt, null);
        }

    }

    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ? ";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            rs = pstmt.executeQuery();
            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }else {
                throw new NoSuchElementException("member not found memberId=" + memberId); // 세부적인 예외 메세지를 적자!
            }

        } catch (SQLException e) {
            log.error("db error",e);
            throw e;
        }finally {
            close(con,pstmt,rs);
        }

    }



    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id = ?";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1,money);
            pstmt.setString(2,memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize={}",resultSize);

        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }

    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id =? ";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {

            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,memberId);
            int resultSize = pstmt.executeUpdate();

        }catch (SQLException e){
            log.error("db error",e);
            throw e;
        }finally {
            close(con,pstmt,null);
        }
    }





    private void close(Connection con, Statement stmt, ResultSet rs){
        // 사용한 자원들 반납하논 로직 !
        // 안 닫으면 외부 리소스를 쓰는 것이기 때문에 자원 낭비임~

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error",e);
            }

        }


        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error",e);
            }

        }

        if(con!= null){
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error",e);
            }

        }

    }
    private  Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

}

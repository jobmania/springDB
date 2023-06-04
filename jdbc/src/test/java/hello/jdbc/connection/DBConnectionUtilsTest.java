package hello.jdbc.connection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class DBConnectionUtilsTest {

    @Test
    void connection(){
        Connection connection = DBConnectionUtil.getConnection(); // h2라이브러리 하위 h2 connection 구현체
        Assertions.assertThat(connection).isNotNull();
    }
}

package rangiffler.data.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rangiffler.data.entity.UserAuthEntity;
import rangiffler.data.jdbc.DataSourceContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static rangiffler.data.DataBase.AUTH;

public class PostgresJdbcUserAuthDAO implements UserAuthDao {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresJdbcUserAuthDAO.class);
    private final DataSource ds = DataSourceContext.INSTANCE.getDatatSource(AUTH);


    @Override
    public UserAuthEntity getUser(String username) {
        UserAuthEntity userAuthEntity = new UserAuthEntity();
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("SELECT * FROM users WHERE username = '%s';", username);
            ResultSet resultSet = st.executeQuery(sql);
            if (resultSet.next()) {
                userAuthEntity.setId(UUID.fromString(resultSet.getString("id")))
                        .setUsername(resultSet.getString("username"));
                return userAuthEntity;
            } else {
                return null;
            }

        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }

}

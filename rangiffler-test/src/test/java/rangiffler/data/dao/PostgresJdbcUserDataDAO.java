package rangiffler.data.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rangiffler.data.entity.UserDataEntity;
import rangiffler.data.jdbc.DataSourceContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static rangiffler.data.DataBase.USERDATA;

public class PostgresJdbcUserDataDAO implements UserDataDao {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresJdbcUserDataDAO.class);
    private final DataSource ds = DataSourceContext.INSTANCE.getDatatSource(USERDATA);


    @Override
    public UserDataEntity getUser(String username) {
        UserDataEntity userDataEntity = new UserDataEntity();
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("SELECT * FROM users WHERE username = '%s';", username);
            ResultSet resultSet = st.executeQuery(sql);
            if (resultSet.next()) {
                userDataEntity.setId(UUID.fromString(resultSet.getString("id")))
                        .setUsername(resultSet.getString("username"))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setAvatar(resultSet.getBytes("avatar"));
                return userDataEntity;
            } else {
                String msg = "Can`t find user : " + username;
                LOG.error(msg);
                throw new RuntimeException(msg);
            }

        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(String username) {
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("DELETE FROM users WHERE username = '%s';", username);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }
}

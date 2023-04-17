package rangiffler.data.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rangiffler.data.entity.PhotoEntity;
import rangiffler.data.jdbc.DataSourceContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static rangiffler.data.DataBase.USERDATA;

public class PostgresJdbcPhotoDAO implements PhotoDao {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresJdbcPhotoDAO.class);
    private final DataSource ds = DataSourceContext.INSTANCE.getDatatSource(USERDATA);


    @Override
    public List<PhotoEntity> getUsersPhoto(String username) {
        List<PhotoEntity> photoEntities = new ArrayList<>();

        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("SELECT * FROM photos WHERE username = '%s';", username);
            ResultSet resultSet = st.executeQuery(sql);
            while (resultSet.next()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(UUID.fromString(resultSet.getString("id")))
                        .setUsername(resultSet.getString("username"))
                        .setDescription(resultSet.getString("description"))
                        .setCountriesId(UUID.fromString(resultSet.getString("countries_id")))
                        .setPhoto(resultSet.getBytes("photo"))
                        .setCode(resultSet.getString("code"))
                        .setName(resultSet.getString("name"));
                photoEntities.add(photoEntity);
            }
            return photoEntities;
        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePhoto(UUID photoUuid) {
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("DELETE FROM photos WHERE id = '%s';", photoUuid);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUserPhoto(String username) {
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("DELETE FROM photos WHERE username = '%s';", username);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }
}

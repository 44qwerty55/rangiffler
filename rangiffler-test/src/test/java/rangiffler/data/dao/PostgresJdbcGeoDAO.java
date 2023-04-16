package rangiffler.data.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rangiffler.data.entity.CountriesEntity;
import rangiffler.data.jdbc.DataSourceContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static rangiffler.data.DataBase.GEO;

public class PostgresJdbcGeoDAO implements CountriesDao {

    private static final Logger LOG = LoggerFactory.getLogger(PostgresJdbcGeoDAO.class);
    private final DataSource ds = DataSourceContext.INSTANCE.getDatatSource(GEO);


    @Override
    public CountriesEntity getCountryByCode(String code) {
        CountriesEntity countriesEntity = new CountriesEntity();
        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {
            String sql = String.format("SELECT * FROM countries WHERE code = '%s';", code);
            ResultSet resultSet = st.executeQuery(sql);
            if (resultSet.next()) {
                countriesEntity.setId(UUID.fromString(resultSet.getString("id")))
                        .setCode(resultSet.getString("code"))
                        .setName(resultSet.getString("name"));
                return countriesEntity;
            } else {
                String msg = "Can`t find user country: " + code;
                LOG.error(msg);
                throw new RuntimeException(msg);
            }

        } catch (SQLException e) {
            LOG.error("Error while database operation", e);
            throw new RuntimeException(e);
        }
    }
}

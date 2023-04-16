package rangiffler.data.dao;

import rangiffler.data.entity.CountriesEntity;

public interface CountriesDao extends DAO {

    CountriesEntity getCountryByCode(String code);
}

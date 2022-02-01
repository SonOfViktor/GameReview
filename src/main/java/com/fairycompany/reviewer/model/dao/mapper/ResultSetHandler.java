package com.fairycompany.reviewer.model.dao.mapper;

import com.fairycompany.reviewer.model.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The interface provide methods to control data in result set.
 *
 * @param <T> some {@link Entity}
 */
public interface ResultSetHandler<T extends Entity> {

    /**
     * Create entity with data that result set keeps.
     *
     * @param resultSet the result set
     * @return object that extends entity
     * @throws SQLException if error occurred when data extracted from result set
     */
    T resultSetToObject(ResultSet resultSet) throws SQLException;
}

package com.fairycompany.reviewer.model.dao.mapper;

import com.fairycompany.reviewer.model.entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler<T extends Entity> {

    T resultSetToObject(ResultSet resultSet) throws SQLException;
}

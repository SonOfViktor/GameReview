package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.Entity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fairycompany.reviewer.exception.DaoException;

import java.sql.*;
import java.util.*;

public class JdbcTemplate<T extends Entity> {
    private static Logger logger = LogManager.getLogger();
    private static final String COUNT_LINES_PARAMETER = "line";
    private static final String GENERATED_KEY = "GENERATED_KEY";
    private ResultSetHandler<T> resultSetHandler;
    private TransactionManager transactionManager;

    public JdbcTemplate(ResultSetHandler<T> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
        this.transactionManager = TransactionManager.getInstance();
    }

    public List<T> executeSelectQuery(String sqlQuery, Object... parameters) throws DaoException {
        List<T> list = new ArrayList<>();
        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParametersInPreparedStatement(statement, parameters);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                T entity = resultSetHandler.resultSetToObject(resultSet);
                list.add(entity);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding all elements. {}", e.getMessage());
            throw new DaoException(e);
        }

        return list;
    }

    public Optional<T> executeSelectQueryForObject(String sqlQuery, Object... parameters) throws DaoException {
        Optional<T> result = Optional.empty();
        List<T> list = executeSelectQuery(sqlQuery, parameters);

        if (!list.isEmpty()) {
            result = Optional.of(list.get(0));
        } else {
            logger.log(Level.WARN, "Element isn't found");
        }

        return result;
    }

//    public Map<String, List<Object>> executeSelectQueryFromTables(String sqlQuery, Set<String> columnNames,
//                                                                  Object... parameters) throws DaoException {
//        Map<String, List<Object>> map = new HashMap<>();              todo delete :(
//
//        Connection connection = transactionManager.getConnection();
//        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                ResultSet.CONCUR_READ_ONLY)) {
//            setParametersInPreparedStatement(statement, parameters);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            for (String column: columnNames) {
//                map.put(column, new ArrayList<>());
//                while (resultSet.next()) {
//                    map.get(column).add(resultSet.getObject(column));
//                }
//                resultSet.beforeFirst();
//            }
//
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Database error. Elements wasn't extracted. {}", e.getMessage());
//            throw new DaoException("Database error. Elements wasn't extracted.", e);
//        }
//
//        return map;
//    }

    public List<Map<String, Object>> executeSelectForList(String sql, Set<String> columnNames, Object... parameters)
            throws DaoException {
        Connection connection = transactionManager.getConnection();
        List<Map<String, Object>> extractedValues = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParametersInPreparedStatement(statement, parameters);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> rowValues = new HashMap<>();
                T entity = resultSetHandler.resultSetToObject(resultSet);
                String gameKey = entity.getClass().getSimpleName().toLowerCase();
                rowValues.put(gameKey, entity);
                for (String name : columnNames) {
                    rowValues.put(name, resultSet.getObject(name));
                }
                extractedValues.add(rowValues);
            }

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database error. Elements wasn't extracted. {}", e.getMessage());
            throw new DaoException("Database error. Elements wasn't extracted.", e);
        }
        return extractedValues;
    }

    public List<Map<String, Object>> executeSelectSomeFields(String sql, Set<String> columnNames, Object... parameters)
            throws DaoException {
        List<Map<String, Object>> extractedValues = new ArrayList<>();

        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            setParametersInPreparedStatement(statement, parameters);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> rowValues = new HashMap<>();
                for (String name : columnNames) {
                    rowValues.put(name, resultSet.getObject(name));
                }
                extractedValues.add(rowValues);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Database error. Elements wasn't extracted. {}", e.getMessage());
            throw new DaoException("Database error. Elements wasn't extracted.", e);
        }
        return extractedValues;
    }

        public boolean executeUpdateDeleteFields(String sqlQuery, Object... parameters) throws DaoException {
        Connection connection = transactionManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParametersInPreparedStatement(statement, parameters);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Element isn't updated or deleted. {}", e.getMessage());
            throw new DaoException(e);
        }

        return true;
    }

    public long executeInsertQuery(String sqlQuery, Object... parameters) throws DaoException {
        long generatedId = 0;
        Connection connection = transactionManager.getConnection();

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            setParametersInPreparedStatement(statement, parameters);
            statement.executeUpdate();

            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                generatedId = resultSet.getLong(GENERATED_KEY);
                logger.log(Level.DEBUG, "Generated id is {}", generatedId);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when insert element. {}", e.getMessage());
            throw new DaoException(e);
        }

        return generatedId;
    }

    public Number executeSelectCalculation(String sqlQuery, String columnName, Object... parameters) throws DaoException {
        Number totalValue = null;

        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParametersInPreparedStatement(statement, parameters);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalValue = (Number) resultSet.getObject(columnName);          // todo новый instance of
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding value. {}", e.getMessage());
            throw new DaoException("Error when finding value.", e);
        }

        return totalValue;
    }

    public void executeBatch(String sql, List<Object[]> batchArguments) throws DaoException {
        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Object[] sqlArgument : batchArguments) {
                setParametersInPreparedStatement(statement, sqlArgument);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to insert batch data. A database access error occurs", e);
            throw new DaoException("Failed to insert batch data. A database access error occurs", e);
        }
    }

    private void setParametersInPreparedStatement(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }
}

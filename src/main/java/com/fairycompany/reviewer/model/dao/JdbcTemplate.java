package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.Entity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fairycompany.reviewer.exception.DaoException;

import java.sql.*;
import java.util.*;

/**
 * Class provides basic operations with SQL query
 *
 * @param <T> entity type
 */
public class JdbcTemplate<T extends Entity> {
    private static Logger logger = LogManager.getLogger();
    private static final String GENERATED_KEY = "GENERATED_KEY";
    private ResultSetHandler<T> resultSetHandler;
    private TransactionManager transactionManager;

    /**
     * Instantiates a new Jdbc template.
     *
     * @param resultSetHandler object that process data of result set
     */
    public JdbcTemplate(ResultSetHandler<T> resultSetHandler) {
        this.resultSetHandler = resultSetHandler;
        this.transactionManager = TransactionManager.getInstance();
    }

    /**
     * Select list of entities from database.
     *
     * @param sqlQuery   sql query
     * @param parameters parameters
     * @return list of entities
     * @throws DaoException if SQL exception occurred
     */
    public List<T> selectEntities(String sqlQuery, Object... parameters) throws DaoException {
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

    /**
     * Select entity from database.
     *
     * @param sqlQuery   the sql query
     * @param parameters the parameters
     * @return optional with entity from database or empty optional if specified entity wasn't found
     * @throws DaoException if SQL exception occurred
     */
    public Optional<T> selectEntity(String sqlQuery, Object... parameters) throws DaoException {
        Optional<T> result = Optional.empty();
        List<T> list = selectEntities(sqlQuery, parameters);

        if (!list.isEmpty()) {
            result = Optional.of(list.get(0));
        } else {
            logger.log(Level.WARN, "Element isn't found");
        }

        return result;
    }

    /**
     * Select entities with extra fields from database.
     *
     * @param sqlQuery         the sql
     * @param columnNames the column names
     * @param parameters  the parameters
     * @return list entities with extra data specified with column names
     * @throws DaoException if SQL exception occurred
     */
    public List<Map<String, Object>> selectEntitiesWithExtraFields(String sqlQuery, Set<String> columnNames, Object... parameters)
            throws DaoException {
        Connection connection = transactionManager.getConnection();
        List<Map<String, Object>> extractedValues = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
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

    /**
     * Select some fields from database.
     *
     * @param sqlQuery    the sql query
     * @param columnNames the column names
     * @param parameters  the parameters
     * @return list with data specified with column names
     * @throws DaoException if SQL exception occurred
     */
    public List<Map<String, Object>> selectSomeFields(String sqlQuery, Set<String> columnNames, Object... parameters)
            throws DaoException {
        List<Map<String, Object>> extractedValues = new ArrayList<>();

        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
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

    /**
     * Update delete fields.
     *
     * @param sqlQuery   the sql query
     * @param parameters the parameters
     * @return true if data was updated or deleted
     * @throws DaoException if SQL exception occurred
     */
    public boolean updateDeleteFields(String sqlQuery, Object... parameters) throws DaoException {
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

    /**
     * Insert entity in database table.
     *
     * @param sqlQuery   the sql query
     * @param parameters the parameters
     * @return entity id generated by database
     * @throws DaoException if SQL exception occurred
     */
    public long insertDataInTable(String sqlQuery, Object... parameters) throws DaoException {
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

    /**
     * Select calculation of some fields of database.
     *
     * @param sqlQuery   the sql query
     * @param columnName the column name
     * @param parameters the parameters
     * @return calculation of fields specified with column names
     * @throws DaoException if SQL exception occurred
     */
    public Number selectFieldsCalculation(String sqlQuery, String columnName, Object... parameters) throws DaoException {
        Number totalValue = null;

        Connection connection = transactionManager.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            setParametersInPreparedStatement(statement, parameters);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalValue = (Number) resultSet.getObject(columnName);
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding value. {}", e.getMessage());
            throw new DaoException("Error when finding value.", e);
        }

        return totalValue;
    }

    /**
     * Execute batch command.
     *
     * @param sql            the sql
     * @param batchArguments the batch arguments
     * @throws DaoException if SQL exception occurred
     */
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

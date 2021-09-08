package com.fairycompany.reviewer.model.pool;

import java.sql.Connection;
import java.sql.SQLException;

import static com.fairycompany.reviewer.model.pool.DatabasePropertyReader.*;

public class _MainConnection {
    public static void main(String[] args) throws SQLException {
        Connection connection = ConnectionFactory.createConnection();
        System.out.println(DATABASE_DRIVER);
        System.out.println(DATABASE_USERNAME);
        System.out.println(DATABASE_PASSWORD);
        System.out.println(CONNECTION_POOL_SIZE);
        System.out.println(DATABASE_URL);
    }
}

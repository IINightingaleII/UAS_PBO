/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author hp
 */

import java.sql.*;

public class DatabaseService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/rs_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        }
        return conn;
    }
}



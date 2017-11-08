package com.lemelo.util;

import java.sql.*;

public class FabricaConexao {

    public void createTables() {
        Connection connection = null;
        Statement statement = null;
        try {
             connection = DriverManager.getConnection("jdbc:hsqldb:file:~/local_controle_hsqldb/banco","SA","");
             statement = connection.createStatement();
            statement.executeUpdate("create table if not exists entrada (id integer identity primary key, data_hora varchar(20), descricao varchar(100), valor varchar(30), ultima_edicao varchar(20))");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insert(String sqlInsert) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:~/local_controle_hsqldb/banco","SA","");
            statement = connection.createStatement();
            statement.executeQuery(sqlInsert);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet getResultSet(String sqlSelect) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:~/local_controle_hsqldb/banco","SA","");
            statement = connection.createStatement();
            return statement.executeQuery(sqlSelect);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void delete(String sqlDelete) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:~/local_controle_hsqldb/banco","SA","");
            statement = connection.createStatement();
            statement.executeQuery(sqlDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(String sqlUpdate) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:file:~/local_controle_hsqldb/banco","SA","");
            statement = connection.createStatement();
            statement.executeQuery(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

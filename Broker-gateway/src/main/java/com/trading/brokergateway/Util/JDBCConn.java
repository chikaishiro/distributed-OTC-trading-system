package com.trading.brokergateway.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCConn {
    private String url = "jdbc:mysql://localhost:3306/books?serverTimezone=UTC";
    private String user = "root";
    private String passwd = "1234";
    private Connection conn;

    public JDBCConn()throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(url, user, passwd);

    }

    public Connection getConn(){
        return this.conn;
    }

    public ResultSet getRs(String sql) throws Exception{
        Statement stmt = this.conn.createStatement();
        return stmt.executeQuery(sql);

    }

    public void close() throws Exception {
        this.conn.close();
    }
}

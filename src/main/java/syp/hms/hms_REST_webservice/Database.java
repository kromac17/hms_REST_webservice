package syp.hms.hms_REST_webservice;

import java.sql.*;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:54321/hms_database", "posgres", "hms_secret");
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if(instance != null){
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return  connection.prepareStatement(sql);
    }

    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }
}

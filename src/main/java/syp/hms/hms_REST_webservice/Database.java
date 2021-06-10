package syp.hms.hms_REST_webservice;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.*;

public class Database {
    private static Database instance;
    private Connection connection;

    private Database() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        System.out.println("Des is des Richtige: 5432");
        String ip = "db"; //TODO: change to db when uploaded
        try {
            InetAddress address = InetAddress.getByName(ip);
            System.out.println(address.getHostAddress());
            ip = address.getHostAddress();
            System.out.println(ping(ip));
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception!!: "+e);;
        }
        connection = DriverManager.getConnection("jdbc:postgresql://"+ip+":5432/hms_database", "postgres", "hms_secret");
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if(instance == null){
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

    private static boolean ping(String host) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows? "-n" : "-c", "1", host);
        Process proc = processBuilder.start();

        int returnVal = proc.waitFor();
        return returnVal == 0;
    }
}

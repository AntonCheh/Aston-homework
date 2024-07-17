import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrepareStatement {

    private static final String URL = "jdbc:postgresql://localhost:5432/lessondata";
    private static final String userName = "myuser";
    private static final String password = "mysecretpassword";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(URL, userName, password)) {
            //createTable(connection);
            //insertRecords(connection);
            ResultSet resultSet = retrieveRecords(connection, 2, 1);
            printRecord(resultSet);
        } catch (SQLException e) {
            System.out.println("got SQL exception: " + e.getMessage());
        }
    }

    private static void printRecord(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            // int id = resultSet.getInt("id");
            //int age = resultSet.getInt("age");

            List<String> dataFromTable = new ArrayList<>();
            dataFromTable.add(resultSet.getString("yo"));
            dataFromTable.add(resultSet.getString("tu"));
            dataFromTable.add(resultSet.getString("el_ella"));
            dataFromTable.add(resultSet.getString("vosotros"));
            dataFromTable.add(resultSet.getString("nosotros"));
            dataFromTable.add(resultSet.getString("ellos"));

            for (String pronouns : dataFromTable) {
                System.out.println(pronouns);
            }
        }
    }

    private static ResultSet retrieveRecords(Connection connection, int limit, int offset) throws SQLException {
        String retrieveStudentsSQL = "SELECT * FROM spanish_words LIMIT ? OFFSET ?";
        PreparedStatement preparedStatement = connection.prepareStatement(retrieveStudentsSQL);
        preparedStatement.setInt(1, limit);
        preparedStatement.setInt(2, offset);
        return preparedStatement.executeQuery();
    }

    private static void insertRecords(Connection connection) throws SQLException {
        String insertDataSQL = "INSERT INTO spanish_words (yo, tu, el_ella, vosotros, nosotros, ellos) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL);
        preparedStatement.setString(1, "Tomo");
        preparedStatement.setString(2, "Tomas");
        preparedStatement.setString(3, "Toma");
        preparedStatement.setString(4, "Tomais");
        preparedStatement.setString(5, "Tomamos");
        preparedStatement.setString(6, "Toman");
        preparedStatement.executeUpdate();
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS spanish_words (" +
                "id SERIAL PRIMARY KEY," +
                "yo VARCHAR(255)," +
                "tu VARCHAR(255)," +
                "el_ella VARCHAR(255)," +
                "vosotros VARCHAR(255)," +
                "nosotros VARCHAR(255)," +
                "ellos VARCHAR(255))";
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);
    }
}

/*

public static void main(String[] args) {


private static void printRecord(ResultSet resultSet) throws SQLException {
    while (resultSet.next()) {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        System.out.println("ID: " + id + " , Name: " + name + " , age: " + age);
    }
}

private static ResultSet retrieveRecords(Connection connection) throws SQLException {
    String retrieveStudentsSQL = "SELECT * FROM students";
    PreparedStatement preparedStatement = connection.prepareStatement(retrieveStudentsSQL);
    return preparedStatement.executeQuery();
}

private static void insertRecords(Connection connection) throws SQLException {
    String insertDataSQL = "INSERT INTO students (name, age) VALUES (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(insertDataSQL)) {
        preparedStatement.setString(1, "Ivan");
        preparedStatement.setInt(2, 20);
        preparedStatement.executeUpdate();
    }
}

private static void createTable(Connection connection) throws SQLException {
    String createTableSQL = "CREATE TABLE IF NOT EXISTS students (" +
            "id SERIAL PRIMARY KEY," +
            "name VARCHAR(255)," +
            "age INT)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(createTableSQL)) {
        preparedStatement.execute();
    }
}
}

 */






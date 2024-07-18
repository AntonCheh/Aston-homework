package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class SpanishWordsDAO {
    private DataSource dataSource;

    public SpanishWordsDAO() throws NamingException {
        // Настройка DataSource напрямую в коде
        org.postgresql.ds.PGSimpleDataSource ds = new org.postgresql.ds.PGSimpleDataSource();
        ds.setUrl("jdbc:postgresql://localhost:5432/lessondata");
        ds.setUser("myuser");
        ds.setPassword("mysecretpassword");
        this.dataSource = ds;
    }

    public List<String> retrieveRecords(int limit, int offset) throws SQLException {
        List<String> dataFromTable = new ArrayList<>();
        String retrieveStudentsSQL = "SELECT * FROM spanish_words LIMIT ? OFFSET ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(retrieveStudentsSQL)) {

            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                dataFromTable.add(resultSet.getString("yo"));
                dataFromTable.add(resultSet.getString("tu"));
                dataFromTable.add(resultSet.getString("el_ella"));
                dataFromTable.add(resultSet.getString("vosotros"));
                dataFromTable.add(resultSet.getString("nosotros"));
                dataFromTable.add(resultSet.getString("ellos"));
            }
        }
        return dataFromTable;
    }

    public void createRecord(String yo, String tu, String el_ella, String vosotros, String nosotros, String ellos) throws SQLException {
        String insertSQL = "INSERT INTO spanish_words (yo, tu, el_ella, vosotros, nosotros, ellos) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, yo);
            preparedStatement.setString(2, tu);
            preparedStatement.setString(3, el_ella);
            preparedStatement.setString(4, vosotros);
            preparedStatement.setString(5, nosotros);
            preparedStatement.setString(6, ellos);
            preparedStatement.executeUpdate();
        }
    }

    public void updateRecord(int id, String yo, String tu, String el_ella, String vosotros, String nosotros, String ellos) throws SQLException {
        String updateSQL = "UPDATE spanish_words SET yo = ?, tu = ?, el_ella = ?, vosotros = ?, nosotros = ?, ellos = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, yo);
            preparedStatement.setString(2, tu);
            preparedStatement.setString(3, el_ella);
            preparedStatement.setString(4, vosotros);
            preparedStatement.setString(5, nosotros);
            preparedStatement.setString(6, ellos);
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRecord(int id) throws SQLException {
        String deleteSQL = "DELETE FROM spanish_words WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public void testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRecordById(int id) throws SQLException {
        List<String> dataFromTable = new ArrayList<>();
        String sql = "SELECT * FROM spanish_words WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                dataFromTable.add(resultSet.getString("yo"));
                dataFromTable.add(resultSet.getString("tu"));
                dataFromTable.add(resultSet.getString("el_ella"));
                dataFromTable.add(resultSet.getString("vosotros"));
                dataFromTable.add(resultSet.getString("nosotros"));
                dataFromTable.add(resultSet.getString("ellos"));
            }
        }
        return dataFromTable;
    }
}
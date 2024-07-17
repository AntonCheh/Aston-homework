package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SpanishWordsDAO {
    private DataSource dataSource;

    public SpanishWordsDAO() throws NamingException {
        Context initialContext = new InitialContext();
        Context envContext = (Context) initialContext.lookup("java:/comp/env");
        dataSource = (DataSource) envContext.lookup("jdbc/PostgresDB");
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

}
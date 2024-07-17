package jdbc;

import java.sql.*;

public class CallableStatementExample {
    private static final String URL = "jdbc:postgresql://localhost:5432/lessondata";
    private static final String userName = "myuser";
    private static final String password = "mysecretpassword";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(URL, userName, password)) {
            String createFunctionSQL = "CREATE OR REPLACE FUNCTION calculate_sum (a integer, b integer)" +
                    "RETURNS integer AS $$ " +
                    "BEGIN " +
                    " RETURN a + b;" +
                    "END; " +
                    "$$ LANGUAGE plpgsql";
            Statement statement = connection.createStatement();
            statement.executeUpdate(createFunctionSQL);
            System.out.println("Procedure is created");
            String functionCall = " { ? = call calculate_sum(?, ?)}";
            int a = 10;
            int b = 20;
            CallableStatement callableStatement = connection.prepareCall(functionCall);
            callableStatement.setInt(2, a);
            callableStatement.setInt(3, b);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.execute();
            int sum = callableStatement.getInt(1);
            System.out.println("sum of " + a + " and " + b + " is equal to " + sum);
            callableStatement.close();

        } catch (SQLException e) {
            System.out.println("got SQL exception: " + e.getMessage());
        }
    }
}

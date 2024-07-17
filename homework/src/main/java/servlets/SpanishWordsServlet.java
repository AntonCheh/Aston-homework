package servlets;

import dao.SpanishWordsDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/spanishwords")
public class SpanishWordsServlet extends HttpServlet {
    private SpanishWordsDAO spanishWordsDAO;

    @Override
    public void init() throws ServletException {
        try {
            spanishWordsDAO = new SpanishWordsDAO();
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<String> spanishWordsList = spanishWordsDAO.retrieveRecords(10, 0); // example values for limit and offset
            request.setAttribute("spanishWordsList", spanishWordsList);
            request.getRequestDispatcher("/spanishWords.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

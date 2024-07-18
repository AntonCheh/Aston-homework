package servlets;

import com.google.gson.Gson;
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
            spanishWordsDAO.testConnection(); // Вызов метода для тестирования соединения
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Получаем параметры из запроса
            String limitParam = request.getParameter("limit");
            String offsetParam = request.getParameter("offset");
            String idParam = request.getParameter("id");

            int limit = (limitParam != null) ? Integer.parseInt(limitParam) : 10;
            int offset = (offsetParam != null) ? Integer.parseInt(offsetParam) : 0;

            List<String> spanishWordsList;

            if (idParam != null) {
                // Получаем одну запись по id
                int id = Integer.parseInt(idParam);
                spanishWordsList = spanishWordsDAO.getRecordById(id);
            } else {
                // Получаем записи с использованием limit и offset
                spanishWordsList = spanishWordsDAO.retrieveRecords(limit, offset);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(spanishWordsList);
            response.getWriter().write(json);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            switch (action) {
                case "create":
                    createRecord(request, response);
                    break;
                case "update":
                    updateRecord(request, response);
                    break;
                case "delete":
                    deleteRecord(request, response);
                    break;
                default:
                    doGet(request, response);
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void createRecord(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String yo = request.getParameter("yo");
        String tu = request.getParameter("tu");
        String el_ella = request.getParameter("el_ella");
        String vosotros = request.getParameter("vosotros");
        String nosotros = request.getParameter("nosotros");
        String ellos = request.getParameter("ellos");
        spanishWordsDAO.createRecord(yo, tu, el_ella, vosotros, nosotros, ellos);
        response.sendRedirect("spanishwords");
    }

    private void updateRecord(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String yo = request.getParameter("yo");
        String tu = request.getParameter("tu");
        String el_ella = request.getParameter("el_ella");
        String vosotros = request.getParameter("vosotros");
        String nosotros = request.getParameter("nosotros");
        String ellos = request.getParameter("ellos");
        spanishWordsDAO.updateRecord(id, yo, tu, el_ella, vosotros, nosotros, ellos);
        response.sendRedirect("spanishwords");
    }

    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        spanishWordsDAO.deleteRecord(id);
        response.sendRedirect("spanishwords");
    }
}
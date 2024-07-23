package servlets;

import com.google.gson.Gson;
import dao.CorrectConjugationDAO;
import dao.VerbDAO;
import insert.CorrectFormInsert;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CorrectConjugation;
import model.Verb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/correct-conjugations")
public class CorrectConjugationServlet extends HttpServlet {

    @Autowired
    private CorrectConjugationDAO correctConjugationDAO;

    @Autowired
    private VerbDAO verbDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        Set<Map<String, String>> conjugations = CorrectFormInsert.inputConjugations();
        CorrectFormInsert.batchInsertConjugations(conjugations);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yo = request.getParameter("yo");
        String tu = request.getParameter("tu");
        String el_ella = request.getParameter("el_ella");
        String vosotros = request.getParameter("vosotros");
        String nosotros = request.getParameter("nosotros");
        String ellos = request.getParameter("ellos");
        String verbId = request.getParameter("verbId");

        try {
            Verb verb = verbDAO.getVerbById(Integer.parseInt(verbId));
            if (verb == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid verb ID");
                return;
            }

            CorrectConjugation correctConjugation = new CorrectConjugation();
            correctConjugation.setYo(yo);
            correctConjugation.setTu(tu);
            correctConjugation.setEl_ella(el_ella);
            correctConjugation.setVosotros(vosotros);
            correctConjugation.setNosotros(nosotros);
            correctConjugation.setEllos(ellos);
            // correctConjugation.setVerb(verb);

            correctConjugationDAO.saveCorrectConjugation(correctConjugation);

            response.getWriter().write("Correct Conjugation created");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error creating correct conjugation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");

        try {
            if (idParam == null) {
                // Получение всех значений
                List<CorrectConjugation> conjugations = correctConjugationDAO.getAllCorrectConjugations();
                String json = new Gson().toJson(conjugations);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } else {
                // Получение значения по ID
                int id = Integer.parseInt(idParam);
                CorrectConjugation correctConjugation = correctConjugationDAO.getCorrectConjugationById(id);
                if (correctConjugation == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Correct Conjugation not found for id: " + id);
                    return;
                }
                String json = new Gson().toJson(correctConjugation);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid 'id' parameter: " + idParam);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}

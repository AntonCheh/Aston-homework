package servlets;


import dao.CorrectConjugationDAO;
import dao.VerbDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.CorrectConjugation;
import model.Verb;

import java.io.IOException;

@WebServlet("/correct-conjugations")
public class CorrectConjugationServlet extends HttpServlet {
    private final CorrectConjugationDAO correctConjugationDAO = new CorrectConjugationDAO();
    private final VerbDAO verbDAO = new VerbDAO();

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
            correctConjugation.setVerb(verb);

            correctConjugationDAO.saveCorrectConjugation(correctConjugation);

            response.getWriter().write("Correct Conjugation created");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error creating correct conjugation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

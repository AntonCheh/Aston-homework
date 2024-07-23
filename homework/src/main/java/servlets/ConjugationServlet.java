package servlets;

import com.google.gson.Gson;
import dao.ConjugationDAO;
import dao.CorrectConjugationDAO;
import dto.ConjugationDTO;
import insert.CorrectFormInsert;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Conjugation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/conjugations")
public class ConjugationServlet extends HttpServlet {

    @Autowired
    private ConjugationDAO conjugationDAO;

    @Autowired
    private CorrectConjugationDAO correctConjugationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        Set<Map<String, String>> conjugations = CorrectFormInsert.inputConjugations();
        CorrectFormInsert.batchInsertConjugations(conjugations);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String conjugationIdParam = request.getParameter("conjugationId");

        if (conjugationIdParam == null || conjugationIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parameter 'conjugationId' is missing or empty");
            return;
        }

        try {
            int conjugationId = Integer.parseInt(conjugationIdParam);
            Conjugation conjugation = conjugationDAO.getConjugationById(conjugationId);

            if (conjugation == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Conjugation not found for id: " + conjugationId);
                return;
            }

            ConjugationDTO conjugationDTO = new ConjugationDTO();
            conjugationDTO.setId(conjugation.getId());
            conjugationDTO.setYo(conjugation.getYo());
            conjugationDTO.setTu(conjugation.getTu());
            conjugationDTO.setEl_ella(conjugation.getEl_ella());
            conjugationDTO.setVosotros(conjugation.getVosotros());
            conjugationDTO.setNosotros(conjugation.getNosotros());
            conjugationDTO.setEllos(conjugation.getEllos());
            conjugationDTO.setVerbId(conjugation.getVerb().getId());

            String json = new Gson().toJson(conjugationDTO);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid 'conjugationId' parameter: " + conjugationIdParam);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String yo = request.getParameter("yo");
        String tu = request.getParameter("tu");
        String el_ella = request.getParameter("el_ella");
        String vosotros = request.getParameter("vosotros");
        String nosotros = request.getParameter("nosotros");
        String ellos = request.getParameter("ellos");

        try {
            Conjugation conjugation = new Conjugation();
            conjugation.setYo(yo);
            conjugation.setTu(tu);
            conjugation.setEl_ella(el_ella);
            conjugation.setVosotros(vosotros);
            conjugation.setNosotros(nosotros);
            conjugation.setEllos(ellos);

            conjugationDAO.saveConjugation(conjugation);

            response.getWriter().write("Conjugation created");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error creating conjugation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String conjugationIdParam = request.getParameter("id");
            String yo = request.getParameter("yo");
            String tu = request.getParameter("tu");
            String el_ella = request.getParameter("el_ella");
            String vosotros = request.getParameter("vosotros");
            String nosotros = request.getParameter("nosotros");
            String ellos = request.getParameter("ellos");

            if (conjugationIdParam == null || conjugationIdParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Parameter 'id' is missing or empty");
                return;
            }

            int conjugationId = Integer.parseInt(conjugationIdParam);
            Conjugation conjugation = conjugationDAO.getConjugationById(conjugationId);

            if (conjugation == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Conjugation not found for id: " + conjugationId);
                return;
            }

            conjugation.setYo(yo);
            conjugation.setTu(tu);
            conjugation.setEl_ella(el_ella);
            conjugation.setVosotros(vosotros);
            conjugation.setNosotros(nosotros);
            conjugation.setEllos(ellos);

            conjugationDAO.updateConjugation(conjugation);
            response.getWriter().write("Conjugation updated");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String conjugationIdParam = request.getParameter("id");

        if (conjugationIdParam == null || conjugationIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parameter 'id' is missing or empty");
            return;
        }

        try {
            int conjugationId = Integer.parseInt(conjugationIdParam);
            conjugationDAO.deleteConjugation(conjugationId);
            response.getWriter().write("Conjugation deleted");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid 'id' parameter: " + conjugationIdParam);
        }
    }
}

package servlets;

import com.google.gson.Gson;
import dao.ConjugationDAO;
import dao.VerbDAO;
import dto.ConjugationDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Conjugation;
import model.Verb;

import java.io.IOException;
import java.util.List;

@WebServlet("/conjugations")
public class ConjugationServlet extends HttpServlet {
    private final ConjugationDAO conjugationDAO = new ConjugationDAO();
    private final VerbDAO verbDAO = new VerbDAO();

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
        try {
            String yo = request.getParameter("yo");
            String tu = request.getParameter("tu");
            String el_ella = request.getParameter("el_ella");
            String vosotros = request.getParameter("vosotros");
            String nosotros = request.getParameter("nosotros");
            String ellos = request.getParameter("ellos");
            String verbId = request.getParameter("verbId");

            Verb verb = verbDAO.getVerbById(Integer.parseInt(verbId));

            Conjugation conjugation = new Conjugation();
            conjugation.setYo(yo);
            conjugation.setTu(tu);
            conjugation.setEl_ella(el_ella);
            conjugation.setVosotros(vosotros);
            conjugation.setNosotros(nosotros);
            conjugation.setEllos(ellos);
            conjugation.setVerb(verb);

            conjugationDAO.saveConjugation(conjugation);
            response.getWriter().write("Conjugation created");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: " + e.getMessage());
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
            String verbId = request.getParameter("verbId");

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

            Verb verb = verbDAO.getVerbById(Integer.parseInt(verbId));

            conjugation.setYo(yo);
            conjugation.setTu(tu);
            conjugation.setEl_ella(el_ella);
            conjugation.setVosotros(vosotros);
            conjugation.setNosotros(nosotros);
            conjugation.setEllos(ellos);
            conjugation.setVerb(verb);

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
            response.getWriter().write("conjugation deleted");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid 'id' parameter: " + conjugationIdParam);
        }
    }

}

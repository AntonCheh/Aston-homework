package servlets;

import com.google.gson.Gson;
import dao.VerbDAO;
import dto.ConjugationDTO;
import dto.VerbDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Verb;
import org.hibernate.Hibernate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/verbs")
public class VerbServlet extends HttpServlet {
    private VerbDAO verbDAO = new VerbDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String verbIdParam = request.getParameter("verbId");

        if (verbIdParam != null && !verbIdParam.isEmpty()) {
            try {
                int verbId = Integer.parseInt(verbIdParam);
                Verb verb = verbDAO.getVerbById(verbId);

                if (verb == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Verb not found for id: " + verbId);
                    return;
                }

                VerbDTO verbDTO = new VerbDTO();
                verbDTO.setId(verb.getId());
                verbDTO.setInfinitive(verb.getVerb());
                verbDTO.setConjugations(verb.getConjugations().stream().map(conjugation -> {
                    ConjugationDTO dto = new ConjugationDTO();
                    dto.setId(conjugation.getId());
                    dto.setYo(conjugation.getYo());
                    dto.setTu(conjugation.getTu());
                    dto.setEl_ella(conjugation.getEl_ella());
                    dto.setVosotros(conjugation.getVosotros());
                    dto.setNosotros(conjugation.getNosotros());
                    dto.setEllos(conjugation.getEllos());
                    dto.setVerbId(conjugation.getVerb().getId());
                    return dto;
                }).collect(Collectors.toList()));

                String json = new Gson().toJson(verbDTO);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);

            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid 'verbId' parameter: " + verbIdParam);
            }
        } else {
            // Получение всех глаголов
            List<Verb> verbs = verbDAO.getAllVerbs();
            List<VerbDTO> verbDTOs = verbs.stream().map(verb -> {
                VerbDTO dto = new VerbDTO();
                dto.setId(verb.getId());
                dto.setInfinitive(verb.getVerb());
                dto.setConjugations(verb.getConjugations().stream().map(conjugation -> {
                    ConjugationDTO conjugationDTO = new ConjugationDTO();
                    conjugationDTO.setId(conjugation.getId());
                    conjugationDTO.setYo(conjugation.getYo());
                    conjugationDTO.setTu(conjugation.getTu());
                    conjugationDTO.setEl_ella(conjugation.getEl_ella());
                    conjugationDTO.setVosotros(conjugation.getVosotros());
                    conjugationDTO.setNosotros(conjugation.getNosotros());
                    conjugationDTO.setEllos(conjugation.getEllos());
                    conjugationDTO.setVerbId(conjugation.getVerb().getId());
                    return conjugationDTO;
                }).collect(Collectors.toList()));
                return dto;
            }).collect(Collectors.toList());

            String json = new Gson().toJson(verbDTOs);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String infinitive = request.getParameter("infinitive");

        if (verbDAO.existsByVerb(infinitive)) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("Verb already exists");
        } else {
            Verb verb = new Verb();
            verb.setVerb(infinitive);
            verbDAO.saveVerb(verb);
            response.getWriter().write("Verb created");
        }
    }



    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String verbIdParam = request.getParameter("id");
        String infinitive = request.getParameter("infinitive");

        if (verbIdParam == null || verbIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parameter 'id' is missing or empty");
            return;
        }

        try {
            int verbId = Integer.parseInt(verbIdParam);
            Verb verb = verbDAO.getVerbById(verbId);

            if (verb == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Verb not found for id: " + verbId);
                return;
            }

            verb.setVerb(infinitive);
            verbDAO.updateVerb(verb);

            response.getWriter().write("Verb updated");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid 'id' parameter: " + verbIdParam);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String verbIdParam = request.getParameter("id");

        if (verbIdParam == null || verbIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Parameter 'id' is missing or empty");
            return;
        }

        try {
            int verbId = Integer.parseInt(verbIdParam);
            verbDAO.deleteVerb(verbId);
            response.getWriter().write("Verb deleted");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid 'id' parameter: " + verbIdParam);
        }
    }


}

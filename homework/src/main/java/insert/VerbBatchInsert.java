package insert;

import lombok.var;
import model.Verb;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class VerbBatchInsert {

    private final SessionFactory sessionFactory;

    @Autowired
    public VerbBatchInsert(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void batchInsertVerbs(Set<String> verbs) {
        try (var session = sessionFactory.getCurrentSession()) {
            int count = 0;
            for (String verb : verbs) {
                Verb newVerb = new Verb();
                newVerb.setVerbo(verb);
                session.save(newVerb);
                count++;
                if (count % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.flush();
            session.clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to trigger rollback
        }
    }

    public static Set<String> inputVerb() {
        String[] listOfCorrectVerbs = {
                "comer", "vivir", "trabajar", "estudiar", "bailar", "cantar", "cocinar", "limpiar", "viajar", "enseñar",
                "aprender", "escuchar", "mirar", "correr", "beber", "decidir", "abrir", "escribir", "recibir", "nadar",
                "caminar", "amar", "arreglar", "ayudar", "buscar", "cambiar", "contestar", "desayunar", "descansar",
                "desear", "dibujar", "enseñar", "entrar", "escuchar", "esperar", "estudiar", "explicar", "ganar", "hablar",
                "invitar", "lavar", "llegar", "llevar", "mirar", "necesitar", "pagar", "preguntar", "preparar", "regresar",
                "terminar", "tomar", "usar", "visitar", "vender", "leer", "comprender", "deber", "prometer", "temer",
                "aprender", "meter", "responder", "insistir", "asistir", "permitir", "compartir", "existir", "partir", "sufrir"
        };

        Set<String> input = new TreeSet<>();
        Collections.addAll(input, listOfCorrectVerbs);
        return input;
    }

    public int size() {
        return inputVerb().size();
    }
}

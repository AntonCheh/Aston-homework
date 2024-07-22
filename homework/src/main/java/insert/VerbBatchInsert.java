package insert;

import jakarta.servlet.annotation.ServletSecurity;
import lombok.Getter;
import lombok.Setter;
import model.Verb;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.*;


public class VerbBatchInsert {


    public static void batchInsertVerbs(Set<String> verbs) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int count = 0;
            for (String verb : verbs) {
                Verb newVerb = new Verb();
                newVerb.setVerbo(verb);
                session.save(newVerb);
                count++;
                // Пакетная вставка каждые 20 записей
                if (count % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.flush();
            session.clear();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Set<String> inputVerb () {
        String [] listOfCorrectVerbs = {"comer", "vivir", "trabajar", "estudiar",
                "bailar", "cantar", "cocinar", "limpiar", "viajar", "enseñar",
                "aprender", "escuchar", "mirar", "correr", "beber", "decidir",
                "abrir", "escribir", "recibir", "nadar", "caminar", "amar",
                "arreglar", "ayudar", "buscar", "cambiar", "contestar", "desayunar",
                "descansar", "desear", "dibujar", "enseñar", "entrar", "escuchar",
                "esperar", "estudiar", "explicar", "ganar", "hablar", "invitar",
                "lavar", "llegar", "llevar", "mirar", "necesitar", "pagar", "preguntar",
                "preparar", "regresar", "terminar", "tomar", "usar", "visitar", "vender",
                "leer", "comprender", "deber", "prometer", "temer", "aprender", "meter",
                "responder", "insistir", "asistir", "permitir", "compartir", "existir",
                "partir", "sufrir"};

        Set<String> input = new TreeSet<>();
        Collections.addAll(input, listOfCorrectVerbs);
        return input;
    }

    private static int size () {
        return inputVerb().size();
    }

}


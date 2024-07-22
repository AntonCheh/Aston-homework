package insert;

import model.CorrectConjugation;
import model.Verb;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.*;

public class CorrectFormInsert {
    public static void batchInsertVerbs(Set<Map<String, String>> verbForms) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int count = 0;
            for (Map<String, String> forms : verbForms) {
                Verb newVerb = new Verb();
                newVerb.setVerbo(forms.get("verbo"));
                session.save(newVerb);

                CorrectConjugation conjugation = new CorrectConjugation();
                conjugation.setYo(forms.get("yo"));
                conjugation.setTu(forms.get("tu"));
                conjugation.setEl_ella(forms.get("el_ella"));
                conjugation.setNosotros(forms.get("nosotros"));
                conjugation.setVosotros(forms.get("vosotros"));
                conjugation.setEllos(forms.get("ellos"));
                conjugation.setVerb(newVerb);

                session.save(conjugation);

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

    public static Set<Map<String, String>> inputVerbForms() {
        // Определите формы глаголов здесь
        String[][] verbFormsArray = {
                {"poder", "puedo", "puedes", "puede", "podemos", "podéis", "pueden"},
                // Добавьте больше глаголов здесь...
        };

        Set<Map<String, String>> input = new TreeSet<>((m1, m2) -> m1.get("verbo").compareTo(m2.get("verbo")));
        for (String[] forms : verbFormsArray) {
            Map<String, String> verbMap = new HashMap<>();
            verbMap.put("verbo", forms[0]);
            verbMap.put("yo", forms[1]);
            verbMap.put("tu", forms[2]);
            verbMap.put("el_ella", forms[3]);
            verbMap.put("nosotros", forms[4]);
            verbMap.put("vosotros", forms[5]);
            verbMap.put("ellos", forms[6]);
            input.add(verbMap);
        }
        return input;
    }

    public static void main(String[] args) {
        Set<Map<String, String>> verbs = inputVerbForms();
        batchInsertVerbs(verbs);
    }
}

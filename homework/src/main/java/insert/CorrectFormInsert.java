package insert;

import lombok.var;
import model.CorrectConjugation;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class CorrectFormInsert {

    private static SessionFactory sessionFactory;

    @Autowired
    public CorrectFormInsert(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public static void batchInsertConjugations(Set<Map<String, String>> conjugations) {
        var session = sessionFactory.getCurrentSession();
        int count = 0;
        for (var forms : conjugations) {
            var conjugation = new CorrectConjugation();
            conjugation.setYo(forms.get("yo"));
            conjugation.setTu(forms.get("tu"));
            conjugation.setEl_ella(forms.get("el_ella"));
            conjugation.setNosotros(forms.get("nosotros"));
            conjugation.setVosotros(forms.get("vosotros"));
            conjugation.setEllos(forms.get("ellos"));

            session.save(conjugation);
            count++;
            if (count % 20 == 0) {
                session.flush();
                session.clear();
            }
        }
        session.flush();
        session.clear();
    }

    public static Set<Map<String, String>> inputConjugations() {
        String[][] conjugationFormsArray = {
                {"puedo", "puedes", "puede", "podemos", "podéis", "pueden"},
                {"como", "comes", "come", "comemos", "coméis", "comen"}
        };

        Set<Map<String, String>> input = new TreeSet<>((m1, m2) -> m1.get("yo").compareTo(m2.get("yo")));
        for (String[] forms : conjugationFormsArray) {
            Map<String, String> conjugationMap = new HashMap<>();
            conjugationMap.put("yo", forms[0]);
            conjugationMap.put("tu", forms[1]);
            conjugationMap.put("el_ella", forms[2]);
            conjugationMap.put("nosotros", forms[3]);
            conjugationMap.put("vosotros", forms[4]);
            conjugationMap.put("ellos", forms[5]);
            input.add(conjugationMap);
        }
        return input;
    }
}

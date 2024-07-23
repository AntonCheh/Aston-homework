package dao;

import lombok.Getter;
import model.Verb;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Repository
@Getter
public class VerbDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public VerbDAO(LocalSessionFactoryBean localSessionFactoryBean) {
        this.sessionFactory = localSessionFactoryBean.getObject();
    }

    @Transactional
    public void saveVerb(Verb verb) {
        sessionFactory.getCurrentSession().save(verb);
    }

    @Transactional(readOnly = true)
    public boolean existsByVerb(String verb) {
        Long count = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(v) FROM Verb v WHERE v.verbo = :verb", Long.class)
                .setParameter("verb", verb)
                .uniqueResult();
        return count != null && count > 0;
    }

    @Transactional(readOnly = true)
    public List<Verb> getAllVerbs() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Verb", Verb.class)
                .list();
    }

    @Transactional(readOnly = true)
    public List<String> getAllVerbsAsStrings() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT v.verbo FROM Verb v", String.class)
                .list();
    }

    @Transactional(readOnly = true)
    public Verb getVerbById(int id) {
        return sessionFactory.getCurrentSession().get(Verb.class, id);
    }

    @Transactional
    public void deleteVerb(int id) {
        Verb verb = getVerbById(id);
        if (verb != null) {
            sessionFactory.getCurrentSession().delete(verb);
        }
    }

    @Transactional
    public void deleteAllVerbs() {
        sessionFactory.getCurrentSession().createQuery("DELETE FROM Conjugation").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("DELETE FROM CorrectConjugation").executeUpdate();
        sessionFactory.getCurrentSession().createQuery("DELETE FROM Verb").executeUpdate();
    }

    @Transactional
    public void updateVerb(Verb verb) {
        sessionFactory.getCurrentSession().update(verb);
    }

    @Transactional
    public static void batchInsertVerbsIfNotExists(Set<String> verbs, SessionFactory sessionFactory) {
        List<String> existingVerbs = sessionFactory.getCurrentSession()
                .createQuery("SELECT v.verbo FROM Verb v", String.class)
                .list();
        Set<String> existingVerbsSet = new TreeSet<>(existingVerbs);
        Set<String> verbsToInsert = new TreeSet<>();

        for (String verb : verbs) {
            if (!existingVerbsSet.contains(verb)) {
                verbsToInsert.add(verb);
            }
        }

        if (!verbsToInsert.isEmpty()) {
            int count = 0;
            for (String verb : verbsToInsert) {
                Verb newVerb = new Verb();
                newVerb.setVerbo(verb);
                sessionFactory.getCurrentSession().save(newVerb);
                count++;
                if (count % 20 == 0) {
                    sessionFactory.getCurrentSession().flush();
                    sessionFactory.getCurrentSession().clear();
                }
            }
            sessionFactory.getCurrentSession().flush();
            sessionFactory.getCurrentSession().clear();
        }
    }

    @Transactional(readOnly = true)
    public long countVerbs() {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(v) FROM Verb v", Long.class)
                .uniqueResult();
    }
}

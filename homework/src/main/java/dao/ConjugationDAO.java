package dao;

import model.Conjugation;
import model.CorrectConjugation;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ConjugationDAO {

    private final SessionFactory sessionFactory;

    private final CorrectConjugationDAO correctConjugationDAO;

    @Autowired
    public ConjugationDAO(LocalSessionFactoryBean localSessionFactoryBean, CorrectConjugationDAO correctConjugationDAO) {
        this.sessionFactory = localSessionFactoryBean.getObject();
        this.correctConjugationDAO = correctConjugationDAO;
    }

    private boolean isValidConjugation(Conjugation conjugation) {
        CorrectConjugation correctConjugation = correctConjugationDAO.getCorrectConjugationById(conjugation.getVerb().getId());
        if (correctConjugation == null) {
            return false;
        }
        return correctConjugation.getYo().equals(conjugation.getYo()) &&
                correctConjugation.getTu().equals(conjugation.getTu()) &&
                correctConjugation.getEl_ella().equals(conjugation.getEl_ella()) &&
                correctConjugation.getVosotros().equals(conjugation.getVosotros()) &&
                correctConjugation.getNosotros().equals(conjugation.getNosotros()) &&
                correctConjugation.getEllos().equals(conjugation.getEllos());
    }

    @Transactional
    public void saveConjugation(Conjugation conjugation) throws Exception {
        if (!isValidConjugation(conjugation)) {
            throw new Exception("Invalid conjugation data");
        }
        sessionFactory.getCurrentSession().save(conjugation);
    }

    @Transactional(readOnly = true)
    public List<Conjugation> getConjugationsByVerbId(int verbId) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Conjugation where verb.id = :verbId", Conjugation.class)
                .setParameter("verbId", verbId)
                .list();
    }

    @Transactional(readOnly = true)
    public Conjugation getConjugationById(int id) {
        return sessionFactory.getCurrentSession().get(Conjugation.class, id);
    }

    @Transactional
    public void deleteConjugation(int id) {
        Conjugation conjugation = getConjugationById(id);
        if (conjugation != null) {
            sessionFactory.getCurrentSession().delete(conjugation);
        }
    }

    @Transactional
    public void updateConjugation(Conjugation conjugation) throws Exception {
        if (!isValidConjugation(conjugation)) {
            throw new Exception("Invalid conjugation data");
        }
        sessionFactory.getCurrentSession().update(conjugation);
    }
}

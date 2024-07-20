package dao;

import model.Conjugation;
import model.CorrectConjugation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class ConjugationDAO {
    private final CorrectConjugationDAO correctConjugationDAO = new CorrectConjugationDAO();

    private boolean isValidConjugation(Conjugation conjugation) {
        CorrectConjugation correctConjugation = correctConjugationDAO.getCorrectConjugationByVerbId(conjugation.getVerb().getId());
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

    public void saveConjugation(Conjugation conjugation) throws Exception {
        if (!isValidConjugation(conjugation)) {
            throw new Exception("Invalid conjugation data");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(conjugation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Conjugation> getConjugationsByVerbId(int verbId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Conjugation where verb.id = :verbId", Conjugation.class)
                    .setParameter("verbId", verbId)
                    .list();
        }
    }

    public Conjugation getConjugationById(int id) {
        Transaction transaction = null;
        Conjugation conjugation = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            conjugation = session.get(Conjugation.class, id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return conjugation;
    }

    public void deleteConjugation(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Conjugation conjugation = session.get(Conjugation.class, id);
            if (conjugation != null) {
                session.delete(conjugation);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateConjugation(Conjugation conjugation) throws Exception {
        if (!isValidConjugation(conjugation)) {
            throw new Exception("Invalid conjugation data");
        }
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(conjugation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}

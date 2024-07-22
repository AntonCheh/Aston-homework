package dao;

import model.CorrectConjugation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

public class CorrectConjugationDAO {

    public void saveCorrectConjugation(CorrectConjugation correctConjugation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            System.out.println("Saving CorrectConjugation: " + correctConjugation);
            session.save(correctConjugation);
            transaction.commit();
            System.out.println("CorrectConjugation saved successfully");
        } catch (Exception e) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                } catch (RuntimeException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            e.printStackTrace();
        }
    }

    public CorrectConjugation getCorrectConjugationByVerbId(int verbId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from CorrectConjugation where verb.id = :verbId", CorrectConjugation.class)
                    .setParameter("verbId", verbId)
                    .uniqueResult();
        }
    }
}

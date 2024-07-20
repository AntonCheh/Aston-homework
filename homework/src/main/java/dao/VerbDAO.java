package dao;

import model.Verb;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class VerbDAO {

    public void saveVerb(Verb verb) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(verb);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean existsByVerb(String verb) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT COUNT(v) FROM Verb v WHERE v.verb = :verb", Long.class)
                    .setParameter("verb", verb)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    public List<Verb> getAllVerbs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Verb> verbs = session.createQuery("from Verb", Verb.class).list();
            for (Verb verb : verbs) {
                Hibernate.initialize(verb.getConjugations());
            }
            return verbs;
        }
    }

    public Verb getVerbById(int id) {
        Transaction transaction = null;
        Verb verb = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            verb = session.get(Verb.class, id);
            Hibernate.initialize(verb.getConjugations());
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return verb;
    }

    public void deleteVerb(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Verb verb = session.get(Verb.class, id);
            if (verb != null) {
                session.delete(verb);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateVerb(Verb verb) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(verb);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }




}

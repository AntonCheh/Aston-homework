package dao;

import insert.VerbBatchInsert;
import model.Verb;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

import java.util.Set;
import java.util.TreeSet;

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

    public List<String> getAllVerbsAsStrings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT v.verbo FROM Verb v", String.class).list();
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

    public void deleteAllVerbs() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Удаляем все записи из таблиц, связанных с таблицей verb
            session.createQuery("DELETE FROM Conjugation").executeUpdate();
            session.createQuery("DELETE FROM CorrectConjugation").executeUpdate();

            // Удаляем все записи из таблицы verb
            session.createQuery("DELETE FROM Verb").executeUpdate();

            transaction.commit();
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


    public static void batchInsertVerbsIfNotExists(Set<String> verbs) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> existingVerbs = session.createQuery("SELECT v.verbo FROM Verb v", String.class).list();
            Set<String> existingVerbsSet = new TreeSet<>(existingVerbs);
            Set<String> verbsToInsert = new TreeSet<>();

            for (String verb : verbs) {
                if (!existingVerbsSet.contains(verb)) {
                    verbsToInsert.add(verb);
                }
            }

            if (!verbsToInsert.isEmpty()) {
                transaction = session.beginTransaction();
                int count = 0;
                for (String verb : verbsToInsert) {
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
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public long countVerbs() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(v) FROM Verb v", Long.class).uniqueResult();
        }
    }
}






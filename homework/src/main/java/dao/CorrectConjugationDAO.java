package dao;

import model.CorrectConjugation;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CorrectConjugationDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public CorrectConjugationDAO(LocalSessionFactoryBean localSessionFactoryBean) {
        this.sessionFactory = localSessionFactoryBean.getObject();
    }

    @Transactional
    public void saveCorrectConjugation(CorrectConjugation correctConjugation) {
        sessionFactory.getCurrentSession().save(correctConjugation);
    }

    @Transactional(readOnly = true)
    public CorrectConjugation getCorrectConjugationById(int id) {
        return sessionFactory.getCurrentSession().get(CorrectConjugation.class, id);
    }

    @Transactional(readOnly = true)
    public List<CorrectConjugation> getAllCorrectConjugations() {
        return sessionFactory.getCurrentSession()
                .createQuery("from CorrectConjugation", CorrectConjugation.class)
                .list();
    }
}

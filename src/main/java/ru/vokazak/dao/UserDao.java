package ru.vokazak.dao;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Category;
import ru.vokazak.entity.User;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.*;

@Service
public class UserDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Transactional
    public User findByEmailAndHash(String email, String hash) {
        return em.createNamedQuery("User.findByEmailAndHash", User.class)
                .setParameter("email", email)
                .setParameter("hash", hash)
                .getResultList()
                .get(0);
    }

    @Transactional
    public User insert(String name, String surname, String email, String hash) {
        User user = new User();
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(hash);
        em.persist(user);

        return user;
    }

}

package ru.vokazak.dao;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Category;
import ru.vokazak.entity.Transaction;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class CategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Category insert(String name) {
        try {
            Category category = new Category();
            category.setTransType(name);
            em.persist(category);

            return category;
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public Category delete(String name) {
        Category category = findByName(name);

        if (category == null) {
            throw new UnsuccessfulCommandExecutionExc("\"" + name + "\" account does not exist");
        }

        em.createNamedQuery("Category.delete")
                .setParameter("name", name)
                .executeUpdate();

        return category;
    }

    @Transactional
    public Category update(String oldName, String newName) {
        Category category = findByName(oldName);
        category.setTransType(newName);
        return category;
    }

    @Transactional
    public List<Category> selectAll() {
        try {
            return em.createNamedQuery("Category.findAll", Category.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public Category findByName(String name) {
        try {
            List<Category> results = em.createNamedQuery("Category.findByName", Category.class)
                    .setParameter("name", name)
                    .getResultList();
            if (results.isEmpty()) {
                throw new UnsuccessfulCommandExecutionExc("Category \"" + name + "\" does not exist");
            }
            return results.get(0);
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public Map<Category, BigDecimal> sumMoneyForEachCategory(long userId, int days) {

        List<Transaction> transList;
        try {
            transList = em.createQuery("SELECT t FROM Transaction t where t.date > :date AND (t.accFrom.userId = :uid OR t.accTo.userId = :uid)", Transaction.class)
                    .setParameter("uid", userId)
                    .setParameter("date", Date.from(Instant.now().minus(Duration.ofDays(days))))
                    .getResultList();
        } catch (Exception e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }

        Map<Category, BigDecimal> resultMap = new HashMap<>();
        transList.forEach(t -> t.getCategories()
                .forEach(c -> {
                    if (!resultMap.containsKey(c)) {
                        resultMap.put(c, t.getMoney());
                    } else {
                        resultMap.put(c, resultMap.get(c).add(t.getMoney()));
                    }
                })
        );
        return resultMap;
    }

}

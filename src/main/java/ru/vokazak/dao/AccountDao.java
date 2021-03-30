package ru.vokazak.dao;

import org.springframework.stereotype.Service;
import ru.vokazak.entity.Account;
import ru.vokazak.exception.UnsuccessfulCommandExecutionExc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Account insert(String name, BigDecimal balance, long userId) {
        try {
            Account account = new Account();
            account.setName(name);
            account.setBalance(balance);
            account.setUserId(userId);

            em.persist(account);
            return account;
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public void update(long id, BigDecimal balance) {
        try {
            Account account = em.find(Account.class, id);
            account.setBalance(balance);
        } catch (Exception e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public Account delete(String name, long userId) {
        try {
            Account account = em.createNamedQuery("Account.find", Account.class)
                    .setParameter("name", name)
                    .setParameter("userId", userId)
                    .getSingleResult();

            if (account == null) {
                throw new UnsuccessfulCommandExecutionExc("\"" + name + "\" account does not exist");
            }

            if (!account.getTransOut().isEmpty() || !account.getTransIn().isEmpty()) {
                throw new UnsuccessfulCommandExecutionExc("\"" + name + "\" account has transactions");
            }

            em.createNamedQuery("Account.delete")
                    .setParameter("name", name)
                    .setParameter("userId", userId)
                    .executeUpdate();

            return account;
        } catch (PersistenceException e) {
            throw new UnsuccessfulCommandExecutionExc(e);
        }
    }

    @Transactional
    public List<Account> findByUserId(long userId) {
        return em.createNamedQuery("Account.findAll", Account.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.hspf.auth;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Marcel
 */
@ApplicationScoped
public class AuthRepository implements Serializable {

    private Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @PersistenceContext
    private EntityManager entityManager;

    public Account safeUser(Account account) {
        logger.log(Level.INFO, "Save user: {0}", account.getUsername());
        entityManager.persist(account);
        return account;
    }

    public boolean userExists(Account signUpAccount) {
        return this.readAllAccounts().stream().anyMatch((account) -> (signUpAccount.getEmail().equals(account.getEmail())));
    }

    public Account getAccountByEmail(Account loginAccount) {
        for (Account account : this.readAllAccounts()) {
            if (loginAccount.getEmail().equals(account.getEmail())) {
                return account;
            }
        }
        return null;
    }
    
    public List<Account> readAllAccounts() {
        return entityManager.createNamedQuery("Account.findAll", Account.class).getResultList();
    }
    
    public List<Account> findEvent(String username, String email, String password) {
        return entityManager.createNamedQuery("Account.findEvent", Account.class)
            .setParameter("username", username)
            .setParameter("email", email)
            .setParameter("password", password).getResultList();
    }

}

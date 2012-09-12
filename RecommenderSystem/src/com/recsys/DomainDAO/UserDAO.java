package com.recsys.DomainDAO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.recsys.Domain.User;


public class UserDAO implements Serializable{
	//Constructor
	public UserDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
	
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //Add a User to the database
    public void create(User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
            em.close();
            }
        }
     }

    
    //Edit a User in the database
    public void edit(User user) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            user = em.merge(user);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long idUser = user.getIdUser();
                if (findUser(idUser) == null) {
                    System.out.println("The user with id " + idUser + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    //Delete a User from database
    public void destroy(Long idUser) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user=null;
            try {
                user = em.getReference(User.class, idUser);
                user.getIdUser();
            } catch (EntityNotFoundException enfe) {
                System.out.println("The user with id " + idUser + " no longer exists."+enfe);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    //Retrieve all users
    public List<User> findUsers() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
  
    
    
    
    //Retrieve a user by using an Id
    public User findUser(Long idUser) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, idUser);
        } finally {
            em.close();
        }
    }
    
    
}

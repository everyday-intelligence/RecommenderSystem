package com.recsys.DomainDAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.recsys.Domain.Item;
import com.recsys.Domain.Rating;
import com.recsys.Domain.User;

public class RatingDAO {

	public RatingDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
	
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //Add a User to the database
    public void create(Rating rating) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(rating);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
            em.close();
            }
        }
     }

 /*   
    //Edit a User in the database
    public void edit(Rating rating) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            rating = em.merge(rating);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int rate = rating.getRating();
                if (findRating(rate) == null) {
                    System.out.println("The user with id " + rate + " no longer exists.");
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
    }*/
    
    //Retrieve all users
    public List<Rating> findRatings() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rating.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
   /* 
    //Retrieve a user by using an Id
    public Rating findRating(int rate) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rating.class, rate);
        } finally {
            em.close();
        }
    }
    
*/
	
}

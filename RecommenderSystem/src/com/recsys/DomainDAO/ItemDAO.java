package com.recsys.DomainDAO;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.recsys.Domain.Item;



public class ItemDAO implements Serializable{

	//Constructor
		public ItemDAO(EntityManagerFactory emf) {
	        this.emf = emf;
	    }
		
	    private EntityManagerFactory emf = null;

	    public EntityManager getEntityManager() {
	        return emf.createEntityManager();
	    }

	    //Add an item to the database
	    public void create(Item item) {
	        EntityManager em = null;
	        try {
	            em = getEntityManager();
	            em.getTransaction().begin();
	            em.persist(item);
	            em.getTransaction().commit();
	        } finally {
	            if (em != null) {
	                em.close();
	            }
	        }
	    }

	    //Edit an Item in the database
	    public void edit(Item item) throws Exception {
	        EntityManager em = null;
	        try {
	            em = getEntityManager();
	            em.getTransaction().begin();
	            item = em.merge(item);
	            em.getTransaction().commit();
	        } catch (Exception ex) {
	            String msg = ex.getLocalizedMessage();
	            if (msg == null || msg.length() == 0) {
	                Long idItem = item.getIdItem();
	                if (findItem(idItem) == null) {
	                    System.out.println("The item with id " + idItem + " no longer exists.");
	                }
	            }
	            throw ex;
	        } finally {
	            if (em != null) {
	                em.close();
	            }
	        }
	    }
	    
	    //Delete an Item from database
	    public void destroy(Long idItem) {
	        EntityManager em = null;
	        try {
	            em = getEntityManager();
	            em.getTransaction().begin();
	            Item item=null;
	            try {
	                item = em.getReference(Item.class, idItem);
	                item.getIdItem();
	            } catch (EntityNotFoundException enfe) {
	                System.out.println("The item with id " + idItem + " no longer exists."+enfe);
	            }
	            em.remove(item);
	            em.getTransaction().commit();
	        } finally {
	            if (em != null) {
	                em.close();
	            }
	        }
	    }
	    
	    //Retrieve all items
	    public List<Item> findItems() {
	        EntityManager em = getEntityManager();
	        try {
	            Query q = em.createQuery("from Item");
	            return q.getResultList();
	        } finally {
	            em.close();
	        }
	    }
	    
	    //Retrieve an item by using an Id
	    public Item findItem(Long idItem) {
	        EntityManager em = getEntityManager();
	        try {
	            return em.find(Item.class, idItem);
	        } finally {
	            em.close();
	        }
	    }
	    
	    
	    
	    
}

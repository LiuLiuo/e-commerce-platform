/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class CartFacade extends AbstractFacade<Cart> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CartFacade() {
        super(Cart.class);
    }

    public void createCartItem(Cart i) {
        em.persist(i);
    }

    public List<Cart> getCurCarts(int userId) {
        List<Cart> curCarts;
        try {
            curCarts = (List<Cart>) em.createQuery("SELECT j FROM Cart j WHERE j.userId.id=:userId")
                    .setParameter("userId", userId)
                    .getResultList();
            return curCarts;
        } catch (NoResultException e) {
            return null;
        }
    }
}

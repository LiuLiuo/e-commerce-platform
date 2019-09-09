/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;
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
public class OrderDetailFacade extends AbstractFacade<OrderDetail> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OrderDetailFacade() {
        super(OrderDetail.class);
    }

    public List getPayedOrder(String state, User userID) {
        List<OrderDetail> payedlist = new ArrayList<OrderDetail>();
        try {
            payedlist = em.createQuery("SELECT u FROM OrderDetail u WHERE u.orderState=:state AND u.userId=:id")
                    .setParameter("state", state)
                    .setParameter("id", userID)
                    .getResultList();

        } catch (NoResultException e) {
            return null;
        }
        return payedlist;
    }

    //czg
    public List<OrderDetail> getCurODs(int userId) {

        List<OrderDetail> curODs;
        try {
            curODs = (List<OrderDetail>) em.createQuery("SELECT j FROM OrderDetail j WHERE j.userId.id=:userId")
                    .setParameter("userId", userId)
                    .getResultList();

            return curODs;
        } catch (NoResultException e) {
            return null;
        }
    }

    //czg
    public void createOrderItems(OrderDetail i) {
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

    //订单状态改为支付
    public void changeState(OrderDetail e) {
        e.setOrderState("已支付");
        em.merge(e);
    }

    public void updateItem(Item i) {
        em.merge(i);
        em.flush();
    }

}

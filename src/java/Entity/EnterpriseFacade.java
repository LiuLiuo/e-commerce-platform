/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import static com.sun.xml.ws.security.addressing.impl.policy.Constants.logger;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author Administrator
 */
@Stateless
public class EnterpriseFacade extends AbstractFacade<Enterprise> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EnterpriseFacade() {
        super(Enterprise.class);
    }
    
    public void create_Enterprise(String EnterpriseName,String EnterpriseLogoPath,String EnterpriseAddress, String EnterpriseTel,User userid){
        Enterprise e = new Enterprise();
        e.setEnterpriseName(EnterpriseName);
        e.setEnterpriseLogoPath(EnterpriseLogoPath);
        e.setEnterpriseAddress(EnterpriseAddress);
        e.setEnterpriseTel(EnterpriseTel);
        e.setUserId(userid);
        em.persist(e);
    }
    
     //判断数据库中是否有企业名与输入的企业名相同
    public Enterprise getIsDuplicate(String EnterpriseName){
        Enterprise mEnterprise;
        try{
            mEnterprise = (Enterprise)em.createQuery("SELECT e FROM Enterprise e WHERE e.enterpriseName=:name")
                .setParameter("name", EnterpriseName)
                .getSingleResult();
            return mEnterprise;
        }catch (NoResultException e){
            return null;
        }
    }
    
    public List<Enterprise> getAllUnCheckEnterprise() {
        List<Enterprise> l;
        l = (List<Enterprise>) em.createQuery("SELECT e FROM Enterprise e WHERE e.userId In(SELECT u FROM User u WHERE u.verifyState=0)").getResultList();
        return l;
    }

    public void accept(Enterprise e) {
        try {
            User u = em.find(User.class, e.getUserId().getId());
            u.setVerifyState((short) 1);
            u.setUserLogoPath("user_logo/init_head.png");
            em.merge(u);
        } catch (ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(err -> logger.log(Level.SEVERE, err.toString()));
        }
    }

    public void delete(Enterprise e) {
         try {
            User u = em.find(User.class, e.getUserId().getId());
            Enterprise enterprise=em.find(Enterprise.class, e.getId());
            em.remove(enterprise);
            u.setUserLogoPath("user_logo/init_head.png");
            em.remove(u);
        } catch (ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(err -> logger.log(Level.SEVERE, err.toString()));
        }
    }
    
    public List<Item> getCurItems(String curEnterpriseName){
       List<Item> curItems;
    try{   
//        curEnterprise= (Enterprise)em.createQuery("SELECT e FROM Enterprise e WHERE e.enterpriseName=:curEnterpriseName")
//                .setParameter("curEnterpriseName",curEnterpriseName)
//                .getSingleResult();
//         return curEnterprise;
         curItems=( List<Item>)em.createQuery("SELECT i FROM Item i WHERE i.enterpriseId.enterpriseName=:curEnterpriseName")
              .setParameter("curEnterpriseName",curEnterpriseName)
              .getResultList();
         return curItems;
    }
    catch (NoResultException e){
        return null;
    }
   }
    
}

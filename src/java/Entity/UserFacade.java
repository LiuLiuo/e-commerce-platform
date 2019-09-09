/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    
    //更改用户头像
    public void upDateUserPicPath(User current_user,String UserLogoPath){
        User tmp = em.find(User.class, current_user.getId());
        tmp.setUserLogoPath(UserLogoPath);
        em.merge(tmp);
    }
    
    //判断输入的用户名和密码是否匹配
    public User getValidateLogin(String username, String password){
        User mUser;
        try{
            mUser = (User)em.createQuery("SELECT u FROM User u WHERE u.password = :password AND u.name=:name")
                .setParameter("name", username)
                .setParameter("password",password)
                .getSingleResult();
            return mUser;
        }catch (NoResultException e){
//            System.out.println("Entity.UserFacade.getValidateLogin()"+mUser.getName());
            return null;
        }
    }
    
    //判断数据库中是否有用户名与输入的用户名相同
    public User getIsDuplicate(String username){
        User mUser;
        try{
            mUser = (User)em.createQuery("SELECT u FROM User u WHERE u.name=:name")
                .setParameter("name", username)
                .getSingleResult();
            return mUser;
        }catch (NoResultException e){
            return null;
        }
    }
    
    //往数据库中插入数据为输入的用户名和密码的普通用户
    public void createUser(String username,String password){
        User e = new User();
        e.setName(username);
        e.setPassword(password);
        e.setRole("U");
        e.setVerifyState((short)1);
        e.setUserLogoPath("user_logo/init_head.jpg");
        em.persist(e);
        
    }
    public User getCurUser(int id){
        User curUser;
    try{   
//        curEnterprise= (Enterprise)em.createQuery("SELECT e FROM Enterprise e WHERE e.enterpriseName=:curEnterpriseName")
//                .setParameter("curEnterpriseName",curEnterpriseName)
//                .getSingleResult();
//         return curEnterprise;
         curUser= (User)em.createQuery("SELECT u FROM User u WHERE u.id=:id")
              .setParameter("id",id)
              .getSingleResult();
         return curUser;
    }
    catch (NoResultException e){
        return null;
    }
   }
    //往数据库中插入数据为输入的企业账号名和密码的企业账号
    public User createEnterprise(String username,String password){
        User e = new User();
        e.setName(username);
        e.setPassword(password);
        e.setRole("E");
        e.setVerifyState((short)0);
        e.setUserLogoPath("user_logo/init_head.jpg");
        em.persist(e);
        return e;
    }
    
    //检索得到当前企业账号的企业对象
    public Enterprise getCurrentEnterprise(User user){
        Enterprise enterprise;
        enterprise=(Enterprise)em.createQuery("SELECT e FROM Enterprise e WHERE e.userId=:user")
                .setParameter("user", user)
                .getSingleResult();
        return enterprise;
    }
    
        public void editInfo(User u,String nickname,String address){
//        User user = em.find(User.class, u.getId());
        u.setUserNickname(nickname);
        u.setUserAddress(address);
        em.merge(u);
        em.flush();
        
    }
    
}

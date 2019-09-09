/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Administrator
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {

    @PersistenceContext(unitName = "WebApplication1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacade() {
        super(Item.class);
    }

    //计算随机数
    public int[] calculateRandom(String tag, int num) {
        int[] randomArray = {-1, -1, -1, -1, -1};
        List ran = new ArrayList();
        Random r = new Random();
        if (num != 0) {
            int temp;
            int i = 0;
            while (i < 5) {
                temp = r.nextInt(num);
                if (!ran.contains(temp)) {
                    randomArray[i] = temp;
                    ran.add(temp);
                    i++;
                }
            }
        }
        return randomArray;
    }

    public List getRandomItem(String tag) {
        List<Item> randomlist;
        List<Item> floorlist = new ArrayList<Item>();
        try {
            randomlist = em.createNamedQuery("Item.findByItemTag")
                    .setParameter("itemTag", tag)
                    .getResultList();

        } catch (NoResultException e) {
            return null;
        }
        int totalNum = randomlist.size();
        int[] ran = calculateRandom(tag, totalNum);
        for (int i = 0; i < 5; i++) {
            floorlist.add(randomlist.get(ran[i]));
        }
        return floorlist;
    }

    public void create_enterprise_item(String itemName, int stock, int itemPrice, String itemPicPath, String itemTag, int itemSaleNum, String enterpriseName) {
        Item i = new Item();
        i.setItemName(itemName);
        i.setStock(stock);
        i.setItemPrice(itemPrice);
        i.setItemPicPath(itemPicPath);
        i.setItemTag(itemTag);
        i.setItemSaleNum(itemSaleNum);
        Enterprise e = (Enterprise) em.createQuery("SELECT e FROM Enterprise e WHERE e.enterpriseName=:enterpriseName")
                .setParameter("enterpriseName", enterpriseName)
                .getSingleResult();
        i.setEnterpriseId(e);
        em.persist(i);

    }


    public void updateItem(Item i) {
        em.merge(i);
        em.flush();
    }

    public List<Item> getItemList(String ItemKey) {
        List<Item> curItem;
        if (em.createQuery("select i from Item i where i.itemTag=:ItemKey").setParameter("ItemKey", ItemKey).getResultList().size() != 0) {
            curItem = (List<Item>) em.createQuery("select i from Item i where i.itemTag=:ItemKey").setParameter("ItemKey", ItemKey).getResultList();
        } else {
            curItem = (List<Item>) em.createQuery("select i from Item i where i.itemName like ?1").setParameter(1, "%" + ItemKey + "%").getResultList();
        }

        return curItem;
    }

    public void edit_item_info(Item i,String pic) {
//        User user = em.find(User.class, u.getId());
//        i.setItemName(itemName);
//        i.setStock(stock);
//        i.setItemPrice(itemPrice);
        i.setItemPicPath("images/"+pic);
//        i.setItemTag(itemTag);
//        i.setItemSaleNum(itemSaleNum);   
        em.merge(i);
        em.flush();

    }
}

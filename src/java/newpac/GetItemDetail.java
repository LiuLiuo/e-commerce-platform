/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpac;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import Entity.CartController;
import Entity.CartFacade;
import Entity.ItemController;
import Entity.ItemFacade;
import Entity.Item;
import javax.ejb.EJB;

@Named
@SessionScoped
/**
 *
 * @author Administrator
 */

//接收到商品的名字，用于从数据库中获取该商品的所有信息
public class GetItemDetail implements Serializable {

    private int key = -1;
    private int count;
    private int number;

    @EJB
    private ItemFacade itemFacade;
    @EJB
    private CartFacade cartFacade;

    //获取Item表中行数
    public int getCount() {
        count = itemFacade.count();
        return count;
    }

    
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    //获取商品ID 
    public int getItemID(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return -1;
        } else {
            return itemFacade.find(i).getId();
        }
    }

    //获取商品库存
    public int getItemStock(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return -1;
        } else {
            return itemFacade.find(i).getStock();
        }
    }

    //获取商品价格
    public int getItemPrice(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return -1;
        } else {
            return itemFacade.find(i).getItemPrice();
        }
    }

    //获取商品图片存取路径
    public String getItemPicPath(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return "Not Found";
        } else {
            return itemFacade.find(i).getItemPicPath();
        }
    }

    //获取商品标签
    public String getItemTag(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return "Not Found";
        } else {
            return itemFacade.find(i).getItemTag();
        }
    }

    //获取商品所属企业ID
    public String getEnterprise(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return "Not Found";
        } else {
            return itemFacade.find(i).getEnterpriseId().getEnterpriseName();
        }
    }

    //获取商品销售量
    public int getItamSaleNum(String ItemName) {
        int i;
        this.count = itemFacade.count();
        for (i = 1; i <= count; i++) {
            if (ItemName.equals(itemFacade.find(i).getItemName())) {
                break;
            }
        }
        if (i > count) {
            return -1;
        } else {
            return itemFacade.find(i).getItemSaleNum();
        }
    }
}

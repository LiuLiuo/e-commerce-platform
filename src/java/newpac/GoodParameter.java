/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpac;

import Entity.UserController;
import Entity.Item;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author 67506
 */
@Named("goodParameter")
@SessionScoped
public class GoodParameter implements Serializable {

    private Item item;
    //点击立即付款后，金额计算
    private int money;
    //记录商品数量
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMoney() {
        money = item.getItemPrice() * number;
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String immediatelyPayment() {
        return "payment.xhtml";
    }
    
    

    public String itemDetail(Item item) {
        this.item = item;
        return "GoodDetail.xhtml";
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

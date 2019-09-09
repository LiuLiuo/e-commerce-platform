/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpac;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import Entity.ItemFacade;
import Entity.Item;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


@Named
@SessionScoped
/**
 *
 * @author Administrator
 */
//用于搜索显示结果，获取输入的关键字
public class ForSearch  implements Serializable{
    private int count;
     @EJB
    private ItemFacade itemFacade;
     
     //获取Item表中行数
    public int getCount(){
        count = itemFacade.count();
        return count; 
    }
    
    //将标签等于ItemTag的Item的ID存入ID[]数组中
    public String[] getTagSearchID(String ItemTag){
        String Name[] = new String[10];
        int index = 0;
        int i;
        this.count = itemFacade.count();
        for(i = 1;i <= count;i++){
            if(ItemTag.equals(itemFacade.find(i).getItemTag())){
                Name[index] = itemFacade.find(i).getItemName();
                index++;
            }
        }
        return Name;
    }
    
    /*public void printItemID(int[] ID){
        for(int i=0;ID[i]!=0;i++){
            System.out.println(ID[i]);
        }
    }*/
    
}

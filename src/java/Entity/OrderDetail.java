/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author VictorChan
 */
@Entity
@Table(name = "order_detail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrderDetail.findAll", query = "SELECT o FROM OrderDetail o")
    , @NamedQuery(name = "OrderDetail.findByOrderTime", query = "SELECT o FROM OrderDetail o WHERE o.orderTime = :orderTime")
    , @NamedQuery(name = "OrderDetail.findByOrderItemNum", query = "SELECT o FROM OrderDetail o WHERE o.orderItemNum = :orderItemNum")
    , @NamedQuery(name = "OrderDetail.findById", query = "SELECT o FROM OrderDetail o WHERE o.id = :id")
    , @NamedQuery(name = "OrderDetail.findByOrderState", query = "SELECT o FROM OrderDetail o WHERE o.orderState = :orderState")
    , @NamedQuery(name = "OrderDetail.findByOrderSumPrice", query = "SELECT o FROM OrderDetail o WHERE o.orderSumPrice = :orderSumPrice")})
public class OrderDetail implements Serializable {
//     @EJB
//    OrderDetailFacade orderDetailFacade;

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_item_num")
    private int orderItemNum;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Item itemId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_sun_price")
    private int orderSumPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "order_state")
    private String orderState;

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public OrderDetail() {
    }

    public OrderDetail(Integer id) {
        this.id = id;
    }

    public OrderDetail(Integer id, Date orderTime, int orderItemNum) {
        this.id = id;
        this.orderTime = orderTime;
        this.orderItemNum = orderItemNum;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getOrderItemNum() {
        return orderItemNum;
    }

    public void setOrderItemNum(int orderItemNum) {
        this.orderItemNum = orderItemNum;
    }

    public Integer getId() {
//        return id;
        return orderItemNum * itemId.getItemPrice();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {
        this.itemId = itemId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public int getOrderSumPrice() {
        return orderSumPrice;
    }

    public void setOrderSumPrice(int orderSumPrice) {
        this.orderSumPrice = orderSumPrice;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderDetail)) {
            return false;
        }
        OrderDetail other = (OrderDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.OrderDetail[ id=" + id + " ]";
    }

}

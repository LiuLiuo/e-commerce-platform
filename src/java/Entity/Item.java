/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
    , @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id")
    , @NamedQuery(name = "Item.findByStock", query = "SELECT i FROM Item i WHERE i.stock = :stock")
    , @NamedQuery(name = "Item.findByItemName", query = "SELECT i FROM Item i WHERE i.itemName = :itemName")
    , @NamedQuery(name = "Item.findByItemPrice", query = "SELECT i FROM Item i WHERE i.itemPrice = :itemPrice")
    , @NamedQuery(name = "Item.findByItemPicPath", query = "SELECT i FROM Item i WHERE i.itemPicPath = :itemPicPath")
    , @NamedQuery(name = "Item.findByItemTag", query = "SELECT i FROM Item i WHERE i.itemTag = :itemTag")
    , @NamedQuery(name = "Item.findByItemSaleNum", query = "SELECT i FROM Item i WHERE i.itemSaleNum = :itemSaleNum")})
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "stock")
    private Integer stock;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "item_name")
    private String itemName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "item_price")
    private int itemPrice;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "item_pic_path")
    private String itemPicPath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "item_tag")
    private String itemTag;
    @Column(name = "item_sale_num")
    private Integer itemSaleNum;
    @JoinColumn(name = "enterprise_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Enterprise enterpriseId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemId")
    private Collection<OrderDetail> orderDetailCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemId")
    private Collection<Cart> cartCollection;

    public Item() {
    }

    public Item(Integer id) {
        this.id = id;
    }

    public Item(Integer id, String itemName, int itemPrice, String itemPicPath, String itemTag) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemPicPath = itemPicPath;
        this.itemTag = itemTag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPicPath() {
        return itemPicPath;
    }

    public void setItemPicPath(String itemPicPath) {
        this.itemPicPath = itemPicPath;
    }

    public String getItemTag() {
        return itemTag;
    }

    public void setItemTag(String itemTag) {
        this.itemTag = itemTag;
    }

    public Integer getItemSaleNum() {
        return itemSaleNum;
    }

    public void setItemSaleNum(Integer itemSaleNum) {
        this.itemSaleNum = itemSaleNum;
    }

    public Enterprise getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Enterprise enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    @XmlTransient
    public Collection<OrderDetail> getOrderDetailCollection() {
        return orderDetailCollection;
    }

    public void setOrderDetailCollection(Collection<OrderDetail> orderDetailCollection) {
        this.orderDetailCollection = orderDetailCollection;
    }

    @XmlTransient
    public Collection<Cart> getCartCollection() {
        return cartCollection;
    }

    public void setCartCollection(Collection<Cart> cartCollection) {
        this.cartCollection = cartCollection;
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
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Item[ id=" + id + " ]";
    }
    
}

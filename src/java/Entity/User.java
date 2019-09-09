/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author VictorChan
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id")
    , @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name")
    , @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
    , @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role")
    , @NamedQuery(name = "User.findByVerifyState", query = "SELECT u FROM User u WHERE u.verifyState = :verifyState")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "role")
    private String role;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verify_state")
    private short verifyState;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "user_logo_path")
    private String userLogoPath;
    @Size(max = 45)
    @Column(name = "user_nickname")
    private String userNickname;
    @Size(max = 45)
    @Column(name = "user_address")
    private String userAddress;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Enterprise> enterpriseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<OrderDetail> orderDetailCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Cart> cartCollection;
    
    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String password, String role, short verifyState) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.verifyState = verifyState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public short getVerifyState() {
        return verifyState;
    }

    public void setVerifyState(short verifyState) {
        this.verifyState = verifyState;
    }
    
     public String getUserLogoPath() {
        return userLogoPath;
    }

    public void setUserLogoPath(String userLogoPath) {
        this.userLogoPath = userLogoPath;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    
    
    public List<Cart> transfer_coll_to_list()
    {
        List<Cart> cart_Collection = new ArrayList<Cart>(cartCollection);
        return cart_Collection;
    }
    public List<OrderDetail> transfer_od_to_list()
    {
        List<OrderDetail> od_Collection = new ArrayList<OrderDetail>(orderDetailCollection);
        return od_Collection;
    }
    public List<Enterprise> transfer_enterprise_to_list()
    {
        List<Enterprise> enterprise_Collection = new ArrayList<Enterprise>(enterpriseCollection);
        return enterprise_Collection;
    }
    public String delete(OrderDetail item){
            orderDetailCollection.remove(item);
            return null;
    }
    @XmlTransient
    public Collection<Enterprise> getEnterpriseCollection() {
        return enterpriseCollection;
    }

    public void setEnterpriseCollection(Collection<Enterprise> enterpriseCollection) {
        this.enterpriseCollection = enterpriseCollection;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "databag.User[ id=" + id + " ]";
    }
    
}

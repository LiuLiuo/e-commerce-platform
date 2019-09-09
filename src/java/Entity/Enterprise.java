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
 * @author 王淳铮
 */
@Entity
@Table(name = "enterprise")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Enterprise.findAll", query = "SELECT e FROM Enterprise e")
    , @NamedQuery(name = "Enterprise.findById", query = "SELECT e FROM Enterprise e WHERE e.id = :id")
    , @NamedQuery(name = "Enterprise.findByEnterpriseName", query = "SELECT e FROM Enterprise e WHERE e.enterpriseName = :enterpriseName")
    , @NamedQuery(name = "Enterprise.findByEnterpriseLogoPath", query = "SELECT e FROM Enterprise e WHERE e.enterpriseLogoPath = :enterpriseLogoPath")
    , @NamedQuery(name = "Enterprise.findByEnterpriseAddress", query = "SELECT e FROM Enterprise e WHERE e.enterpriseAddress = :enterpriseAddress")
    , @NamedQuery(name = "Enterprise.findByEnterpriseTel", query = "SELECT e FROM Enterprise e WHERE e.enterpriseTel = :enterpriseTel")})
public class Enterprise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "enterprise_name")
    private String enterpriseName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "enterprise_logo_path")
    private String enterpriseLogoPath;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "enterprise_address")
    private String enterpriseAddress;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "enterprise_tel")
    private String enterpriseTel;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "enterpriseId")
    private Collection<Item> itemCollection;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;

    public Enterprise() {
    }

    public Enterprise(Integer id) {
        this.id = id;
    }

    public Enterprise(Integer id, String enterpriseName, String enterpriseLogoPath, String enterpriseAddress, String enterpriseTel) {
        this.id = id;
        this.enterpriseName = enterpriseName;
        this.enterpriseLogoPath = enterpriseLogoPath;
        this.enterpriseAddress = enterpriseAddress;
        this.enterpriseTel = enterpriseTel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseLogoPath() {
        return enterpriseLogoPath;
    }

    public void setEnterpriseLogoPath(String enterpriseLogoPath) {
        this.enterpriseLogoPath = enterpriseLogoPath;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getEnterpriseTel() {
        return enterpriseTel;
    }

    public void setEnterpriseTel(String enterpriseTel) {
        this.enterpriseTel = enterpriseTel;
    }
    
    @XmlTransient
    public Collection<Item> getItemCollection() {
        return itemCollection;
    }
   
   
    public List<Item> transfer_coll_to_list()
    {
        List<Item> enterpriseItems = new ArrayList<Item>(itemCollection);
        return enterpriseItems;
    }
    
    public void setItemCollection(Collection<Item> itemCollection) {
        this.itemCollection = itemCollection;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Enterprise)) {
            return false;
        }
        Enterprise other = (Enterprise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.Enterprise[ Name=" + enterpriseName+ " ]";
    }
    
}

package Entity;

import Entity.util.JsfUtil;
import Entity.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("cartController")
@SessionScoped
public class CartController implements Serializable {

    @EJB
    private Entity.CartFacade ejbFacade;
    private List<Cart> items = null;
    private Cart selected;

    public CartController() {
    }

    public Cart getSelected() {
        return selected;
    }

    public void setSelected(Cart selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private CartFacade getFacade() {
        return ejbFacade;
    }

    public List<Cart> user_curCarts(int userId) {
        List<Cart> carts = getFacade().getCurCarts(userId);
        return carts;
    }

    public String removeCart(Cart cart) {
        try {
            ejbFacade.remove(cart);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "cart_1.xhtml";
    }

    public String removeCart1(Cart cart) {
        try {
            ejbFacade.remove(cart);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "cart.xhtml";
    }

    public Cart prepareCreate() {
        selected = new Cart();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CartCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CartUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CartDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Cart> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Cart getCart(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Cart> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Cart> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Cart.class)
    public static class CartControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CartController controller = (CartController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cartController");
            return controller.getCart(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Cart) {
                Cart o = (Cart) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Cart.class.getName()});
                return null;
            }
        }

    }

    public String IsItemEnough(Item i, int num, User user, int islogin) {
        if (islogin == 1) {
            int cur_Stock = i.getStock();
            if (cur_Stock - num > 0) {
                Cart c = new Cart();
                c.setCartItemNum(num);
                c.setCartSumPrice(num * i.getItemPrice());
                c.setItemId(i);
                c.setUserId(user);
                getFacade().createCartItem(c);
                return "cart.xhtml";
            } else {
                return "GoodDetail.xhtml";
            }
        } else {
            return "template";
        }
    }

}

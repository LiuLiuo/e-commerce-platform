package Entity;

import Entity.util.JsfUtil;
import Entity.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.Date;
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

@Named("orderDetailController")
@SessionScoped
public class OrderDetailController implements Serializable {

    @EJB
    private Entity.ItemFacade itemFacade;
    @EJB
    private Entity.OrderDetailFacade ejbFacade;
    @EJB
    private Entity.CartFacade cartFacade;
    private List<OrderDetail> items = null;
    private OrderDetail selected;
    private int money = 0;
    private int number;
    private List<OrderDetail> payedOrder;

    public void findAllOrder() {
        this.payedOrder = getFacade().findAll();
    }

    public void setState(String state, User user) {
        this.payedOrder = getFacade().getPayedOrder(state,user);
    }

    public List<OrderDetail> getPayedOrder() {
        return payedOrder;
    }

    public void setPayedOrder(List<OrderDetail> payedOrder) {
        this.payedOrder = payedOrder;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public OrderDetailController() {
    }

    public OrderDetail getSelected() {
        return selected;
    }

    public void setSelected(OrderDetail selected) {
        this.selected = selected;
    }

    //czg
    public String removeOrderDetail(OrderDetail order) {
        try {
            ejbFacade.remove(order);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/myorders_1.xhtml";
    }

    public String removeOrderDetail_1(OrderDetail order) {
        try {
            ejbFacade.remove(order);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/myorders.xhtml";
    }

    public List<OrderDetail> user_curODs(int userId) {
        List<OrderDetail> ods = getFacade().getCurODs(userId);
        return ods;
    }

//    //在商品详情页立即付款时，将订单加入我的订单中xxxxxx
//     public String updateOrder(int userId) {
//        List<OrderDetail> order = getFacade().getCurODs(userId);
//        for (int j = 0; j < order.size(); j++) {
//            getFacade().changeState(order.get(j));
//        }
//        return "template.xhtml?faces-redirect=true";
//    }
//     
//      public String updateOrder(Item item, User user) {
//        OrderDetail i = new OrderDetail();
//        i.setItemId(item);
//        i.setOrderItemNum(number);
//        i.setOrderState("已支付");
//        i.setOrderSumPrice(number * item.getItemPrice());
//        i.setOrderTime(new Date());
//        i.setUserId(user);
//        return "template.xhtml?faces-redirect=true";
//    }
    //购物车结算时，将订单加入我的订单
    public String updateOrder_State(int userId) {
        List<OrderDetail> order = getFacade().getCurODs(userId);
        for (int j = 0; j < order.size(); j++) {
//            int cur_SaleNum = order.get(j).getItemId().getItemSaleNum();
//            int cur_Stock = order.get(j).getItemId().getStock();
//            order.get(j).getItemId().setItemSaleNum(cur_SaleNum + number);
//            order.get(j).getItemId().setStock(cur_Stock - number);
//            getFacade().updateItem(order.get(j).getItemId());
            getFacade().changeState(order.get(j));
        }
        return "myorders.xhtml?faces-redirect=true";
    }

    public String createODItem(int userId) {
        money = 0;
        List<Cart> carts = getFacade().getCurCarts(userId);
        for (int j = 0; j < carts.size(); j++) {
            OrderDetail i = new OrderDetail();
            i.setOrderTime(new Date());
            i.setOrderItemNum(carts.get(j).getCartItemNum());
            i.setItemId(carts.get(j).getItemId());
            i.setUserId(carts.get(j).getUserId());
            i.setOrderSumPrice(carts.get(j).getCartItemNum() * carts.get(j).getItemId().getItemPrice());
            i.setOrderState("未支付");
            getFacade().createOrderItems(i);

            int cur_SaleNum = carts.get(j).getItemId().getItemSaleNum();
            int cur_Stock = carts.get(j).getItemId().getStock();
            carts.get(j).getItemId().setItemSaleNum(cur_SaleNum + carts.get(j).getCartItemNum());
            carts.get(j).getItemId().setStock(cur_Stock - carts.get(j).getCartItemNum());
            getFacade().updateItem(carts.get(j).getItemId());

            try {
                cartFacade.remove(carts.get(j));
            } catch (Exception e) {
                System.out.println(e);
            }
            money += carts.get(j).getCartSumPrice();

        }
        return "payment_1.xhtml?faces-redirect=true";
    }

    public List<Cart> getCurCarts(int userId) {
        List<Cart> ods = getFacade().getCurCarts(userId);
        return ods;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private OrderDetailFacade getFacade() {
        return ejbFacade;
    }

    public OrderDetail prepareCreate() {
        selected = new OrderDetail();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("OrderDetailCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("OrderDetailUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("OrderDetailDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<OrderDetail> getItems() {
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

    public OrderDetail getOrderDetail(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<OrderDetail> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<OrderDetail> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = OrderDetail.class)
    public static class OrderDetailControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            OrderDetailController controller = (OrderDetailController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "orderDetailController");
            return controller.getOrderDetail(getKey(value));
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
            if (object instanceof OrderDetail) {
                OrderDetail o = (OrderDetail) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), OrderDetail.class.getName()});
                return null;
            }
        }

    }

    public String instantBuy(int num, User user, Item item, int islogin) {
        if (islogin == 1) {
            OrderDetail i = new OrderDetail();
            i.setOrderTime(new Date());
            i.setOrderItemNum(num);
            i.setItemId(item);
            i.setUserId(user);
            i.setOrderSumPrice(num * item.getItemPrice());
            i.setOrderState("未支付");
            getFacade().createOrderItems(i);
            return "payment.xhtml";
        } else {
            return "template.xhtml";
        }
    }

    //czg.立即购买改状态
    public String updateItem(Item i, int num, int userId) {
        int cur_SaleNum = i.getItemSaleNum();
        int cur_Stock = i.getStock();

        if (cur_Stock - num > 0) {
            i.setItemSaleNum(cur_SaleNum + num);
            i.setStock(cur_Stock - num);
            itemFacade.updateItem(i);
            //更改订单状态
            List<OrderDetail> order = getFacade().getCurODs(userId);
            getFacade().changeState(order.get(order.size() - 1));
            return "myorders.xhtml";
        } else {
            return "GoodDetail.xhtml";
        }
    }

}

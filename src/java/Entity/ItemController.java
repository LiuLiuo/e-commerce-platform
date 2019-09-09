package Entity;

import Entity.util.JsfUtil;
import Entity.util.JsfUtil.PersistAction;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("itemController")
@SessionScoped
public class ItemController implements Serializable {

    @EJB
    private Entity.ItemFacade ejbFacade;
    private List<Item> items = null;
    private Item selected;
    private Item curItem;
    private List<Item> firstflooritem;
    private List<Item> secondflooritem;
    private List<Item> thirdflooritem;
    private String itemName = "";
    private int stock = 0;
    private int itemPrice = 0;
    private String itemPicPath = null;
    private String itemTag = null;
    private int itemSaleNum = 0;
    private String enterpriseName;
    private Enterprise enterprise;
    private UploadedFile cur_upload_file;
    private boolean is_upload = false;
    private String tempPath;
    private String tagName;
    private List<Item> itemss = null;
    private List<Item> Tag1 = null;
    private List<Item> Tag2 = null;
    private List<Item> Tag3 = null;
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext extContext = facesContext.getExternalContext();
    HttpSession session = (HttpSession) extContext.getSession(true);

    public List<Item> getTag1() {
        Tag1 = getFacade().getItemList("女装");
        return Tag1;
    }

    public List<Item> getTag2() {
        Tag2 = getFacade().getItemList("智能产品");
        return Tag2;
    }

    public List<Item> getTag3() {
        Tag3 = getFacade().getItemList("食品");
        return Tag3;
    }
    
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<Item> getItemss() {
        itemss = getFacade().getItemList(tagName);
        return itemss;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public Item getCurItem() {
        return curItem;
    }

    public void setCurItem(Item curItem) {
        this.curItem = curItem;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
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

    public int getItemSaleNum() {
        return itemSaleNum;
    }

    public void setItemSaleNum(int itemSaleNum) {
        this.itemSaleNum = itemSaleNum;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String itemEnterpriseName) {
        this.enterpriseName = itemEnterpriseName;
    }

    public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise e) {
        this.enterprise = e;
    }

    /**平雅霓*/
    public List<Item> getFirstflooritem(int times) {
        if (times == 1) {
            this.firstflooritem = getFacade().getRandomItem("智能产品");
        }
        return firstflooritem;
    }

    public void setFirstflooritem(List<Item> firstflooritem) {
        this.firstflooritem = firstflooritem;
    }

    public List<Item> getSecondflooritem(int times) {
        if (times == 1) {
            this.secondflooritem = getFacade().getRandomItem("女装");
        }
        return secondflooritem;
    }

    public void setSecondflooritem(List<Item> secondflooritem) {
        this.secondflooritem = secondflooritem;
    }

    public List<Item> getThirdflooritem(int times) {
        if (times == 1) {
            this.thirdflooritem = getFacade().getRandomItem("食品");
        }
        return thirdflooritem;
    }

    public void setThirdflooritem(List<Item> thirdflooritem) {
        this.thirdflooritem = thirdflooritem;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        is_upload = true;
        cur_upload_file = file;
        this.FileSave(file.getInputstream(), file.getFileName(), 1);

    }

    public String create_item(String enterprise_name) throws IOException {
        itemPicPath = "images/" + itemName + ".jpg";

        File file = new File(session.getServletContext().getRealPath(".") + "/resources/" + "images/temp/" + cur_upload_file.getFileName());
        InputStream inputStream = new FileInputStream(file);
        FileSave(inputStream, itemPicPath, 0);

//        if (is_upload == true) {
//            File file = new File(session.getServletContext().getRealPath("") + "resources/images/temp/" + cur_upload_file.getFileName());
//            InputStream inputStream = new FileInputStream(file);
//            FileSave(inputStream, itemPicPath, 0);
//        }
        getFacade().create_enterprise_item(itemName, stock, itemPrice, itemPicPath, itemTag, itemSaleNum, enterprise_name);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("商品添加成功"));
        is_upload=false;
        return "enterprise_homepage.xhtml";
    }

    public String edit_item(Item i,String pic) throws IOException {
        itemPicPath = "images/" + cur_upload_file.getFileName();

        File file = new File(session.getServletContext().getRealPath(".") + "/resources/" + "images/temp/" + cur_upload_file.getFileName());
        InputStream inputStream = new FileInputStream(file);
        FileSave(inputStream, itemPicPath, 0);

        getFacade().edit_item_info(i,pic);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("商品信息修改成功"));
        is_upload=false;
        return "enterprise_homepage.xhtml";
    }

    public void FileSave(InputStream inputStream, String imgPath, int i) throws IOException {
        String root = session.getServletContext().getRealPath(".");
        //构造一个文件，保存图片到项目的根目录下
        String path;
        if (i == 0) {
            path = root + "/../../web/resources/" + imgPath;
        } else {
            path = root + "/resources/" + "images/temp/" + cur_upload_file.getFileName();
        }
//        String root = session.getServletContext().getRealPath("/");
//        //构造一个文件，保存图片到项目的根目录下
//        String path;
//        if (i == 0) {
//            path = root + "resources\\" + imgPath;
//        } else {
//            path = root + "resources\\images\\temp\\" + cur_upload_file.getFileName();
//        }
        //创建一个Buffer字符串
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while ((len = inputStream.read(buffer)) != -1) {
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inputStream.close();
            //把outStream里的数据写入内存

            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = outStream.toByteArray();
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(path);
            imageFile.createNewFile();
            //写入数据
            try ( //创建输出流
                    FileOutputStream fileOutStream = new FileOutputStream(imageFile)) {
                //写入数据
                fileOutStream.write(data);
            } catch (IOException e) {
            }
        } catch (IOException e) {
        }
    }

    public String getTempPath() {
        if (is_upload) {
            tempPath = cur_upload_file.getFileName();
            return tempPath;
        } else {
            return null;
        }

    }

    public ItemController() {
    }

    public Item getSelected() {
        return selected;
    }

    public void setSelected(Item selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ItemFacade getFacade() {
        return ejbFacade;
    }

    public Item prepareCreate() {
        selected = new Item();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Item> getItems() {
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

    public Item getItem(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Item> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Item> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Item.class)
    public static class ItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemController controller = (ItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemController");
            return controller.getItem(getKey(value));
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
            if (object instanceof Item) {
                Item o = (Item) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Item.class.getName()});
                return null;
            }
        }

    }


    public String updateItem(Item i, int num) {
        int cur_SaleNum = i.getItemSaleNum();
        int cur_Stock = i.getStock();

        if (cur_Stock - num > 0) {
            i.setItemSaleNum(cur_SaleNum + num);
            i.setStock(cur_Stock - num);
            getFacade().updateItem(i);
            return "myorders.xhtml";
        } else {
            return "GoodDetail.xhtml";
        }
    }
}

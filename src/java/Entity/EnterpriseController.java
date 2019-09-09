package Entity;

import Entity.util.JsfUtil;
import Entity.util.JsfUtil.PersistAction;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
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
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpSession;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("enterpriseController")
@SessionScoped
public class EnterpriseController implements Serializable {

    @EJB
    private Entity.EnterpriseFacade ejbFacade;
    private List<Enterprise> items = null;
    private Enterprise selected;

    //当前企业状态
    private boolean is_login = false;
    private Enterprise curEnterprise;
    private String EnterpriseName;
    private String EnterpriseLogoPath;
    private String EnterpriseAddress;
    private String EnterpriseTel;
    private UploadedFile cur_upload_file = null;
    private boolean is_upload = false;
    
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext extContext = facesContext.getExternalContext();
    HttpSession session = (HttpSession) extContext.getSession(true);

    //创建一个User为当前账号的企业
    public String createEnterprise(User userid) throws IOException {
        Enterprise temp = getFacade().getIsDuplicate(EnterpriseName);

        if (temp == null && is_upload == true) {
            EnterpriseLogoPath = "images/" + EnterpriseName + ".jpg";

            File file = new File(session.getServletContext().getRealPath(".") +"/resources/" + "images/temp/" + cur_upload_file.getFileName());
            InputStream inputStream = new FileInputStream(file);
            FileSave(inputStream, EnterpriseLogoPath, 0);

            getFacade().create_Enterprise(EnterpriseName, EnterpriseLogoPath, EnterpriseAddress, EnterpriseTel, userid);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("企业注册申请已提交，请等待审核"));
            is_upload = false;
            EnterpriseAddress = null;
            EnterpriseLogoPath = null;
            EnterpriseName = null;
            EnterpriseTel = null;
            return "/login.xhtml";
        } else if (is_upload == true) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("企业名称已存在，请重新输入"));
            return "/enterprise_signup_2.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("请上传图像！"));
            return "/enterprise_signup_2.xhtml";
        }
    }

    
    
     
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        is_upload = true;
        
        cur_upload_file = file;
        this.FileSave(file.getInputstream(), file.getFileName(), 1);
    }

    public void FileSave(InputStream inputStream, String imgPath, int i) throws IOException {
        String root = session.getServletContext().getRealPath(".");
        //构造一个文件，保存图片到项目的根目录下
        String path;
        if (i == 0) {
            path = root + "/../../web/resources/" + imgPath;
        } else {
            path = root +"/resources/" + "images/temp/" + cur_upload_file.getFileName();
        }
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
    
    
    
    public String getPicturePath() {
        
            if (is_upload) 
                return cur_upload_file.getFileName();
            else
                return null;
                
    }
    
    public void accept(){
        getFacade().accept(selected);
    }
    
    public void refuse(){
        getFacade().delete(selected);
    }
    
    public List<Enterprise> getAllUnCheckEnterprise(){
        return getFacade().getAllUnCheckEnterprise();
    }

     public List<Item> getcurItems(String name){
      List<Item> curItems=getFacade().getCurItems(name);
        return curItems;
     
    }
    public EnterpriseController() {
    }

    public Enterprise getSelected() {
        return selected;
    }

    public void setSelected(Enterprise selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EnterpriseFacade getFacade() {
        return ejbFacade;
    }

    public Enterprise prepareCreate() {
        selected = new Enterprise();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EnterpriseCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EnterpriseUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EnterpriseDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Enterprise> getItems() {
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

    public Enterprise getEnterprise(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Enterprise> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Enterprise> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public Enterprise getCurEnterprise() {
        return curEnterprise;
    }

    public void setCurEnterprise(Enterprise curEnterprise) {
        this.curEnterprise = curEnterprise;
    }

    public String getEnterpriseName() {
        return EnterpriseName;
    }

    public void setEnterpriseName(String EnterpriseName) {
        this.EnterpriseName = EnterpriseName;
    }

    public String getEnterpriseLogoPath() {
        return EnterpriseLogoPath;
    }

    public void setEnterpriseLogoPath(String EnterpriseLogoPath) {
        this.EnterpriseLogoPath = EnterpriseLogoPath;
    }

    public String getEnterpriseAddress() {
        return EnterpriseAddress;
    }

    public void setEnterpriseAddress(String EnterpriseAddress) {
        this.EnterpriseAddress = EnterpriseAddress;
    }

    public String getEnterpriseTel() {
        return EnterpriseTel;
    }

    public void setEnterpriseTel(String EnterpriseTel) {
        this.EnterpriseTel = EnterpriseTel;
    }

    public UploadedFile getCur_upload_file() {
        return cur_upload_file;
    }

    public boolean get_isUpload(){
        return is_upload;
    }
    
    @FacesConverter(forClass = Enterprise.class)
    public static class EnterpriseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EnterpriseController controller = (EnterpriseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "enterpriseController");
            return controller.getEnterprise(getKey(value));
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
            if (object instanceof Enterprise) {
                Enterprise o = (Enterprise) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Enterprise.class.getName()});
                return null;
            }
        }

    }

}

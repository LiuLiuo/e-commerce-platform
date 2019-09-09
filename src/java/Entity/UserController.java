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
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
//    private Entity.EnterpriseFacade enterprise_ejbFacade;
    private Entity.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;
    //是否处于登录状态
    private boolean is_log_in = false;
    private String username;
    private String password;
    private User current_user;
    private User curUser;
    private String web1;
    private String web2;

    private List<OrderDetail> orderCollection;
    private Enterprise current_enterprise;
    private UploadedFile cur_upload_file = null;
    private boolean is_upload = false;
    private String UserLogoPath;
    private String userNickName;
    private String userAddress;

    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExternalContext extContext = facesContext.getExternalContext();
    HttpSession session = (HttpSession) extContext.getSession(true);

    public String getUserNickName() {
        return userNickName;

    }

    public String getUserAddress() {
        return userAddress;

    }

    public void setUserNickName(String s) {
        this.userNickName = s;
    }

    public void setUserAddress(String s) {
        this.userAddress = s;
    }

    public String getWeb1() {
        if (is_log_in) {
            web1 = "cart.xhtml";
        } else {
            web1 = "login.xhtml";
        }
        return web1;
    }

    public void setWeb1(String web1) {
        this.web1 = web1;
    }

    public String getWeb2() {
        if (is_log_in) {
            web2 = "myorders.xhtml";
        } else {
            web2 = "login.xhtml";
        }
        return web2;
    }

    public void setWeb2(String web2) {
        this.web2 = web2;
    }

    /**
     * 平雅霓 获取当前的登录状态
     */
    public int getis_log_in() {
        if (is_log_in == false) {
            return 0;
        } else {
            return 1;
        }
    }

    public String setState(int state) {
        if (state == 0) {
            this.is_log_in = false;
        } else {
            this.is_log_in = true;
        }
        return "template.xhtml";
    }

    /*平雅霓更改头像*/
    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile file = event.getFile();
        is_upload = true;
        cur_upload_file = file;
        this.FileSave(file.getInputstream(), file.getFileName(), 1);
    }

    public String fileUpdate() {
        try {
            if (is_upload == true) {
                UserLogoPath = "user_logo/" + current_user.getId() + ".jpg";
                //获取当前缓存文件
                File file = new File(session.getServletContext().getRealPath(".") + "/resources/" + "images/temp/" + cur_upload_file.getFileName());
                InputStream inputStream = new FileInputStream(file);
                FileSave(inputStream, UserLogoPath, 0);
                FileSave(inputStream, UserLogoPath, 2);
//                getFacade().upDateUserPicPath(current_user, UserLogoPath);
            }
        } catch (Exception exception) {
        }
        return "personal_center.xhtml";
    }

    public void FileSave(InputStream inputStream, String imgPath, int i) throws IOException {
        String root = session.getServletContext().getRealPath(".");
        //构造一个文件，保存图片到项目的根目录下
        String path;
        if (i == 0) {
            path = root + "/../../web/resources/" + imgPath;
        } else if (i==1){
            path = root + "/resources/" + "images/temp/" + cur_upload_file.getFileName();
        } else
            path = root+"/resources/"+imgPath;
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

        if (is_upload) {
            return cur_upload_file.getFileName();
        } else {
            return null;
        }

    }

    public UploadedFile getCur_upload_file() {
        return cur_upload_file;
    }

    public boolean get_isUpload() {
        return is_upload;
    }

    public String edit_user() {
        getFacade().editInfo(current_user, userNickName, userAddress);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("用户信息修改成功"));
        return "personal_center.xhtml";
    }

    public String delete(OrderDetail item) {
        orderCollection.remove(item);
        return null;
    }

    public List<OrderDetail> transfer_od_to_list(int id) {
        curUser = getFacade().getCurUser(id);
        orderCollection = new ArrayList<OrderDetail>(curUser.getOrderDetailCollection());
        return orderCollection;
    }

    //调用该函数执行登录操作
    public String login(String urlString) {
        User temp = getFacade().getValidateLogin(username, password);

        if (temp != null) {
            if (temp.getVerifyState() == 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage("该企业尚未通过验证"));
                return urlString;
            } else {
                current_user = temp;
                is_log_in = true;
//                FacesContext.getCurrentInstance().addMessage(null,
//                        new FacesMessage("成功登录"));

                //判断此时user的角色，要判断！！！
                if (current_user.getRole().compareTo("A") == 0) {
                    current_enterprise = null;
                    return "administrator.xhtml";
                } else if (current_user.getRole().compareTo("E") == 0) {
                    current_enterprise = getFacade().getCurrentEnterprise(current_user);
                    return "enterprise_homepage.xhtml";
                } else {
                    return "template.xhtml";
                }
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("用户名与密码不匹配，请重新输入"));
        }

        return urlString;
    }
    //调用该函数执行用户注册操作

    public String user_signup() {
        User temp = getFacade().getIsDuplicate(username);

        if (temp == null) {
            getFacade().createUser(username, password);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("注册成功"));
            return "/login.xhtml";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("用户名已存在，请重新输入"));
        }

        return "/user_signup.xhtml";
    }

    //调用该函数执行企业账号注册操作，转到企业信息填写页面
    public String enterprise_signup() {
        User temp = getFacade().getIsDuplicate(username);

        if (temp != null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("用户名已存在，请重新输入"));
            return "/enterprise_signup_1.xhtml";
        } else {
            current_user = getFacade().createEnterprise(username, password);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("请输入企业信息"));

            return "/enterprise_signup_2.xhtml";
        }
    }

    public Enterprise getCurrent_enterprise() {
        return current_enterprise;
    }

    public void setCurrent_enterprise(Enterprise current_enterprise) {
        this.current_enterprise = current_enterprise;
    }

    public User getSelected() {
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public User prepareCreate() {
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<User> getItems() {
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

    public User getUser(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(User current_user) {
        this.current_user = current_user;
    }

    public void setIs_log_in(boolean is_log_in) {
        this.is_log_in = is_log_in;
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}

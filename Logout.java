package f15g110;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
@ManagedBean
public class Logout {
public String logout(){
	 FacesContext facesContext = FacesContext.getCurrentInstance();
     HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(false);
     httpSession.invalidate();
     return "logout";
}
}

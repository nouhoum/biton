package controllers;

import models.Category;
import models.Setting;
import models.User;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {
	@Before(priority=0)
	static void globals() {
		renderArgs.put("setting", setting());
		renderArgs.put("connectedUser", connectedUser());
		renderArgs.put("loggedInUser", connectedUser());
		renderArgs.put("categories", Category.findAll());
	}
	
	static Setting setting() {
		return Setting.instance();
	}
	
	static boolean isUserConnected() {
    	return connectedUser() != null;
    }
    
    static void connect(User user) {
    	session.put("loggedInUser", user.id);
    }
    
    static User connectedUser() {
    	String userId = session.get("loggedInUser");
    	return (userId == null ? null : User.findById(Long.parseLong(userId)));
    }
    
    static void informSuccess(String message) {
    	flash("info", message);
    	flash("infotype", "success");
    }
    
    static void informError(String message) {
    	flash("info", message);
    	flash("infotype", "error");
    }
}
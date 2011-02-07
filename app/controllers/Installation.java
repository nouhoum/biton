package controllers;

import models.Category;
import models.Setting;
import models.User;
import play.data.validation.Required;
import play.i18n.Messages;
import play.mvc.Controller;

public class Installation extends Application {
	public static void index() {
		Setting setting = Setting.instance();
		if(setting == null || !setting.installed) {
			render();
		} else {
			Blog.index();
		}
	}
	
	public static void install(
			String fullName, 
			@Required String username, 
			@Required String email, 
			@Required String password,
			@Required String title,
			String description) {
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			informError(Messages.get("Oops un utilisateur avec cette adresse email existe déjà!"));
			index();
		} else {
			new User(true, fullName, username, email, password).insert();
			new Setting(true, email, title, description, fullName).insert();
			new Category("Default", "Default category").insert();
			redirect("/");
		}
	}
}

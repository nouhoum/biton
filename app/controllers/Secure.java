package controllers;

import play.mvc.Before;

public class Secure extends Application {
	@Before(priority=2)
	static void before() {
		if(!isUserConnected()) {
			flash("info", "Vous devez vous authentifier pour accéder à la page demandée.");
			redirect("/signin");
		}
	}
}

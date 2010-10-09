package models;

import play.libs.Crypto;
import siena.Id;
import siena.Model;
import siena.Query;

public class User extends Model {
	@Id
	public Long id;
	public String email;
	public String password;
	public String username;
	public String fullName;
	public boolean isAdmin;
	public String description;
	
	public User(String fullName, String username, String email, String password) {
		this.email = email;
		this.password = Crypto.passwordHash(password);
		
		this.username = username;
		this.fullName = fullName;
	}
	
	public User(boolean isAdmin, String fullName, String username, String email, String password) {
		this(fullName, username, email, password);
		this.isAdmin = isAdmin;
	}
	
	public String sreenName() {
		return fullName == null ? username : fullName; 
	}
	
	public static User instance() {
		return all().get();
	}
	
	public static Query<User> all() {
		return Model.all(User.class);
	}
	
	public static User findById(Long id) {
        return all().filter("id", id).get();
	}
	
	public static User findByUsername(String username) {
		return all().filter("username", username).get();
	}
	
	public static User authenticate(String email, String password) {
		return User.all().filter("email", email).filter("password", Crypto.passwordHash(password)).get();
	}
}

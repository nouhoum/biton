package models;

import siena.Id;
import siena.Model;

/**
 * Engine settings.
 * 
 * @author nouhoum
 */
public class Setting extends Model {
	@Id
	public Long id;
	public Photo avatar;
	public String email;
	public String title;
	public String fullName;
	public boolean installed;
	/**
	 * The profile description.
	 */
	public String description;
	
	public Setting(boolean installed, String email, String title, String description, String fullName) {
		this.email = email;
		this.title = title;
		this.installed = installed;
		this.description = description;
		this.fullName = fullName;
	}
	
	public static Setting instance() {
		return Setting.all(Setting.class).get();
	}
}

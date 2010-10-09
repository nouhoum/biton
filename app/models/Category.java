package models;

import java.util.List;

import siena.Filter;
import siena.Id;
import siena.Model;
import siena.Query;

public class Category extends Model {
	@Id
	Long id;
	public String title;
	public String description;
	
	@Filter("category")
	public Query<Post> posts;
	
	public Category(String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	public int countOfOnlinePost() {
		return Post.all().filter("category", this)
		                 .filter("online", true)
		                   .count();
	}
	
	public static Category findByTitle(String title) {
		return all().filter("title", title).get();
	}
	
	public static Category findById(Long id) {
		return all().filter("id", id).get();
	}
	
	public static List<Category> findAll() {
		return all().fetch();
	}
	
	public static Query<Category> all() {
		return all(Category.class);
	}
	
	@Override
	public String toString() {
		return title;
	}
}

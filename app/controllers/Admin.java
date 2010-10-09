package controllers;

import java.util.List;

import com.google.appengine.api.datastore.Blob;

import models.Category;
import models.Photo;
import models.Post;
import models.Setting;
import play.data.Upload;
import play.data.validation.Required;
import play.i18n.Messages;
import utils.Posts;

public class Admin extends Secure {
	public static void index() {
		List<Category> categories = Category.findAll();
		List<Post> posts = Post.findAll();
		int onlineCount = Post.onlineCount();
		int offlineCount = Post.offlineCount();
		render(categories, posts, onlineCount, offlineCount);
	}
	
	public static void unpublishPost(Long id) {
		Post post = Post.findById(id);
		notFoundIfNull(post);
		post.unpublish().update();
		postsList();
	}
	
	public static void publishPost(Long id) {
		Post post = Post.findById(id);
		notFoundIfNull(post);
		post.publish().update();
		postsList();
	}
	
	public static void postsList() {
		List<Post> posts = Post.all().order("-postedAt").fetch();
		render(posts);
	}
	
	public static void categoriesList() {
		List<Category> categories = Category.all().order("title").fetch();
		render(categories);
	}
	
	public static void categoryForm(Long id) {
		if(id != null) {
			Category category = Category.findById(id);
			render(category);
		}
		render();
	}
	
	public static void createCategory(Long id, @Required String title, String description) {
		System.out.println("====> ID = " + id);
		Category category = null;
		if(id == null) {
			category = new Category(title, description);
		} else {
			category = Category.findById(id);
			notFoundIfNull(category);
			category.title = title;
			category.description = description;
		}
		
		validation.valid(category);		
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			informError(Messages.get("error.invalidInfo"));
			render("@categoryForm", category);
		} 
		if(id == null){
			category.insert();
		} else {
			category.update();
		}			
		categoriesList();
	}
	
	public static void deleteCategory(Long id) {
		Category category = Category.findById(id);
		notFoundIfNull(category);
		category.delete();
		categoriesList();
	}
	
	public static void postForm(Long id) {
		List<Category> categories = Category.findAll();
		if(id != null) {
			Post post = Post.findById(id);
			notFoundIfNull(post);
			render(post, categories);
		}
		render(categories);
	}
	
	public static void create(Long id, @Required String title, String content, @Required Long categoryId) {
		System.out.println("Content is = " + content);
		Post post = null;
		Category category = Category.findById(categoryId);
		
		if(id == null) {
			post = new Post(connectedUser().sreenName(), title,	new Blob(content.getBytes()), 
					Posts.makeUrl(title), category);
		} else {
			post = Post.findById(id);
			post.title = title;
			post.content = new Blob(content.getBytes());
			post.category = category;
		}
		validation.valid(post);
		
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			informError(Messages.get("Information incorrecte"));
			render("@postForm", post);
		} 
		if(id == null) { //Creating a new post.
			post.insert();
		} else { //Updating a post.
			post.update();
		}
		
		if(category != null) category.update();
		
		postsList();
	}
	
	public static void deletePost(Long id) {
		Post post = Post.findById(id);
		notFoundIfNull(post);
		post.delete();
		postsList();
	}
	
	public static void settings() {
		render();
	}
	
	public static void uploadPhoto(@Required Upload photo) {
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			settings();
		}
		Setting setting = Setting.instance();
		if(setting != null) {
			byte[]data = photo.asBytes();
			if(setting.avatar == null){
				Photo avatar = new Photo(setting.email, new Blob(data));
				avatar.insert();
				setting.avatar = avatar;
			} else {
				setting.avatar.data = new Blob(data);
				setting.avatar.update();
			}			
			setting.update();
		}
		redirect("/");
	}
}

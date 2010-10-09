package controllers;

import java.io.ByteArrayInputStream;
import java.util.List;

import play.data.validation.Required;
import play.i18n.Messages;

import models.Category;
import models.Comment;
import models.Photo;
import models.Post;
import models.Setting;
import models.User;

public class Blog extends Application {
	public static void index() {
		List<Post> posts = Post.all().filter("online", true).order("-postedAt").fetch();
		render(posts);
	}
	
	public static void avatar() {
		Setting setting = Setting.instance();
		if(setting != null) {
			renderBinary(new ByteArrayInputStream(Photo.findByTitle(setting.email).asBytes()));
		}
	}
	
	public static void view(String url) {
		Post post = Post.all().filter("online", true).filter("url", url).get();
		notFoundIfNull(post);
		post.views++;
		post.update();
		List<Comment> comments = post.comments();
		int commentCount = comments.size();
		render(post, comments, commentCount);
	}
	
	public static void postsOfCategory(String title) {
		Category category = Category.findByTitle(title);
		notFoundIfNull(category);
		List<Post> posts = Post.all().filter("category", category).fetch();
		render(title, posts);
	}
	
	public static void viewCategory(String title) {
		//TODO
	}
	
	public static void commentForm() {
		//render();
	}
	
	public static void createComment(
			@Required Long postId,
			@Required String url,
			@Required String author, 
			@Required String content) {
		System.out.println("Url " + url);
		if(validation.hasErrors()) {
			validation.keep();
			params.flash();
			informError(Messages.get("error.invalidInfo"));
		} else {
			Post post = Post.findById(postId);
			new Comment(post, content, author).insert();
			post.commentCount++;
			post.update();
			informSuccess(Messages.get("info.success"));			
		}	
		view(url);
	}
	
	public static void login() {
		render();
	}
	
	public static void logout() {
		session.clear();
    	redirect("/");
	}
	
    public static void authenticate(String email, String password) {
    	User user = User.authenticate(email, password);
    	if(user == null) {    		
    		informError(Messages.get("Information incorrecte"));
    		login();
    	}
    	connect(user);
    	index();
    }
    
	public static void about() {
		render();
	}
}

package controllers;

import java.io.ByteArrayInputStream;
import java.util.List;

import play.cache.Cache;
import play.data.validation.Required;
import play.i18n.Messages;
import play.libs.Codec;
import play.libs.Images;

import models.Category;
import models.Comment;
import models.Photo;
import models.Post;
import models.Setting;
import models.User;

/**
 * This represents the main controller of the blog engine.
 * 
 * @author nouhoum
 *
 */
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
	
	public static void view(Long id , String url) {
		Post post = Post.all().filter("online", true).filter("id", id).get();
		notFoundIfNull(post);
		post.views++;
		post.update();
		List<Comment> comments = post.comments();
		int commentCount = comments.size();
		String randomID = Codec.UUID();
		render(post, comments, commentCount, randomID);
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
			@Required(message="Please type a content") String content,
			@Required String code,
			String randomID) {
		validation.equals(code, Cache.get(randomID)).message("Invalid code! Retry please.");
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
			Cache.delete(randomID);
		}	
		view(postId, url);
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
    
    public static void showCaptcha(String id) {
    	Images.Captcha captcha = Images.captcha();
    	String code = captcha.getText("#111");
    	Cache.set(id, code, "10mn");
    	renderBinary(captcha);
    }
    
	public static void about() {
		render();
	}
}

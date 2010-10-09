package models;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Blob;

import siena.Id;
import siena.Model;
import siena.Query;

public class Post extends Model {
	@Id
	public Long id;

	public int views;
	public String url;
	public String title;
	public Date postedAt;
	public Blob content;
	public String author;
	public boolean online;
	public int commentCount;
	public Category category;
	
	public Post(String author, String title, Blob content, String url) {
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
		this.url = url;
	}
	
	public Post(String author, String title, Blob content, String url, Category category) {
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
		this.url = url;
		this.category = category;
	}
	
	public Post publish() {
		this.online = true;
		return this;
	}
	
	public Post unpublish() {
		this.online = false;
		return this;
	}
	
	public static int onlineCount() {
		return all().filter("online", true).count();
	}
	
	public static int offlineCount() {
		return all().filter("online", false).count();
	}
	
	public String contentAsString() {
		return new String(content.getBytes());
	}
	
	public List<Comment> comments() {
		return Comment.all().filter("post", this).order("-postedAt").fetch();
	}
	
	public static Post findByUrl(String url) {
		return all().filter("url", url).get();
	}
	
	public static List<Post> findAll() {
		return all().fetch();
	}
	
	public static Query<Post> all() {
		return Model.all(Post.class);
	}
	
	public static Post findById(Long id) {
		return all().filter("id", id).get();
	}
	
	@Override
	public String toString() {
		return title;
	}
}

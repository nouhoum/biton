package models;

import java.util.Date;

import play.data.validation.Required;

import siena.Id;
import siena.Model;
import siena.Query;

public class Comment extends Model {
	@Id
	public Long id;
	
	@Required
	public Post post;
	public String author;
	public String content;
	public Date postedAt;
	
	public Comment(Post post, String content, String author) {
		this.post = post;
		this.content = content;
		this.author = author;
		this.postedAt = new Date();
	}
	
	public static Query<Comment> all() {
		return all(Comment.class);
	}
}

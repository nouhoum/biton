package models;

import com.google.appengine.api.datastore.Blob;

import siena.Id;
import siena.Model;
import siena.Query;

public class Photo extends Model {
	@Id
	public Long id;
	
	public String title;
	public Blob data;
	
	public Photo(String title, Blob data) {
		this.title = title;
		this.data = data;
	}
	
	public static Photo findByTitle(String title) {
		return all().filter("title", title).get();
	}
	
	public static Query<Photo> all() {
		return all(Photo.class);
	}
	
	public byte[] asBytes() {
		return data.getBytes();
	}
}

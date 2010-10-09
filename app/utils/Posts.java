package utils;

public class Posts {
	public static String makeUrl(String title) {
		String result = title.replaceAll(" ", "-");
		return result.replaceAll("[!?]", "").toLowerCase();
	}
} 

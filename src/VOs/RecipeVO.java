package VOs;

import java.sql.Timestamp;

public class RecipeVO {

	private int no;
	private String id;
	private String title;
	private String thumbnail;
	private String description;
	private String contents;
	private int category;
	private int views;
	private float rating;
	private String ingredient;
	private String ingredientAmount;
	private String orders;
	private int empathy;
	private Timestamp postDate;

	public RecipeVO() {
	}

	public RecipeVO(int no, String id, String title, String thumbnail, String description, String contents,
			int category, int views, float rating, String ingredient, String ingredientAmount, String orders,
			int empathy) {

		this.no = no;
		this.id = id;
		this.title = title;
		this.thumbnail = thumbnail;
		this.description = description;
		this.contents = contents;
		this.category = category;
		this.views = views;
		this.rating = rating;
		this.ingredient = ingredient;
		this.ingredientAmount = ingredientAmount;
		this.orders = orders;
		this.empathy = empathy;
	}

	public RecipeVO(int no, String id, String title, String thumbnail, String description, String contents,
			int category, int views, float rating, String ingredient, String ingredientAmount, String orders,
			int empathy, Timestamp postDate) {

		this(no, id, title, thumbnail, description, contents, category, 
				views, rating, ingredient, ingredientAmount, orders, empathy);
		this.postDate = postDate;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getIngredient() {
		return ingredient;
	}

	public void setIngredient(String ingredient) {
		this.ingredient = ingredient;
	}

	public String getIngredientAmount() {
		return ingredientAmount;
	}

	public void setIngredientAmount(String ingredientAmount) {
		this.ingredientAmount = ingredientAmount;
	}

	public String getOrders() {
		return orders;
	}

	public void setOrders(String orders) {
		this.orders = orders;
	}

	public int getEmpathy() {
		return empathy;
	}

	public void setEmpathy(int empathy) {
		this.empathy = empathy;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}
}

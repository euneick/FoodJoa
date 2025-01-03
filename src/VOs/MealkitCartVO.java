package VOs;

import java.sql.Timestamp;

public class MealkitCartVO {
		
	private int no;
	private String id;
	private int mealkit_no;
	private int quantity;
	private Timestamp choice_date;
	
	public MealkitCartVO() {
	
	}	
	
	public MealkitCartVO(int no, String id, int mealkit_no, int quantity) {
		
		this.no = no;
		this.id = id;
		this.mealkit_no = mealkit_no;
		this.quantity = quantity;

	}
	
	public MealkitCartVO(int no, String id, int mealkit_no, int quantity, Timestamp choice_date ) {
		
		this.no = no;
		this.id = id;
		this.mealkit_no = mealkit_no;
		this.quantity = quantity;
		this.choice_date = choice_date;	
		
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

	public int getMealkit_no() {
		return mealkit_no;
	}

	public void setMealkit_no(int mealkit_no) {
		this.mealkit_no = mealkit_no;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Timestamp getChoice_date() {
		return choice_date;
	}

	public void setChoice_date(Timestamp choice_date) {
		this.choice_date = choice_date;
	}
	
	
	
	
	
}

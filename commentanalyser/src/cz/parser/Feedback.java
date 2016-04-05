package cz.parser;

public class Feedback {
	
	private String feedback;
	private boolean isPositive;
	public Feedback(){
		feedback = new String();
		isPositive = true;
	}
	
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public boolean isPositive() {
		return isPositive;
	}
	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}
	
}

package cz.parser;

import java.util.ArrayList;
import java.util.List;

public class Entity {
	private String link;
	private String imageLink;
	private String feedbackLink;
	private List<Feedback> feedbackList;
	private boolean isPositive;
	
	public Entity() {
		this.link = new String();
		this.imageLink = new String();
		this.feedbackLink = new String();
		this.feedbackList = new ArrayList<>();
		isPositive = true;
	}

	public boolean isPositive() {
		return isPositive;
	}

	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}


	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getFeedbackLink() {
		return feedbackLink;
	}

	public void setFeedbackLink(String feedbackLink) {
		this.feedbackLink = feedbackLink;
	}
	
	public void addFeedback(List<Feedback> f){
		feedbackList.addAll(f);
	}
	public List<Feedback> getFeedbackList(){
		return feedbackList;
	}
}

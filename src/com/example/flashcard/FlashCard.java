package com.example.flashcard;

import java.io.Serializable;
import java.util.UUID;

public class FlashCard implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5534195328137787016L;
	private String question;
	private String answer;
	private UUID mId;
	
	public FlashCard(String q, String a) {
		question = q;
		answer = a;
		mId = UUID.randomUUID();
	}
	
	public UUID getId() {
		return mId;
	}

	public String getQuestion() {
		return question;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setQuestion(String newQuestion) {
		question = newQuestion;
	}
	
	public void setAnswer(String newAnswer) {
		answer = newAnswer;
	}
	
	public String toString() {
		return "FlashCard - Q: " + question + " / A: " + answer;
	}

}


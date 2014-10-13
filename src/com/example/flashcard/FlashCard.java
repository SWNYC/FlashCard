package com.example.flashcard;

import java.io.Serializable;

public class FlashCard implements Serializable {
	private String question;
	private String answer;
	
	public FlashCard(String q, String a) {
		question = q;
		answer = a;
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
		return "FlashCard - Question:" + question + "/ Answer: " + answer;
	}

}


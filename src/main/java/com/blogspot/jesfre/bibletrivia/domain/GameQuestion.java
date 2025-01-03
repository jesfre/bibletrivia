package com.blogspot.jesfre.bibletrivia.domain;

/**
 * @author jorge
 *
 */
public class GameQuestion {

	private Integer questionNumber;
	private String questionText;

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}
	
	public String getQuestionText() {
		return questionText;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	@Override
	public String toString() {
		return "GameQuestion [questionNumber=" + questionNumber + ", questionText=" + questionText + "]";
	}

}

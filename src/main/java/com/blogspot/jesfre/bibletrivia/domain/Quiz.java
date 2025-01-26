package com.blogspot.jesfre.bibletrivia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Quiz.
 */
@Entity
@Table(name = "quiz")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Quiz implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 120)
    @Column(name = "quiz_taker", length = 120, nullable = false)
    private String quizTaker;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime startDate;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "correct_questions")
    private Integer correctQuestions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "quiz")
    @JsonIgnoreProperties(value = { "triviaQuestion", "triviaAnswer", "quiz" }, allowSetters = true)
    private Set<QuizEntry> quizEntries = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quiz id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizTaker() {
        return this.quizTaker;
    }

    public Quiz quizTaker(String quizTaker) {
        this.setQuizTaker(quizTaker);
        return this;
    }

    public void setQuizTaker(String quizTaker) {
        this.quizTaker = quizTaker;
    }

    public ZonedDateTime getStartDate() {
        return this.startDate;
    }

    public Quiz startDate(ZonedDateTime startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public Integer getTotalQuestions() {
        return this.totalQuestions;
    }

    public Quiz totalQuestions(Integer totalQuestions) {
        this.setTotalQuestions(totalQuestions);
        return this;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public void incrementTotalQuestions() {
    	synchronized(Quiz.class) {
    		this.totalQuestions++;
        }
    }

    public Integer getCorrectQuestions() {
        return this.correctQuestions;
    }

    public Quiz correctQuestions(Integer correctQuestions) {
        this.setCorrectQuestions(correctQuestions);
        return this;
    }

    public void setCorrectQuestions(Integer correctQuestions) {
        this.correctQuestions = correctQuestions;
    }

    public Set<QuizEntry> getQuizEntries() {
        return this.quizEntries;
    }

    public void setQuizEntries(Set<QuizEntry> quizEntries) {
        if (this.quizEntries != null) {
            this.quizEntries.forEach(i -> i.setQuiz(null));
        }
        if (quizEntries != null) {
            quizEntries.forEach(i -> i.setQuiz(this));
        }
        this.quizEntries = quizEntries;
    }

    public Quiz quizEntries(Set<QuizEntry> quizEntries) {
        this.setQuizEntries(quizEntries);
        return this;
    }

    public Quiz addQuizEntries(QuizEntry quizEntry) {
        this.quizEntries.add(quizEntry);
        quizEntry.setQuiz(this);
        return this;
    }

    public Quiz removeQuizEntries(QuizEntry quizEntry) {
        this.quizEntries.remove(quizEntry);
        quizEntry.setQuiz(null);
        return this;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Quiz owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quiz)) {
            return false;
        }
        return getId() != null && getId().equals(((Quiz) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quiz{" +
            "id=" + getId() +
            ", quizTaker='" + getQuizTaker() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", totalQuestions=" + getTotalQuestions() +
            ", correctQuestions=" + getCorrectQuestions() +
            "}";
    }
}

package com.blogspot.jesfre.bibletrivia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A QuizEntry.
 */
@Entity
@Table(name = "quiz_entry")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class QuizEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_num", nullable = false)
    private Integer orderNum = 0;

    @Column(name = "correct")
    private Boolean correct = false;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "triviaAnswers", "trivias" }, allowSetters = true)
    private TriviaQuestion triviaQuestion;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_quiz_entry__trivia_answers",
        joinColumns = @JoinColumn(name = "quiz_entry_id"),
        inverseJoinColumns = @JoinColumn(name = "trivia_answers_id")
    )
    @JsonIgnoreProperties(value = { "bibleReferences", "triviaQuestion", "quizEntries" }, allowSetters = true)
    private Set<TriviaAnswer> triviaAnswers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "quizEntries", "owner" }, allowSetters = true)
    private Quiz quiz;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public QuizEntry id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }

    public QuizEntry orderNum(Integer orderNum) {
        this.setOrderNum(orderNum);
        return this;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Boolean getCorrect() {
        return this.correct;
    }

    public QuizEntry correct(Boolean correct) {
        this.setCorrect(correct);
        return this;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public TriviaQuestion getTriviaQuestion() {
        return this.triviaQuestion;
    }

    public void setTriviaQuestion(TriviaQuestion triviaQuestion) {
        this.triviaQuestion = triviaQuestion;
    }

    public QuizEntry triviaQuestion(TriviaQuestion triviaQuestion) {
        this.setTriviaQuestion(triviaQuestion);
        return this;
    }

    public Set<TriviaAnswer> getTriviaAnswers() {
        return this.triviaAnswers;
    }

    public void setTriviaAnswers(Set<TriviaAnswer> triviaAnswers) {
        this.triviaAnswers = triviaAnswers;
    }

    public QuizEntry triviaAnswers(Set<TriviaAnswer> triviaAnswers) {
        this.setTriviaAnswers(triviaAnswers);
        return this;
    }

    public QuizEntry addTriviaAnswers(TriviaAnswer triviaAnswer) {
        this.triviaAnswers.add(triviaAnswer);
        return this;
    }

    public QuizEntry removeTriviaAnswers(TriviaAnswer triviaAnswer) {
        this.triviaAnswers.remove(triviaAnswer);
        return this;
    }

    public Quiz getQuiz() {
        return this.quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public QuizEntry quiz(Quiz quiz) {
        this.setQuiz(quiz);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuizEntry)) {
            return false;
        }
        return getId() != null && getId().equals(((QuizEntry) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "QuizEntry{" +
            "id=" + getId() +
            ", orderNum=" + getOrderNum() +
            ", correct='" + getCorrect() + "'" +
            "}";
    }
}

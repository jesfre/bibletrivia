package com.blogspot.jesfre.bibletrivia.domain;

import com.blogspot.jesfre.bibletrivia.domain.enumeration.AnswerType;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaLevel;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TriviaQuestion.
 */
@Entity
@Table(name = "trivia_question")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TriviaQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "question_id")
    private Long questionId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private TriviaLevel level;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private TriviaType questionType;

    @Column(name = "question")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_type")
    private AnswerType answerType;

    @Column(name = "value")
    private Integer value;

    @Column(name = "picture")
    private String picture;

    // TODO change back to LAZY
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "triviaQuestion")
    @JsonIgnoreProperties(value = { "bibleReferences", "triviaQuestion" }, allowSetters = true)
    private Set<TriviaAnswer> triviaAnswers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "triviaQuestions")
    @JsonIgnoreProperties(value = { "triviaQuestions" }, allowSetters = true)
    private Set<Trivia> trivias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TriviaQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return this.questionId;
    }

    public TriviaQuestion questionId(Long questionId) {
        this.setQuestionId(questionId);
        return this;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public TriviaLevel getLevel() {
        return this.level;
    }

    public TriviaQuestion level(TriviaLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(TriviaLevel level) {
        this.level = level;
    }

    public TriviaType getQuestionType() {
        return this.questionType;
    }

    public TriviaQuestion questionType(TriviaType questionType) {
        this.setQuestionType(questionType);
        return this;
    }

    public void setQuestionType(TriviaType questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return this.question;
    }

    public TriviaQuestion question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public AnswerType getAnswerType() {
        return this.answerType;
    }

    public TriviaQuestion answerType(AnswerType answerType) {
        this.setAnswerType(answerType);
        return this;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public Integer getValue() {
        return this.value;
    }

    public TriviaQuestion value(Integer value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getPicture() {
        return this.picture;
    }

    public TriviaQuestion picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<TriviaAnswer> getTriviaAnswers() {
        return this.triviaAnswers;
    }

    public void setTriviaAnswers(Set<TriviaAnswer> triviaAnswers) {
        if (this.triviaAnswers != null) {
            this.triviaAnswers.forEach(i -> i.setTriviaQuestion(null));
        }
        if (triviaAnswers != null) {
            triviaAnswers.forEach(i -> i.setTriviaQuestion(this));
        }
        this.triviaAnswers = triviaAnswers;
    }

    public TriviaQuestion triviaAnswers(Set<TriviaAnswer> triviaAnswers) {
        this.setTriviaAnswers(triviaAnswers);
        return this;
    }

    public TriviaQuestion addTriviaAnswer(TriviaAnswer triviaAnswer) {
        this.triviaAnswers.add(triviaAnswer);
        triviaAnswer.setTriviaQuestion(this);
        return this;
    }

    public TriviaQuestion removeTriviaAnswer(TriviaAnswer triviaAnswer) {
        this.triviaAnswers.remove(triviaAnswer);
        triviaAnswer.setTriviaQuestion(null);
        return this;
    }

    public Set<Trivia> getTrivias() {
        return this.trivias;
    }

    public void setTrivias(Set<Trivia> trivias) {
        if (this.trivias != null) {
            this.trivias.forEach(i -> i.removeTriviaQuestion(this));
        }
        if (trivias != null) {
            trivias.forEach(i -> i.addTriviaQuestion(this));
        }
        this.trivias = trivias;
    }

    public TriviaQuestion trivias(Set<Trivia> trivias) {
        this.setTrivias(trivias);
        return this;
    }

    public TriviaQuestion addTrivia(Trivia trivia) {
        this.trivias.add(trivia);
        trivia.getTriviaQuestions().add(this);
        return this;
    }

    public TriviaQuestion removeTrivia(Trivia trivia) {
        this.trivias.remove(trivia);
        trivia.getTriviaQuestions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TriviaQuestion)) {
            return false;
        }
        return getId() != null && getId().equals(((TriviaQuestion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TriviaQuestion{" +
            "id=" + getId() +
            ", questionId=" + getQuestionId() +
            ", level='" + getLevel() + "'" +
            ", questionType='" + getQuestionType() + "'" +
            ", question='" + getQuestion() + "'" +
            ", answerType='" + getAnswerType() + "'" +
            ", value=" + getValue() +
            ", picture='" + getPicture() + "'" +
            "}";
    }
}

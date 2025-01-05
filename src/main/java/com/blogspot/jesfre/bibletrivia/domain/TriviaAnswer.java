package com.blogspot.jesfre.bibletrivia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TriviaAnswer.
 */
@Entity
@Table(name = "trivia_answer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TriviaAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "answer_id")
    private Long answerId;

    @Column(name = "answer")
    private String answer;

    @Column(name = "explanation")
    private String explanation;

    @Column(name = "correct")
    private Boolean correct;

    @Column(name = "picture")
    private String picture;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_trivia_answer__bible_reference",
        joinColumns = @JoinColumn(name = "trivia_answer_id"),
        inverseJoinColumns = @JoinColumn(name = "bible_reference_id")
    )
    @JsonIgnoreProperties(value = { "triviaAnswers" }, allowSetters = true)
    private Set<BibleReference> bibleReferences = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "triviaAnswers", "trivias" }, allowSetters = true)
    private TriviaQuestion triviaQuestion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TriviaAnswer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnswerId() {
        return this.answerId;
    }

    public TriviaAnswer answerId(Long answerId) {
        this.setAnswerId(answerId);
        return this;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getAnswer() {
        return this.answer;
    }

    public TriviaAnswer answer(String answer) {
        this.setAnswer(answer);
        return this;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public TriviaAnswer explanation(String explanation) {
        this.setExplanation(explanation);
        return this;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Boolean getCorrect() {
        return this.correct;
    }

    public TriviaAnswer correct(Boolean correct) {
        this.setCorrect(correct);
        return this;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }

    public String getPicture() {
        return this.picture;
    }

    public TriviaAnswer picture(String picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<BibleReference> getBibleReferences() {
        return this.bibleReferences;
    }

    public void setBibleReferences(Set<BibleReference> bibleReferences) {
        this.bibleReferences = bibleReferences;
    }

    public TriviaAnswer bibleReferences(Set<BibleReference> bibleReferences) {
        this.setBibleReferences(bibleReferences);
        return this;
    }

    public TriviaAnswer addBibleReference(BibleReference bibleReference) {
        this.bibleReferences.add(bibleReference);
        return this;
    }

    public TriviaAnswer removeBibleReference(BibleReference bibleReference) {
        this.bibleReferences.remove(bibleReference);
        return this;
    }

    public TriviaQuestion getTriviaQuestion() {
        return this.triviaQuestion;
    }

    public void setTriviaQuestion(TriviaQuestion triviaQuestion) {
        this.triviaQuestion = triviaQuestion;
    }

    public TriviaAnswer triviaQuestion(TriviaQuestion triviaQuestion) {
        this.setTriviaQuestion(triviaQuestion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TriviaAnswer)) {
            return false;
        }
        return getId() != null && getId().equals(((TriviaAnswer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TriviaAnswer{" +
            "id=" + getId() +
            ", answerId=" + getAnswerId() +
            ", answer='" + getAnswer() + "'" +
            ", explanation='" + getExplanation() + "'" +
            ", correct='" + getCorrect() + "'" +
            ", picture='" + getPicture() + "'" +
            ", triviaQuestion='" + getTriviaQuestion() + "'" +
            "}";
    }
}

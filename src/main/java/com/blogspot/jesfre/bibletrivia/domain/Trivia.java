package com.blogspot.jesfre.bibletrivia.domain;

import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaLevel;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.TriviaType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Trivia.
 */
@Entity
@Table(name = "trivia")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trivia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private TriviaLevel level;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TriviaType type;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_trivia__trivia_question",
        joinColumns = @JoinColumn(name = "trivia_id"),
        inverseJoinColumns = @JoinColumn(name = "trivia_question_id")
    )
    @JsonIgnoreProperties(value = { "triviaAnswers", "trivias" }, allowSetters = true)
    private Set<TriviaQuestion> triviaQuestions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trivia id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TriviaLevel getLevel() {
        return this.level;
    }

    public Trivia level(TriviaLevel level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(TriviaLevel level) {
        this.level = level;
    }

    public String getName() {
        return this.name;
    }

    public Trivia name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TriviaType getType() {
        return this.type;
    }

    public Trivia type(TriviaType type) {
        this.setType(type);
        return this;
    }

    public void setType(TriviaType type) {
        this.type = type;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Trivia startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Trivia endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Set<TriviaQuestion> getTriviaQuestions() {
        return this.triviaQuestions;
    }

    public void setTriviaQuestions(Set<TriviaQuestion> triviaQuestions) {
        this.triviaQuestions = triviaQuestions;
    }

    public Trivia triviaQuestions(Set<TriviaQuestion> triviaQuestions) {
        this.setTriviaQuestions(triviaQuestions);
        return this;
    }

    public Trivia addTriviaQuestion(TriviaQuestion triviaQuestion) {
        this.triviaQuestions.add(triviaQuestion);
        return this;
    }

    public Trivia removeTriviaQuestion(TriviaQuestion triviaQuestion) {
        this.triviaQuestions.remove(triviaQuestion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trivia)) {
            return false;
        }
        return getId() != null && getId().equals(((Trivia) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trivia{" +
            "id=" + getId() +
            ", level='" + getLevel() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}

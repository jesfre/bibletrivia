package com.blogspot.jesfre.bibletrivia.domain;

import com.blogspot.jesfre.bibletrivia.domain.enumeration.Book;
import com.blogspot.jesfre.bibletrivia.domain.enumeration.Testament;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A BibleReference.
 */
@Entity
@Table(name = "bible_reference")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BibleReference implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bible_verse", length = 500)
    private String bibleVerse;

    @Column(name = "version")
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(name = "book")
    private Book book;

    @Column(name = "chapter")
    private Integer chapter;

    @Column(name = "verse")
    private Integer verse;

    @Enumerated(EnumType.STRING)
    @Column(name = "testament")
    private Testament testament;

    @Column(name = "url")
    private String url;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "bibleReferences")
    @JsonIgnoreProperties(value = { "bibleReferences", "triviaQuestion", "quizEntries" }, allowSetters = true)
    private Set<TriviaAnswer> triviaAnswers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BibleReference id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBibleVerse() {
        return this.bibleVerse;
    }

    public BibleReference bibleVerse(String bibleVerse) {
        this.setBibleVerse(bibleVerse);
        return this;
    }

    public void setBibleVerse(String bibleVerse) {
        this.bibleVerse = bibleVerse;
    }

    public String getVersion() {
        return this.version;
    }

    public BibleReference version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Book getBook() {
        return this.book;
    }

    public BibleReference book(Book book) {
        this.setBook(book);
        return this;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getChapter() {
        return this.chapter;
    }

    public BibleReference chapter(Integer chapter) {
        this.setChapter(chapter);
        return this;
    }

    public void setChapter(Integer chapter) {
        this.chapter = chapter;
    }

    public Integer getVerse() {
        return this.verse;
    }

    public BibleReference verse(Integer verse) {
        this.setVerse(verse);
        return this;
    }

    public void setVerse(Integer verse) {
        this.verse = verse;
    }

    public Testament getTestament() {
        return this.testament;
    }

    public BibleReference testament(Testament testament) {
        this.setTestament(testament);
        return this;
    }

    public void setTestament(Testament testament) {
        this.testament = testament;
    }

    public String getUrl() {
        return this.url;
    }

    public BibleReference url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<TriviaAnswer> getTriviaAnswers() {
        return this.triviaAnswers;
    }

    public void setTriviaAnswers(Set<TriviaAnswer> triviaAnswers) {
        if (this.triviaAnswers != null) {
            this.triviaAnswers.forEach(i -> i.removeBibleReference(this));
        }
        if (triviaAnswers != null) {
            triviaAnswers.forEach(i -> i.addBibleReference(this));
        }
        this.triviaAnswers = triviaAnswers;
    }

    public BibleReference triviaAnswers(Set<TriviaAnswer> triviaAnswers) {
        this.setTriviaAnswers(triviaAnswers);
        return this;
    }

    public BibleReference addTriviaAnswer(TriviaAnswer triviaAnswer) {
        this.triviaAnswers.add(triviaAnswer);
        triviaAnswer.getBibleReferences().add(this);
        return this;
    }

    public BibleReference removeTriviaAnswer(TriviaAnswer triviaAnswer) {
        this.triviaAnswers.remove(triviaAnswer);
        triviaAnswer.getBibleReferences().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BibleReference)) {
            return false;
        }
        return getId() != null && getId().equals(((BibleReference) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BibleReference{" +
            "id=" + getId() +
            ", bibleVerse='" + getBibleVerse() + "'" +
            ", version='" + getVersion() + "'" +
            ", book='" + getBook() + "'" +
            ", chapter=" + getChapter() +
            ", verse=" + getVerse() +
            ", testament='" + getTestament() + "'" +
            ", url='" + getUrl() + "'" +
            "}";
    }
}

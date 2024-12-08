package com.blogspot.jesfre.bibletrivia.domain.enumeration;

/**
 * The Book enumeration.
 */
public enum Book {
	Genesis(Testament.OLD_TESTAMENT),
	Exodus(Testament.OLD_TESTAMENT),
	Leviticus(Testament.OLD_TESTAMENT),
	Numbers(Testament.OLD_TESTAMENT),
	Deuteronomy(Testament.OLD_TESTAMENT),
	Joshua(Testament.OLD_TESTAMENT),
	Judges(Testament.OLD_TESTAMENT),
	Ruth(Testament.OLD_TESTAMENT),
	Samuel_1(Testament.OLD_TESTAMENT),
	Samuel_2(Testament.OLD_TESTAMENT),
	Kings_1(Testament.OLD_TESTAMENT),
	Kings_2(Testament.OLD_TESTAMENT),
	Chronicles_1(Testament.OLD_TESTAMENT),
	Chronicles_2(Testament.OLD_TESTAMENT),
	Ezra(Testament.OLD_TESTAMENT),
	Nehemiah(Testament.OLD_TESTAMENT),
	Esther(Testament.OLD_TESTAMENT),
	Job(Testament.OLD_TESTAMENT),
	Psalms(Testament.OLD_TESTAMENT),
	Proverbs(Testament.OLD_TESTAMENT),
	Ecclesiastes(Testament.OLD_TESTAMENT),
	Song_of_Solomon(Testament.OLD_TESTAMENT),
	Isaiah(Testament.OLD_TESTAMENT),
	Jeremiah(Testament.OLD_TESTAMENT),
	Lamentations(Testament.OLD_TESTAMENT),
	Ezekiel(Testament.OLD_TESTAMENT),
	Daniel(Testament.OLD_TESTAMENT),
	Hosea(Testament.OLD_TESTAMENT),
	Joel(Testament.OLD_TESTAMENT),
	Amos(Testament.OLD_TESTAMENT),
	Obadiah(Testament.OLD_TESTAMENT),
	Jonah(Testament.OLD_TESTAMENT),
	Micah(Testament.OLD_TESTAMENT),
	Nahum(Testament.OLD_TESTAMENT),
	Habakkuk(Testament.OLD_TESTAMENT),
	Zephaniah(Testament.OLD_TESTAMENT),
	Haggai(Testament.OLD_TESTAMENT),
	Zechariah(Testament.OLD_TESTAMENT),
	Malachi(Testament.OLD_TESTAMENT),
	Matthew(Testament.NEW_TESTAMENT),
	Mark(Testament.NEW_TESTAMENT),
	Luke(Testament.NEW_TESTAMENT),
	John(Testament.NEW_TESTAMENT),
	Acts(Testament.NEW_TESTAMENT),
	Romans(Testament.NEW_TESTAMENT),
	Corinthians_1(Testament.NEW_TESTAMENT),
	Corinthians_2(Testament.NEW_TESTAMENT),
	Galatians(Testament.NEW_TESTAMENT),
	Ephesians(Testament.NEW_TESTAMENT),
	Philippians(Testament.NEW_TESTAMENT),
	Colossians(Testament.NEW_TESTAMENT),
	Thessalonians_1(Testament.NEW_TESTAMENT),
	Thessalonians_2(Testament.NEW_TESTAMENT),
	Timothy_1(Testament.NEW_TESTAMENT),
	Timothy_2(Testament.NEW_TESTAMENT),
	Titus(Testament.NEW_TESTAMENT),
	Philemon(Testament.NEW_TESTAMENT),
	Hebrews(Testament.NEW_TESTAMENT),
	James(Testament.NEW_TESTAMENT),
	Peter_1(Testament.NEW_TESTAMENT),
	Peter_2(Testament.NEW_TESTAMENT),
	John_1(Testament.NEW_TESTAMENT),
	John_2(Testament.NEW_TESTAMENT),
	John_3(Testament.NEW_TESTAMENT),
	Jude(Testament.NEW_TESTAMENT),
	Revelation(Testament.NEW_TESTAMENT);
    
    private Testament testament;

	private Book(Testament testament) {
		this.testament = testament;
	}

	public Testament getTestament() {
		return testament;
	}
    
}

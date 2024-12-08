import bibleReference from 'app/entities/bible-reference/bible-reference.reducer';
import trivia from 'app/entities/trivia/trivia.reducer';
import triviaAnswer from 'app/entities/trivia-answer/trivia-answer.reducer';
import triviaQuestion from 'app/entities/trivia-question/trivia-question.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  bibleReference,
  trivia,
  triviaAnswer,
  triviaQuestion,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

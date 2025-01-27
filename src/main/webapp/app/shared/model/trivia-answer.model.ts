import { IBibleReference } from 'app/shared/model/bible-reference.model';
import { ITriviaQuestion } from 'app/shared/model/trivia-question.model';
import { IQuizEntry } from 'app/shared/model/quiz-entry.model';

export interface ITriviaAnswer {
  id?: number;
  answerId?: number | null;
  answer?: string | null;
  explanation?: string | null;
  correct?: boolean | null;
  picture?: string | null;
  bibleReferences?: IBibleReference[] | null;
  triviaQuestion?: ITriviaQuestion | null;
  quizEntries?: IQuizEntry[] | null;
}

export const defaultValue: Readonly<ITriviaAnswer> = {
  correct: false,
};

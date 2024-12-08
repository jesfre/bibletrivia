import { ITriviaAnswer } from 'app/shared/model/trivia-answer.model';
import { Book } from 'app/shared/model/enumerations/book.model';
import { Testament } from 'app/shared/model/enumerations/testament.model';

export interface IBibleReference {
  id?: number;
  bibleVerse?: string | null;
  version?: string | null;
  book?: keyof typeof Book | null;
  chapter?: number | null;
  verse?: number | null;
  testament?: keyof typeof Testament | null;
  url?: string | null;
  triviaAnswers?: ITriviaAnswer[] | null;
}

export const defaultValue: Readonly<IBibleReference> = {};

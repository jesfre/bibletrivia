import { ITriviaQuestion } from 'app/shared/model/trivia-question.model';
import { ITriviaAnswer } from 'app/shared/model/trivia-answer.model';
import { IQuiz } from 'app/shared/model/quiz.model';

export interface IQuizEntry {
  id?: number;
  orderNum?: number;
  correct?: boolean | null;
  triviaQuestion?: ITriviaQuestion;
  selectedAnswers?: ITriviaAnswer[] | null;
  quiz?: IQuiz | null;
}

export const defaultValue: Readonly<IQuizEntry> = {
  correct: false,
};

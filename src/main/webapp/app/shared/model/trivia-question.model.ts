import { ITrivia } from 'app/shared/model/trivia.model';
import { TriviaType } from 'app/shared/model/enumerations/trivia-type.model';
import { AnswerType } from 'app/shared/model/enumerations/answer-type.model';
import { ITriviaAnswer } from 'app/shared/model/trivia-answer.model';

export interface ITriviaQuestion {
  id?: number;
  questionId?: number | null;
  questionType?: keyof typeof TriviaType | null;
  question?: string | null;
  answerType?: keyof typeof AnswerType | null;
  value?: number | null;
  picture?: string | null;
  trivias?: ITrivia[] | null;
  triviaAnswers?: ITriviaAnswer[] | null;
}

export const defaultValue: Readonly<ITriviaQuestion> = {};

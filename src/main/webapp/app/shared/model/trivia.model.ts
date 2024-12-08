import dayjs from 'dayjs';
import { ITriviaQuestion } from 'app/shared/model/trivia-question.model';
import { TriviaLevel } from 'app/shared/model/enumerations/trivia-level.model';
import { TriviaType } from 'app/shared/model/enumerations/trivia-type.model';

export interface ITrivia {
  id?: number;
  level?: keyof typeof TriviaLevel | null;
  name?: string | null;
  type?: keyof typeof TriviaType | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  triviaQuestions?: ITriviaQuestion[] | null;
}

export const defaultValue: Readonly<ITrivia> = {};

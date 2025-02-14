import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IQuizEntry } from './quiz-entry.model';

export interface IQuiz {
  id?: number;
  quizTaker?: string;
  startDate?: dayjs.Dayjs;
  totalQuestions?: number | null;
  correctQuestions?: number | null;
  owner?: IUser | null;
  errorCount?: number | null;
  quizEntries?: IQuizEntry[] | null;
}

export const defaultValue: Readonly<IQuiz> = {};

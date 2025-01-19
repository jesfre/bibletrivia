import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IQuiz {
  id?: number;
  quizTaker?: string;
  startDate?: dayjs.Dayjs;
  totalQuestions?: number | null;
  correctQuestions?: number | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<IQuiz> = {};

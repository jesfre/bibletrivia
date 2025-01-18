import dayjs from 'dayjs';

export interface IQuiz {
  id?: number;
  quizTaker?: string;
  startDate?: dayjs.Dayjs;
  totalQuestions?: number | null;
  correctQuestions?: number | null;
}

export const defaultValue: Readonly<IQuiz> = {};

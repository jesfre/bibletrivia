
export interface ITriviaGameQuestion {
  questionNumber?: number;
  questionText?: string | null;
}

export const defaultValue: Readonly<ITriviaGameQuestion> = {};
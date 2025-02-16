import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IQuizEntry, defaultValue } from 'app/shared/model/quiz-entry.model';

const initialState: EntityState<IQuizEntry> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/trivia-game';

// Actions
{/* prettier-ignore */}

export const createQuiz = createAsyncThunk(
  'triviaGameQuestion/create_quiz',
  async (level: string) => {
    const requestUrl = `${apiUrl}/create/${level}`;
    return axios.get(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const resetQuiz = createAsyncThunk(
  'triviaGameQuestion/reset_quiz',
  async () => {
    const requestUrl = `${apiUrl}/reset`;
    return await axios.get(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getNextQuestion = createAsyncThunk(
  'triviaGameQuestion/next_question',
  async (currentQuestionOrder: string | number ) => {
    const requestUrl = `${apiUrl}/next/${currentQuestionOrder}`;
    return axios.get<IQuizEntry>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getPreviousQuestion = createAsyncThunk(
  'triviaGameQuestion/previous_question',
  async (currentQuestionOrder: string | number) => {
    const requestUrl = `${apiUrl}/previous/${currentQuestionOrder}`;
    return await axios.get<IQuizEntry>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);


export const updateQuiz = createAsyncThunk(
  'triviaGameQuestion/update_quiz',
  async ({ questionNum, answers }: { questionNum: string | number, answers: string[] }) => {
    const requestUrl = `${apiUrl}/update/${questionNum}?answers=${answers.join(',')}`;
    return await axios.get<IQuizEntry>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// slice

export const TriviaGameSlice = createEntitySlice({
  name: 'triviaGameQuestion',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(createQuiz.fulfilled, (state, action) => {
        state.loading = false;
      })
      .addCase(getPreviousQuestion.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(getNextQuestion.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getNextQuestion), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          errorMessage: null,
          updateSuccess: false,
          loading: true,
          isFirstQuestion: headers['x-bibletriviaapp-first-question'],
          isLastQuestion: headers['x-bibletriviaapp-last-question'],
          entity: action.payload.data
        };
      })
      .addMatcher(isFulfilled(getPreviousQuestion), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          errorMessage: null,
          updateSuccess: false,
          loading: true,
          isFirstQuestion: headers['x-bibletriviaapp-first-question'],
          isLastQuestion: headers['x-bibletriviaapp-last-question'],
          entity: action.payload.data
        };
      })
      .addMatcher(isPending(createQuiz, resetQuiz, updateQuiz), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = TriviaGameSlice.actions;

// Reducer
export default TriviaGameSlice.reducer;

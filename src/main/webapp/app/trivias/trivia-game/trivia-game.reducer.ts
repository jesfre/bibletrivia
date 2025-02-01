import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { ASC } from 'app/shared/util/pagination.constants';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ITriviaQuestion, defaultValue } from 'app/shared/model/trivia-question.model';

const initialState: EntityState<ITriviaQuestion> = {
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


export const resetTrivia = createAsyncThunk(
  'triviaGameQuestion/reset_trivia',
  async (level: string) => {
    const requestUrl = `${apiUrl}/reset/${level}`;
    return await axios.get<ITriviaQuestion>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getTriviaQuestionInLevel = createAsyncThunk(
  'triviaGameQuestion/fetch_entity_by_level',
  async (level: string) => {
    const requestUrl = `${apiUrl}/level/${level}`;
    return axios.get<ITriviaQuestion>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getQuestion = createAsyncThunk(
  'triviaGameQuestion/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/question/${id}`;
    return axios.get<ITriviaQuestion>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createTrivia = createAsyncThunk(
  'triviaGameQuestion/create_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/create`;
    return axios.get<ITriviaQuestion>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const updateTrivia = createAsyncThunk(
  'triviaGameQuestion/update_entity',
  async ({ questionId, answers }: { questionId: string, answers: string[] }) => {
    const requestUrl = `${apiUrl}/update/${questionId}?answers=${answers.join(',')}`;
    return axios.get<ITriviaQuestion>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// slice

export const TriviaGameSlice = createEntitySlice({
  name: 'triviaGameQuestion',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getQuestion.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(getTriviaQuestionInLevel.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getTriviaQuestionInLevel), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          errorMessage: null,
          updateSuccess: false,
          loading: true,
          isLastQuestion: headers['x-bibletriviaapp-last-question'],
          entity: action.payload.data
        };
      })
      .addMatcher(isPending(getQuestion, createTrivia, updateTrivia), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = TriviaGameSlice.actions;

// Reducer
export default TriviaGameSlice.reducer;

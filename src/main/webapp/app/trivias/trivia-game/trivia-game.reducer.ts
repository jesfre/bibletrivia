import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { ASC } from 'app/shared/util/pagination.constants';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ITriviaGameQuestion, defaultValue } from 'app/shared/model/trivia-game-question.model';

const initialState: EntityState<ITriviaGameQuestion> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/trivia-game/question';

// Actions

export const getEntities = createAsyncThunk(
  'triviaGameQuestion/fetch_entity_list',
  async ({ sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${sort ? `sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<ITriviaGameQuestion[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getQuestion = createAsyncThunk(
  'triviaGameQuestion/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ITriviaGameQuestion>(requestUrl);
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
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data.sort((a, b) => {
            if (!action.meta?.arg?.sort) {
              return 1;
            }
            const order = action.meta.arg.sort.split(',')[1];
            const predicate = action.meta.arg.sort.split(',')[0];
            return order === ASC ? (a[predicate] < b[predicate] ? -1 : 1) : b[predicate] < a[predicate] ? -1 : 1;
          }),
        };
      })
      .addMatcher(isPending(getEntities, getQuestion), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      });
  },
});

export const { reset } = TriviaGameSlice.actions;

// Reducer
export default TriviaGameSlice.reducer;

import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IQuiz, defaultValue } from 'app/shared/model/quiz.model';


const initialState: EntityState<IQuiz> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/trivia-game/score';

// Actions
{/* prettier-ignore */}

export const getScore = createAsyncThunk(
  'triviaGameScore/get_score',
  async () => {
    const requestUrl = `${apiUrl}/get`;
    return axios.get<IQuiz>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

// slice

export const TriviaGameScoreSlice = createEntitySlice({
  name: 'triviaGameScore',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getScore.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getScore), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          errorMessage: null,
          updateSuccess: false,
          loading: true,
          entity: action.payload.data
        };
      });
  },
});

export const { reset } = TriviaGameScoreSlice.actions;

// Reducer
export default TriviaGameScoreSlice.reducer;

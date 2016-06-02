package montecarlo;

import java.util.ArrayList;
import java.util.function.Supplier;

public class NestedMonteCarloSearch {

	public static <TState, TAction> Pair<Double, ArrayList<TAction>> executeSearch(INmcsState<TState, TAction> state,
			final int level, final Supplier<Boolean> isCanceled) {

		Pair<Double, ArrayList<TAction>> globalBestResult = Pair.of(state.getScore(), new ArrayList<TAction>());
		final ArrayList<TAction> previousAppliedActions = new ArrayList<TAction>();

		while (!state.isTerminalPosition() && !isCanceled.get()) {

			Pair<Double, ArrayList<TAction>> currentBestResult = Pair.of(0.0, new ArrayList<TAction>());
			TAction currentBestAction = null;

			for (TAction action : state.findAllLegalActions()) {
				final INmcsState<TState, TAction> currentState = state.TakeAction(action);
				final Pair<Double, ArrayList<TAction>> simulationResult = level <= 1 
						? currentState.simulation()
						: executeSearch(currentState, level - 1, isCanceled);

				if (simulationResult.item1 >= currentBestResult.item1) {
					currentBestAction = action;
					currentBestResult = simulationResult;
				}
			}

			previousAppliedActions.add(currentBestAction);

			if (currentBestResult.item1 > globalBestResult.item1) {
				globalBestResult = currentBestResult;
				globalBestResult.item2.addAll(0, previousAppliedActions);
			}

			state = state.TakeAction(currentBestAction);
		}
		return globalBestResult;
	}
}
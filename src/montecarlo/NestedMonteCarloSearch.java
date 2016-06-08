package montecarlo;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class NestedMonteCarloSearch {
	public static <TState, TAction> Pair<Double, List<TAction>> executeSearch(INmcsState<TState, TAction> state,
			final int level, final Supplier<Boolean> isCanceled) {

		// terminating case
		if (level <= 0)
			return state.simulation();

		Pair<Double, List<TAction>> globalBestResult = state.getInitionSolution();
		final List<TAction> visitedNodes = new LinkedList<TAction>();

		while (!state.isTerminalPosition() && !isCanceled.get()) {

			final INmcsState<TState, TAction> curentNode = state;

			Triple<TAction, Double, List<TAction>> currentBestResult = state
				.findAllLegalActions()
				.stream()
				.parallel()
				.map(action -> {
					final INmcsState<TState, TAction> child = curentNode.takeAction(action);
					// recursion
					Pair<Double, List<TAction>> result = executeSearch(child, level - 1, isCanceled);
					return Triple.of(action, result.item1, result.item2);
				})
				.max((x, y) -> x.item2.compareTo(y.item2)).get();

			TAction currentBestAction = currentBestResult.item1;

			if (currentBestResult.item2 >= globalBestResult.item1) {
				visitedNodes.add(currentBestAction);
				globalBestResult = Pair.of(currentBestResult.item2, currentBestResult.item3);
				globalBestResult.item2.addAll(0, visitedNodes);
			} else {
				currentBestAction = globalBestResult.item2.get(visitedNodes.size());
				visitedNodes.add(currentBestAction);
			}

			state = state.takeAction(currentBestAction);
		}

		return globalBestResult;
	}
}
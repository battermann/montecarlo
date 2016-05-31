package montecarlo;

import java.util.ArrayList;

public interface INmcsState<TState, TAction> {
	double getScore();
	boolean isTerminalPosition();
	ArrayList<TAction> findAllLegalActions();
	INmcsState<TState, TAction> TakeAction(TAction action);
	Pair<Double, ArrayList<TAction>> simulation();
}

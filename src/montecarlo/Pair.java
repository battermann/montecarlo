package montecarlo;

public class Pair<T1, T2> {
	public final T1 item1;
	public final T2 item2;
	
	public Pair(final T1 i1,final T2 i2) {
		item1 = i1;
		item2 = i2;
	}
	
	public static <T1, T2> Pair<T1, T2> of(final T1 i1, final T2 i2) {
		return new Pair<T1, T2>(i1, i2);
	}

	@Override
	public String toString() {
		return String.format("{ %s; %s}", item1.toString(), item2.toString());
	}
}

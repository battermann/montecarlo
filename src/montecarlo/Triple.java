package montecarlo;

public class Triple<T1,T2,T3> {

		public final T1 item1;
		public final T2 item2;
		public final T3 item3;
		
		public Triple(final T1 i1,final T2 i2, final T3 i3) {
			item1 = i1;
			item2 = i2;
			item3 = i3;
		}
		
		public static <T1, T2, T3> Triple<T1, T2, T3> of(final T1 i1, final T2 i2, final T3 i3) {
			return new Triple<T1, T2, T3>(i1, i2, i3);
		}

		@Override
		public String toString() {
			return String.format("{ %s; %s; %s }", item1.toString(), item2.toString(), item3.toString());
		}
}

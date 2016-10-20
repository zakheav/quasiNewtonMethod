package function;

import java.util.List;

public class OriginFunction implements Function {

	@Override
	public double value(List<Double> x) {
		return x.get(0) * x.get(0) + x.get(1) * x.get(1);
	}

}

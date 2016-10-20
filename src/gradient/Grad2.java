package gradient;

import java.util.List;

import function.Function;

public class Grad2 implements Function {// 对第二个变量求偏导

	public double value(List<Double> x) {
		return 2 * x.get(1);
	}

}

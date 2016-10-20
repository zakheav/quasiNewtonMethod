package gradient;

import java.util.List;

import function.Function;

public class Grad1 implements Function {// 对第一个变量求偏导

	public double value(List<Double> x) {
		return 2 * x.get(0);
	}

}

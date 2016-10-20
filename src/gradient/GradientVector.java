package gradient;

import java.util.ArrayList;
import java.util.List;
import function.Function;
import matrix.Matrix;

public class GradientVector {
	// z = x^2 + y^2
	private static GradientVector instance = new GradientVector();
	
	private List<Function> gradientVector = new ArrayList<Function>();

	private GradientVector() {
		gradientVector.add(new Grad1());
		gradientVector.add(new Grad2());
	}

	public static GradientVector getInstance() {
		return instance;
	}
	
	public Matrix getGradient(List<Double> x) {// 求解给定坐标的梯度向量
		List<Double> r = new ArrayList<Double>();
		for (Function function : gradientVector) {
			r.add(function.value(x));
		}
		List<List<Double>> result = new ArrayList<List<Double>>();
		result.add(r);
		return new Matrix(result);
	}
}

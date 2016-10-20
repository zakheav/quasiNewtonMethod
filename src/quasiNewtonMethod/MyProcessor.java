package quasiNewtonMethod;

import java.util.ArrayList;
import java.util.List;

public class MyProcessor extends Processor {

	public MyProcessor(List<Double> InitPoint, double accuracy) {
		super(InitPoint, accuracy);
	}

	@Override
	double getStepLength(List<Double> p, List<Double> x) {
		double C1 = 0.3;
		double C2 = 0.5;
		// 根据不同的优化函数，需要手工修改这部分代码
		// 解出wolfe condition不等式
		double x0 = x.get(0);
		double x1 = x.get(1);
		double p0 = p.get(0);
		double p1 = p.get(1);
		double upperA = (-2.0 * x0 * p0 - 2.0 * x1 * p1 + 2 * C1 * x0 * p0 + 2.0 * C1 * x1 * p1) / (p0 * p0 + p1 * p1);// a的上界
		double lowerA = (x0 * p0 + x1 * p1) * (1.0 - C2) / (p0 * p0 + p1 * p1);// a的下界
		// a取上界和下界的均值
		return (upperA + lowerA) / 2;
	}

	public static void main(String[] args) {
		List<Double> init = new ArrayList<Double>();
		init.add(-1000.0);
		init.add(230.0);
		Processor newton = new MyProcessor(init, 0.01);
		newton.start();
		List<Double> r = newton.getResultX();
		double y = newton.getResultY();
		System.out.println(r.get(0) + " " + r.get(1) + " " + y);
	}
}

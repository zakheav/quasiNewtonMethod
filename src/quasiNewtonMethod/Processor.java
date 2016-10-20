package quasiNewtonMethod;

import java.util.ArrayList;
import java.util.List;

import function.OriginFunction;
import gradient.GradientVector;
import matrix.Matrix;
import threadPool.ThreadPool;

public abstract class Processor {
	private OriginFunction function;// 原函数
	private Matrix resultX;// 结果的自变量
	private int iterationTimes;// 迭代最大次数
	private Matrix B;
	private double threshold;// 迭代精度

	public Processor(List<Double> InitPoint, double accuracy) {
		function = new OriginFunction();
		List<List<Double>> temp = new ArrayList<List<Double>>();
		temp.add(InitPoint);
		resultX = new Matrix(temp);
		iterationTimes = 1000;

		temp = new ArrayList<List<Double>>();
		for (int i = 0; i < resultX.col; ++i) {
			temp.add(new ArrayList<Double>());
			for (int j = 0; j < resultX.col; ++j) {
				if (i == j)
					temp.get(i).add(1.0);
				else
					temp.get(i).add(0.0);
			}
		}
		B = new Matrix(temp);
		threshold = accuracy;
		ThreadPool.getInstance();// 启动线程池
	}

	private boolean checkFinish(List<Double> a, List<Double> b) {
		double r = 0.0;
		for (int i = 0; i < a.size(); ++i) {
			r += Math.abs(a.get(i) - b.get(i));
		}
		if (r > threshold)
			return false;
		return true;
	}

	abstract double getStepLength(List<Double> p, List<Double> x);

	public void start() {// 开始拟牛顿迭代法
		// 根据B计算
		int counter = 0;
		Matrix lastResult;
		do {
			List<List<Double>> temp = new ArrayList<List<Double>>();
			for (int i = 0; i < resultX.row; ++i) {
				temp.add(new ArrayList<Double>());
				for (int j = 0; j < resultX.col; ++j) {
					temp.get(i).add(resultX.getMatrix().get(i).get(j));
				}
			}
			lastResult = new Matrix(temp);

			Matrix grad = GradientVector.getInstance().getGradient(lastResult.getMatrix().get(0));// 梯度向量
			Matrix p = grad.mProduct(B).product(-1.0);// 根据B矩阵计算迭代方向向量p
			double norm = p.vectorNorm();
			p = p.product(1.0 / norm);// 归一化
			double a = getStepLength(p.getMatrix().get(0), lastResult.getMatrix().get(0));// 根据wolfeCondition计算迭代步长a
			resultX = lastResult.add(p.product(a));// 得到新的resultX
			// 迭代修改B矩阵
			Matrix S = resultX.add(lastResult.product(-1.0));
			Matrix gradF_lastX = GradientVector.getInstance().getGradient(lastResult.getMatrix().get(0));
			Matrix gradF_newX = GradientVector.getInstance().getGradient(resultX.getMatrix().get(0));
			Matrix Y = gradF_newX.add(gradF_lastX.product(-1.0));

			Matrix M1 = (S.reverse().mProduct(S)).product(1.0 / S.mProduct(Y.reverse()).getMatrix().get(0).get(0));
			Matrix M2 = (B.mProduct(Y.reverse()).mProduct(Y).mProduct(B))
					.product(1.0 / Y.mProduct(B).mProduct(Y.reverse()).getMatrix().get(0).get(0)).product(-1.0);
			B = B.add(M1).add(M2);

			++counter;
		} while (!checkFinish(lastResult.getMatrix().get(0), resultX.getMatrix().get(0)) && counter < iterationTimes);
	}

	public List<Double> getResultX() {
		return resultX.getMatrix().get(0);
	}

	public double getResultY() {
		return function.value(getResultX());
	}
}

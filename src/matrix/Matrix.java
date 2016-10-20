package matrix;

import java.util.ArrayList;
import java.util.List;

import threadPool.ThreadPool;

public class Matrix {
	public int row;
	public int col;
	private List<List<Double>> matrix;
	private List<List<Double>> result;// 存放矩阵运算的结果

	public Matrix(List<List<Double>> matrix) {
		this.matrix = matrix;
		this.row = matrix.size();
		this.col = matrix.get(0).size();
	}

	public List<List<Double>> getMatrix() {
		return matrix;
	}

	public Matrix mProduct(Matrix m) {// 矩阵相乘
		if (col == m.row) {
			// 矩阵运算的结果一定是row行，m.col列的矩阵
			this.result = new ArrayList<List<Double>>();
			for (int i = 0; i < row; ++i) {
				this.result.add(new ArrayList<Double>());
				for (int j = 0; j < m.col; ++j) {
					this.result.get(i).add(0.0);
				}
			}
			// 把相乘的矩阵拆分成任务，加入到线程池
			List<Runnable> taskList = new ArrayList<Runnable>();
			for (int i = 0; i < row; ++i) {
				for (int j = 0; j < m.col; ++j) {
					MatrixProductTask task = new MatrixProductTask(i, j, this.result, this.matrix, m.getMatrix());
					taskList.add(task);
				}
			}
			ThreadPool.getInstance().addTasksInbatches(taskList);
			return new Matrix(this.result);
		} else {
			return null;
		}
	}

	public Matrix product(double a) {// 矩阵乘以常数
		this.result = new ArrayList<List<Double>>();
		for (int i = 0; i < row; ++i) {
			this.result.add(new ArrayList<Double>());
			for (int j = 0; j < col; ++j) {
				this.result.get(i).add(0.0);
			}
		}
		// 把数乘拆分成任务，加入到线程池
		List<Runnable> taskList = new ArrayList<Runnable>();
		for (int i = 0; i < row; ++i) {
			MatrixProductScalarTask task = new MatrixProductScalarTask(a, matrix, i, result);
			taskList.add(task);
		}
		ThreadPool.getInstance().addTasksInbatches(taskList);
		return new Matrix(this.result);
	}

	public Matrix add(Matrix m) {// 矩阵相加
		if (row == m.row && col == m.col) {
			this.result = new ArrayList<List<Double>>();
			for (int i = 0; i < row; ++i) {
				this.result.add(new ArrayList<Double>());
				for (int j = 0; j < col; ++j) {
					this.result.get(i).add(0.0);
				}
			}
			// 把加法任务拆分成任务，加入到线程池
			List<Runnable> taskList = new ArrayList<Runnable>();
			for (int i = 0; i < row; ++i) {
				MatrixAddTask task = new MatrixAddTask(i, matrix, m.getMatrix(), this.result);
				taskList.add(task);
			}
			ThreadPool.getInstance().addTasksInbatches(taskList);
			return new Matrix(this.result);
		} else {
			return null;
		}
	}

	public Matrix reverse() {// 矩阵转置
		List<List<Double>> rMatrix = new ArrayList<List<Double>>();
		for (int i = 0; i < this.col; ++i) {
			rMatrix.add(new ArrayList<Double>());
			for (int j = 0; j < this.row; ++j) {
				rMatrix.get(i).add(this.matrix.get(j).get(i));
			}
		}
		return new Matrix(rMatrix);
	}

	public Double vectorNorm() {
		if (row == 1) {
			double r = 0.0;
			for (int i = 0; i < col; ++i) {
				r += matrix.get(0).get(i) * matrix.get(0).get(i);
			}
			return Math.sqrt(r);
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		List<List<Double>> a = new ArrayList<List<Double>>();
		List<List<Double>> b = new ArrayList<List<Double>>();

		a.add(new ArrayList<Double>());
		a.get(0).add(1.0);
		a.get(0).add(1.0);
		a.get(0).add(1.0);
		a.add(new ArrayList<Double>());
		a.get(1).add(1.0);
		a.get(1).add(1.0);
		a.get(1).add(1.0);
		a.add(new ArrayList<Double>());
		a.get(2).add(1.0);
		a.get(2).add(1.0);
		a.get(2).add(1.0);
		
		b.add(new ArrayList<Double>());
		b.get(0).add(1.0);
		b.get(0).add(1.0);
		b.get(0).add(1.0);
		b.add(new ArrayList<Double>());
		b.get(1).add(1.0);
		b.get(1).add(1.0);
		b.get(1).add(1.0);
		b.add(new ArrayList<Double>());
		b.get(2).add(1.0);
		b.get(2).add(1.0);
		b.get(2).add(1.0);

		Matrix A = new Matrix(a);
		Matrix B = new Matrix(b);

		List<List<Double>> r = A.mProduct(B).getMatrix();
		// 输出结果
		for (int i = 0; i < r.size(); ++i) {
			for (double e : r.get(i)) {
				System.out.print(e + " ");
			}
			System.out.println();
		}
	}
}

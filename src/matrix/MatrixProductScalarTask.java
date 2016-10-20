package matrix;

import java.util.List;

public class MatrixProductScalarTask implements Runnable {

	private double scalar;
	private List<List<Double>> matrix;
	private int rowIdx;
	private List<List<Double>> result;

	public MatrixProductScalarTask(double scalar, List<List<Double>> matrix, int rowIdx, List<List<Double>> result) {
		this.scalar = scalar;
		this.matrix = matrix;
		this.rowIdx = rowIdx;
		this.result = result;
	}

	@Override
	public void run() {
		for (int i = 0; i < matrix.get(rowIdx).size(); ++i) {
			result.get(rowIdx).set(i, matrix.get(rowIdx).get(i) * scalar);
		}
	}

}

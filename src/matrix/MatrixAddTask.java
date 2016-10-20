package matrix;

import java.util.List;

public class MatrixAddTask implements Runnable {

	private int rowIdx;
	private List<List<Double>> matrix1;
	private List<List<Double>> matrix2;
	private List<List<Double>> result;

	public MatrixAddTask(int rowIdx, List<List<Double>> matrix1, List<List<Double>> matrix2,
			List<List<Double>> result) {
		this.rowIdx = rowIdx;
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.result = result;
	}

	@Override
	public void run() {
		for (int i = 0; i < matrix1.get(rowIdx).size(); ++i) {
			result.get(rowIdx).set(i, matrix1.get(rowIdx).get(i) + matrix2.get(rowIdx).get(i));
		}
	}
}

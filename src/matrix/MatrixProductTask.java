package matrix;

import java.util.List;

public class MatrixProductTask implements Runnable {

	private int firstMatrixRowIdx;
	private int secondMatrixColIdx;
	private List<List<Double>> result;
	private List<List<Double>> matrix1;
	private List<List<Double>> matrix2;
	
	public MatrixProductTask(int rowIdx, int colIdx, List<List<Double>> result, List<List<Double>> m1, List<List<Double>> m2) {
		this.firstMatrixRowIdx = rowIdx;
		this.secondMatrixColIdx = colIdx;
		this.result = result;
		this.matrix1 = m1;
		this.matrix2 = m2;
	}
	
	@Override
	public void run() {
		double r  = 0.0;
		for(int i=0; i<matrix2.size(); ++i) {
			r += matrix1.get(firstMatrixRowIdx).get(i) * matrix2.get(i).get(secondMatrixColIdx);
		}
		result.get(firstMatrixRowIdx).set(secondMatrixColIdx, r);
	}
}

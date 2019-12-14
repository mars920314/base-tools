package sudoku;

public enum SoduLevel {
	FREE(81, 81), EASY(32, 50), COMMON(26, 32), HARD(22, 26);
	int minSum;
	int maxSum;

	SoduLevel(int minSum, int maxSum) {
		this.minSum = minSum;
		this.maxSum = maxSum;
	}

	public int getMinSum() {
		return minSum;
	}

	public int getMaxSum() {
		return maxSum;
	}
}

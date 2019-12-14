package sudoku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

public class MySoduGenerator {
	private static final String TAG = "MySoduGenerator: ";
	SoduNode[][] soduNodes;
	private char[] charArray;
	HashMap<Integer, Integer> avaiableNum;
	private int x;

	public int getNoNeedToSolve() {
		return noNeedSolve;
	}

	public String getDataString() {
		if (charArray == null) {
			return null;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (char node : charArray) {
			stringBuilder.append(node);
		}
		return stringBuilder.toString();
	}

	private int noNeedSolve;

	// int index = 0;
	// int maxSolutionNum=0;
	public SoduNode[][] generateGame(SoduLevel level) {
		int minSum = level.getMinSum();
		int maxSum = level.getMaxSum();
		charArray = new char[81];
		for (int i = 0; i < 81; i++) {
			charArray[i] = '0';
		}
		soduNodes = SoduUtils.getNodes(charArray);
		avaiableNum = new HashMap<Integer, Integer>(){{this.put(1, 5);this.put(2, 9);this.put(3, 9);this.put(4, 9);this.put(5, 9);this.put(6, 9);this.put(7, 9);this.put(8, 9);this.put(9, 9);}};
		noNeedSolve = 0;
		a: do {
			int index = getMostSuitableValueIndex();

			int list = index / 9;
			int row = index % 9;
			System.out.println(TAG + "select fill node:" + "list:" + list + "row" + row + "index" + index);
			SoduNode currentNode = soduNodes[list][row];
			Integer[] suitValue = currentNode.getSuitValue();
			HashMap<Integer, Integer> avaiableNumCopy = (HashMap<Integer, Integer>) avaiableNum.clone();
			for (int i = 0; i < 9; i++) 
				for(SoduNode soduNode : soduNodes[i][0].listNode){
					if(soduNode.value==0)
						continue;
					else if(avaiableNumCopy.get(soduNode.value)==0)
						break a;
					else{
						avaiableNumCopy.put(soduNode.value, avaiableNumCopy.get(soduNode.value)-1);
					}
				}
			suitValue = Stream.of(suitValue).filter(num -> avaiableNumCopy.get(num)>0).toArray(Integer[]::new);
			
			Integer[] hasSolutionValue = new Integer[9];
			int hasSolutionValueNum = 0;
			if (suitValue.length < 2) {
				continue;
			}
			currentNode.needTobeSolve = false;
			int noTrySuit = suitValue.length;
			boolean hasFound = false;
			while (noTrySuit > 0) {
				int random = (int) (Math.random() * suitValue.length);
				if (suitValue[random] == null) {
					continue;
				}
				currentNode.value = suitValue[random];
				System.out.println(TAG + "suitValue:" + currentNode.value);
				charArray[index] = (char) ('0' + suitValue[random]);

				int solutionNum = getSolutionNum(charArray);
				System.out.println(TAG + "solutionNum:" + solutionNum);
				if (solutionNum == 1) {
					break a;

				} else if (solutionNum > 1) {
					if (minSum - noNeedSolve > 4) {
						/*
						 * charArray[index] = (char) ('0' + currentNode.value);
						 */
						hasFound = true;
						break;
					} else {
						hasSolutionValue[hasSolutionValueNum] = suitValue[random];
						hasSolutionValueNum++;
						suitValue[random] = null;
						noTrySuit--;
					}

				} else {
					suitValue[random] = null;
					noTrySuit--;
				}

			}

			if (!hasFound) {
				System.out.println(TAG + "has not found");
				int hasSolutionRandom = (int) (hasSolutionValueNum * Math.random());
				currentNode.value = hasSolutionValue[hasSolutionRandom];
				charArray[index] = (char) ('0' + hasSolutionValue[hasSolutionRandom]);
			}
			noNeedSolve++;
			printsoDuProcess(noNeedSolve);

		} while (true);
		System.out.println(TAG + "********************result*********************");
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				System.out.println(TAG + "********************************************");
			}
			System.out.println(TAG + SoduNode.getNodesValue(soduNodes[i][0].listNode));
		}
		int needEnterNum = (int) (minSum - noNeedSolve + 1 + (maxSum - minSum) * Math.random());
		System.out.println(TAG + "need enter num " + needEnterNum);
		if (needEnterNum > 0) {
			enterNum(needEnterNum);
			noNeedSolve = noNeedSolve + needEnterNum;
		}
		return soduNodes;
	}

	private int getMostSuitableValueIndex() {
		int mostSuitable = 1;
		ArrayList<SoduNode> mostSuitValueNodes = new ArrayList<>();
		for (int i = 0; i < 81; i++) {
			int y = i / 9;
			int x = i % 9;
			if (!soduNodes[x][y].needTobeSolve) {
				continue;
			}
			Integer[] suitValue = soduNodes[x][y].getSuitValue();
			if (suitValue.length > mostSuitable) {
				mostSuitable = suitValue.length;
				mostSuitValueNodes.clear();
				mostSuitValueNodes.add(soduNodes[x][y]);
			} else if (suitValue.length == mostSuitable) {
				mostSuitValueNodes.add(soduNodes[x][y]);
			}

		}
		int size = mostSuitValueNodes.size();
		SoduNode select = mostSuitValueNodes.get((int) (size * Math.random()));
		return select.yPosition * 9 + select.xPosition;
	}

	private void enterNum(int needEnterNum) {
		if (needEnterNum <= 0) {
			return;
		}
		int enterNum = 0;
		MySolutionFinder mySolutionFinder = new MySolutionFinder();
		SoduNode[][] result = mySolutionFinder.findResult(soduNodes);
		while (true) {
			if (enterNum == needEnterNum) {
				break;
			}
			int x = (int) (9 * Math.random());
			int y = (int) (9 * Math.random());
			if (soduNodes[x][y].value != 0) {
				continue;
			}
			if (soduNodes[x][y].getSuitValue() == null) {
				if (!hasNoSuitTableValue()) {
					break;
				} else {
					continue;
				}

			}
			if (soduNodes[x][y].getSuitValue().length < 3) {
				continue;
			}
			soduNodes[x][y].value = result[x][y].value;
			charArray[9 * x + y] = (char) ('0' + result[x][y].value);
			soduNodes[x][y].needTobeSolve = false;
			enterNum++;
		}

	}

	private boolean hasNoSuitTableValue() {
		boolean hasSuitableValue = false;
		for (int i = 0; i < 9; i++) {
			for (int k = 0; k < 9; k++) {
				if (!soduNodes[i][k].needTobeSolve) {
					continue;
				}
				Integer[] suitValue = soduNodes[i][k].getSuitValue();
				if (suitValue != null && suitValue.length > 2) {
					return true;
				}
			}
		}
		return false;
	}

	void printsoDuProcess(int time) {
		System.out.println(TAG + "********************" + time + "*********************");
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				System.out.println(TAG + "********************************************");
			}
			System.out.println(TAG + SoduNode.getNodesValue(soduNodes[i][0].listNode));
		}
		System.out.println(TAG + "*****************************************");
		System.out.println(TAG + "");
		System.out.println(TAG + "");
	}

	private int getSolutionNum(char[] charArray) {
		// maxSolutionNum++;
		return new MySolutionFinder().findSolution(charArray);
	}

	public SoduNode[][] generateEmptyGame() {
		charArray = new char[81];
		for (int i = 0; i < 81; i++) {
			charArray[i] = '0';
		}
		soduNodes = SoduUtils.getNodes(charArray);
		return soduNodes;
	}
	
	public static void main(String[] args){
		SoduNode[][] soduNodes = new MySoduGenerator().generateGame(SoduLevel.FREE);
		soduNodes = new MySolutionFinder().findResult(soduNodes);
		System.out.println(TAG + "********************solution*********************");
		for (int i = 0; i < 9; i++) {
			if (i % 3 == 0) {
				System.out.println(TAG + "********************************************");
			}
			System.out.println(TAG + SoduNode.getNodesValue(soduNodes[i][0].listNode));
		}
	}
}

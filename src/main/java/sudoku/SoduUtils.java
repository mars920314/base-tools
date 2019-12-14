package sudoku;

public class SoduUtils {
	private static final String TAG = "SoduUtils";

	public static SoduNode[][] getNodes(char[] charArray) {

		SoduNode[][] soduNodes = new SoduNode[9][9];
		for (int i = 0; i < 9; i++) {
			for (int k = 0; k < 9; k++) {
				SoduNode soduNode = new SoduNode();
				soduNode.value = charArray[i * 9 + k] - '0';
				soduNode.xPosition = k;
				soduNode.yPosition = i;
				if (soduNode.value != 0) {
					soduNode.needTobeSolve = false;
				}
				soduNodes[i][k] = soduNode;
			}
		}
		for (int i = 0; i < 9; i++) {
			SoduNode[] listNode = new SoduNode[9];
			for (int k = 0; k < 9; k++) {
				listNode[k] = soduNodes[i][k];
				soduNodes[i][k].listNode = listNode;
			}
			// System.out.println("listNode:"+SoduNode.getNodesValue(listNode));
		}
		for (int i = 0; i < 9; i++) {
			SoduNode[] rowNode = new SoduNode[9];
			for (int k = 0; k < 9; k++) {
				rowNode[k] = soduNodes[k][i];
				soduNodes[k][i].rowNode = rowNode;
			}
			// System.out.println("rowNode:"+SoduNode.getNodesValue(rowNode));
		}
		for (int i = 0; i <= 2; i++) {
			for (int k = 0; k <= 2; k++) {
				SoduNode[] groupNode = new SoduNode[9];
				int index = 0;
				int middlex = 3 * i + 1;
				int middley = 3 * k + 1;
				for (int j = -1; j <= 1; j++) {
					for (int l = -1; l <= 1; l++) {
						groupNode[index] = soduNodes[middlex + j][middley + l];
						soduNodes[middlex + j][middley + l].groupNode = groupNode;
						index++;
					}
				}
				// System.out.println("groupNode:"+SoduNode.getNodesValue(groupNode));
			}
		}
		return soduNodes;

	}
}

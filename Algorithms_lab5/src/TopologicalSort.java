
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class TopologicalSort {
	public static int v,e;
	public static int n=-1;
	public static String[] name;
	public static int[] inDegree;
	public static int[][] map;
	public static String[] result;
	public static int toNumber(String str) {
		int ans = 0;
		int length = str.length();
		for (int i=0;i<length;i++) {
			ans = ans*10 + str.charAt(i)-'0';
		}
		return ans;
	}
	public static void testStone(String fileName) throws IOException {
		int x,y;
		String str;
		String[] ss;
		BufferedReader br = new BufferedReader(new FileReader(fileName + ".in"));
		v = toNumber(br.readLine());
		name = new String[v];
		inDegree = new int[v];
		result = new String[v];
		map = new int[v][v];
		for (int i=0;i<v;i++) {
			name[i] = br.readLine();
		}
		e = toNumber(br.readLine());
		for (int i=0;i<e;i++){	
			str = br.readLine();
			ss = str.split(" ");
			x = toNumber(ss[0]);
			y = toNumber(ss[1]);
			map[x][y] = 1;
			inDegree[y]++;
		}
		br.close();
		
		topologicalSortRecursive();
		
		BufferedWriter bw = new BufferedWriter(
				new FileWriter("result1.txt"));
		if (n<v-1) {
			bw.write("No topological Order! There may exist a cycle!");
		} else {
			for (int i=0;i<v;i++) {
				if (!"ALL COURSES".equals(result[i])) {
					bw.write(result[i]+System.getProperty("line.separator"));
				}
			}
		}
		bw.close();
	}

	public static void topologicalSortLoop() {
		boolean flag = true;
		int from = 0;
		n = -1;
		while (flag) {
			flag = false;
			for (int i=0;i<v;i++) {
				if (inDegree[i] == 0) {
					flag = true;
					from = i;
					break;
				}
			}
			if (!flag) break;
			n++;
			result[n] = name[from];
			inDegree[from]--;
			for (int i=0;i<v;i++) {
				if (map[from][i] == 1) {
					inDegree[i]--;
					map[from][i] = 0;
				}
			}
		}
	}

	public static void topologicalSortRecursive() {
		for (int i=0;i<v;i++) {
			if (inDegree[i] == 0) {
				n++;
				result[n] = name[i];
				inDegree[i]--;
				for (int j=0;j<v;j++) {
					if (map[i][j] == 1) {
						inDegree[j]--;
						map[i][j] = 0;
					}
				}
				topologicalSortRecursive();			
			}
		}
	}
	public static void main(String[] args) throws IOException {
		testStone("lab5");
	}
}

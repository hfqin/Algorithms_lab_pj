import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyGraph {
	int n;
	int[] val, inDegree, dist;
	String[] road;
	boolean[][] edge;
	// read data
	public void readData(int testn) throws IOException {	
		int x,y;
		String str;
		String[] ss;
		BufferedReader br = new BufferedReader(new FileReader("lab6_test"+ testn +".in"));
		n = toNumber(br.readLine());
		val = new int[n];
		inDegree = new int[n];
		dist = new int[n];
		road = new String[n];
		edge = new boolean[n][n];
		for (int i=0;i<n;i++) {
			road[i] = "";
			val[i] = toNumber(br.readLine());
		}
		str = br.readLine();
		while (str != null){				
			ss = str.split(" ");
			x = toNumber(ss[0]);
			y = toNumber(ss[1]);
			edge[x][y] = true;
			inDegree[y]++;
			str = br.readLine();
		}
		br.close();
	}
	
	// count longest path
	public String topoSort() {
		int ans = 0;
		String ansString = "";
		boolean flag = true;
		while (flag) {
			int i = 0;
			// find a node which in_degree is 0 
			for (i=0;i<n;i++) {
				flag = false;
				if (inDegree[i] == 0) {
					// if start at i is longer
					if (val[i] > dist[i]) {
						dist[i] = val[i];
						road[i] += ""+val[i];
					}
					flag = true;
					break;
				}			
			}
			if (!flag) break;
			
			if (dist[i] > ans) {
				ans = dist[i];
				ansString = road[i];
			}
			// update dist
			for (int j=0;j<n;j++) {
				if (edge[i][j]) {
					if (dist[i] + val[j] > dist[j]) {					
						dist[j] = dist[i] + val[j];
						road[j] = road[i] + " " + val[j];						
					}
					inDegree[j]--;
				}
			}
			inDegree[i]--;
		}
		return ansString;
	}
	public static int toNumber(String str) {
		int ans = 0;
		int length = str.length();
		if (str.charAt(0)=='-') {
			for (int i=1;i<length;i++) {
				ans = ans*10 + str.charAt(i)-'0';
			} 
			ans = -ans;
		} else {
			for (int i=0;i<length;i++) {
				ans = ans*10 + str.charAt(i)-'0';
			} 
		}
		return ans;
	}
	public static void main(String[] args) throws IOException {
		for (int i=1;i<=3;i++) {
			MyGraph myGraph = new MyGraph();
			myGraph.readData(i);
			System.out.println("figure"+ i +": "+ myGraph.topoSort());
		}
	}
}

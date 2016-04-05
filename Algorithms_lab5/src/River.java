import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class River {
	public static int[] result = new int[100];
	public static boolean v[] = new boolean[16];
	public static boolean flag = false;
	public static int total = 0;
	public static String name[] = {"farmer","wolf","goat","cabbage"};
	public static boolean isLegal(int x) {
		if (x == 3)
			return false;
		if (x >= 6 && x <= 9)
			return false;
		if (x == 12)
			return false;
		return true;

	}

	public static void dfs(int deep, int now, int[] a) {
		if (flag)
			return;
		int y, temp;
		a[deep] = now;
		if (now == 15) {
			flag = true;
			total = deep;
			return;
		}
		if (now < 8) {
			for (int i = 0; i < 4; i++) {
				y = (now >>> i) & 1;
				if (y == 0) {
					temp = now | (1 << i) | 8;
					if (isLegal(temp) && !v[temp]) {						
						v[temp] = true;
						dfs(deep + 1, temp, a);
						v[temp] = false;
					}
				}
			}
		} else {		
			for (int i = 3; i >= 0; i--) {
				y = (now >>> i) & 1;
				if (y == 1) {
					temp = now & (~(1 << i)) & 7;
					if (isLegal(temp) && !v[temp]) {
						v[temp] = true;
						dfs(deep + 1, temp, a);
						v[temp] = false;
					}
				}
			}		
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		v[0] = true;
		dfs(0, 0, result);
		BufferedWriter bw = new BufferedWriter(new FileWriter("result2.txt"));
		int x,y;
		String start="";
		String end="";
		bw.write("              startEdge         |         endEdge" + System.getProperty("line.separator"));
		for (int i = 0; i <= total; i++) {
			x = result[i];
			start="";
			end="";
			for (int j=0;j<4;j++) {
				y = (x >>> j) & 1;
				if (y == 0) {
					start = start + name[3-j] + " ";
				} else {
					end = end + name[3-j] + " ";
				}
			}
			if (start=="") start = "nothing";
			if (end=="") end = "nothing";
			bw.write("step "+i+": "+start+"      |     "+end + System.getProperty("line.separator"));
		}

		bw.close();
	}

}

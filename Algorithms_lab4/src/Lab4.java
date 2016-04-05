import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Lab4 {
	public static int[][] f;
	public static char[][] road;
	public static void testStone(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName + ".in"));
		char[] x = br.readLine().toCharArray();
		char[] y = br.readLine().toCharArray();
		br.close();
		BufferedWriter bw = new BufferedWriter(
				new FileWriter(fileName + ".out"));

		int ans = calcDistance(x, y);
		bw.write(ans + "\n");
		bw.close();
	}

	public static int min(int x, int y) {
		if (x < y) return x; else return y;
	}
	// TODO You should implement a dynamic-programming algorithms and print the
	// optimal operation sequence to the console.
	public static int calcDistance(char[] x, char[] y) {	
		int length1 = x.length;
		int length2 = y.length;
		f = new int[length1+2][length2+2];
		road = new char[length1+2][length2+2];
		
		for (int i=0;i<length2+1;i++) {
			f[0][i] = i;
		}
		for (int i=0;i<length1+1;i++) {
			f[i][0] = i;
		}
		for (int i=1;i<length1+1;i++) {
			for (int j=1;j<length2+1;j++) {
				int temp = min(f[i-1][j]+1, f[i][j-1]+1);
				if (x[i-1] == y[j-1]) {
					f[i][j] = min(f[i-1][j-1], temp);
				} else {
					f[i][j] = min(f[i-1][j-1]+1, temp);
				}
				if (f[i][j] == f[i-1][j]+1) {
					// delete, incrementing i but leaving j alone
					road[i][j] = 'D';
				} else {
					if (f[i][j] == f[i][j-1]+1) {
						// insert, incrementing j but leaving i alone
						road[i][j] = 'I';
					} else {
						// replace
						road[i][j] = 'R';
					}
				}
			}
		}
		int i = length1;
		int j = length2;
		String str="";
		while (!(i == 0 && j == 0)) {
			if(road[i][j] == 'R') {			
				str = "Replace\t"+x[i-1] +" to "+ y[j-1] +"\n"+str;
				i--;
				j--;
			} else {
				if (road[i][j] == 'I' || i<=0)  {
					str = "Insert\t"+y[j-1] +"\n"+str;
					j--;
				} else {
					str = "Delete\t"+x[i-1] +"\n"+str;
					i--;
				}
			}
		}
		System.out.println(str);
		return f[length1][length2];
	}

	public static void main(String[] args) throws IOException {
		testStone("lab4");
	}
}

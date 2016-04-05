/*
 * 用RC4算法生成随机数作为Zobrist键值
 */
public class RC4password {
	byte[] s = new byte[256];
	int x,y;
	public void initial() {
		byte[] t = new byte[256];
		int j = 0;
		byte temp;
		for (int i=0;i<256;i++) {
			s[i] = (byte)i;
			t[i] = 0; // empty password
		}
		for (int i=0;i<256;i++) {
			j = (j + s[i] +t[i]) & 255;
			temp = s[i];
			s[i] = s[j];
			s[j] = temp;
		}
	}
	public byte nextByte() {
		byte temp;
		x = (x+1) & 255;
		y = (y + s[x]) & 255;
		temp = s[x];
		s[x] = s[y];
		s[y] = temp;
		return s[(s[x] + s[y]) & 255];	
	}
	public int nextInt() {
		int k1 = nextByte();
		int k2 = nextByte();
		int k3 = nextByte();
		int k4 = nextByte();
		return k1 + (k2<<8) + (k3<<16) + (k4<<24);
	}
}

/*
 * Zobrist值,表示局面。
 */
public class Zobrist {
	int dwKey = 0;
	public void initial(RC4password rc4password) {
		dwKey = rc4password.nextInt();
	}
	public void xorKey(Zobrist zobr) {
		dwKey ^= zobr.dwKey;
	}
}

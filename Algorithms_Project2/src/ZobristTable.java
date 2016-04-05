/*
 * Zobrist表,player表示是哪方玩家,table[i][j]表示棋子i在j位置时的键值,用RC4
 * 算法生成，保证其随机性
 */
public class ZobristTable {
	Zobrist player;
	Zobrist[][] table = new Zobrist[14][100];
	public ZobristTable() {
		RC4password rc4password = new RC4password();
		rc4password.initial();
		player = new Zobrist();
		player.initial(rc4password);
		for (int i=0;i<14;i++) {
			for (int j=0;j<100;j++) {
				table[i][j] = new Zobrist();
				table[i][j].initial(rc4password);
			}
		}
	}
}

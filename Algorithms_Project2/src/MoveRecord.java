/*
 * 记录走过走法,和其对应的局面键值
 */
public class MoveRecord {
	int mv;
	String tempPiece;
	boolean isDanger;
	int dwKey;
	public MoveRecord(int mv, String tempPiece, boolean isDanger, int dwKey) {
	    this.mv = mv;
	    this.tempPiece = tempPiece;
	    this.isDanger = isDanger;
	    this.dwKey = dwKey;
	  }
}

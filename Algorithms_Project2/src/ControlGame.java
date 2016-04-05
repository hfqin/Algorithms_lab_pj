import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JOptionPane;

/*
 * 控制程序的流程和记录关键变量
 */
public class ControlGame {
	public boolean isChoosed = false;
	public int nDistance;
	public MoveRecord[] moveRecords = new MoveRecord[10000];
	public int nRecordMoves;
	public Zobrist zobrist = new Zobrist();
	public ZobristTable zobristTable = new ZobristTable();
	public boolean isPcSide = false;
	public int choosedX;
	public int choosedY;
	public int mvResult;
	public int nHistory[] = new int[10000];
	public int totalStatus = 0;
	public boolean isGameOver = false;
	public int redVal;
	public int blackVal;
	public MyFunction myFunction; 
	public Chess[][] chess = new Chess[10][9];
	public String[][] map = new String[10][9];
	/*
	 * 构造方法,获取程序的View和Data
	 */
	public ControlGame(Chess[][] chess, String[][] map) {
		this.chess = chess;
		this.map = map;
		myFunction = new MyFunction(this);
	}
	
	/*
	 * 初始化棋盘
	 */
	public void playChess() {
		String[] ss = new String[9];
		FileReader fileReader = null;	
		//读取初始位置
		try {
			fileReader = new FileReader("res/start.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.addPiece(i, j, ss[j]);
			}
		}
		
		// initial black
		for (int i=0;i<10;i++) {
			for (int j=0;j<9;j++) {
				chess[i][j] = MyGame.findChessById(map[i][j], i, j);
			}
		}
	
		MyListener myListener = new MyListener(this);
		for (int i=0;i<10;i++) {
			for (int j=0;j<9;j++) {		
				chess[i][j].addActionListener(myListener);
			}
		}
		
		// 读取各个棋子在不同位置的价值,记录在数组中
		// initital valB[][]
		try {
			fileReader = new FileReader("res/valB.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valB[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		
		//initial valS[][]
		try {
			fileReader = new FileReader("res/valS.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valS[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		
		// initial valX[][]
		try {
			fileReader = new FileReader("res/valX.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valX[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		
		//initial valM[][]
		try {
			fileReader = new FileReader("res/valM.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valM[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		
		// initial valJ[][]
		try {
			fileReader = new FileReader("res/valJ.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valJ[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		
		//initial valP[][]
		try {
			fileReader = new FileReader("res/valP.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valP[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		
		// initial valZ[][]
		try {
			fileReader = new FileReader("res/valZ.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		br = new BufferedReader(fileReader);
		for (int i=0;i<10;i++){	
			String str = null;
			try {
				str = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ss = str.split(" ");
			for (int j=0;j<9;j++) {
				myFunction.valZ[i][j] = myFunction.toNumber(ss[j]);
			}
		}
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 初始化历史记录
		this.moveRecords[0] = new MoveRecord(0, "00", myFunction.isDanger(), zobrist.dwKey);
	    this.nRecordMoves = 0;
	   // responseMoveChess();
	}
	
	// 玩家走一步棋
	public void moveChess(int mv) {
		int vlRepStatus;
		// 是否合法
		if (myFunction.isLegalMove(mv)) {			
			if (myFunction.makeMove(mv, "") != null) {
				System.out.println("player: " + mv);
				if (myFunction.isMate()) {
					// 如果玩家胜利,显示提示并结束游戏
					JOptionPane.showMessageDialog(null, "You win!");
					isGameOver = true;
				} else {
					// 判断重复局面
					vlRepStatus = myFunction.repStatus(3);
					// 如果出现3次重复局面
					if (vlRepStatus > 0) {
						// 电脑单方面长将,玩家胜利
						if (myFunction.repValue(vlRepStatus) > myFunction.WIN_VALUE) {
							JOptionPane.showMessageDialog(null,
									"AI act shamelessly, You win!");
							this.isGameOver = true;
						} else {
							// 玩家单方面长将,电脑胜利
							if (myFunction.repValue(vlRepStatus) < -myFunction.WIN_VALUE) {
								JOptionPane.showMessageDialog(null,
										"You act shamelessly, AI win!");
								this.isGameOver = true;
							} else {
								// 双方都长将或都无长将,判和
								JOptionPane.showMessageDialog(null,
										"It is a Draw!");
								this.isGameOver = true;
							}
						}
					} else {
						// 如果没有出现重复局面,但超过50回合,判和
						if (nRecordMoves > 100) {
							JOptionPane
									.showMessageDialog(null, "It is a Draw!");
							this.isGameOver = true;
						}
					}
				}
				// 如果游戏未结束,换电脑走棋
				if (!isGameOver) {
					isChoosed = false;
					printBoard();
					responseMoveChess();
				}
			}
		}
	}
	
	// 电脑走一步棋
	public void responseMoveChess() {
		// 搜索走法
		int vlRepStatus;
		myFunction.searchMain();
		myFunction.makeMove(mvResult,"");
		System.out.println("AI: "+mvResult);
		printBoard();
		
		// 如果电脑胜利,提示并结束游戏
		if (myFunction.isMate()) {
			JOptionPane.showMessageDialog(null, "AI win!");
			this.isGameOver = true;
		} else {
			// 判断重复局面
			vlRepStatus = myFunction.repStatus(3);
			// 如果出现3次重复局面
			if (vlRepStatus>0) {
				// 玩家方单方面长将,电脑胜利
				if (myFunction.repValue(vlRepStatus) > myFunction.WIN_VALUE) {
					JOptionPane.showMessageDialog(null, "You act shamelessly, AI win!");
					this.isGameOver = true;
				} else {
					// 电脑单方面长将,玩家胜利
					if (myFunction.repValue(vlRepStatus) < - myFunction.WIN_VALUE) {
						JOptionPane.showMessageDialog(null, "AI act shamelessly, You win!");
						this.isGameOver = true;
					} else {
						// 双方都长将或都无长将,判和
						JOptionPane.showMessageDialog(null, "It is a Draw!");
						this.isGameOver = true;
					}
				}
			} else {
				// 如果没有出现重复局面,但超过50回合,判和
				if (nRecordMoves > 100) {
					JOptionPane.showMessageDialog(null, "It is a Draw!");
					this.isGameOver = true;
				}
			}
		}
		
	}
	
	// 根据Data更新View
	public void printBoard() {
		for (int i=0;i<10;i++) {
			for (int j=0;j<9;j++) {
				chess[i][j] .setPicture(MyGame.findIconById(map[i][j]));
			}
		}
	}
}

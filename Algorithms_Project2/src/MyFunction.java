
import java.util.Arrays;
import java.util.Comparator;

/*
 * 提供AI的各种方法
 */
public class MyFunction {
	public int[][] valZ = new int[10][9];
	public int[][] valJ = new int[10][9];
	public int[][] valM = new int[10][9];
	public int[][] valP = new int[10][9];
	public int[][] valX = new int[10][9];
	public int[][] valS = new int[10][9];
	public int[][] valB = new int[10][9];
	public int[] hangJPBZ = { 0, 0, 1, -1 };
	public int[] lieJPBZ = { 1, -1, 0, 0 };
	public int[] hangM = { 1, 1, -1, -1, 2, 2, -2, -2 };
	public int[] lieM = { 2, -2, 2, -2, 1, -1, 1, -1 };
	public int[] hangX = { 2, 2, -2, -2 };
	public int[] lieX = { -2, 2, -2, 2 };
	public int[] hangS = { 1, 1, -1, -1 };
	public int[] lieS = { 1, -1, 1, -1 };
	public int[] mvvLvaVal = {5,1,1,3,4,3,2};
	public final int MATE_VALUE = 10000;
	public final int WIN_VALUE = MATE_VALUE - 100;
	public final int NULL_VALUE = 400;
	public final int LIMIT_DEEP = 10;
	public ControlGame controller;
	public String[][] map = new String[10][9];
	public Zobrist zobrist;
	public ZobristTable zobristTable;
	public MoveRecord[] mvRecords;
	public MyHistoryCmp historyCmp = new MyHistoryCmp();
	public MyMvvLvaCmp mvvLvaCmp = new MyMvvLvaCmp();
	/*
	 * 构造方法,获取与游戏控制和流程相关的关键值
	 */
	public MyFunction(ControlGame controller) {
		this.controller = controller;
		this.map = controller.map;
		this.zobrist = controller.zobrist;
		this.zobristTable = controller.zobristTable;
		this.mvRecords = controller.moveRecords;
	}
	
	// 改变当前该哪方走棋
	public void changeSide() {
		controller.isPcSide = !controller.isPcSide;
		zobrist.xorKey(zobristTable.player);
	}
	// 判断坐标(x,y)是否在棋盘内
	public boolean isInMap(int x, int y) {
		if (x >= 0 && y >= 0 && x < 10 && y < 9)
			return true;
		return false;
	}

	// 把数字的字符串形式转化为它的整数类型
	public int toNumber(String str) {
		int ans = 0;
		int length = str.length();
		for (int i = 0; i < length; i++) {
			ans = ans * 10 + str.charAt(i) - '0';
		}
		return ans;
	}

	// 判断两个棋子是否是敌人
	public boolean isEnemy(String s1, String s2) {
		if (s1.charAt(0) == 'r' && s2.charAt(0) == 'b')
			return true;
		if (s1.charAt(0) == 'b' && s2.charAt(0) == 'r')
			return true;
		return false;
	}

	// 判断是否在九宫格内
	public boolean isInSudoku(int x, int y) {
		if ((x < 7 && !controller.isPcSide) || (x > 2 && controller.isPcSide))
			return false;
		if (y < 3 || y > 5)
			return false;
		return true;
	}

	// 判断是否过河
	public boolean isCrossRiver(int x) {
		if ((x < 5 && !controller.isPcSide) || (x > 4 && controller.isPcSide))
			return true;
		return false;
	}
	
	// 通过棋子类型返回其在某些数组中的编号
	public int numId(String str) {
		int x , y;
		if (str.charAt(0)=='r') {
			x = 0;
		} else {
			x = 7;
		}
		switch (str.charAt(1)) {
		case 'b':
		    y = 0;	
			break;
		case 's':
			y = 1;
			break;
		case 'x':
			y = 2;
			break;
		case 'm':
			y = 3;
			break;
		case 'j':
			y = 4;
			break;
		case 'p':
			y = 5;
			break;
		default:
			y = 6;
			break;
		}
		return x + y;
	}
	
	// 计算局面的价值
	public int totalVal() {
		if (controller.isPcSide) {
			return controller.blackVal - controller.redVal;
		} else {
			return controller.redVal - controller.blackVal;
		}
	}
	
	// 返回单个棋子what在(i,j)位置的价值
	public int countValue(int i, int j, String what) {
		if (what.charAt(1) == 'j')
			return valJ[i][j];

		if (what.charAt(1) == 'm')
			return valM[i][j];

		if (what.charAt(1) == 'p')
			return valP[i][j];

		if (what.charAt(1) == 'x')
			return valX[i][j];

		if (what.charAt(1) == 's')
			return valS[i][j];

		if (what.charAt(1) == 'z')
			return valZ[i][j];

		return valB[i][j];

	}

	// 计算mv走法(该走法必须为吃子着法)的Mvv/Lva价值,即吃方棋子的价值减去被吃方棋子的价值
	public int countMvvLva(int mv) {
		int dstj = mv % 10;
		mv = mv / 10;
		int dsti = mv % 10;
		mv = mv / 10;
		int srcj = mv % 10;
		mv = mv / 10;
		int srci = mv % 10;
		return mvvLvaVal[numId(map[dsti][dstj]) % 7]
				- mvvLvaVal[numId(map[srci][srcj]) % 7];
	}
	
	// 计算当前局面当前方的所有走法,若不被将军(isDanger等于false),只生成吃子着法
	public int searchStatus(Integer[] myMoves, int totalStatus, boolean isDanger) {

		int x, y, mv;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 9; j++) {
				if ((map[i][j].charAt(0) == 'r' && !controller.isPcSide)
						|| (map[i][j].charAt(0) == 'b' && controller.isPcSide)) {
					x = i;
					y = j;
					switch (map[i][j].charAt(1)) {
					case 'm':
						for (int k = 0; k < 8; k++) {
							x = i + hangM[k];
							y = j + lieM[k];

							if (isInMap(x, y)) {
								mv = i * 1000 + j * 100 + x * 10 + y;
								if (isLegalMove(mv)) {

									// System.out.println("find and get "+map[i][j]+" "+i+" "+j+" "+x+" "+y);
									if (isDanger) {
										totalStatus++;
										myMoves[totalStatus] = mv;
									} else {
										if (isEnemy(map[i][j], map[x][y])) {
											totalStatus++;
											myMoves[totalStatus] = mv;
										}
									}
								}
							}
						}
						break;
					case 'x':
						for (int k = 0; k < 4; k++) {
							x = i + hangX[k];
							y = j + lieX[k];
							if (isInMap(x, y)) {
								mv = i * 1000 + j * 100 + x * 10 + y;
								if (isLegalMove(mv)) {
									if (isDanger) {
										totalStatus++;
										myMoves[totalStatus] = mv;
									} else {
										if (isEnemy(map[i][j], map[x][y])) {
											totalStatus++;
											myMoves[totalStatus] = mv;
										}
									}
								}
							}
						}
						break;
					case 's':
						for (int k = 0; k < 4; k++) {
							x = i + hangS[k];
							y = j + lieS[k];
							if (isInMap(x, y)) {
								mv = i * 1000 + j * 100 + x * 10 + y;
								if (isLegalMove(mv)) {
									if (isDanger) {
										totalStatus++;
										myMoves[totalStatus] = mv;
									} else {
										if (isEnemy(map[i][j], map[x][y])) {
											totalStatus++;
											myMoves[totalStatus] = mv;
										}
									}
								}
							}
						}
						break;
					case 'z':
						for (int k = 0; k < 4; k++) {
							x = i + hangJPBZ[k];
							y = j + lieJPBZ[k];
							if (isInMap(x, y)) {
								mv = i * 1000 + j * 100 + x * 10 + y;
								if (isLegalMove(mv)) {
									if (isDanger) {
										totalStatus++;
										myMoves[totalStatus] = mv;
									} else {
										if (isEnemy(map[i][j], map[x][y])) {
											totalStatus++;
											myMoves[totalStatus] = mv;
										}
									}
								}
							}
						}
						break;
					case 'b':
						for (int k = 0; k < 4; k++) {
							x = i + hangJPBZ[k];
							y = j + lieJPBZ[k];
							if (isInMap(x, y)) {
								mv = i * 1000 + j * 100 + x * 10 + y;
								if (isLegalMove(mv)) {
									if (isDanger) {
										totalStatus++;
										myMoves[totalStatus] = mv;
									} else {
										if (isEnemy(map[i][j], map[x][y])) {
											totalStatus++;
											myMoves[totalStatus] = mv;
										}
									}
								}
							}
						}
						break;
					default:
						for (int k = 0; k < 4; k++) {
							x += hangJPBZ[k];
							y += lieJPBZ[k];
							while (isInMap(x, y)) {
								mv = i * 1000 + j * 100 + x * 10 + y;
								if (isLegalMove(mv)) {
									if (isDanger) {
										totalStatus++;
										myMoves[totalStatus] = mv;
									} else {
										if (isEnemy(map[i][j], map[x][y])) {
											totalStatus++;
											myMoves[totalStatus] = mv;
										}
									}
								}
								x += hangJPBZ[k];
								y += lieJPBZ[k];
							}
							x = i;
							y = j;
						}
						break;
					}

				}
			}
		}
		return totalStatus;
	}

	// 判断走法mv是否合法
	public boolean isLegalMove(int mv) {
		int dstj = mv % 10;
		mv = mv / 10;
		int dsti = mv % 10;
		mv = mv / 10;
		int srcj = mv % 10;
		mv = mv / 10;
		int srci = mv % 10;
		if ((map[dsti][dstj].charAt(0) == 'r' && !controller.isPcSide)
				|| (map[dsti][dstj].charAt(0) == 'b' && controller.isPcSide)) {
			return false;
		}
		switch (map[srci][srcj].charAt(1)) {
		case 'm':
			if (Math.abs(srci - dsti) == 2) {
				if (Math.abs(srcj - dstj) != 1) {
					return false;
				}
				if (!"00".equals(map[(srci + dsti) / 2][srcj])) {
					return false;
				}
				return true;
			} else {
				if (Math.abs(srci - dsti) != 1 || Math.abs(srcj - dstj) != 2) {
					return false;
				}
				if (!"00".equals(map[srci][(srcj + dstj) / 2])) {
					return false;
				}
			}
			break;
		case 'x':
			if (isCrossRiver(dsti)) {
				// System.out.println(ControlGame.isPcSide+"HHHHHH");
				return false;
			}
			if (Math.abs(srci - dsti) != 2 || Math.abs(srcj - dstj) != 2) {
				return false;
			}
			if (!"00".equals(map[(srci + dsti) / 2][(srcj + dstj) / 2])) {
				return false;
			}
			break;
		case 's':
			if (!isInSudoku(dsti, dstj)) {
				return false;
			}
			if (Math.abs(srci - dsti) != 1 || Math.abs(srcj - dstj) != 1) {
				return false;
			}
			break;
		case 'b':
			if (!isInSudoku(dsti, dstj)) {
				return false;
			}
			if ((Math.abs(srci - dsti) + Math.abs(srcj - dstj)) != 1) {
				return false;
			}
			break;
		case 'z':
			if ((srci < dsti && !controller.isPcSide)
					|| (srci > dsti && controller.isPcSide)) {
				return false;
			}
			if ((Math.abs(srci - dsti) + Math.abs(srcj - dstj)) != 1) {
				return false;
			}
			if (!isCrossRiver(dsti) && srcj != dstj) {
				return false;
			}
			break;
		case 'p':
			int count = 0;
			int from;
			int to;
			if (srci != dsti && srcj != dstj) {
				return false;
			}
			if (srci == dsti) {
				from = Math.min(srcj, dstj);
				to = Math.max(srcj, dstj);
				for (int j = from; j <= to; j++) {
					if (!"00".equals(map[srci][j])) {
						count++;
					}
				}
			} else {
				from = Math.min(srci, dsti);
				to = Math.max(srci, dsti);
				for (int i = from; i <= to; i++) {
					if (!"00".equals(map[i][srcj])) {
						count++;
					}
				}
			}
			if (!((count == 3 && !"00".equals(map[dsti][dstj])) || count == 1)) {
				return false;
			}
			break;
		default:
			int count1 = 0;
			int from1;
			int to1;
			if (srci != dsti && srcj != dstj) {
				return false;
			}
			if (srci == dsti) {
				from1 = Math.min(srcj, dstj);
				to1 = Math.max(srcj, dstj);
				for (int j = from1; j <= to1; j++) {
					if (!"00".equals(map[srci][j])) {
						count1++;
					}
				}
			} else {
				from1 = Math.min(srci, dsti);
				to1 = Math.max(srci, dsti);
				for (int i = from1; i <= to1; i++) {
					if (!"00".equals(map[i][srcj])) {
						count1++;
					}
				}
			}
			if (!((count1 == 2 && !"00".equals(map[dsti][dstj])) || count1 == 1)) {
				return false;
			}
			break;
		}
		return true;
	}

	// 向棋盘添加一个棋子,改变局面价值和局面Zobristz值
	public void addPiece(int i, int j, String what) {
		map[i][j] = what;
		if (what.charAt(0) == 'r') {
			controller.redVal += countValue(i, j, what);
		} else {
			controller.blackVal += countValue(9 - i, 8 - j, what);			
		}
		zobrist.xorKey(zobristTable.table[numId(what)][i*10+j]);
	}

	// 删除棋盘中一个棋子,改变局面价值和局面Zobristz值
	public void delPiece(int i, int j, String what) {
		map[i][j] = "00";
		if (what.charAt(0) == 'r') {
			controller.redVal -= countValue(i, j, what);
		} else {
			controller.blackVal -= countValue(9 - i, 8 - j, what);
		}
		zobrist.xorKey(zobristTable.table[numId(what)][i*10+j]);
	}

	// 搬一步棋子
	public String movePiece(int mv) {
		int dstj = mv % 10;
		mv = mv / 10;
		int dsti = mv % 10;
		mv = mv / 10;
		int srcj = mv % 10;
		mv = mv / 10;
		int srci = mv % 10;
		String tempPiece, fromPiece;
		tempPiece = map[dsti][dstj];
		if (!"00".equals(tempPiece)) {
			delPiece(dsti, dstj, tempPiece);
		}
		fromPiece = map[srci][srcj];
		delPiece(srci, srcj, fromPiece);
		addPiece(dsti, dstj, fromPiece);
		return tempPiece;
	}

	// 撤销搬一步棋子
	public void moveBack(int mv, String tempPiece) {
		int dstj = mv % 10;
		mv = mv / 10;
		int dsti = mv % 10;
		mv = mv / 10;
		int srcj = mv % 10;
		mv = mv / 10;
		int srci = mv % 10;
		String dstPiece = map[dsti][dstj];
		delPiece(dsti, dstj, dstPiece);
		addPiece(srci, srcj, dstPiece);
		if (!"00".equals(tempPiece)) {
			addPiece(dsti, dstj, tempPiece);
		}
	}
	
	// 判断是否能空步裁剪
	public boolean canNullMove() {
		if (controller.isPcSide) {
			if (controller.redVal < NULL_VALUE) return false;
		} else {
			if (controller.blackVal < NULL_VALUE) return false;
		}
		if (isDanger()) {
			return false;
		}
		return true;
	}
	
	// 判断当前是否被将军
	public boolean isDanger() {
		int x, y, count;
		int i = 0;
		int j = 0;
		boolean findBoss = false;
		for (i = 0; i < 10; i++) {
			for (j = 0; j < 9; j++) {
				if (("bb".equals(map[i][j]) && controller.isPcSide)
						|| ("rb".equals(map[i][j]) && !controller.isPcSide)) {
					findBoss = true;
					break;
				}
			}
			if (findBoss)
				break;
		}
		if (i==10 && j==9) return true;
		// z
		for (int k = 0; k < 4; k++) {
			x = i + hangJPBZ[k];
			y = j + lieJPBZ[k];
			if (isInMap(x, y)) {
				if (map[x][y].charAt(1) == 'z' && isEnemy(map[i][j], map[x][y])) {
					// System.out.println(x + " " + y +" " + "danger z");
					if(x != i) {
						if (controller.isPcSide) {
							if (x>i) return true;				
						} else {
							if (x<i) return true;
						}
					} else {
						return true;
					}
				}
			}
		}

		// m
		for (int k = 0; k < 8; k++) {
			x = i + hangM[k];
			y = j + lieM[k];

			if (isInMap(x, y)) {
				// System.out.println(controller.nDistance+" :  "+i+" "+j+"  "+x+" "+y);
				if (map[x][y].charAt(1) == 'm' && isEnemy(map[i][j], map[x][y])) {
					// System.out.println(x + " " + y +" " + "danger m");
					if (Math.abs(x - i) == 2) {
						if ("00".equals(map[(x + i) / 2][y])) {
					//		System.out.println("danger m is "+i+" "+j+"  "+x+" "+y);
							return true;
						}
					} else {
						if ("00".equals(map[x][(y + j) / 2])) {
							return true;
						}
					}
				}
			}
		}

		// j b p
		x = i;
		y = j;
		for (int k = 0; k < 4; k++) {
			// j b
			x += hangJPBZ[k];
			y += lieJPBZ[k];
			while (isInMap(x, y)) {
				// System.out.println(i+" "+j+"  "+x+" "+y);
				if ("00".equals(map[x][y])) {
					x += hangJPBZ[k];
					y += lieJPBZ[k];
					continue;
				}
				if (map[i][j].charAt(0) == map[x][y].charAt(0))
					break;
				if (map[x][y].charAt(1) == 'j' || map[x][y].charAt(1) == 'b') {
					// System.out.println(x + " " + y +" " + "danger jb");
					return true;
				} else {
					break;
				}

			}
			x = i;
			y = j;

			// p
			x += hangJPBZ[k];
			y += lieJPBZ[k];
			count = 0;
			while (isInMap(x, y)) {
				if (!"00".equals(map[x][y])) {
					count++;
					if (count == 2) {
						if (map[x][y].charAt(1) == 'p'
								&& isEnemy(map[x][y], map[i][j])) {
							// System.out.println(x + " " + y +" " +
							// "danger p");
							return true;
						}
						break;
					}
				}
				x += hangJPBZ[k];
				y += lieJPBZ[k];
			}
			x = i;
			y = j;
		}
		return false;
	}

	// 判断是否胜利
	public boolean isMate() {
		Integer[] myMoves = new Integer[10000];
		int totalStatus = 0;
		String tempPiece;
		totalStatus = searchStatus(myMoves, 0, true);
		for (int i = 1; i <= totalStatus; i++) {
			tempPiece = movePiece(myMoves[i]);
			if (!isDanger()) {
				moveBack(myMoves[i], tempPiece);
				return false;
			} else {
				moveBack(myMoves[i], tempPiece);
			}
		}
		return true;
	}
	
	// 检查重复局面,返回值int值,0表示无重复局面,大于0表示有重复局面
	// 1表示双方都无长将,3表示本方单方面长将,5表示对方单方面长将,7表示双方都有长将
	public int repStatus(int step) {
		boolean isRedSide, isBlackDanger, isRedDanger;
		isRedSide = false;
		isBlackDanger = true;
		isRedDanger = true;
		int redDanger, blackDanger;
		int i = controller.nRecordMoves;
		while (mvRecords[i].mv != 0 && "00".equals(mvRecords[i].tempPiece)) {
		//	System.out.println(mvRecords[i].mv + " "+mvRecords[i].tempPiece +" "+mvRecords[i].isDanger+" "+mvRecords[i].dwKey);
			if (isRedSide) {
			//	System.out.println("danger "+mvRecords[i].isDanger + " "+i);
				isRedDanger = isRedDanger && mvRecords[i].isDanger;
				if (mvRecords[i].dwKey == zobrist.dwKey) {
					step--;
					if (step == 0) {
						if (isRedDanger) {
							redDanger = 2;
						} else {
							redDanger = 0;
						}
						if (isBlackDanger) {
							blackDanger = 4;
						} else {
							blackDanger = 0;
						}
						return 1 + redDanger + blackDanger;
					}
				}
			} else {
				isBlackDanger = isBlackDanger && mvRecords[i].isDanger;
			}
			isRedSide = !isRedSide;
			i--;
		}
		return 0;
	}
	
	// 计算重复局面的价值,1和7判和,3判本方负,5判本方胜
	public int repValue(int vlRepStatus) {
		if (vlRepStatus == 1 || vlRepStatus == 7) {
			return 0;
		}
		if (vlRepStatus == 3) {
			return controller.nDistance-MATE_VALUE;
		} else {
			return MATE_VALUE-controller.nDistance;
		}
	}
	
	// 走一步空步
	public void nullMove() { 
		int dwKey;
		dwKey = zobrist.dwKey;
		changeSide();
		controller.nRecordMoves++;
		mvRecords[controller.nRecordMoves] = new MoveRecord(0, "00", false,
				dwKey);
		controller.nDistance++;
	}
	
	// 撤销一步空步
	public void unDoNullMove() { 
		controller.nDistance--;
		changeSide();
		controller.nRecordMoves--;
	}
	
	// 走一步棋
	public String makeMove(int mv, String tempPiece) {
		int dwKey;
		dwKey = zobrist.dwKey;
		tempPiece = movePiece(mv);
		boolean isDanger = isDanger();
		if (isDanger) {
			moveBack(mv, tempPiece);
			return null;
		}
		changeSide();
		controller.nDistance++;
		controller.nRecordMoves++;
		mvRecords[controller.nRecordMoves] = new MoveRecord(mv, tempPiece, isDanger, dwKey);	
		return tempPiece;
	}

	// 撤销走一步棋
	public void unDoMakeMove(int mv, String tempPiece) {
		moveBack(mv, tempPiece);
		controller.nDistance--;
		controller.nRecordMoves--;
		changeSide();
	}
	
	// 静态搜索
	public int staticSearch(int vlAlpha, int vlBeta) {
		Integer[] myMoves = new Integer[10000];
		int vl, vlBest;
		int i;
		int totalStatus = 0;
		String tempPiece = "00";
		
		// 一个静态搜索分为以下几个阶段

		  // 1. 检查重复局面
		vl = repStatus(1);
		if (vl != 0) {
			return repValue(vl);
		}
		
		// 2. 到达极限深度就返回局面评价
		if (controller.nDistance == LIMIT_DEEP) {
			return totalVal();
		}
		
		// 3. 初始化最佳值
		vlBest = -MATE_VALUE; // 这样可以知道，是否一个走法都没走过(杀棋)

		if (isDanger()) {
			// 4. 如果被将军，则生成全部走法
			totalStatus = searchStatus(myMoves, 0, true);
			Arrays.sort(myMoves, 1, totalStatus + 1, historyCmp);
		} else {
			// 5. 如果不被将军，先做局面评价
			vl = totalVal();
			if (vl > vlBest) {
				vlBest = vl;
				if (vl >= vlBeta) {
					return vl;
				}
				if (vl > vlAlpha) {
					vlAlpha = vl;
				}
			}
			
			// 6. 如果局面评价没有截断，再生成吃子走法
			totalStatus = searchStatus(myMoves, 0, false);
			Arrays.sort(myMoves, 1, totalStatus + 1, mvvLvaCmp);
		}
		
		// 7. 逐一走这些走法，并进行递归
		for (i = 1; i <= totalStatus; i++) {
		
			tempPiece = makeMove(myMoves[i], tempPiece);
			if (tempPiece != null) {
				vl = -staticSearch(-vlBeta, -vlAlpha);
				unDoMakeMove(myMoves[i], tempPiece);
				
				// 8. 进行Alpha-Beta大小判断和截断
				if (vl > vlBest) {	  // 找到最佳值(但不能确定是Alpha、PV还是Beta走法)
					vlBest = vl;	  // "vlBest"就是目前要返回的最佳值，可能超出Alpha-Beta边界
					if (vl >= vlBeta) { // 找到一个Beta走法
						return vl;     // Beta截断
					}
					if (vl > vlAlpha) {  // 找到一个PV走法
						vlAlpha = vl;    // 缩小Alpha-Beta边界
					}
				}
			}
		}
		// 9. 所有走法都搜索完了，返回最佳值
		if (vlBest == -MATE_VALUE) {
			return controller.nDistance - MATE_VALUE;
		} else {
			return vlBest;
		}
	}
	
	// Alpha-Beta搜索过程
	public int alphaBeta(int vlAlpha, int vlBeta, int nDepth, int nullMoveTime) {
		Integer[] myMoves = new Integer[10000];
		int vl, vlBest;
		int mvBest;
		int i;
		int totalStatus = 0;
		String tempPiece = "00";
		// 一个Alpha-Beta完全搜索分为以下几个阶段
		if (controller.nDistance > 0) {
			// 1. 到达水平线，则调用静态搜索(注意：由于空步裁剪，深度可能小于零)
			if (nDepth <= 0) {
				return staticSearch(vlAlpha, vlBeta);
			}

			// 1-1. 检查重复局面(注意：不要在根节点检查，否则就没有走法了)
			vl = repStatus(1);
			if (vl != 0) {
				return repValue(vl);
			}
			
			// 1-2. 到达极限深度就返回局面评价
			if (controller.nDistance == LIMIT_DEEP) {
				System.out.println("dafdfa");
				return totalVal();
			}
			
			// 1-3. 尝试空步裁剪(根节点的Beta值是"MATE_VALUE"，所以不可能发生空步裁剪)
			if (nullMoveTime == 1 && canNullMove()) {
				nullMove();
				vl = -alphaBeta(-vlBeta, -vlAlpha, nDepth - 3, 2);
				unDoNullMove();
				if (vl >= vlBeta) {
					return vl;
				}
			}
		}
		
		// 2. 初始化最佳值和最佳走法
		vlBest = -MATE_VALUE; // 这样可以知道，是否一个走法都没走过(杀棋)
		mvBest = 0;// 这样可以知道，是否搜索到了Beta走法或PV走法，以便保存到历史表
		
		// 3. 生成全部走法，并根据历史表排序
		totalStatus = searchStatus(myMoves, 0, true);
		Arrays.sort(myMoves, 1, totalStatus + 1, historyCmp);

		// 4. 逐一走这些走法，并进行递归
		for (i = 1; i <= totalStatus; i++) {
			tempPiece = makeMove(myMoves[i], tempPiece);
			if (tempPiece != null) {

				vl = -alphaBeta(-vlBeta, -vlAlpha, nDepth - 1,1);
				unDoMakeMove(myMoves[i], tempPiece);

				// 5. 进行Alpha-Beta大小判断和截断
				if (vl > vlBest) {             // 找到最佳值(但不能确定是Alpha、PV还是Beta走法)
					vlBest = vl;               // "vlBest"就是目前要返回的最佳值，可能超出Alpha-Beta边界
					if (vl >= vlBeta) {        // 找到一个Beta走法
						mvBest = myMoves[i];   // Beta走法要保存到历史表
						break;                 // Beta截断
					}
					if (vl > vlAlpha) {        // 找到一个PV走法
						mvBest = myMoves[i];   // PV走法要保存到历史表
						vlAlpha = vl;          // 缩小Alpha-Beta边界
					}
				}
			}
		}
		
		// 6. 所有走法都搜索完了，把最佳走法(不能是Alpha走法)保存到历史表，返回最佳值
		if (vlBest == -MATE_VALUE) {
			// 如果是杀棋，就根据杀棋步数给出评价
			return controller.nDistance - MATE_VALUE;
		}
		if (mvBest != 0) {
			// 如果不是Alpha走法，就将最佳走法保存到历史表
			controller.nHistory[mvBest] += nDepth * nDepth;
			if (controller.nDistance == 0) {
				// 搜索根节点时，总是有一个最佳走法(因为全窗口搜索不会超出边界)，将这个走法保存下来
				controller.mvResult = mvBest;
			}
		}
		return vlBest;
	}

	// 迭代加深搜索过程
	public void searchMain() {
		int i, vl;
		long t;

		// 初始化
		t = System.nanoTime();
		for (i = 0; i < 10000; i++) {
			controller.nHistory[i] = 0;
		}
		controller.nDistance = 0;
		
		// 迭代加深过程
		for (i = 1; i <= LIMIT_DEEP; i++) {
			vl = alphaBeta(-MATE_VALUE, MATE_VALUE, i,1);
			// 搜索到杀棋，就终止搜索
			if (vl > WIN_VALUE || vl < -WIN_VALUE) {
				break;
			}
			
			// 超过一秒，就终止搜索
			if (System.nanoTime() - t > 1000000000) {
				break;
			}
		}
		System.out.println("deep is "+i);
		System.out.println("time is "+(System.nanoTime() - t));
	}
	
	/*
	 * 内部类,生成按历史表排序的比较器
	 */
	public class MyHistoryCmp implements Comparator{

		public int compare(Object o1, Object o2) {
			int x = (Integer)o1;
			int y = (Integer)o2;
			if (controller.nHistory[x] > controller.nHistory[y]) {
				return -1;
			}
			if (controller.nHistory[x] < controller.nHistory[y]) {
				return 1;
			}
			return 0;
		}	
	}
	
	/*
	 * 内部类,生成按Mvv/Lva启发排序的比较器
	 */
	public class MyMvvLvaCmp implements Comparator{

		public int compare(Object o1, Object o2) {
			int x = (Integer)o1;
			int y = (Integer)o2;
			if (countMvvLva(x) > countMvvLva(y)) {
				return -1;
			}
			if (countMvvLva(x) < countMvvLva(y)) {
				return 1;
			}
			return 0;
		}	
	}
}

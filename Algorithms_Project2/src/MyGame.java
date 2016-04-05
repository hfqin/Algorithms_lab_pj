import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * GUI部分,读取图片,生成棋盘
 */
public class MyGame extends JFrame {
	public Chess[][] chess = new Chess[10][9];
	public String[][] map = new String[10][9];
	
	MyGame() {
		JLabel background = new JLabel();
		background.setIcon(new ImageIcon("res/background.gif"));
		ControlGame controller = new ControlGame(chess, map);
		controller.playChess();
		background.setLayout(new GridLayout(10, 9));
		for (int i=0;i<10;i++) {
			for (int j=0;j<9;j++) {		
				background.add(chess[i][j]);
			}
		}
		add(background);
		
	}
	public static void main(String[] args) {
		MyGame myGame = new MyGame();
		myGame.setSize(535, 615);
		myGame.setLocation(600, 50);
		myGame.setResizable(false);
		myGame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		myGame.setVisible(true);
	}
	
	
	public static ImageIcon findIconById(String id) {
		if ("00".equals(id)) {
			return null;
		}
		String address = "res/"+id+".gif";
		ImageIcon imageIcon = new ImageIcon(address);
		return imageIcon;
	}
	public static Chess findChessById(String id, int row, int column) {
		ImageIcon picture;		
		picture = findIconById(id);
		Chess chess = new Chess(picture, row, column);
		return chess;
	}
}

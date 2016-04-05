
import javax.swing.ImageIcon;
import javax.swing.JButton;

/*
 * 棋子控件,提供玩家与程序交互的接口
 */
public class Chess extends JButton {	
	private int row;
	private int column;
	private ImageIcon picture;

	public Chess(ImageIcon picture, int row, int column) {
		super(picture);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.row = row;
		this.column = column;		
	}
	public ImageIcon getPicture() {
		return picture;
	}
	public void setPicture(ImageIcon picture) {
		this.picture = picture;
		setIcon(picture);
		repaint();
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
}

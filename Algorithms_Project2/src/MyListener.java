import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * 响应鼠标点击事件,控制玩家走棋
 */
public class MyListener implements ActionListener {
	ControlGame controller;
	public MyListener(ControlGame controller) {
		this.controller = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (controller.isGameOver) return;
		int mv;
		Chess chess = (Chess) e.getSource();
		if (controller.map[chess.getRow()][chess.getColumn()].charAt(0) == 'b' && !controller.isChoosed) {
			return;
		}
		if (!controller.isChoosed) {
			controller.isChoosed = true;
			controller.choosedX = chess.getRow();
			controller.choosedY = chess.getColumn();	
		} else {
			if (controller.map[chess.getRow()][chess.getColumn()].charAt(0) == 'r') {
				controller.choosedX = chess.getRow();
				controller.choosedY = chess.getColumn();
				return;
			}
			mv = controller.choosedX*1000 + controller.choosedY*100+chess.getRow()*10+chess.getColumn();
			controller.moveChess(mv);
		}
		
	}

}

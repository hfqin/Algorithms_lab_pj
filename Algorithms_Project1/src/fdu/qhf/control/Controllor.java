package fdu.qhf.control;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fdu.qhf.mytree.BTree;
import fdu.qhf.mytree.RedBlackTree;

/*
 * Controllor, get the reference of View and Model
 */
public class Controllor {
	RedBlackTree rbTree;
	BTree bTree;
	JTextArea outputPanel;
	JTextField translateInput;
	JTextField textFrom;
	JTextField textTo;
	JTextField englishInput;
	JTextField chineseInput;
	JComboBox importBox;

	boolean isRBTree = false;
	boolean isBTree = false;
	boolean isImported = false;

	ArrayList<String> arrayList1;
	ArrayList<String> arrayList2;

	public Controllor(JTextArea outputPanel, JTextField translateInput,
			JTextField textFrom, JTextField textTo, JTextField englishInput,
			JTextField chineseInput, JComboBox importBox) {
		// TODO Auto-generated constructor stub
		this.outputPanel = outputPanel;
		this.translateInput = translateInput;
		this.textFrom = textFrom;
		this.textTo = textTo;
		this.englishInput = englishInput;
		this.chineseInput = chineseInput;
		this.importBox = importBox;
	}
}

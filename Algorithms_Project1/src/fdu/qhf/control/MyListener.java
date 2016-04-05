package fdu.qhf.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fdu.qhf.mytree.BTree;
import fdu.qhf.mytree.RedBlackTree;

/*
 * class Mylistener, it is a Controllor, it can sync the Model and View
 */
public class MyListener implements ActionListener, ItemListener {
	RedBlackTree rbTree;
	BTree bTree;
	JTextArea outputPanel;
	JTextField translateInput;
	JTextField textFrom;
	JTextField textTo;
	JTextField englishInput;
	JTextField chineseInput;
	JComboBox importBox;
	ArrayList<String> arrayList1;
	ArrayList<String> arrayList2;
	Controllor controllor;
	/*
	 * structure method, get the controllor
	 */
	public MyListener(Controllor controllor) {
		this.controllor = controllor;
		this.rbTree = controllor.rbTree;
		this.bTree = controllor.bTree;
		this.outputPanel = controllor.outputPanel;
		this.translateInput = controllor.translateInput;
		this.textFrom = controllor.textFrom;
		this.textTo = controllor.textTo;
		this.englishInput = controllor.englishInput;
		this.chineseInput = controllor.chineseInput;
		this.importBox = controllor.importBox;
		this.arrayList1 = controllor.arrayList1;
		this.arrayList2 = controllor.arrayList2;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * deal with button even
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			String buttonName = button.getText();
			// if click import
			if ("import".equals(buttonName)) {
				String lib = (String) importBox.getSelectedItem();
				File file = new File(System.getProperty("user.dir") + "/res/"
						+ lib);
				arrayList1 = new ArrayList<>(0);
				arrayList2 = new ArrayList<>(0);
				readFile(file, arrayList1, arrayList2);
				if ("1_initial.txt".equals(lib)) {
					rbTree = new RedBlackTree(arrayList1, arrayList2);
					bTree = new BTree(5, arrayList1, arrayList2);
					controllor.isImported = true;
					printOut();
					outputPanel.setText("You have initialed the tree!");
				}
				return;
			}
			if (!controllor.isImported) {
				outputPanel.setText("Please import a dictionary first!");
				return;
			}
			// if click insert
			if ("insert".equals(buttonName)) {
				String x, y;
				String lib = (String) importBox.getSelectedItem();
				if ("3_insert.txt".equals(lib)) {
					int length = arrayList1.size();
					for (int i = 0; i < length; i++) {
						rbTree.insert(arrayList1.get(i), arrayList2.get(i));
						bTree.insert(arrayList1.get(i), arrayList2.get(i));
					}
					printOut();
					outputPanel
							.setText("You have insert the words in the file!");
					return;
				}
				x = englishInput.getText();
				y = chineseInput.getText();
				if ("".equals(x.trim())) {
					outputPanel.setText("Please input your word!");
					return;
				}
				rbTree.insert(x, y);
				bTree.insert(x, y);
				outputPanel.setText("The insertion of word " + x
						+ " is finished!");
				return;
			}
			
			// if click delete
			if ("delete".equals(buttonName)) {
				String x;
				String lib = (String) importBox.getSelectedItem();
				if ("2_delete.txt".equals(lib)) {
					int length = arrayList1.size();
					for (int i = 0; i < length; i++) {
						rbTree.delete(arrayList1.get(i));
						bTree.delete(arrayList1.get(i));
					}
					length = arrayList2.size();
					for (int i = 0; i < length; i++) {
						rbTree.delete(arrayList2.get(i));
						bTree.delete(arrayList2.get(i));
					}
					printOut();
					outputPanel
							.setText("You have delete the words in the file!");
					return;
				}
				x = englishInput.getText();
				if ("".equals(x.trim())) {
					outputPanel.setText("Please input your word!");
					return;
				}
				rbTree.delete(x);
				bTree.delete(x);
				outputPanel.setText("The deletion of word " + x
						+ " is finished!");
				return;
			}

			if (!controllor.isRBTree && !controllor.isBTree) {
				outputPanel.setText("Please choose a kind of tree first!");
				return;
			}

			// if click translate
			if ("translate".equals(buttonName)) {
				String outText = null;
				String englishString = translateInput.getText();
				if (controllor.isRBTree) {
					outText = rbTree.search(englishString);
				} else {
					outText = bTree.search(englishString);
				}
				if (outText != null) {
					outputPanel.setText(outText);
				} else {
					outputPanel.setText("Sorry, not found!");
				}
				return;
			}
			
			// if click search
			if ("search".equals(buttonName)) {
				String x, y;
				String outText = null;
				x = textFrom.getText();
				y = textTo.getText();
				if (controllor.isRBTree) {
					outText = rbTree.show(x, y);

				} else {
					outText = bTree.show(x, y);
				}
				if (outText != null && !"".equals(outText.trim())) {
					outputPanel.setText(outText);
				} else {
					outputPanel.setText("Sorry, not found!");
				}
				return;
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 * deal with the item change for the JRadioButton
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JRadioButton) {
			JRadioButton checkerButton = (JRadioButton) e.getSource();
			if (checkerButton.getText().equals("Red-Black_Tree")) {
				controllor.isRBTree = !controllor.isRBTree;
			}
			if (checkerButton.getText().equals("B_Tree")) {
				controllor.isBTree = !controllor.isBTree;
			}
		}
	}

	/*
	 * offer a public static method, it can read file name in res
	 */
	public static String[] getFileName() {
		String[] fileName;
		File file = new File(System.getProperty("user.dir") + "/res");

		File[] array = file.listFiles();
		int length = array.length;
		fileName = new String[length];
		for (int i = 0; i < array.length; i++) {
			fileName[i] = "" + array[i].getName();
		}
		return fileName;
	}

	/*
	 * offer a static method, it can read data from file and store in the array list.
	 */
	private static void readFile(File file, ArrayList<String> arrayList1,
			ArrayList<String> arrayList2) {
		String str1 = "";
		String str2 = "";

		InputStreamReader read = null;
		try {
			read = new InputStreamReader(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(read);
		try {
			str1 = reader.readLine();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		while (str1 != null) {
			try {
				str1 = reader.readLine();
				if (str1 == null)
					break;
				arrayList1.add(str1);
				str2 = reader.readLine();
				if (str2 == null)
					break;
				arrayList2.add(str2);
			} catch (IOException ex1) {
				ex1.printStackTrace();
			}
		}
	}

	/*
	 * Print the current tree, store in rbt.txt and bt.txt.
	 */
	private void printOut() {
		String outText1 = rbTree.show();
		String outText2 = bTree.show();
		File file1 = new File(System.getProperty("user.dir") + "/out/rbt.txt");
		File file2 = new File(System.getProperty("user.dir") + "/out/bt.txt");
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.write(outText1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer = new OutputStreamWriter(new FileOutputStream(file2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.write(outText2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

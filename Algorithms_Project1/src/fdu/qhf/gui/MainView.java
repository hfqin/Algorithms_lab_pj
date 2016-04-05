package fdu.qhf.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

import fdu.qhf.control.Controllor;
import fdu.qhf.control.MyListener;

/*
 * This is GUI. It is View part. 
 */
public class MainView extends JFrame {
	
	private JPanel searchPanel;
	private JPanel operatePanel;
	
	private JPanel importView;
	private JPanel libChooser;
	private JPanel libImporter;
	
	private JPanel insertDeleteView;
	private JPanel chineseInputer;
	private JPanel englishInputer;
	private JPanel insertDeleter;
	
	private JPanel searchInputView;
	private JPanel treeChooser;
	private JPanel translater;
	private JPanel searcher;
	
	private JScrollPane outView;
	
	private JLabel searchFrom;
	private JLabel searchTo;
	private JLabel englishLabel;
	private JLabel chineseLabel;
	private JTextArea outputPanel;
	
	private JTextField translateInput;
	private JTextField textFrom;
	private JTextField textTo;
	private JTextField englishInput;
	private JTextField chineseInput;
	
	private JComboBox importBox;
	private ButtonGroup treeChecker;
	private JRadioButton rbTreeButton;
	private JRadioButton bTreeButton;
	private JButton searchButton;
	private JButton translateButton;
	private JButton insertButton;
	private JButton deleteButton;
	private JButton importButton;
		
	private String[] fileName;
	
	private Controllor controllor;
	private MyListener myListener;
	
	public MainView() {
		
		searchPanel = new JPanel();
		operatePanel = new JPanel();
		operatePanel.setBorder(BorderFactory.createTitledBorder("OperatePanel"));
		operatePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		searchPanel.setBorder(BorderFactory.createTitledBorder("SearchPanel"));
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		searchInputView = new JPanel();
		treeChooser = new JPanel();
		translater = new JPanel();
		searcher = new JPanel();	
			
		importView = new JPanel();
		importView.setBorder(BorderFactory.createTitledBorder("Import"));
		importView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		libChooser = new JPanel();
		libImporter = new JPanel();
		
		insertDeleteView = new JPanel();	
		insertDeleteView.setBorder(BorderFactory.createTitledBorder("Insert and Delete"));
		insertDeleteView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		englishInputer = new JPanel();
		chineseInputer = new JPanel();
		insertDeleter = new JPanel();
		
		
		
		rbTreeButton = new JRadioButton("Red-Black_Tree");	
		bTreeButton = new JRadioButton("B_Tree");
		treeChecker = new ButtonGroup();
		treeChecker.add(rbTreeButton);
		treeChecker.add(bTreeButton);
		treeChooser.setLayout(new GridLayout(1,2));
		treeChooser.add(rbTreeButton);
		treeChooser.add(bTreeButton);
		
		searchFrom = new JLabel(" Search from ");
		searchFrom.setFont(new Font("Calibri",Font.BOLD,15));
		searchTo = new JLabel(" to ");
		searchTo.setFont(new Font("Calibri",Font.BOLD,15));
		englishLabel = new JLabel(" English   ");
		englishLabel.setFont(new Font("Calibri",Font.BOLD,15));
		chineseLabel = new JLabel(" Chinese ");
		chineseLabel.setFont(new Font("Calibri",Font.BOLD,15));
		
		translateInput = new JTextField();
		textFrom = new JTextField();
		textFrom.setColumns(8);
		textTo = new JTextField();
		textTo.setColumns(8);
		englishInput = new JTextField();
		englishInput.setColumns(13);
		chineseInput = new JTextField();
		chineseInput.setColumns(13);
		outputPanel = new JTextArea();		
		outputPanel.setFont(new Font("宋体",Font.BOLD,15));
		outView = new JScrollPane(outputPanel);	
		
		translateButton = new JButton("translate");
		searchButton = new JButton("search");		
		importButton = new JButton("import");
		insertButton = new JButton("insert");
		deleteButton = new JButton("delete");
		fileName = MyListener.getFileName();
		importBox = new JComboBox(fileName);
		importBox.setSize(400, 30);
		
		libChooser.setLayout(null);
		libChooser.add(importBox);
		libImporter.add(importButton);
		importView.setLayout(new GridLayout(2,1,3,5));
		importView.add(libChooser);
		importView.add(libImporter);
		
		englishInputer.setLayout(new FlowLayout(FlowLayout.CENTER,30,10));
		englishInputer.add(englishLabel);
		englishInputer.add(englishInput);
		
		chineseInputer.setLayout(new FlowLayout(FlowLayout.CENTER,30,10));
		chineseInputer.add(chineseLabel);
		chineseInputer.add(chineseInput);
		
		insertDeleter.setLayout(new FlowLayout(FlowLayout.CENTER,30,10));
		insertDeleter.add(insertButton);
		insertDeleter.add(deleteButton);
		
		insertDeleteView.setLayout(new GridLayout(3,1,4,4));
		insertDeleteView.add(englishInputer);
		insertDeleteView.add(chineseInputer);
		insertDeleteView.add(insertDeleter);
		
		translater.setLayout(new BorderLayout());
		translater.add(translateInput, BorderLayout.CENTER);
		translater.add(translateButton, BorderLayout.EAST);
		
		searcher.setLayout(new FlowLayout(FlowLayout.LEFT,6,0));
		searcher.add(searchFrom);
		searcher.add(textFrom);
		searcher.add(searchTo);
		searcher.add(textTo);
		searcher.add(searchButton);
		
		searchInputView.setLayout(new GridLayout(3,1,0,5));
		searchInputView.add(treeChooser);
		searchInputView.add(translater);
		searchInputView.add(searcher);
		searchInputView.setBorder(BorderFactory.createTitledBorder("Search Input"));
		searchInputView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		outView.setBorder(BorderFactory.createTitledBorder("Print Out"));
		outView.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		searchPanel.setLayout(new GridLayout(2,1,0,2));
		searchPanel.add(searchInputView);
		searchPanel.add(outView);
			
		
		operatePanel.setLayout(new BorderLayout(5,5));
		operatePanel.add(importView, BorderLayout.NORTH);
		operatePanel.add(insertDeleteView, BorderLayout.CENTER);
		
		this.setLayout(new GridLayout(1,2,10,5));
		this.add(operatePanel);
		this.add(searchPanel);
		
		controllor = new Controllor(outputPanel, translateInput,
				textFrom, textTo, englishInput, chineseInput, importBox);
		myListener = new MyListener(controllor);
		
		translateButton.addActionListener(myListener);	
		searchButton.addActionListener(myListener);				
		importButton.addActionListener(myListener);
		insertButton.addActionListener(myListener);
		deleteButton.addActionListener(myListener);	
		rbTreeButton.addItemListener(myListener); 
		bTreeButton.addItemListener(myListener);
		
	}
	public static void main(String[] args) {
		MainView mainView = new MainView();
		mainView.setTitle("English-Chinese Dictionary");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		mainView.setResizable(false);
		mainView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainView.setLocation(300, 200);
		mainView.setSize(820,330);
		mainView.setVisible(true);

	}
}

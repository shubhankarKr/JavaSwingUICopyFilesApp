import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import java.awt.SystemColor;
import javax.swing.JProgressBar;
public class App extends JFrame {

	private JPanel contentPane;
	private JFileChooser fileChooser = null;
	File sourceFolder=null;
	File destFolder=null;
	private JCheckBox allCheckBox=null;
	private JCheckBox docsCheckBox_3=null;
	private JCheckBox videosCheckBox_2=null;
	private JCheckBox imgCheckBox_1=null;
	JProgressBar progressBar=null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	void removeAllSelected() {
			this.allCheckBox.setSelected(false);;
	}

	/**
	 * Create the frame.
	 */
	public App() {
		ArrayList<String> fileExtentions=new ArrayList<String>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 476);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("ToolBar.background"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("       Source");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(29, 40, 106, 51);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("  Destination");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(48, 147, 87, 44);
		contentPane.add(lblNewLabel_1);
		
		JTextArea textArea = new JTextArea();
		textArea.setBackground(new Color(235, 252, 243));
		textArea.setEditable(false);
		textArea.setBounds(29, 337, 662, 92);
		contentPane.add(textArea);
		textArea.setVisible(false);
		
		JLabel sourceLabel = new JLabel("");
		sourceLabel.setBackground(UIManager.getColor("Tree.dropLineColor"));
		sourceLabel.setBounds(194, 40, 415, 51);
		contentPane.add(sourceLabel);
		sourceLabel.setVisible(false);
		
		JLabel destLabel = new JLabel("");
		destLabel.setBackground(new Color(50, 205, 50));
		destLabel.setBounds(194, 147, 413, 51);
		contentPane.add(destLabel);
		destLabel.setVisible(false);
		
		JButton copyButton = new JButton("Copy");
		copyButton.setBackground(new Color(158, 223, 254));
		copyButton.setEnabled(false);
		
		
		allCheckBox = new JCheckBox("All");
		allCheckBox.setSelected(true);
		allCheckBox.setBounds(58, 248, 100, 21);
		contentPane.add(allCheckBox);
		
		imgCheckBox_1 = new JCheckBox("Images");
		imgCheckBox_1.setBounds(204, 248, 93, 21);
		contentPane.add(imgCheckBox_1);
		
		videosCheckBox_2 = new JCheckBox("Videos");
		videosCheckBox_2.setBounds(369, 248, 93, 21);
		contentPane.add(videosCheckBox_2);
		
		docsCheckBox_3 = new JCheckBox("Documents");
		docsCheckBox_3.setBounds(532, 248, 93, 21);
		contentPane.add(docsCheckBox_3);
		
		progressBar = new JProgressBar();
		progressBar.setBackground(new Color(240, 255, 240));
		progressBar.setBounds(162, 350, 415, 21);
		contentPane.add(progressBar);
		
		progressBar.setValue(0);
		allCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(allCheckBox.isSelected()) {
					imgCheckBox_1.setSelected(false);
					videosCheckBox_2.setSelected(false);
					docsCheckBox_3.setSelected(false);
				}
			}
		});
		
		imgCheckBox_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(imgCheckBox_1.isSelected()) {
					removeAllSelected();
				}
			}
		});
		videosCheckBox_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(videosCheckBox_2.isSelected()) {
					removeAllSelected();
				}
			}
		});
		docsCheckBox_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(docsCheckBox_3.isSelected()) {
					removeAllSelected();
				}
			}
		});
		
		JLabel checkBoxLabel = new JLabel("Please Select Files type to be copied");
		checkBoxLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		checkBoxLabel.setBounds(58, 201, 339, 30);
		contentPane.add(checkBoxLabel);
		
		
		copyButton.addActionListener(new ActionListener() {
			ArrayList<File> fileList=new ArrayList<>();
			public void actionPerformed(ActionEvent e) {
//				String source=sourceFolder.getText();
				try {
					LocalDateTime d1=LocalDateTime.now();
					buildFileExtentions();
					FindListOfFiles(sourceFolder,fileExtentions);
					DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");  
//					System.out.println("Started copying file "+d1.format(format));
//					System.out.println("fileExtentions "+fileExtentions);
//					CopyFile.copyDirectory(sourceFolder,destFolder,fileExtentions);
//					System.out.println(fileList.size()+" value 2");
//					System.out.println("Started copying file "+d1.format(format));
					ArrayList<File> fileListForThread=new ArrayList<>();
					progressBarTask task=new progressBarTask(fileList.size());
					task.start();
					CopyFileWithThread thread=null;
					for (int i = 0; i < fileList.size(); i++) {
						if (i==fileList.size()-1 || fileListForThread.size() == 50) {
							if(i==fileList.size()-1) {
								fileListForThread.add(fileList.get(i));
							}
							thread=new CopyFileWithThread(fileListForThread, destFolder);
							thread.start();
							fileListForThread=new ArrayList<>();
						}
						fileListForThread.add(fileList.get(i));
					}
//					textArea.setVisible(true);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			private void FindListOfFiles(File sourceFolder,ArrayList<String> fileExtentions) {
				if (sourceFolder.isDirectory()) {
					File[] children = sourceFolder.listFiles();
					if(children.length!=0) {
						for (int i = 0; i < children.length; i++) {
							if(children[i].isDirectory()) {
								FindListOfFiles(children[i],fileExtentions);
							}
							else {
								if(checkExtention(children[i].getName(),fileExtentions)) {
									this.fileList.add(children[i]);
								}
							}
						}
					}
				}
				
			}

			private boolean checkExtention(String fileName, ArrayList<String> fileExtentions) {
				Boolean value = false;
				for (String string : fileExtentions) {
					if(fileName.endsWith(string) || fileName.endsWith(string.toUpperCase())) {
						value = true;
						break;
					}
					else {
//						System.out.println("Ignoring file "+fileName);
					}
				}
				return value;
			}

			private void buildFileExtentions() {
				if(allCheckBox.isSelected()) {
					fileExtentions.add("All");
				}else {
					if(imgCheckBox_1.isSelected()) {
						fileExtentions.add("jpg");
					}else if(videosCheckBox_2.isSelected()) {
						fileExtentions.add("mp4");
					}else if(docsCheckBox_3.isSelected()) {
						fileExtentions.add("json");
					}
				}
				
			}
		});
		copyButton.setBounds(245, 283, 204, 44);
		contentPane.add(copyButton);
		
		JButton sourceButton = new JButton("Select Source Folder");
		sourceButton.setBackground(SystemColor.menu);
		sourceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 	fileChooser = new JFileChooser(); 
				    fileChooser.setCurrentDirectory(new java.io.File("."));
				    fileChooser.setDialogTitle("Select Source Folder");
				    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				    fileChooser.setAcceptAllFileFilterUsed(false);
				    if (fileChooser.showOpenDialog(sourceButton) == JFileChooser.APPROVE_OPTION) {
				    	sourceFolder=fileChooser.getSelectedFile();
//						System.out.println("getCurrentDirectory(): " + fileChooser.getSelectedFile());
					} else {
//						System.out.println("No Selection ");
					}
				    if(sourceFolder != null)
				    {
				    	 sourceButton.setEnabled(false);
				    	 sourceButton.setVisible(false);
				    	 sourceLabel.setText("Selected Folder : "+sourceFolder.toPath());
				    	 sourceLabel.setVisible(true);
				    }
			}
		});
		sourceButton.setBounds(194, 41, 415, 51);
		contentPane.add(sourceButton);
		
		JButton destButton = new JButton("Select Destination Folder");
		destButton.setBackground(SystemColor.menu);
		destButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser = new JFileChooser(); 
			    fileChooser.setCurrentDirectory(new java.io.File("."));
			    fileChooser.setDialogTitle("Select Destination Folder");
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    if (fileChooser.showOpenDialog(destButton) == JFileChooser.APPROVE_OPTION) {
			    	destFolder=fileChooser.getSelectedFile();
//					System.out.println("getCurrentDirectory(): " + fileChooser.getSelectedFile());
				} else {
//					System.out.println("No Selection ");
				}
			    destButton.setEnabled(false);
			    if(sourceFolder != null && destFolder!=null) {
			    	copyButton.setEnabled(true);
			    	destButton.setVisible(false);
			    	destLabel.setText("Selected Folder : "+destFolder.toPath());
			    	destLabel.setVisible(true);
			    }
			}
		});
		destButton.setBounds(194, 146, 415, 51);
		contentPane.add(destButton);
		
	}

	private class progressBarTask extends Thread {
		int totalSize;
		public progressBarTask(int size) {
			// TODO Auto-generated constructor stub
			this.totalSize=size;
		}

		@Override
		public void run() {
			int value;
//			System.out.println("progressBarTask thread called "+totalSize);
//			System.out.println("progressBarTask thread CopyFileWithThread.written "+CopyFileWithThread.written);
			try {
				while(CopyFileWithThread.written < totalSize) {
//					System.out.println("called "+totalSize);
//					System.out.println("CopyFileWithThread.written "+CopyFileWithThread.written);
					if(CopyFileWithThread.written >= (totalSize-5)) {
						 value=100;
					}else {
						 value=(CopyFileWithThread.written*100)/totalSize;;
					}
					
					progressBar.setValue(value);
//					System.out.println(" progress bar value "+progressBar.getValue());
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

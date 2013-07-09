package com.isaacjg.SimpleLogger;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
 * SimpleLogger, a simple logging application
 * Copyright (C) 2013  Isaac Grant
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class SimpleLogger {
	private final String TITLE = "Simple Logger - by IsaacJG";

	private JFrame frame;
	private JPanel entryPanel;
	private JScrollPane scrollPane;

	private ArrayList<String> logEntries;
	private JList<String> logList;
	private JTextField logEntry;
	private JButton logButton;

	public SimpleLogger() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void main(String[] args) {
		new SimpleLogger();
	}

	private JPanel makeCoreComponents() {
		JPanel main = new JPanel(new BorderLayout());

		logEntries = new ArrayList<String>();
		logList = new JList<String>();
		logEntry = new JTextField(75);
		logButton = new JButton("Log");

		logList.setFont(new Font("Arial", Font.PLAIN, 12));
		logList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		logList.addListSelectionListener(new LogListSelectionListener());
		logList.setVisibleRowCount(15);

		scrollPane = new JScrollPane(logList);

		logEntry.addKeyListener(new FrameKeyListener());

		logButton.addActionListener(new LogButtonActionListener());
		logButton.setEnabled(true);;

		entryPanel = new JPanel();
		entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.LINE_AXIS));
		entryPanel.add(logEntry);

		entryPanel.add(Box.createHorizontalStrut(5));
		entryPanel.add(new JSeparator(SwingConstants.VERTICAL));
		entryPanel.add(Box.createHorizontalStrut(5));

		entryPanel.add(logButton);

		entryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		main.add(entryPanel, BorderLayout.PAGE_START);
		main.add(scrollPane, BorderLayout.CENTER);

		return main;
	}

	private void createAndShowGUI() {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JComponent contentPane = makeCoreComponents();
		contentPane.setOpaque(true);
		frame.setContentPane(contentPane);

		frame.pack();
		frame.setVisible(true);
	}

	protected void kill() {
		frame.dispose();
	}

	private String generateDateStr() {
		Calendar now = Calendar.getInstance();
		StringBuilder dateStr = new StringBuilder();
		switch (now.get(Calendar.MONTH)) {
			case Calendar.JANUARY: dateStr.append("Jan. ");
				break;
			case Calendar.FEBRUARY: dateStr.append("Feb. ");
				break;
			case Calendar.MARCH: dateStr.append("Mar. ");
				break;
			case Calendar.APRIL: dateStr.append("Apr. ");
				break;
			case Calendar.MAY: dateStr.append("May ");
				break;
			case Calendar.JUNE: dateStr.append("Jun. ");
				break;
			case Calendar.JULY: dateStr.append("Jul. ");
				break;
			case Calendar.AUGUST: dateStr.append("Aug. ");
				break;
			case Calendar.SEPTEMBER: dateStr.append("Sep. ");
				break;
			case Calendar.OCTOBER: dateStr.append("Oct. ");
				break;
			case Calendar.NOVEMBER: dateStr.append("Nov. ");
				break;
			case Calendar.DECEMBER: dateStr.append("Dec. ");
				break;
		}
		dateStr.append(now.get(Calendar.DAY_OF_MONTH) + ", ");
		dateStr.append((now.get(Calendar.YEAR) - 1900) + ", ");
		dateStr.append(now.get(Calendar.HOUR) + ":");
		dateStr.append((now.get(Calendar.MINUTE) > 9) ? now.get(Calendar.MINUTE) : "0" + now.get(Calendar.MINUTE));
		switch (now.get(Calendar.AM_PM)) {
			case Calendar.AM: dateStr.append(" am");
				break;
			case Calendar.PM: dateStr.append(" pm");
				break;
		}
		
		return dateStr.toString();
	}
	
	private String generateLogStr() {
		StringBuilder builder = new StringBuilder();
		String dateStr = generateDateStr();

		builder.append("<html><strong>");
		builder.append(dateStr + ": ");
		builder.append("</strong>");
		builder.append(logEntry.getText());
		builder.append("</html>");

		return builder.toString();
	}

	protected String stripHTML(String html) {
		String rawEntry = html.substring(html.indexOf("</strong>") + 9);
		rawEntry = rawEntry.replace("</html>", "");
		return rawEntry;
	}

	class LogListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				if (logList.getSelectedIndex() > -1) {
					logEntry.setText(stripHTML(logEntries.get(logList.getSelectedIndex())));
				}
			}
		}
	}
	class LogButtonActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			logEntries.add(generateLogStr());
			logList.setListData(logEntries.toArray(new String[] {}));
			logEntry.setText("");
		}
	}
	class FrameKeyListener implements KeyListener {
		Key ctrl = new Key();
		Key w = new Key();
		Key s = new Key();
		Key enter = new Key();

		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_CONTROL) {
				ctrl.setPressed(true);
			} else if (keyCode == KeyEvent.VK_W) {
				w.setPressed(true);
			} else if (keyCode == KeyEvent.VK_S) {
				s.setPressed(true);
			} else if (keyCode == KeyEvent.VK_ENTER) {
				enter.setPressed(true);
			}

			if (ctrl.isPressed()) {
				if (s.isPressed()) {
					try {
						FileWriter fw = new FileWriter(new File(new File("."), "log.html"));
						for (String entry : logEntries) {
							fw.write(entry + "<br />\r\n");
						}
						fw.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} else if (enter.isPressed()) {
				logEntries.add(generateLogStr());
				logList.setListData(logEntries.toArray(new String[] {}));
				logEntry.setText("");
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_CONTROL) {
				ctrl.setPressed(false);
			} else if (keyCode == KeyEvent.VK_W) {
				w.setPressed(false);
			} else if (keyCode == KeyEvent.VK_S) {
				s.setPressed(false);
			} else if (keyCode == KeyEvent.VK_ENTER) {
				enter.setPressed(false);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {}

		class Key {
			private boolean pressed = false;

			public void setPressed(boolean pressed) {
				this.pressed = pressed;
			}

			public boolean isPressed() {
				return pressed;
			}
		}
	}
}
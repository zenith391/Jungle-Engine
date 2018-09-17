package org.jungle.game;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameOptionsPrompt extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private GameOptions opt;

	/**
	 * Create the dialog.
	 */
	public GameOptionsPrompt() {
		setTitle("Jungle Game");
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblGame = new JLabel("Game");
		lblGame.setBounds(185, 11, 46, 14);
		contentPanel.add(lblGame);
		
		JLabel lblAntialising = new JLabel("Antialising:");
		lblAntialising.setBounds(10, 104, 66, 14);
		contentPanel.add(lblAntialising);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Yes", "No"}));
		comboBox.setSelectedIndex(1);
		comboBox.setBounds(86, 100, 53, 22);
		contentPanel.add(comboBox);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Windowed");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setEnabled(false);
		chckbxNewCheckBox.setBounds(316, 100, 97, 23);
		contentPanel.add(chckbxNewCheckBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						opt = new GameOptions();
						if (comboBox.getSelectedItem().equals("Yes")) {
							opt.antialiasing = true;
						}
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}
}

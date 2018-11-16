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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.SystemColor;

public class GameOptionsPrompt extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private GameOptions opt;
	private JCheckBox chckbxNewCheckBox_1;
	private JCheckBox chckbxWireframeRender;
	private JLabel lblAdvanced;

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
		lblAntialising.setBounds(10, 36, 66, 14);
		contentPanel.add(lblAntialising);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"x4", "x2", "No"}));
		comboBox.setSelectedIndex(2);
		comboBox.setBounds(86, 32, 53, 22);
		contentPanel.add(comboBox);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Windowed");
		chckbxNewCheckBox.setSelected(true);
		chckbxNewCheckBox.setEnabled(false);
		chckbxNewCheckBox.setBounds(320, 36, 97, 23);
		contentPanel.add(chckbxNewCheckBox);
		
		chckbxNewCheckBox_1 = new JCheckBox("Frustum Culling");
		chckbxNewCheckBox_1.setSelected(true);
		chckbxNewCheckBox_1.setBounds(153, 82, 122, 23);
		contentPanel.add(chckbxNewCheckBox_1);
		
		chckbxWireframeRender = new JCheckBox("Wireframe Render");
		chckbxWireframeRender.setBounds(153, 118, 122, 23);
		contentPanel.add(chckbxWireframeRender);
		
		lblAdvanced = new JLabel("Advanced");
		lblAdvanced.setBounds(178, 61, 53, 14);
		contentPanel.add(lblAdvanced);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(SystemColor.info);
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

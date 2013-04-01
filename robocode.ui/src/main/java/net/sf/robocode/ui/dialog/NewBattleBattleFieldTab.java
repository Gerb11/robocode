/*******************************************************************************
 * Copyright (c) 2001-2013 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 *******************************************************************************/
package net.sf.robocode.ui.dialog;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (original)
 */
@SuppressWarnings("serial")
public class NewBattleBattleFieldTab extends JPanel {

	private final static int MIN_BATTLEFIELD_SIZE = 400;
	private final static int MAX_BATTLEFIELD_SIZE = 5000;
	private final static int BATTLEFIELD_STEP_SIZE = 100;

	private final EventHandler eventHandler = new EventHandler();

	private SizeButton[] predefinedSizeButtons = {
		new SizeButton(400, 400),
		new SizeButton(600, 400),
		new SizeButton(600, 600),
		new SizeButton(800, 600),
		new SizeButton(800, 800),
		new SizeButton(1000, 800),
		new SizeButton(1000, 1000),
		new SizeButton(1200, 1200),
		new SizeButton(2000, 2000),
		new SizeButton(5000, 5000)
	};

	private JSlider battlefieldWidthSlider;
	private JSlider battlefieldHeightSlider;
	private JLabel battlefieldSizeLabel;

	public NewBattleBattleFieldTab() {
		super();

		add(createBattlefieldSizePanel());
	}

	public int getBattleFieldWidth() {
		return battlefieldWidthSlider.getValue();
	}

	public void setBattleFieldWidth(int width) {
		battlefieldWidthSlider.setValue(width);
		updateBattlefieldSizeLabel();
	}

	public int getBattleFieldHeight() {
		return battlefieldHeightSlider.getValue();
	}

	public void setBattleFieldHeight(int height) {
		battlefieldHeightSlider.setValue(height);
		updateBattlefieldSizeLabel();
	}

	private JPanel createBattlefieldSizePanel() {
		JPanel panel = new JPanel();
		
		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Battlefield Size"),
				BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBorder(border);

		panel.setLayout(new BorderLayout());

		JPanel sliderPanel = createBattlefieldSlidersPanel();
		panel.add(sliderPanel, BorderLayout.CENTER);

		JPanel buttonsPanel = createPredefinedSizesPanel();
		panel.add(buttonsPanel, BorderLayout.EAST);

		return panel;
	}
	
	private JPanel createBattlefieldSlidersPanel() {
		battlefieldWidthSlider = createBattlefieldSizeSlider();
		battlefieldWidthSlider.setOrientation(SwingConstants.HORIZONTAL);

		battlefieldHeightSlider = createBattlefieldSizeSlider();
		battlefieldHeightSlider.setOrientation(SwingConstants.VERTICAL);
		battlefieldHeightSlider.setInverted(true);

		battlefieldSizeLabel = new BattlefieldSizeLabel();
		battlefieldSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		battlefieldSizeLabel.setMinimumSize(new Dimension(250, 250));

		JPanel panel = new JPanel();

		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);

		GroupLayout.SequentialGroup leftToRight = layout.createSequentialGroup();

		GroupLayout.ParallelGroup left = layout.createParallelGroup();
		left.addComponent(battlefieldSizeLabel);
		left.addComponent(battlefieldWidthSlider);
		leftToRight.addGroup(left);
		
		GroupLayout.ParallelGroup right = layout.createParallelGroup();
		right.addComponent(battlefieldHeightSlider);
		leftToRight.addGroup(right);
		
		GroupLayout.SequentialGroup topToBottom = layout.createSequentialGroup();

		GroupLayout.ParallelGroup top = layout.createParallelGroup();
		top.addComponent(battlefieldSizeLabel);
		top.addComponent(battlefieldHeightSlider);
		topToBottom.addGroup(top);

		GroupLayout.ParallelGroup bottom = layout.createParallelGroup();
		bottom.addComponent(battlefieldWidthSlider);
		topToBottom.addGroup(bottom);

		layout.setHorizontalGroup(leftToRight);
	    layout.setVerticalGroup(topToBottom);

		return panel;
	}

	private JPanel createPredefinedSizesPanel() {
		JPanel panel = new JPanel();

		Border border = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(0, 20, 0, 0),
				BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Predefined Sizes"));
		panel.setBorder(border);

		panel.setLayout(new GridLayout(predefinedSizeButtons.length, 1));
		
		for (SizeButton button : predefinedSizeButtons) {
			panel.add(button);
		}
		return panel;
	}

	private JSlider createBattlefieldSizeSlider() {
		JSlider slider = new JSlider();
		slider.setMinimum(MIN_BATTLEFIELD_SIZE);
		slider.setMaximum(MAX_BATTLEFIELD_SIZE);
		slider.setMajorTickSpacing(BATTLEFIELD_STEP_SIZE);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.addChangeListener(eventHandler);
		return slider;
	}

	private void updateBattlefieldSizeLabel() {
		int w = battlefieldWidthSlider.getValue();
		int h = battlefieldHeightSlider.getValue();
		battlefieldSizeLabel.setText(w + " x " + h);
	}

	private class SizeButton extends JButton {
		final int width;
		final int height;

		public SizeButton(int width, int height) {
			super(width + "x" + height);
			this.width = width;
			this.height = height;
			addActionListener(eventHandler);
		}
	}

	private class EventHandler implements ActionListener, ChangeListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() instanceof SizeButton) {
				SizeButton button = (SizeButton) e.getSource();
				battlefieldWidthSlider.setValue(button.width);
				battlefieldHeightSlider.setValue(button.height);
				updateBattlefieldSizeLabel();
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if ((e.getSource() == battlefieldWidthSlider) || (e.getSource() == battlefieldHeightSlider)) {
				updateBattlefieldSizeLabel();
			}
		}
	}
	
	private class BattlefieldSizeLabel extends JLabel {
		@Override
		public void paintComponent(Graphics g) {
			g.setColor(SystemColor.activeCaption);
			
			int width = getWidth() * battlefieldWidthSlider.getValue() / MAX_BATTLEFIELD_SIZE;
			int height = getHeight() * battlefieldHeightSlider.getValue() / MAX_BATTLEFIELD_SIZE;

			g.fillRect(0, 0, width, height);
			super.paintComponent(g);
		}
	}
}

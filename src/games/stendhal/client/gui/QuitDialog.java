package games.stendhal.client.gui;


import games.stendhal.client.StendhalClient;
import games.stendhal.client.gui.styled.StyledButtonUI;
import games.stendhal.client.gui.wt.InternalManagedDialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.ButtonUI;

public class QuitDialog {
	Component quitDialog;
	
	Component getQuitDialog() {
		return quitDialog;
	}
	public QuitDialog() {
		quitDialog = buildQuitDialog();
		quitDialog.setVisible(false);
		quitDialog.addHierarchyBoundsListener(new ParentResizeListener());
	}
	/**
	 * Build the in-window quit dialog [panel].
	 * @return the quitdialog
	 * 
	 * 
	 */
	protected Component buildQuitDialog() {
		InternalManagedDialog imd;
		JPanel panel;
		JButton b;

		panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(150, 75));

		ButtonUI ui = new StyledButtonUI();
		b = new JButton();
		b.setUI(ui);
		b.setText("Yes");
		b.setBounds(30, 25, 40, 25);
		b.addActionListener(new QuitConfirmCB());

		panel.add(b);

		b = new JButton();
		b.setUI(ui);
		b.setText("No");
		b.setBounds(80, 25, 40, 25);
		b.addActionListener(new QuitCancelCB());

		panel.add(b);

		imd = new InternalManagedDialog("quit", "Quit");
		imd.setContent(panel);
		imd.setMinimizable(false);
		imd.setMovable(false);

		return imd.getDialog();
	}
	
	protected class QuitCancelCB implements ActionListener {
		public void actionPerformed(final ActionEvent ev) {
			quitDialog.setVisible(false);
		}
	}

	protected class QuitConfirmCB implements ActionListener {
		public void actionPerformed(final ActionEvent ev) {
		
				j2DClient.get().shutdown();
		
		}
	}
	
	/**
	 * Request quit confirmation from the user. This stops all player actions
	 * and shows a dialog in which the player can confirm that they really wants
	 * to quit the program. If so it flags the client for termination.
	 */
	
	public void requestQuit() {
		/*
		 * Stop the player
		 */
		StendhalClient.get().stop();

		centerDialog();

		quitDialog.validate();
		quitDialog.setVisible(true);
	}
	
	private void centerDialog() {
		final Dimension psize = quitDialog.getPreferredSize();
		quitDialog.setBounds((j2DClient.get().getWidth() - psize.width) / 2,
				(j2DClient.get().getHeight() - psize.height) / 2, psize.width, psize.height);
	}
	
	/**
	 * For keeping the dialog centered on game screen resizes.
	 */
	private class ParentResizeListener implements HierarchyBoundsListener {
		public void ancestorMoved(HierarchyEvent e) {
		}

		public void ancestorResized(HierarchyEvent e) {
			if (quitDialog.isVisible()) {
				if (e.getChanged().equals(quitDialog.getParent())) {
					centerDialog();
				}
			}
		}
	}
}

package timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.MainFrame;
import tools.StaticTool;

public class TimerListener implements ActionListener {
	MainFrame mainFrame;

	public TimerListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		StaticTool.timecount++;
		if (StaticTool.timecount > 999) {
			StaticTool.timecount = 999;

		}
		mainFrame.getFaceJPanel().setTime(StaticTool.timecount);

	}

}

import javax.swing.JFrame;

public class ReversiFrame extends JFrame{

	public ReversiFrame() {
		// if user push the X button, close window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//change frame size(height × width)
		setSize(Reverse2.height, Reverse2.width);

		//set propaty
		setContentPane(new Reverse2());

		//display this window
		setVisible(true);
	}

	public static void main(String[] args) {
		new ReversiFrame();
	}

}

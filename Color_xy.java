import java.awt.Color;

public class Color_xy extends Thread{
	int x, y; //x and y coordinate of a piece
	Color color; //color of a piece

	//constructor of class of a piece
	public Color_xy(int x, int y, Color color) {
		this.x=x;
		this.y=y;
		this.color=color;

		start();
	}

	public void run() {
		while(true) {
			try {
				Thread.sleep(50);
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Reverse2 extends JPanel implements MouseListener{
	static final int width=550; //パネルの横幅
	static final int height=500; //パネルの縦幅
	private static final int left_end_line_x=50; //オセロボードの左端のx座標
	private static final int right_end_line_x=450;//オセロボードの右端のx座標
	private static final int top_of_line_y=100;//オセロボードの上端のy座標
	private static final int bottom_of_line_y=500;//オセロボードの下端のy座標
	private static final int number_of_mass=8; //オセロボードの縦のマスの数
	private static final int line_to_line_space=(right_end_line_x - left_end_line_x)/number_of_mass; //線と線との幅
	private static final int diameter_of_piece=(right_end_line_x - left_end_line_x - 1)/number_of_mass; //駒の直径
	private static final int up = 0;
	private static final int dia_up_right = 1;
	private static final int right = 2;
	private static final int dia_down_right = 3;
	private static final int down = 4;
	private static final int dia_down_left = 5;
	private static final int left = 6;
	private static final int dia_up_left = 7;
	private Color_xy[][] mass = new Color_xy[number_of_mass][number_of_mass]; //駒の並びを記憶する配列
	private Color player_color = Color.black; //初手のプレイヤーの駒の色

	public Reverse2() {
		//ウィンドウサイズを設定
		setPreferredSize(new Dimension(height, width));

		//盤上の初期化
		init_map(this.mass);

		//マウスリスナーの追加
		addMouseListener(this);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						repaint();
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}

		});
		thread.start();
	}

	//オセロボードと駒を描く
	public void paint(Graphics g) {
		super.paint(g);

		//オセロボードを描く
		g.setColor(Color.green);
		g.fillRect(0, 0, height, width);

		//縦方向の線を描く
		g.setColor(Color.black);
		for(int i=top_of_line_y; i<=bottom_of_line_y; i+=line_to_line_space) {
			g.drawLine(left_end_line_x, i, right_end_line_x, i);
		}

		//横方向の線を描く
		for(int i=left_end_line_x; i<=right_end_line_x; i+=line_to_line_space) {
			g.drawLine(i, top_of_line_y, i, bottom_of_line_y);
		}

		//駒を描く
		for(int i=0; i<this.mass.length; i++) {
			for(int j=0; j<this.mass.length; j++) {
				if(this.mass[i][j]!=null) {
					g.setColor(this.mass[i][j].color);
					g.fillOval((i+1)*line_to_line_space, (j+2)*line_to_line_space,
							diameter_of_piece, diameter_of_piece);
				}
			}
		}
	}

	//スタート次の駒の並び
	public void init_map(Color_xy[][] mat) {
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat.length; j++) {
				mat[i][j]=null;
			}
		}
		//mat[横方向][縦方向]
		mat[number_of_mass / 2 - 1][number_of_mass / 2 - 1]
				= new Color_xy(number_of_mass / 2 - 1, number_of_mass / 2 - 1, Color.black);
		mat[number_of_mass / 2][number_of_mass / 2]
				= new Color_xy(number_of_mass / 2, number_of_mass / 2, Color.black);
		mat[number_of_mass / 2 - 1][number_of_mass / 2]
				= new Color_xy(number_of_mass / 2 - 1, number_of_mass / 2, Color.white);
		mat[number_of_mass / 2][number_of_mass / 2 - 1]
				= new Color_xy(number_of_mass / 2, number_of_mass / 2 - 1, Color.white);
	}

	//自身と自身と同じ色の駒に挟まれた駒の色を自身と同じ色に変える（八方向）
	public void change_color_sandwiched_piece(Color_xy clicked_position) {
		Color_xy target;
		for(int direction = 0; direction < 4; direction++) {
			target = search_different_color_piece(clicked_position, direction);
			change_color_piece(clicked_position, target);
			target = search_different_color_piece(clicked_position, direction + 4);
			change_color_piece(clicked_position, target);
		}
	}

	//その方向に自分と同じ色の駒が存在するか判定
	public Color_xy search_different_color_piece(Color_xy clicked_position, int direction) {
		switch(direction) {
		case up: //上
			for(int i = clicked_position.y - 1; i >= 0; i--) {
				if(this.mass[clicked_position.x][i] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[clicked_position.x][i].color ==clicked_position.color)
					return this.mass[clicked_position.x][i]; //自身と同じ色の駒が存在する場合
			}
		case dia_up_right: //右斜め上
			for(int i = clicked_position.x + 1; clicked_position.y + clicked_position.x - i < number_of_mass; i++) {
				if(i >= number_of_mass || clicked_position.y + clicked_position.x - i < 0) return null;
				if(this.mass[i][clicked_position.y + clicked_position.x - i] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[i][clicked_position.y + clicked_position.x - i].color ==clicked_position.color)
					return this.mass[i][clicked_position.y + clicked_position.x - i]; //自身と同じ色の駒が存在する場合
			}
		case right: //右
			for(int i = clicked_position.x + 1; i < number_of_mass; i++) {
				if(this.mass[i][clicked_position.y] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[i][clicked_position.y].color ==clicked_position.color)
					return this.mass[i][clicked_position.y]; //自身と同じ色の駒が存在する場合
			}
		case dia_down_right: //右斜め下
			for(int i = clicked_position.x + 1; clicked_position.y - clicked_position.x + i < number_of_mass; i++) {
				if(i >= number_of_mass) return null;
				if(this.mass[i][clicked_position.y - clicked_position.x + i] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[i][clicked_position.y - clicked_position.x + i].color ==clicked_position.color)
					return this.mass[i][clicked_position.y - clicked_position.x + i]; //自身と同じ色の駒が存在する場合
			}
		case down: //下
			for(int i = clicked_position.y + 1; i < number_of_mass; i++) {
				if(this.mass[clicked_position.x][i] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[clicked_position.x][i].color ==clicked_position.color)
					return this.mass[clicked_position.x][i]; //自身と同じ色の駒が存在する場合
			}
		case dia_down_left: //左斜め下
			for(int i = clicked_position.x - 1; clicked_position.y + clicked_position.x - i >= 0; i--) {
				if(i < 0 || clicked_position.y + clicked_position.x - i >= number_of_mass) return null;
				if(this.mass[i][clicked_position.y + clicked_position.x - i] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[i][clicked_position.y + clicked_position.x - i].color ==clicked_position.color)
					return this.mass[i][clicked_position.y + clicked_position.x - i]; //自身と同じ色の駒が存在する場合
			}
		case left: //左
			for(int i = clicked_position.x - 1; i >= 0; i--) {
				if(this.mass[i][clicked_position.y] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[i][clicked_position.y].color ==clicked_position.color)
					return this.mass[i][clicked_position.y]; //自身と同じ色の駒が存在する場合
			}
		case dia_up_left: //左斜め上
			for(int i = clicked_position.x - 1; clicked_position.y - clicked_position.x + i >= 0; i--) {
				if(i < 0) return null;
				if(this.mass[i][clicked_position.y - clicked_position.x + i] == null) return null; //自分と同じ色の駒が存在しない場合
				else if(this.mass[i][clicked_position.y - clicked_position.x + i].color ==clicked_position.color)
					return this.mass[i][clicked_position.y - clicked_position.x + i]; //自身と同じ色の駒が存在する場合
			}
		default : return null;
		}
	}

	//自身と同じ色の駒との間にある駒の色を変える
	public void change_color_piece(Color_xy clicked_position, Color_xy target) {
		//自身と同じ色の駒が存在しない場合
		if(target == null) return;

		//自身と同じ色の駒が存在する場合
		while(clicked_position.x != target.x || clicked_position.y != target.y) {
			this.mass[clicked_position.x + (target.x - clicked_position.x)][clicked_position.y + (target.y - clicked_position.y)]
				= new Color_xy(clicked_position.x + (target.x - clicked_position.x),
						clicked_position.y + (target.y - clicked_position.y), clicked_position.color);

			if(clicked_position.x > target.x) target.x++;
			else if(clicked_position.x < target.x) target.x--;

			if(clicked_position.y > target.y) target.y++;
			else if(clicked_position.y < target.y) target.y--;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		//クリックした場所の座標を取得
		int x=e.getX(); //x座標
		int y=e.getY(); //y座標

		//ボードの外をクリックした場合
		if(x<left_end_line_x) return;
		if(x>right_end_line_x) return;
		if(y<top_of_line_y) return;
		if(y>bottom_of_line_y) return;

		//クリックした場所に駒が存在している場合
		if(this.mass[x/line_to_line_space-1][y/line_to_line_space-2]!=null) return;

		//クリックした場所に駒が存在していないなら、新しい駒をその場所に設置する
		Color_xy new_element = new Color_xy(x/line_to_line_space - 1, y/line_to_line_space - 2, this.player_color);
		this.mass[x/line_to_line_space-1][y/line_to_line_space-2] = new_element;
		change_color_sandwiched_piece(new_element);

		//プレイヤーの交代
		if(this.player_color==Color.black) this.player_color=Color.white;
		else if(this.player_color==Color.white) this.player_color=Color.black;

	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}
}


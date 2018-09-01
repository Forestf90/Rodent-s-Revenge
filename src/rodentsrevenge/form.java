package rodentsrevenge;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class form extends JFrame{

	plansza mapa;
	public form() {
		super("Rodent's Revenge");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(384 ,438);
		this.setLayout(new BorderLayout());
		
		
		mapa = new plansza();
		this.add(mapa);
		 mapa.setFocusable(true);
		 mapa.requestFocusInWindow();
		// pack();
		 ImageIcon img = new ImageIcon("./images/mouse.png");
		 this.setIconImage(img.getImage());
		 this.setVisible(true);


	}
}

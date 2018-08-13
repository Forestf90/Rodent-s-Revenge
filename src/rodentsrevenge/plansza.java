package rodentsrevenge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class plansza extends JPanel implements KeyListener{

	BufferedImage[] zdj_pola ;
	public int[][] tablica = new int[23][23];
	mysz gracz;
	ArrayList<kot> kocury;
	List<Integer> fale;
	public int aktualna_fala;
	
	public plansza() {
		//this.setSize(384 ,408);
		zdj_pola = new BufferedImage[9];
		gracz = new mysz();
		kocury = new ArrayList<kot>();
		fale = new ArrayList<>();
		this.addKeyListener(this);
		wczytaj_zdjecia();
		wczytaj_level();
	}
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.drawImage(zdj_pola[0] ,0 ,0, null);
		g.drawImage(zdj_pola[1] ,60 ,0, null);
		g.drawImage(zdj_pola[2] ,0 ,90, null);
		
		for(int i=0 ; i<tablica.length ;i++) {
			for(int j=0 ; j<tablica[i].length;j++) {
			g.drawImage(zdj_pola[tablica[i][j]], i*16, j*16, null);
			}
		}
		
		g.drawImage(zdj_pola[5], gracz.pozX*16 , gracz.pozY*16, null);
		
		//for(kot k: kocury) {
		//	g.drawImage(zdj_pola[6], k.pozX*16, k.pozY*16 ,null);
		//}
	}
	
	
	
	public void wczytaj_zdjecia() {
		File voidd = new File("./images/void.png");
		File block = new File("./images/block.png");
		File wall = new File("./images/wall.png");
		
		File mysza = new File("./images/mouse.png");
		File kot = new File("./images/cat.png");
		try {
		zdj_pola[0]  = ImageIO.read(voidd);
		zdj_pola[1]  = ImageIO.read(block);
		zdj_pola[2]  = ImageIO.read(wall);
		zdj_pola[5] = ImageIO.read(mysza);
		zdj_pola[6] = ImageIO.read(kot);
		
		}
		catch(IOException e)
		{
			System.err.println("Blad odczytu obrazka");
			e.printStackTrace();
			
		}
	}
		
		public void wczytaj_level(){
			String poziom="1";
			try {
	            File f = new File("levels/level"+poziom+".txt");
	            
				Scanner sc = new Scanner(f);

	            int i=0;
	            while(sc.hasNextLine()){
	                String line = sc.nextLine();
	                String[] dane = line.split(" ");
	                
	                if(!sc.hasNextLine()) {
	    	            for(String j: dane) {
	    	            	int temp =Integer.valueOf(j);
	                    	fale.add(temp);	
	                    }
	    	            aktualna_fala=0;
	                	break;
	                }
	                
	                
	                 //= details[0];
	                for(int j=0 ; j<dane.length ;j++) {
	                	tablica[i][j]= Integer.valueOf(dane[j]);	
	                }
	                		i++;
	                		
	            }



	        } catch (FileNotFoundException e) {         
	            e.printStackTrace();
	        }
		}



		@Override
		public void keyPressed(KeyEvent evt) {
			int c=evt.getKeyCode();
			int x= gracz.pozX;
			int y = gracz.pozY;
			switch(c){
				case KeyEvent.VK_RIGHT:;
					if(tablica[x+1][y]==1)move_block(x , y,1 , 0);
					else if(tablica[x+1][y]!=0);
					else gracz.pozX++;
					break;
				case KeyEvent.VK_UP:
					if(tablica[x][y-1]==1)move_block(x , y,0 , -1);
					else if(tablica[x][y-1]!=0);
					else gracz.pozY--;
					
					break;
				case KeyEvent.VK_LEFT:
					if(tablica[x-1][y]==1)move_block(x , y,-1 , 0);
					else if(tablica[x-1][y]!=0);
					else gracz.pozX--;
						
					
					break;
				case KeyEvent.VK_DOWN:
					if(tablica[x][y+1]==1)move_block(x , y,0 , 1);
					else if(tablica[x][y+1]!=0); 
					else gracz.pozY++;
					
					break;
				case KeyEvent.VK_SPACE:
					spawn();
					break;
			
		}
			//this.repaint();
			repaint();
	}



		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	
		
		
		public void move_block( int x , int y ,int zm_x , int zm_y) {
			//tablica[x+2*zm_x][y+2*zm_y] =
			int tempx=x;
			int tempy=y;
			while (true) {
				tempx+=zm_x;
				tempy+=zm_y;
				if(tablica[tempx][tempy]==0) break;
				else if(tablica[tempx][tempy]==1) continue;
				else return;
			}
			
		tablica[x+zm_x][y+zm_y]=0;
		tablica[tempx][tempy]=1;
		gracz.pozX+=zm_x;
		gracz.pozY+=zm_y;
		}
		
		
		
		public void spawn() {
			Random rand = new Random();
			for(int i=0; i<fale.get(aktualna_fala); i++) {
				int tempX= rand.nextInt(21)+1;
				int tempY =rand.nextInt(21)+1;
				while(!(tempX>17 || tempX<5))tempX= rand.nextInt(21)+1;
				while(!(tempY>17 || tempY<5))tempY= rand.nextInt(21)+1;
				if(Math.abs(tempX - gracz.pozX)+Math.abs(tempY-gracz.pozY)>8 &&tablica[tempX][tempY]==0) {
					kot temp= new kot(tempX , tempY);
					tablica[tempX][tempY]=6;
					kocury.add(temp);
				}
				else i--;
				
			}
			
			aktualna_fala++;
			if(aktualna_fala==fale.size())nastepny_level();
			
			repaint();
		}
		
		public void nastepny_level() {
			
			JOptionPane.showMessageDialog(null, "konic fal", "tak", JOptionPane.INFORMATION_MESSAGE);
		}
}

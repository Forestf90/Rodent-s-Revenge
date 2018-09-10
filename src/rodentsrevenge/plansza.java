package rodentsrevenge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.Timer;



public class plansza extends JPanel implements KeyListener, ActionListener{

	BufferedImage[] zdj_pola ;
	BufferedImage live;
	public int[][] tablica = new int[23][23];
	mysz gracz;
	ArrayList<kot> kocury;
	List<Integer> fale;
	public int aktualna_fala;
	Timer Truch_kota;
	boolean koniec_fali;
	int punkty;
	int poziom;
	
	public plansza() {
		//this.setSize(384 ,408);
		poziom=1;
		zdj_pola = new BufferedImage[12];
		gracz = new mysz();
		kocury = new ArrayList<kot>();
		fale = new ArrayList<>();
		this.addKeyListener(this);
		wczytaj_zdjecia();
		wczytaj_level();
		punkty=0;
		Truch_kota=new Timer(1000, this);
		Truch_kota.start();
		spawn();
	}
	
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//g.drawImage(zdj_pola[0] ,0 ,0, null);
		//g.drawImage(zdj_pola[1] ,60 ,0, null);
		//g.drawImage(zdj_pola[2] ,0 ,90, null);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 368, 368, 32);
		g.setColor(Color.BLACK);
		g.drawString("Punkty: "+punkty,5, 388);
		g.drawString("Poziom "+poziom, 156, 388);
		for(int i=0 ; i<gracz.zycia; i++) {
			g.drawImage(live, 304+i*16, 376, null);
		}
		
		for(int i=0 ; i<tablica.length ;i++) {
			for(int j=0 ; j<tablica[i].length;j++) {
			g.drawImage(zdj_pola[tablica[i][j]], i*16, j*16, null);
			}
		}
		
		if(!gracz.zablokowany)g.drawImage(zdj_pola[5], gracz.pozX*16 , gracz.pozY*16, null);
		else g.drawImage(zdj_pola[10], gracz.pozX*16 , gracz.pozY*16, null);
		
		//for(kot k: kocury) {
		//	g.drawImage(zdj_pola[6], k.pozX*16, k.pozY*16 ,null);
		//}
	}
	
	
	
	public void wczytaj_zdjecia() {
		File zlive = new File("./images/lives.png");
		File voidd = new File("./images/void.png");
		File block = new File("./images/block.png");
		File wall = new File("./images/wall.png");
		
		File mysza = new File("./images/mouse.png");
		File kot = new File("./images/cat.png");
		File kot_spi = new File("./images/cat_awaiting.png");
		File ser = new File("./images/cheese.png");
		File dziura = new File("./images/hole.png");
		File myszdziura = new File("./images/mousehole.png");
		File plapka = new File("./images/mousetrap.png");
		try {
			live = ImageIO.read(zlive);
			zdj_pola[0] = ImageIO.read(voidd);
			zdj_pola[1] = ImageIO.read(block);
			zdj_pola[2] = ImageIO.read(wall);
			zdj_pola[5] = ImageIO.read(mysza);
			zdj_pola[6] = ImageIO.read(kot);
			zdj_pola[7] = ImageIO.read(kot_spi);
			zdj_pola[8] = ImageIO.read(ser);
			zdj_pola[9] = ImageIO.read(dziura);
			zdj_pola[10] = ImageIO.read(myszdziura);
			zdj_pola[11] = ImageIO.read(plapka);
		
		}
		catch(IOException e)
		{
			System.err.println("Blad odczytu obrazka");
			e.printStackTrace();
			
		}
	}
		
		public void wczytaj_level(){
			String Stringpoziom=Integer.toString(poziom);
			try {
	            File f = new File("levels/level"+Stringpoziom+".txt");
	            
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
			if(!gracz.zablokowany) {
				int c=evt.getKeyCode();
				int x= gracz.pozX;
				int y = gracz.pozY;
				switch(c){
					case KeyEvent.VK_RIGHT:;
						if(tablica[x+1][y]==1)move_block(x , y,1 , 0);
						else if(tablica[x+1][y]==8) {
							gracz.pozX++;
							tablica[x+1][y]=0;
							punkty+=100;
						}
						else if(tablica[x+1][y]==9) {
							gracz.pozX++;
							gracz.zablokowany=true;
							gracz.licznik=10;
							tablica[x+1][y]=0;
						}
						else if(tablica[x+1][y]==11) {
							gracz.zycia--;
							gracz.zablokowany=false;
							gracz.licznik=0;
							if(gracz.zycia==0) przegrana();
							else spawn_myszy();
							tablica[x+1][y]=0;
						}
						else if(tablica[x+1][y]==0)gracz.pozX++;
						
						break;
					case KeyEvent.VK_UP:
						if(tablica[x][y-1]==1)move_block(x , y,0 , -1);
						else if(tablica[x][y-1]==8) {
							gracz.pozY--;
							tablica[x][y-1]=0;
							punkty+=100;
						}
						else if(tablica[x][y-1]==9) {
							gracz.pozY--;
							gracz.zablokowany=true;
							gracz.licznik=10;
							tablica[x][y-1]=0;
						}
						else if(tablica[x][y-1]==11) {

							gracz.zycia--;
							gracz.zablokowany=false;
							gracz.licznik=0;
							if(gracz.zycia==0) przegrana();
							else spawn_myszy();
							tablica[x][y-1]=0;
						}
						else if(tablica[x][y-1]==0) gracz.pozY--;
						//else gracz.pozY--;
						
						break;
					case KeyEvent.VK_LEFT:
						if(tablica[x-1][y]==1)move_block(x , y,-1 , 0);
						else if(tablica[x-1][y]==8) {
							gracz.pozX--;
							tablica[x-1][y]=0;
							punkty+=100;
						}
						else if(tablica[x-1][y]==9) {
							gracz.pozX--;
							gracz.zablokowany=true;
							gracz.licznik=10;
							tablica[x-1][y]=0;
						}
						else if(tablica[x-1][y]==11) {
							gracz.zycia--;
							gracz.zablokowany=false;
							gracz.licznik=0;
							if(gracz.zycia==0) przegrana();
							else spawn_myszy();
							tablica[x-1][y]=0;
						}
						else if(tablica[x-1][y]==0)gracz.pozX--;
						
							
						
						break;
					case KeyEvent.VK_DOWN:
						if(tablica[x][y+1]==1)move_block(x , y,0 , 1);
						else if(tablica[x][y+1]==8) {
							gracz.pozY++;
							tablica[x][y+1]=0;
							punkty+=100;
						}
						else if(tablica[x][y+1]==9) {
							gracz.pozY++;
							gracz.zablokowany=true;
							gracz.licznik=10;
							tablica[x][y+1]=0;
						}
						else if(tablica[x][y+1]==11) {
							gracz.zycia--;
							gracz.zablokowany=false;
							gracz.licznik=0;
							if(gracz.zycia==0) przegrana();
							else spawn_myszy();
							tablica[x][y+1]=0;
						}
						else if(tablica[x][y+1]==0)gracz.pozY++; 
						
						
						break;
					case KeyEvent.VK_SPACE:
						nastepny_level();
						break;
					case KeyEvent.VK_Q:
						kot temp = kocury.get(0);
						ArrayList<Node> trasa =Astar.wyznacz_trase(temp.pozX , temp.pozY , gracz.pozX , gracz.pozY ,tablica );
						break;
				
			}
				//this.repaint();
				
				repaint();
		}
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
				if(tablica[tempx][tempy]==0 || tablica[tempx][tempy]==8) break;
				else if(tablica[tempx][tempy]==1) continue;
				else if(tablica[tempx][tempy]==9) {
					tablica[x+zm_x][y+zm_y]=0;
					gracz.pozX+=zm_x;
					gracz.pozY+=zm_y;
					return;
				}
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
			
			
			repaint();
		}
		
		
		public void spawn_myszy() {
			Random rand= new Random();
			int tempx , tempy;
			while(true) {
				tempx = rand.nextInt(21)+1;
				tempy = rand.nextInt(21)+1;
				if(tablica[tempx][tempy]!=0) continue;
				
				for(kot k: kocury) {
					if(Math.abs(tempx - k.pozX)+Math.abs(tempy-k.pozY)<8)spawn_myszy() ;
					
				}
				
				break;
			}
			
			gracz.pozX=tempx;
			gracz.pozY=tempy;
			
			this.repaint();
		}
		
		public void ruch_kota() {
			
			koniec_fali=true;
			for(kot k: kocury) {
				tablica[k.pozX][k.pozY]=0;
				ArrayList<Node> trasa =Astar.wyznacz_trase(k.pozX ,k.pozY , gracz.pozX , gracz.pozY ,tablica );
				if(trasa==null) {
					if(k.pozX>gracz.pozX && tablica[k.pozX-1][k.pozY]==0)k.pozX--;
					else if (k.pozX<gracz.pozX && tablica[k.pozX+1][k.pozY]==0)k.pozX++;
					else if(k.pozY>gracz.pozY && tablica[k.pozX][k.pozY-1]==0)k.pozY--;
					else if (k.pozY<gracz.pozY && tablica[k.pozX][k.pozY+1]==0)k.pozY++;
					else losowy_ruch_kota(k);
				}
				else {
					k.pozX=trasa.get(trasa.size()-2).x;
					k.pozY = trasa.get(trasa.size()-2).y;
					//k.pozX=trasa.get(1).x;
					//k.pozY = trasa.get(1).y;
				}
				if(!k.sen) {
					tablica[k.pozX][k.pozY]=6;
					koniec_fali=false;
					if(k.pozX==gracz.pozX && k.pozY==gracz.pozY) {
						//JOptionPane.showMessageDialog(null, "giniesz", "tak", JOptionPane.INFORMATION_MESSAGE);
						gracz.zycia--;
						gracz.zablokowany=false;
						gracz.licznik=0;
						if(gracz.zycia==0) przegrana();
						else spawn_myszy();
					}
				}
				else tablica[k.pozX][k.pozY]=7;
			}
			
			if(koniec_fali)kot_w_ser();
			
		}
		public void losowy_ruch_kota(kot k) {
			Random rand= new Random();
			int kierunek = rand.nextInt(3);
			int tempX =k.pozX;
			int tempY = k.pozY;
			
			for(int i=0 ; i<4 ; i++) {
				switch(kierunek) {
				case 0:
					if(tablica[k.pozX][k.pozY+1]==0)k.pozY++;
					else kierunek++;
					break;
				case 1:
					if(tablica[k.pozX-1][k.pozY]==0)k.pozX--;
					else kierunek++;
					break;
				case 2:
					if(tablica[k.pozX][k.pozY-1]==0)k.pozY--;
					else kierunek++;
					break;
				case 3:
					if(tablica[k.pozX+1][k.pozY]==0)k.pozX++;
					else kierunek=0;
					break;
				
				}				
			}
			if(k.pozX==tempX && k.pozY==tempY) {
				k.licznik++;
				if(k.licznik==3) {
					k.sen=true;
					punkty+=10;
				}
			}
			else {
				k.licznik=0;
				k.sen=false;
			}
			
			
			
		}
		public void kot_w_ser() {
			for(kot k :kocury) {
				    tablica[k.pozX][k.pozY]=8;                                                    
			}
			kocury.clear();
			if(aktualna_fala==fale.size())nastepny_level();
			else spawn();
			
		}
		
		public void nastepny_level() {
			
		//	JOptionPane.showMessageDialog(null, "konic fal", "tak", JOptionPane.INFORMATION_MESSAGE);
			poziom++;
			kocury.clear();
			fale.clear();
			wczytaj_level();
			gracz.pozX=11;
			gracz.pozY=11;
		}

		public void przegrana() {
			
			repaint();
			
			
			int response = JOptionPane.showConfirmDialog(null, "Zdobyles  "+punkty+" punktow. Zaczac od poczatku ?", "Koniec gry",
	                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.NO_OPTION) {
		            System.exit(0);
		    } 
		        else if (response == JOptionPane.YES_OPTION) {
		        	punkty=0;
		        	poziom=1;
		        	gracz.zycia=3;
		        	kocury.clear();
		        	gracz.pozX=11;
		        	gracz.pozY=11;
		        	wczytaj_level();
		        	spawn();
		        	
		        	

		    } 
		        else if (response == JOptionPane.CLOSED_OPTION) {
		            System.exit(0);
		    }
			
		}

		@Override
		public void actionPerformed(ActionEvent ev) {
			if(ev.getSource()==Truch_kota){
				if(gracz.zablokowany) gracz.licznik--;
				if(gracz.licznik==0) {
					gracz.zablokowany=false;
					
				}
				ruch_kota();
			      repaint();
			      
			    }
			
		}
}

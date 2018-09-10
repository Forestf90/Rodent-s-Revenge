package rodentsrevenge;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Astar {
	
	static ArrayList<Node> otwarte = new ArrayList<Node>();
	static ArrayList<Node> zamkniete= new ArrayList<Node>();
	public static Node[][] mapa = new Node[23][23];
	
	private static void tworz_mape(int[][] grid) {
		
		for(int i=0 ; i<mapa.length ; i++) {
			
			for(int j=0 ; j<mapa[i].length; j++) {
				mapa[i][j] = new Node();
				if(grid[i][j]!=0) mapa[i][j].przejscie=false;
				else mapa[i][j].przejscie=true;
			}
		}
	}
	
	private static void oblicz_hurystke(int kx ,int ky) {
		//String komunikat = "";
	for(int i=0 ; i<mapa.length ; i++) {
			
			for(int j=0 ; j<mapa[i].length; j++) {
				mapa[i][j].heurystyka= Math.abs(i-kx)+Math.abs(j-ky);
				mapa[i][j].x=i;
				mapa[i][j].y=j;
				//	komunikat +=mapa[i][j].heurystyka +" ";
			//	if(mapa[i][j].heurystyka<10)komunikat+=" ";
			}
			//komunikat+= System.lineSeparator();
		}
		
	//JOptionPane.showMessageDialog(null, komunikat, "tak", JOptionPane.INFORMATION_MESSAGE);
		
		
	}
	
	public  static ArrayList<Node> wyznacz_trase(int sx , int sy , int kx, int ky ,int[][] grid){
		
		ArrayList<Node> trasa = new ArrayList<Node>();
		tworz_mape(grid);
		mapa[sx][sy].przejscie=true;
		
		oblicz_hurystke(kx ,ky);
		Node aktualny =mapa[sx][sy];
		while(true) {
			zamkniete.add(aktualny);
			
			for(int i=-1 ; i<2 ;i++) {
				for(int j=-1 ; j<2 ; j++) {
					if(Math.abs(i)==Math.abs(j))continue;
					//int tempi = i ^ 1;
					if(aktualny.x+i <mapa.length && aktualny.y+j< mapa.length &&
							aktualny.x+i >-1 && aktualny.y+j>-1) {
						if(mapa[aktualny.x+i][aktualny.y+j].przejscie && !zamkniete.contains(mapa[aktualny.x+i][aktualny.y+j])) {
							/*
							if(aktualny.rodzic==null || mapa[aktualny.x+i][aktualny.y+j].koszt +mapa[aktualny.x+i][aktualny.y+j].heurystyka>
							mapa[aktualny.x][aktualny.y].heurystyka+mapa[aktualny.x][aktualny.y].koszt+10) {
								mapa[aktualny.x+i][aktualny.y+j].rodzic= aktualny;
							}
							mapa[aktualny.x+i][aktualny.y+j].koszt = aktualny.koszt+ 10;
							otwarte.add(mapa[aktualny.x+i][aktualny.y+j]);
							*/
							if(otwarte.contains(mapa[aktualny.x+i][aktualny.y+j])){
								if(mapa[aktualny.x+i][aktualny.y+j].rodzic.koszt +mapa[aktualny.x+i][aktualny.y+j].rodzic.heurystyka >
								mapa[aktualny.x][aktualny.y].heurystyka+mapa[aktualny.x][aktualny.y].koszt+10) {
									mapa[aktualny.x+i][aktualny.y+j].rodzic= aktualny;
									mapa[aktualny.x+i][aktualny.y+j].koszt = aktualny.koszt+ 10;
								}
							}
							else {
								mapa[aktualny.x+i][aktualny.y+j].rodzic= aktualny;
								otwarte.add(mapa[aktualny.x+i][aktualny.y+j]);
								mapa[aktualny.x+i][aktualny.y+j].koszt = aktualny.koszt+ 10;
							}
							
							
						}
					}
				}
			}
				
				Node temp= otwarte.get(0);
			for(Node n: otwarte) {
				
				if(n.koszt+n.heurystyka <temp.heurystyka +temp.koszt) temp=n;
				
			}
			otwarte.remove(temp);
			aktualny=temp;
			
				String kom="";
			for(int i=0 ; i<mapa.length ;i++) {
				for(int j=0 ;j<mapa[i].length ;j++) {
					if(zamkniete.contains(mapa[i][j])) kom+="# ";
					else if(otwarte.contains(mapa[i][j]))kom+="1 ";
					else if(!mapa[i][j].przejscie) kom+="   ";
					else kom+="- ";
					
				}
				kom+= System.lineSeparator();
				
			}
			kom+="zamkniete : "+zamkniete.size();
			kom+=System.lineSeparator() +"otwarte : "+otwarte.size();
		//	JOptionPane.showMessageDialog(null, kom, "tak", JOptionPane.INFORMATION_MESSAGE);
			
			if(otwarte.size() ==0) {
				trasa= null;
				return trasa;
			}
			if( aktualny.heurystyka==0) {
				//String komunikat="";
				Node temp2= aktualny ;
				//komunikat ="x= "+aktualny.x +" y= "+aktualny.y;
				trasa.add(temp2);
				while(true) {
					if(temp2.rodzic==null) break;
					temp2= temp2.rodzic;
					trasa.add(temp2);
				//	komunikat +="x= "+temp2.x +" y= "+temp2.y;
					//komunikat +=System.lineSeparator();
				}
				//JOptionPane.showMessageDialog(null, komunikat, "tak", JOptionPane.INFORMATION_MESSAGE);
				return trasa;
				//break;
			}
			
		}
		
		
		
		
	}
		//return trasa;
	}



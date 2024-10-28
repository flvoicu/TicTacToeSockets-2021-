package juego;

import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Juego2 implements ActionListener {

	Socket sc;
	OutputStream ostream;
	ObjectInput in;
	String host;
	DataInputStream istream;
	ObjectInputStream istreamOBJ;
	ServerSocket serverAddr;
	ObjectOutput s;

	JFrame frame = new JFrame();
	JPanel title_panel = new JPanel();
	JPanel button_panel = new JPanel();
	JLabel textfield = new JLabel();
	JButton[] buttons = new JButton[9];
	
	// variables usadas en el juego
	boolean player1_turn;
	boolean winsX= false;
	boolean winsO= false;
	int[] xWins = new int [3];
	int[] oWins = new int [3];
	boolean draw = false;

	Juego2() {
		// primero se crea la conexion
		try {
			host = "localhost";
			sc = new Socket(host, 2500);
		} catch (Exception e1) {
			System.err.println("excepcion " + e1.toString());
			e1.printStackTrace();
		}
		
		//se define los valores del panel 
		// frame
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // crear marco, solo se cierra esa ventana
		frame.setSize(800, 800);
		frame.getContentPane().setBackground(new Color(255, 0, 0));
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);

		// texto en general
		textfield.setBackground(new Color(250, 250, 0)); // crear fondo
		textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setOpaque(true);

		// panel de texto arriba
		title_panel.setLayout(new BorderLayout());
		title_panel.setBounds(0, 0, 800, 100);

		//orden de bottones 
		button_panel.setLayout(new GridLayout(3, 3));

		//crear botones y adaptarlos
		for (int i = 0; i < 9; i++) {
			buttons[i] = new JButton();
			button_panel.add(buttons[i]);
			buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
		}

		title_panel.add(textfield);
		frame.add(title_panel, BorderLayout.NORTH);
		frame.add(button_panel);

		Notify();
		
		firstTurn();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// metodo para leer los clicks 
		for (int i = 0; i < 9; i++) {

			if (e.getSource() == buttons[i]) {
				try {
					//iniciar conexion
					ostream = sc.getOutputStream();
					s = new ObjectOutputStream(ostream);
					istream = new DataInputStream(sc.getInputStream());
					istreamOBJ= new ObjectInputStream(sc.getInputStream());
					//enviar el indice del botton seleccionado al servidor para hacer las comprobaciones
					s.writeObject(i);
					s.flush();
					//lee las comprobaciones del servidor 
					winsX = istream.readBoolean();					
					winsO = istream.readBoolean();	
					//el action listener decide de quien es el movimiento y adapta el boton
					if (player1_turn) {
					     	buttons[i].removeActionListener(this);
							buttons[i].setForeground(new Color(0, 0, 0));
							buttons[i].setText("X");
							textfield.setText("O Turn");
							player1_turn = istream.readBoolean();
						
					} else {
					    	buttons[i].removeActionListener(this);
							buttons[i].setForeground(new Color(0, 0, 0));
							buttons[i].setText("O");
							textfield.setText("X Turn");
							player1_turn = istream.readBoolean();
							
					}
					//si hay comprobaciones verdaderas se llama a los debidos metodos para expresarlas
					if(winsX) {
						xWins = (int[]) istreamOBJ.readObject();
						xWins(xWins[0],xWins[1],xWins[2]);
					}else if (winsO){
						oWins = (int[]) istreamOBJ.readObject();
						oWins(oWins[0],oWins[1],oWins[2]);
					}
					if(!winsX && !winsO) {
						draw = istream.readBoolean();
						if(draw) {
							textfield.setText("Draw");
						}
					}
					
				} catch (Exception e1) {
					System.err.println("excepcion " + e1.toString());
					e1.printStackTrace();
				}

			}
		}

	}

	public void Notify() {
		try {
			ostream = sc.getOutputStream();
			s = new ObjectOutputStream(ostream);
			s.writeObject(2);
			s.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//metodo que recibe del servidor un un boolean declarando el turno
	public void firstTurn() {
		
		try {
			istream = new DataInputStream(sc.getInputStream());
			player1_turn = istream.readBoolean();
			
			if (player1_turn) {
				textfield.setText("X turn");
			} else {
				textfield.setText("O turn");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void xWins(int a, int b, int c) {

		for (int i = 0; i < 9; i++) {
			buttons[i].removeActionListener(this);
		}

		buttons[a].setForeground(new Color(41, 148, 22));
		buttons[b].setForeground(new Color(41, 148, 22));
		buttons[c].setForeground(new Color(41, 148, 22));

		textfield.setText("X Wins");

	}

	public void oWins(int a, int b, int c) {

		for (int i = 0; i < 9; i++) {
			buttons[i].removeActionListener(this);
		}

		buttons[a].setForeground(new Color(41, 148, 22));
		buttons[b].setForeground(new Color(41, 148, 22));
		buttons[c].setForeground(new Color(41, 148, 22));

		textfield.setText("O Wins");

	}

}

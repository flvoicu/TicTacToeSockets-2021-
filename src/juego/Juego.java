package juego;

import javax.swing.*;
import java.util.*;
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

public class Juego implements ActionListener {

	//declarar los datos para la conexion
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
	Random random = new Random();
	
	boolean player1_turn=true;
	boolean libre = false;
	boolean winsX = false;
	boolean winsO = false;
	int[] xWins = new int[3];
	int[] oWins = new int[3];
	boolean draw = false;
	int move;

	Juego() {
		try {
			host = "localhost";
			sc = new Socket(host, 2500);
		} catch (Exception e1) {
			System.err.println("excepcion " + e1.toString());
			e1.printStackTrace();
		}
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		frame.setSize(800, 800);
		frame.getContentPane().setBackground(new Color(255, 0, 0));
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);

		textfield.setBackground(new Color(250, 250, 0)); 
		textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
		textfield.setHorizontalAlignment(JLabel.CENTER);
		textfield.setOpaque(true);

		title_panel.setLayout(new BorderLayout());
		title_panel.setBounds(0, 0, 800, 100);

		button_panel.setLayout(new GridLayout(3, 3));

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

		for (int i = 0; i < 9; i++) {

			if (e.getSource() == buttons[i]) {
				try {
					//inicia conexion
					ostream = sc.getOutputStream();
					s = new ObjectOutputStream(ostream);
					istream = new DataInputStream(sc.getInputStream());
					istreamOBJ= new ObjectInputStream(sc.getInputStream());
					//enviar el indice del botton al servidor para hacer las comprobaciones
					s.writeObject(i);
					s.flush();
					//lee el movimiento del bot en el servidor
					move = istream.readInt();
					//lee las comprobaciones del servidor 
					winsX = istream.readBoolean();					
					winsO = istream.readBoolean();
					//el action listener decide cual es el botón y lo adapta
			     	buttons[i].removeActionListener(this);
					buttons[i].setForeground(new Color(0, 0, 0));
					buttons[i].setText("X");
					//se adapta el boton del movimiento del bot
					buttons[move].removeActionListener(this);
					buttons[move].setForeground(new Color(0, 0, 0));
					buttons[move].setText("O");
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
	
	//metodo que envia al servidor un int diciendo en que juego estoy
	public void Notify() {
		try {
			ostream = sc.getOutputStream();
			s = new ObjectOutputStream(ostream);
			s.writeObject(1);
			s.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ya que no hay segundo jugador se establece el texto del panel de arriba a Tu turno en inglés
	public void firstTurn() {
		
		textfield.setText("Your turn");

	}

	// metodo que cambia de color el contenido de los botones si hay una combinacion ganadora de X
	public void xWins(int a, int b, int c) {

		for (int i = 0; i < 9; i++) {
			buttons[i].removeActionListener(this);
		}

		buttons[a].setForeground(new Color(41, 148, 22));
		buttons[b].setForeground(new Color(41, 148, 22));
		buttons[c].setForeground(new Color(41, 148, 22));

		textfield.setText("X Wins");

	}
	
	// metodo que cambia de color el contenido de los botones si hay una combinacion ganadora de O	
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

package juego;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import javax.swing.JButton;

public class TratarPeticion extends Thread {

	Socket sc;
	DataOutputStream ostream = null;
	ObjectOutputStream ostreamOBJ = null;
	ObjectInput in;
	InputStream istream;
	JButton[] buttons = new JButton[9];
	JButton[] buttonsBOT = new JButton[9];
	Random random = new Random();
	
	int game = 0; //variable que decide el modo de juego
	boolean player1_turn; // variable para establecer el turno
	boolean winsX = false; // variable para saber si gana X
	boolean winsO = false; // variable para saber si gana O
	int[] xWins = new int[3]; // variable en la que se guarda la combinacion ganadora de X
	int[] oWins = new int[3]; // variable en la que se guarda la combinacion ganadora de O
	boolean draw = false; // variable para saber si hay un empate
	int move = 0; // variable en la que se guarda el movimiento del bot
	//variables para hacer comprobaciones en el bot
	boolean winsXbot = false;
	boolean winsObot = false;

	// constructor para recibir el socket
	TratarPeticion(Socket sc) {
		this.sc = sc;
	}

	// método run
	public void run() {

		checkGame(); // obtiene la opcion del juego 

		for (int i = 0; i < 9; i++) {
			buttons[i] = new JButton();
		}

		int a; // variable para guardar la posicion del movimiento entrante

		// switch para entrar en el juego seleccionado
		switch (game) {
		case 1:

			for (int i = 0; i < 9; i++) {
				try {
					//inicia conexion
					istream = sc.getInputStream();
					in = new ObjectInputStream(istream);
					ostream = new DataOutputStream(sc.getOutputStream());
					ostreamOBJ = new ObjectOutputStream(sc.getOutputStream());
					//recibir la posicion del movimiento
					a = (int) in.readObject();
					buttons[a].setText("X");
					//mira si hay empate 
					checkDraw();
					// si no hay empate el bot elige movimiento
					if (!draw) {
						bot();
					}
					//se guarda el movimiento en los botones
					buttons[move].setText("O");
					// mira si hay algun ganador
					check();
					//envio de variables
					ostream.writeInt(move);
					ostream.writeBoolean(winsX);
					ostream.writeBoolean(winsO);
					//si hay variables verdaderas se envia lo que debe hacer el cliente
					if (winsX) {
						ostreamOBJ.writeObject(xWins);

					} else if (winsO) {
						ostreamOBJ.writeObject(oWins);
					}
					if (!winsX && !winsO) {
						ostream.writeBoolean(draw);
					}
					//se borra lo enviado
					ostream.flush();

				} catch (Exception e) {
					System.err.println("excepcion1 " + e.toString());
					e.printStackTrace();
				}

			}

			break;

		case 2:
			
			//se establece el turno
			firstTurn();

			for (int i = 0; i < 9; i++) {
				try {
					istream = sc.getInputStream();
					in = new ObjectInputStream(istream);
					ostream = new DataOutputStream(sc.getOutputStream());
					ostreamOBJ = new ObjectOutputStream(sc.getOutputStream());
					a = (int) in.readObject();
					// se establece el contenido del botón en base al turno 
					if (player1_turn) {
						buttons[a].setText("X");
						player1_turn = false;
					} else {
						buttons[a].setText("O");
						player1_turn = true;

					}
					//comprobar si hay ganadores
					check();
					//comprobar si hay empate
					checkDraw();
					//envio de variables
					ostream.writeBoolean(winsX);
					ostream.writeBoolean(winsO);
					ostream.writeBoolean(player1_turn);
					//si hay variables verdaderas se envia lo que debe hacer al cliente
					if (winsX) {
						ostreamOBJ.writeObject(xWins);

					} else if (winsO) {
						ostreamOBJ.writeObject(oWins);
					}
					if (!winsX && !winsO) {
						ostream.writeBoolean(draw);
					}
					// se borra lo enviado
					ostream.flush();

				} catch (Exception e) {
					System.err.println("excepcion2 " + e.toString());
					e.printStackTrace();
				}
			}

			break;

		}

	}

	//obtiene la opcion del juego
	public void checkGame() {
		try {
			istream = sc.getInputStream();
			in = new ObjectInputStream(istream);
			game = (int) in.readObject();
		} catch (Exception e) {
			System.err.println("excepcion " + e.toString());
			e.printStackTrace();
		}

	}

	//elige el primer jugador
	public void firstTurn() {

		try {
			ostream = new DataOutputStream(sc.getOutputStream());

			if (random.nextInt(2) == 0) {
				player1_turn = true;
			} else {
				player1_turn = false;
			}
			ostream.writeBoolean(player1_turn);
			ostream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// mira si hay alguna convinación ganadora, si hay la guarda en un array y cambia la variable correspondiente a true
	public void check() {

		if ((buttons[0].getText() == "X") && (buttons[1].getText() == "X") && (buttons[2].getText() == "X")) {
			xWins[0] = 0;
			xWins[1] = 1;
			xWins[2] = 2;
			winsX = true;
		}
		if ((buttons[3].getText() == "X") && (buttons[4].getText() == "X") && (buttons[5].getText() == "X")) {
			xWins[0] = 3;
			xWins[1] = 4;
			xWins[2] = 5;
			winsX = true;
		}
		if ((buttons[6].getText() == "X") && (buttons[7].getText() == "X") && (buttons[8].getText() == "X")) {
			xWins[0] = 6;
			xWins[1] = 7;
			xWins[2] = 8;
			winsX = true;
		}
		if ((buttons[0].getText() == "X") && (buttons[4].getText() == "X") && (buttons[8].getText() == "X")) {
			xWins[0] = 0;
			xWins[1] = 4;
			xWins[2] = 8;
			winsX = true;
		}
		if ((buttons[2].getText() == "X") && (buttons[4].getText() == "X") && (buttons[6].getText() == "X")) {
			xWins[0] = 2;
			xWins[1] = 4;
			xWins[2] = 6;
			winsX = true;
		}
		if ((buttons[0].getText() == "X") && (buttons[3].getText() == "X") && (buttons[6].getText() == "X")) {
			xWins[0] = 0;
			xWins[1] = 3;
			xWins[2] = 6;
			winsX = true;
		}
		if ((buttons[1].getText() == "X") && (buttons[4].getText() == "X") && (buttons[7].getText() == "X")) {
			xWins[0] = 1;
			xWins[1] = 4;
			xWins[2] = 7;
			winsX = true;
		}

		if ((buttons[2].getText() == "X") && (buttons[5].getText() == "X") && (buttons[8].getText() == "X")) {
			xWins[0] = 2;
			xWins[1] = 5;
			xWins[2] = 8;
			winsX = true;
		}

		if ((buttons[0].getText() == "O") && (buttons[1].getText() == "O") && (buttons[2].getText() == "O")) {
			oWins[0] = 0;
			oWins[1] = 1;
			oWins[2] = 2;
			winsO = true;
		}
		if ((buttons[3].getText() == "O") && (buttons[4].getText() == "O") && (buttons[5].getText() == "O")) {
			oWins[0] = 3;
			oWins[1] = 4;
			oWins[2] = 5;
			winsO = true;
		}
		if ((buttons[6].getText() == "O") && (buttons[7].getText() == "O") && (buttons[8].getText() == "O")) {
			oWins[0] = 6;
			oWins[1] = 7;
			oWins[2] = 8;
			winsO = true;
		}
		if ((buttons[0].getText() == "O") && (buttons[4].getText() == "O") && (buttons[8].getText() == "O")) {
			oWins[0] = 0;
			oWins[1] = 4;
			oWins[2] = 8;
			winsO = true;
		}
		if ((buttons[2].getText() == "O") && (buttons[4].getText() == "O") && (buttons[6].getText() == "O")) {
			oWins[0] = 2;
			oWins[1] = 4;
			oWins[2] = 6;
			winsO = true;
		}
		if ((buttons[0].getText() == "O") && (buttons[3].getText() == "O") && (buttons[6].getText() == "O")) {
			oWins[0] = 0;
			oWins[1] = 3;
			oWins[2] = 6;
			winsO = true;
		}
		if ((buttons[1].getText() == "O") && (buttons[4].getText() == "O") && (buttons[7].getText() == "O")) {
			oWins[0] = 1;
			oWins[1] = 4;
			oWins[2] = 7;
			winsO = true;
		}

		if ((buttons[2].getText() == "O") && (buttons[5].getText() == "O") && (buttons[8].getText() == "O")) {
			oWins[0] = 2;
			oWins[1] = 5;
			oWins[2] = 8;
			winsO = true;
		}

	}

	//mira si hay empate y si lo hay cambia la variable correspondiente a true
	public void checkDraw() {

		int contador = 0;

		for (int i = 0; i < 9; i++) {

			if (buttons[i].getText() == "X" || buttons[i].getText() == "O") {
				contador++;
				if (contador == 9) {
					this.draw = true;

				}
			}
		}
	}
	
	// checked si hay alguna convinacion ganadora para las comprobaciones del bot
	public void checkBOT() {

		if ((buttons[0].getText() == "X") && (buttons[1].getText() == "X") && (buttons[2].getText() == "X")) {
			winsXbot = true;
		}
		if ((buttons[3].getText() == "X") && (buttons[4].getText() == "X") && (buttons[5].getText() == "X")) {
			winsXbot = true;
		}
		if ((buttons[6].getText() == "X") && (buttons[7].getText() == "X") && (buttons[8].getText() == "X")) {
			winsXbot = true;
		}
		if ((buttons[0].getText() == "X") && (buttons[4].getText() == "X") && (buttons[8].getText() == "X")) {
			winsXbot = true;
		}
		if ((buttons[2].getText() == "X") && (buttons[4].getText() == "X") && (buttons[6].getText() == "X")) {
			winsXbot = true;
		}
		if ((buttons[0].getText() == "X") && (buttons[3].getText() == "X") && (buttons[6].getText() == "X")) {
			winsXbot = true;
		}
		if ((buttons[1].getText() == "X") && (buttons[4].getText() == "X") && (buttons[7].getText() == "X")) {
			winsXbot = true;
		}

		if ((buttons[2].getText() == "X") && (buttons[5].getText() == "X") && (buttons[8].getText() == "X")) {
			winsXbot = true;
		}

		if ((buttons[0].getText() == "O") && (buttons[1].getText() == "O") && (buttons[2].getText() == "O")) {
			winsObot = true;
		}
		if ((buttons[3].getText() == "O") && (buttons[4].getText() == "O") && (buttons[5].getText() == "O")) {
			winsObot = true;
		}
		if ((buttons[6].getText() == "O") && (buttons[7].getText() == "O") && (buttons[8].getText() == "O")) {
			winsObot = true;
		}
		if ((buttons[0].getText() == "O") && (buttons[4].getText() == "O") && (buttons[8].getText() == "O")) {
			winsObot = true;
		}
		if ((buttons[2].getText() == "O") && (buttons[4].getText() == "O") && (buttons[6].getText() == "O")) {
			winsObot = true;
		}
		if ((buttons[0].getText() == "O") && (buttons[3].getText() == "O") && (buttons[6].getText() == "O")) {
			winsObot = true;
		}
		if ((buttons[1].getText() == "O") && (buttons[4].getText() == "O") && (buttons[7].getText() == "O")) {
			winsObot = true;
		}

		if ((buttons[2].getText() == "O") && (buttons[5].getText() == "O") && (buttons[8].getText() == "O")) {
			winsObot = true;
		}

	}

	public void bot() {

		boolean opcion2 = true;
		boolean rand = true;
		int[] prefMoves = new int[] { 0, 2, 6, 8 };
		
		// mira a ver si puede ganar en la siguiente convinación
		for (int i = 0; i < 9; i++) {
			if (buttons[i].getText() == "") {
				buttons[i].setText("O");
				checkBOT();
				buttons[i].setText("");

				//si en el check del bot ganan las Os se opcion2= false y no se gener el siguiente if
				if (winsObot) {
					move = i;
					winsObot = false;
					opcion2 = false;
					rand = false;
					i = 9;
				
				// si no ganan las Os entonces el bot elige entre algun movimiento preferido
				} else if (i == prefMoves[3] || i == prefMoves[1] || i == prefMoves[2] || i == prefMoves[0]) {
					move = i;
					rand = false;
					i = 9;
				}

			}
		}
		
		// si las Os no han ganado comprueba si X puede ganar, si X puede ganar cambia el movimiento
		if (opcion2) {
			for (int j = 0; j < 9; j++) {
				if (buttons[j].getText() == "") {
					buttons[j].setText("X");
					checkBOT();
					buttons[j].setText("");
					if (winsXbot) {
						move = j;
						winsXbot = false;
						j = 9;
					}
				}
			}
		}
		// si ninguno de esas comprobaciones salen bien se generar movimientos aleatorios
		if (rand) {
			while (rand) {
				int num = random.nextInt(9);
				if (buttons[num].getText() == "") {
					move = num;
					rand = false;
				}
			}
		}
		
		// si el jugador X no empieza en el centro el bot lo usará
		if(buttons[4].getText() == ""){
			buttons[4].setText("O");
			move = 4;
		}

	}
}

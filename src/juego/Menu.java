package juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Menu implements ActionListener {

	// se crea la vista
	JFrame frame = new JFrame(); //marco
	JPanel title_panel = new JPanel(); // panel de título
	JPanel button_panel = new JPanel(); // panel de juego
	JLabel textfield = new JLabel(); // area de texto que estara dentro de los botones
	JButton[] buttons = new JButton[2]; // lista de botones 
	
	boolean opcion;
	boolean done = false;

	Menu() {

		// marco
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // crear frame
		frame.setSize(800, 800); // establecer tamaño
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);

		// texto de titulo
		textfield.setBackground(new Color(250, 250, 0)); //se establece el color del fondo
		textfield.setForeground(new Color(0, 0, 0)); //color texto
		textfield.setFont(new Font("Ink Free", Font.BOLD, 75));  //se establece la fuente
		textfield.setHorizontalAlignment(JLabel.CENTER); //aliniacion del contenido
		textfield.setText("Tic-Tac-Toe"); //se establece el texto 
		textfield.setOpaque(true); // se hace opaco 

		title_panel.setLayout(new BorderLayout()); //crear panel de titulo
		title_panel.add(textfield); //se le añade toda la informacion sobre el texto del titulo

		button_panel.setLayout(new GridLayout(2, 0)); // se establece la disposicion de los botones

		// se crea el primer boton y se añade las caracteristicas
		buttons[0] = new JButton();
		button_panel.add(buttons[0]);
		buttons[0].setFont(new Font("MV Boli", Font.PLAIN, 60));
		buttons[0].setText("1 Player");
		buttons[0].setFocusable(false); // hace el boton no enfocable
		buttons[0].addActionListener(this); // habilita la opcion de reconocer el click en el botón

		buttons[1] = new JButton();
		button_panel.add(buttons[1]);
		buttons[1].setFont(new Font("MV Boli", Font.PLAIN, 60));
		buttons[1].setText("2 Player");
		buttons[1].setFocusable(false);
		buttons[1].addActionListener(this);

		frame.add(title_panel, BorderLayout.NORTH);// añade el panel del título al marco y establece la alineacion arriba
		frame.add(button_panel); //añade el panel de botones al marco

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//metodo que mira que boton ha sido pulsado 
		if (e.getSource() == buttons[0]) {
			Juego j = new Juego(); //si ha sido el primero inicializa juego
			
		} else if (e.getSource() == buttons[1]) {
			Juego2 j2 = new Juego2(); // si ha sido el segundo inicializa juego2
			
		} 


	}

}

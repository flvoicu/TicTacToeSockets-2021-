package juego;

import java.net.Socket;
import java.net.ServerSocket;

public class Servidor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Socket sc; // se crea un socket
		
		//se hace un loop intentando acceptar una conexión, si se accepta se manda al socket a la clase TratarPeticion
		while(true) {
			try {
				ServerSocket serverAddr = new ServerSocket(2500);
				sc = serverAddr.accept();
				new TratarPeticion(sc).start();
			
			}catch(Exception e) {
				System.err.println("excepcion "+ e.toString());
				e.printStackTrace();
			}
		
		}
		
	}
}

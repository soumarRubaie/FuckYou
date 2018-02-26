import java.util.ArrayList;
import java.util.Random;

public class Transmission implements Runnable{
	//Constante
	private final int TAUX_ERREUR = 50;
	
	//Donné par les paramètres d'entrée
	private int tempsLatence = 0;
	private int typeErreur = 0;
	
	//
	private boolean statutEmission = true;
	private boolean statutReception = false;
	private Trame trameEmise;
	private Trame trameRecu;
	
	
	public void setTypeErreur(int e) {
		typeErreur = e;
	}
	
	public void setTempsLatence(int t) {
		tempsLatence = t;
	}
	
	public boolean isPretEmission() {
		return statutEmission;
	}

	public boolean isDonneeRecu() {
		return statutReception;
	}
	
	public void setTrameEmise(Trame t) {
		if (isPretEmission()) {
			trameEmise = t;
		}
	}
	
	//TODO Fonction de corruption des trames
	private Trame corrompreTrame(Trame t) {
		byte[] donnees = t.getDonnes();
		Random rand = new Random();
		int  randByte = rand.nextInt(donnees.length);
		int  randBit = rand.nextInt(7);
		//donnees[randByte] ^= ~(1 << randBit); //Ou exclusif
		return t;
	}
	
	private void transmettreTrame() {
		
		//1 - Attendre le signal (quelqu'un veut envoyer quelque chose)
		// TODO Signal de demande de transmission
		
		
		//2 - Mettre les erreurs s'il y en a! (inverser X bits aléatoirement dans la trame?)
		//TODO Mettre un % de chance d'erreur et l'appliquer
		
		//3 - Transmettre la trame (ou non)
		switch(typeErreur) {
		case 1:
			trameRecu = corrompreTrame(trameEmise);
			typeErreur = 0;
			break;
		case 2:
			typeErreur = 0;
			break;
		default: 
			trameRecu = trameEmise;
		}
		
		//4 - Attendre le temps de latence
		//5 - Mettre à "True" la reception d'un message
		try {
			Thread.sleep(tempsLatence);
			statutReception = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		transmettreTrame();
	}
	
}

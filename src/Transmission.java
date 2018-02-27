import java.util.ArrayList;
import java.util.Random;

public class Transmission implements Runnable {
	// Constante
	private final int TAUX_ERREUR = 100;

	// Donné par les paramètres d'entrée
	private int tempsLatence = 0;
	private int typeErreur = 1;

	//
	private boolean statutEmission = true;
	private boolean statutReception = false;
	private Trame trameEmise;
	private Trame trameRecu;
	private Object signal = new Object();
	
	
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

	public boolean setTrameEmise(Trame t) {
		synchronized(signal) {
			if (isPretEmission()) {
				trameEmise = t;
				statutEmission = false;
				signal.notify();
				return true;
			}
			//retourne faux si on a pas réussi à mettre la trame (concurrence)
			return false;
		}
	}

	// Permet de récupérer les infos de la trame reçu
	public Trame getTrameEmise(){
		synchronized(signal) {
			return trameRecu;
		}
	}
	// Permet de notifier le support que la trame était pour nous et qu'on l'a prise
	public Trame takeTrameEmise() {
		synchronized(signal) {
			statutReception = false;
			signal.notify();
			return trameRecu;
		}
	}
	
	private void corrompreTrame() {
		byte[] donnees = trameEmise.getDonnes();
		Random rand = new Random();
		int randByte = rand.nextInt(donnees.length);
		int randBit = rand.nextInt(7);
		System.out.println("[CANAL] Trame corrompue!\n Octet n°: " + randByte + "\nBit n°: " + randBit);
		donnees[randByte] = (byte) (donnees[randByte]^((byte)Math.pow(2,randBit))); //Ou exclusif
		trameEmise.setDonnes(donnees);
	}

	private void simulerLatence() {
		try {
			Thread.sleep(tempsLatence);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void transmettreTrame() {
		simulerLatence();
		trameRecu = trameEmise;
		synchronized(signal) {
			statutReception = true;
			statutEmission = true;
			signal.notify();
		}
	}

	@Override
	public void run(){
		while (true) {
			while(isDonneeRecu() || isPretEmission()) {
				synchronized(signal) {
					try {
						System.out.println("[CANAL]\nEn attente d'un déblocage :\nEmission: "+ isPretEmission()
						+ "\nReception: " + isDonneeRecu() +"\n");
						signal.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println("[CANAL]\nJe transmet une trame!");
			// Application des erreurs
			Random rand = new Random();
			int randErreur = rand.nextInt(100);
			if (randErreur < TAUX_ERREUR) {
				System.out.println("[CANAL]\nErreur n°" + typeErreur + " appliquée");
				switch (typeErreur) {
				case 1:
					corrompreTrame();
					transmettreTrame();
					break;
				case 2:
					statutEmission = true;
					break;
				default:
					transmettreTrame();
				}
			} else {
				transmettreTrame();
			}
			System.out.println("[CANAL]\nTrame transmise!");
		}
	}
}

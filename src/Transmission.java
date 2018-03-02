import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Transmission implements Runnable {
	// Constante
	private  int tauxErreur;

	// Donné par les paramètres d'entrée
	private int tempsLatence = 0;
	private int typeErreur = 0;

	//
	private boolean statutEmission = true;
	private boolean statutReception = false;
	private byte[] trameEmise;
	private byte[] trameRecu;
	private Object signalEmission = new Object();
	private Object signalReception = new Object();

	// Locker
	Lock l = new ReentrantLock();

	public void setTypeErreur(int typeErreur) {
		this.typeErreur = typeErreur;
	}

	public void setTauxErreur(int tauxErreur) {
		this.tauxErreur = tauxErreur;
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

	public boolean setTrameEmise(byte[] t) {
		l.lock();
		if (isPretEmission()) {
			trameEmise = t;
			statutEmission = false;
			l.unlock();
			return true;
		}
		// retourne faux si on a pas réussi à mettre la trame (concurrence)
		l.unlock();
		return false;
	}

	// Permet de récupérer les infos de la trame reçu
	public byte[] getTrameEmise() {
		return trameRecu;
	}

	// Permet de notifier le support que la trame était pour nous et qu'on l'a prise
	public byte[] takeTrameEmise() {
		l.lock();
		System.out.println("[CANAL] Quelqu'un prend la trame!");
		statutReception = false;
		l.unlock();
		return trameRecu;
	}

	private void corrompreTrame() {
		byte[] donnees = trameEmise;
		Random rand = new Random();
		int randByte = rand.nextInt(donnees.length);
		int randBit = rand.nextInt(7);
		System.out.println("[CANAL] Trame corrompue!\n Octet n°: " + randByte + "\nBit n°: " + randBit);
		donnees[randByte] = (byte) (donnees[randByte] ^ ((byte) Math.pow(2, randBit))); // Ou exclusif
		trameEmise = donnees;
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
		l.lock();
		trameRecu = trameEmise;
		statutReception = true;
		statutEmission = true;
		
		l.unlock();
	}

	@Override
	public void run() {
		while (true) {
			int i = 0;
			while (isDonneeRecu() || isPretEmission()) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				if(i%20==0) {
					//System.out.println("[CANAL] StatutEmission: " + statutEmission + "\nStatutReception: " + statutReception);
					//System.out.println("[CANAL] Trame recu: " + statutEmission + "\nStatutReception: " + statutReception);
				}
			}
			System.out.println("[CANAL] Je transmet une trame!");
			// Application des erreurs
			Random rand = new Random();
			int randErreur = rand.nextInt(100);
			if (randErreur < tauxErreur) {
				//System.out.println("[CANAL] Erreur n°" + typeErreur + " appliquée");
				switch (typeErreur) {
				case 1:
					corrompreTrame();
					transmettreTrame();
					break;
				case 2:
					l.lock();
					statutEmission = true;
					System.out.println(" Trame perdue !");
					l.unlock();
					break;
				default:
					transmettreTrame();
				}
			} else {
				transmettreTrame();
			}
			System.out.println("[CANAL] Trame transmise!");
		}
	}
}

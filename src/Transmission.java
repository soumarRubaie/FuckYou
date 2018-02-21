
public class Transmission implements Runnable{
	
	private boolean statutEmission = true;
	private boolean statutReception = false;
	private int tempsLatence = 0;
	private Trame trameEmise;
	private Trame trameRecu;
	
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
	
	private Trame insertError(Trame t) {
		//Inverser des bits aléatoirement?
		return t;
	}
	
	private void transmettreTrame() {
		try {
			trameRecu = insertError(trameEmise);
			Thread.sleep(tempsLatence);
			statutReception = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		//1 - Attendre le signal (quelqu'un veut envoyer quelque chose)
		//2 - "Transmettre la trame"
		//3 - Mettre les erreurs s'il y en a! (inverser X bits aléatoirement dans la trame?)
		//4 - Attendre le temps de latence
		transmettreTrame();
	}
	
}

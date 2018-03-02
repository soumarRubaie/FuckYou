import java.util.ArrayList;

public class EmissionReception implements Runnable {
	public int id = 0; // Id pour reconnaitre la station
	private Trame trameAEmettre;
	private Trame trameRecu;
	private byte[] byteAEmettre;
	private byte[] byteARecevoir;
	//code de detection/correction zero ou 1
	private int code;
	// Le canal de transmission et la TrameFactory associé au thread (A1 et C)
	// Avec le signal de transmission partagé avec A1
	private Transmission canal;
	private A1_Thread tFactory;
	private Object signalTrameAEmettre;
	private Object signalTrameRecue;

	// Tampons:
	private Tampon tamponEmission;
	private Tampon tamponReception;

	// Etat de la station vu par les autres threads
	private boolean pretAEmettre = true;
	private boolean pretARecevoir = true;

	EmissionReception(int tailleTampons){
		tamponEmission = new Tampon(tailleTampons);
		tamponReception = new Tampon(tailleTampons);
	}
	
	public boolean isPretAEmettre() {
		return pretAEmettre;
	};

	public boolean isPretARecevoir() {
		return pretARecevoir;
	};

	// Action possible des autres threads:
	public void setTrameAEmettre(Trame t) {
		synchronized (signalTrameAEmettre) {
			if (isPretAEmettre()) {
				trameAEmettre = t;
				pretAEmettre = false;
			}
		}
	}

	// Actions du thread:

	private void traiterTrameAEmettre() {
		// TODO Appliquer Hamming sur la trame
		
		// TODO byteAEmettre = trameAEmettre.toByte();
		while(!tamponEmission.ajouterTrame(byteAEmettre)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		pretAEmettre = true;
	}

	private void envoyerTrame() {
		// Si on arrive à envoyer la trame, on la retire du tampon
		if (canal.setTrameEmise(tamponEmission.getLastTrame())) {
			tamponEmission.freeLastTrame();
		}
	}

	private void recevoirTrame() {
		// Si la trame est pour cette station: la prendre
		if (isTramePourMoi(canal.getTrameEmise())) {
			byteARecevoir = canal.takeTrameEmise();
			while(!tamponReception.ajouterTrame(byteARecevoir)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	 
		}
	}

	private boolean isTramePourMoi(Trame t) {
		// TODO fonction isTramePourMoi
		return true;
	}

	private void traiterTrameRecu() {
		// TODO : A1 : isPretARecevoir()
		// TODO : Valider trame avec Hamming
		boolean erreur = false;
		if(code ==0)  {
			if(HammingEncodeAndDecode.decodeDetection(byteARecevoir, byteARecevoir.length-1)) {
				System.out.println("La trame a été rejeté ");
				erreur = true;
			}
		} else  {
			HammingEncodeAndDecode.decodeCorrection(byteARecevoir, byteARecevoir.length-1);
		}
		if(!erreur) {
			// TODO : ByteToTrame()
			// TODO : A1 : setTrameRecu()
		}
		
	}

	@Override
	public void run() {
		while (true) {
			// Si j'ai une trame à traiter (envoi):
			// TODO : fonction tampon emission full?
			if (!isPretAEmettre()) {
				traiterTrameAEmettre();
			}
			// Si le canal est dispo et que j'ai une trame à envoyer:
			if (!tamponEmission.estVide() && canal.isPretEmission()) {
				envoyerTrame();
			}

			// Si il y a une donnée à receptionner sur le canal
			// TODO : fonction tampon reception full?
			if (canal.isDonneeRecu()) {
				recevoirTrame();
			}
			// Si on a une trame receptionnée à traiter:
			if (!tamponReception.estVide()) {
				traiterTrameRecu();
			}
		}
	}
}

public class EmissionReception implements Runnable {
	private int idStation;
	private Trame trameAEmettre;
	private byte[] byteAEmettre;
	private byte[] byteARecevoir;
	//code de detection/correction zero ou 1
	private int code = 0;
	// Le canal de transmission et la TrameFactory associé au thread (A1 et C)
	// Avec le signal de transmission partagé avec A1
	private Transmission canal;
	private A1_B1_Thread a1_b1;

	// Tampons:
	private Tampon tamponEmission;
	private Tampon tamponReception;

	// Etat de la station vu par les autres threads
	private boolean pretAEmettre = true;
	private boolean pretARecevoir = true;

	EmissionReception(int tailleTampons, int idStation){
		tamponEmission = new Tampon(tailleTampons);
		tamponReception = new Tampon(tailleTampons);
		this.idStation = idStation;
	}
	
	public void setCanal(Transmission canal) {
		this.canal = canal;
	}

	public void setA1_B1(A1_B1_Thread a1_B1) {
		a1_b1 = a1_B1;
	}
	
	public boolean isPretAEmettre() {
		return pretAEmettre;
	};

	public boolean isPretARecevoir() {
		return pretARecevoir;
	};

	// Action possible des autres threads:
	public void setTrameAEmettre(Trame t) {
		if (isPretAEmettre()) {
			trameAEmettre = t;
			pretAEmettre = false;
		}
	}

	// Actions du thread:

	private void traiterTrameAEmettre() {
		byteAEmettre = trameAEmettre.getTrameToByte();
		byteAEmettre = HammingEncodeAndDecode.code(byteAEmettre);
		
		if(tamponEmission.ajouterTrame(byteAEmettre)) {
			pretAEmettre = true;
		}
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
			byteARecevoir = canal.getTrameEmise();
			if(tamponReception.ajouterTrame(byteARecevoir)) {
				byteARecevoir = canal.takeTrameEmise();
			}	 
		}
	}

	private boolean isTramePourMoi(byte[] tbytes) {
		tbytes = HammingEncodeAndDecode.decodeOnly(tbytes, tbytes.length-1);
		Trame t = Trame.getByteToTrame(tbytes);
		if (t.dest == this.idStation) {
			return true;
		} else {
			System.out.println("["+ Thread.currentThread().getName() + "] La trame dans C n'est pas pour moi!");
			System.out.println("["+ Thread.currentThread().getName() + "] Mon id est le n°" + this.idStation);
			System.out.println("["+ Thread.currentThread().getName() + "] l'id du destinataire est le n°" + t.dest);
			return false;
		}
	}

	private void traiterTrameRecu() {
		boolean a1b1Pret = a1_b1.isPretARecevoir();
		if(a1b1Pret){
			byteARecevoir = tamponReception.getLastTrame();
			tamponReception.freeLastTrame();
			boolean erreur = false;
			if(code == 0)  {
				if(HammingEncodeAndDecode.decodeDetection(byteARecevoir, byteARecevoir.length-1)) {
					System.out.println("La trame a été rejeté ");
					erreur = true;
				}
			} else {
				HammingEncodeAndDecode.decodeCorrection(byteARecevoir, byteARecevoir.length-1);
			}
			if(!erreur) {
				byteARecevoir = HammingEncodeAndDecode.decodeCorrection(byteARecevoir, byteARecevoir.length-1);
				Trame t = Trame.getByteToTrame(byteARecevoir);
				System.out.println("["+ Thread.currentThread().getName() + "] Je donne une trame à A1B1");
				a1_b1.setTrameRecue(t);
			}
		} else {
			System.out.println("["+ Thread.currentThread().getName() + "] A1B1 n'est pas prêt, je ne peux lui donner la trame");
		}
		
	}

	@Override
	public void run() {
		boolean isDonneeRecu;
		boolean isPretEmission;
		while (true) {
			// Si j'ai une trame à traiter (envoi):
			
			if (!isPretAEmettre()) {
				//System.out.println("["+ Thread.currentThread().getName() + "] J'ai recu une trame de A1B1, je la traite");
				traiterTrameAEmettre();
			}
			// Si le canal est dispo et que j'ai une trame à envoyer:
			isPretEmission = canal.isPretEmission();
			if (!tamponEmission.estVide() && isPretEmission) {
				//System.out.println("["+ Thread.currentThread().getName() + "] J'envoi une trame à C");
				envoyerTrame();
			}

			// Si il y a une donnée à receptionner sur le canal:
			isDonneeRecu = canal.isDonneeRecu();
			if (isDonneeRecu && !tamponReception.estPlein()) {
				//System.out.println("["+ Thread.currentThread().getName() + "] Il y a une trame à receptionner sur le canal C, je regarde...");
				recevoirTrame();
			}
			// Si on a une trame receptionnée à traiter:
			if (!tamponReception.estVide()) {
				//System.out.println("["+ Thread.currentThread().getName() + "] J'ai reçu une trame de C, je la traite");
				traiterTrameRecu();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

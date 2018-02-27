import java.util.ArrayList;

public class EmissionReception implements Runnable{
	public int id = 0; //Id pour reconnaitre la station
	private Trame trameAEmettre;
	private Trame trameRecu;
	 
	//Le canal de transmission et la TrameFactory associé au thread (A1 et C)
	//Avec le signal de transmission partagé avec A1
	private Transmission canal;
	private TrameFactory tFactory;
	private Object signalTrameAEmettre;
	private Object signalTrameRecue;
	
	//Tampons:
	private ArrayList<Trame> tamponEmission = new ArrayList<Trame>();
	private ArrayList<Trame> tamponReception = new ArrayList<Trame>();
	
	//Etat de la station vu par les autres threads
	private boolean pretAEmettre;
	private boolean pretARecevoir;
	public boolean isPretAEmettre() {return pretAEmettre;};
	public boolean isPretARecevoir() {return pretARecevoir;};

	//Action possible des autres threads:
	public void setTrameAEmettre(Trame t) {
		synchronized(signalTrameAEmettre) {
			if(isPretAEmettre()) {
				trameAEmettre = t;
				pretAEmettre = false;
				signalTrameAEmettre.notify();
			}
		}
	}
	//Action du thread:
	private void traiterTrameAEmettre() {
		
	}
	private void envoyerTrame() {
		
	}
	
	private boolean isTramePourMoi(Trame t) {
		return true;
	}
	
	private void traiterTrameRecu() {
		
	}
	
	@Override
	public void run() {
		while (true) {
			//Si j'ai une trame à traiter (envoi):
			if(!isPretAEmettre()) {
				traiterTrameAEmettre();
			}
			//Si le canal est dispo et que j'ai une trame à envoyer:
			if(!tamponEmission.isEmpty() && canal.isPretEmission()) {
				//Si on arrive à envoyer la première trame
				if(canal.setTrameEmise(tamponEmission.get(0))) {
					tamponEmission.remove(0);
				}
			}
			
			//Si il y a une donnée à receptionner sur le canal
			if(canal.isDonneeRecu()) {
				//Si la trame est pour cette station:
				if(isTramePourMoi(canal.getTrameEmise())){
					tamponReception.add(canal.takeTrameEmise());
				}
			}
			//Si on a une trame receptionnée à traiter:
			if(!tamponReception.isEmpty()) {
				traiterTrameRecu();
			}
		}
	}
}

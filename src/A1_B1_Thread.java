import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class A1_B1_Thread extends Thread {
	private String nomFichier;
	private String nomFichierDestination;
	private boolean pretARecevoir;
	private Trame trameA1;
	private Trame trameRecue;
	private EmissionReception emissionReception;
	private int idStation;
	private int destinataire;
	private boolean read;
	private boolean a2PretAEmettre;

	public A1_B1_Thread(int idStation, EmissionReception emissionReception, int destinataire, boolean read) {
		super();
		this.emissionReception = emissionReception;
		this.idStation = idStation;
		this.destinataire = destinataire;
		this.pretARecevoir = true;
		this.read = read;
	}

	public void setFileName(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	
	public void setFileNameOutPut(String nomFichierDestination) {
		this.nomFichierDestination = nomFichierDestination;
	}
	public boolean isPretARecevoir() {
		return pretARecevoir;
	}

	public void setTrameRecue(Trame t) {
		trameRecue = t;
		pretARecevoir = false;
	}

	public Trame getTrameA1() {
		return this.trameA1;
	}

	public void setEmissionReception(EmissionReception emissionReception) {
		this.emissionReception = emissionReception;
	}
	
	public void recevoirTrame() {
		if (!isPretARecevoir()) {
			if (!trameRecue.isACK()) {
				// Ecriture dans le fichier
					try {
						PrintWriter writer = new PrintWriter(new FileOutputStream(new File("ressources/reception/" + nomFichierDestination), true));
					for (byte x : trameRecue.getDonnes()) {
						writer.print((char) x);
					}
					writer.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				// Création et envoi de la trame ACK
				
				Trame trameACK = new Trame(new byte[] {0,0,0,0}, idStation, destinataire, 1);
				a2PretAEmettre = emissionReception.isPretAEmettre();
				int compteur = 0;
				while (!a2PretAEmettre) {
					try {
						compteur ++;
						if (compteur%20 ==0) {
							System.out.println("["+ Thread.currentThread().getName() + "] B2 n'est pas dispo depuis longtemps! TimeOut: je n'envoi pas d'accusé de reception!");
							pretARecevoir = true;
							return;
						}
						Thread.sleep(100);
						a2PretAEmettre = emissionReception.isPretAEmettre();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (a2PretAEmettre) {
					System.out.println("["+ Thread.currentThread().getName() + "] J'envoie une trame ACK à B2");
					this.emissionReception.setTrameAEmettre(trameACK);
					pretARecevoir = true;
				}
			} else {
				pretARecevoir = true;
			}
		}
	}

	public void run() {

		if (read) {
			File file = new File("ressources/emission/" + nomFichier);

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];

			// Initialiser la taille de la trame au niveau de « A1 »
			int M = 0;

			// Lire le fichier octet par octet et me
			try {
				for (int i; (i = fis.read(buf)) != -1;) {
					bos.write(buf, 0, i);
					M++;
				}
				fis.close();
			} catch (IOException ex) {
				System.out.println("Erreur lors de la lecture");
			}

			// mettre les données dans un tableau d'octet
			System.out.println("[A1] Fichier lu et dans un tableau de Byte");
			byte[] bytes = bos.toByteArray();

			// tant qu'il reste des données a lire
			// on créé des trames de 4 octets de données
			// on les envoi
			int bytesRestant = bytes.length % 4;
			int limite = bytes.length - bytesRestant - 1;
			boolean a2PretAEmettre;
			for (int i = 0; i <= limite; i = i + 4) {
				byte[] temp = new byte[] { bytes[i], bytes[i + 1], bytes[i + 2], bytes[i + 3] };
				this.trameA1 = new Trame(temp, idStation, destinataire, 0);
				a2PretAEmettre = emissionReception.isPretAEmettre();
				while (!a2PretAEmettre) {
					try {
						Thread.sleep(100);
						a2PretAEmettre = emissionReception.isPretAEmettre();
						recevoirTrame();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (a2PretAEmettre) {
					System.out.println("[A1] Trame " + i/4 + " transmise à A2");
					this.emissionReception.setTrameAEmettre(trameA1);
				}
				recevoirTrame();
			}
			// S'il reste des octets:
			if (bytesRestant != 0) {
				byte[] temp = new byte[4];
				for (int i = 0; i < 4; i++) {
					if (i < bytesRestant) {
						temp[i] = bytes[bytes.length - bytesRestant + i];
					} else {
						temp[i] = 0x00;
					}
				}
				this.trameA1 = new Trame(temp, idStation, destinataire, 0);
				a2PretAEmettre = emissionReception.isPretAEmettre();
				while (!a2PretAEmettre) {
					try {
						Thread.sleep(100);
						a2PretAEmettre = emissionReception.isPretAEmettre();
						recevoirTrame();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				a2PretAEmettre = emissionReception.isPretAEmettre();
				if (a2PretAEmettre) {
					System.out.println("[A1] Dernière trame transmise à A2");
					this.emissionReception.setTrameAEmettre(trameA1);
				}
			}

		}
		// Routine de réception
		PrintWriter writer;
		if(!read) {
			try {
				writer = new PrintWriter("ressources/reception/" + nomFichierDestination, "utf-8");
				writer.print("");
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
		}
		
		while (true) {
			recevoirTrame();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

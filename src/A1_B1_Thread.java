import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class A1_B1_Thread extends Thread {
	private String nomFichier;
	private boolean pretARecevoir;
	private Trame trameA1;
	private Trame trameRecue;
	private EmissionReception emissionReception;
	private int idStation;
	private int destinataire;

	public A1_B1_Thread(int idStation, EmissionReception emissionReception, int destinataire) {
		super();
		this.emissionReception = emissionReception;
		this.idStation = idStation;
		this.destinataire = destinataire;
		this.pretARecevoir = true;
	}

	public void setFileName(String nomFichier) {
		this.nomFichier = nomFichier;
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

	public void run() {

		File file = new File("ressources/emission/" + nomFichier);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
		} catch (IOException ex) {
			System.out.println("erreur lors de la copie");
		}

		// mettre les données dans un tableau d'octet

		byte[] bytes = bos.toByteArray();

		// Crée un trame de M octets contenant l'information a transmettre et un numéro
		// de trame
		//	tant qu'il reste des données a lire on créé des trames de 4 octets
//			on les envoit
		int bytesRestant = bytes.length % 4;
		int limite = bytes.length - bytesRestant - 1;

		for (int i=0; i<= limite; i=i+4)
		{
			byte[] temp = new byte []{bytes[i], bytes[i+1], bytes[i+2], bytes[i+3]};
			this.trameA1 = new Trame(temp, M, idStation, destinataire);
			while(!this.emissionReception.isPretAEmettre()) {
				Thread.sleep(100);
			}
			if (this.emissionReception.isPretAEmettre()) {
				this.emissionReception.setTrameAEmettre(trameA1);
			}
		}
//		s`il reste des bits
		if(bytesRestant != 0) {
			byte [] temp;
			for(int i = 0; i < 4; i++) {
				if(bytesRestant > i) {
					temp[i] = bytes[bytes.length-bytesRestant+i];
				} else {
					temp[i] = 0x00;
				}
			}
			this.trameA1 = new Trame(temp, M, idStation, destinataire);
			while(!this.emissionReception.isPretAEmettre()) {
				Thread.sleep(100);
			}
			if (this.emissionReception.isPretAEmettre()) {
				this.emissionReception.setTrameAEmettre(trameA1);
			}
		}
		
		
		
		while (true) {
			if(!isPretARecevoir()) {
//				TODO Trame.isACK
				if(!trameRecue.isACK()) {
					PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
					for( byte x : trameRecue.getDonnes()) {	 
						writer.print((char)x);
			    	 }
					writer.close();
					Trame trameACK = new Trame(bytes, M, idStation, destinataire);
					while(!this.emissionReception.isPretAEmettre()) {
						Thread.sleep(100);
					}
					if (this.emissionReception.isPretAEmettre()) {
						this.emissionReception.setTrameAEmettre(trameACK);
					}
				}
			}
		}
	}

}

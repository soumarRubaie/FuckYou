
public class Trame {
	
	private byte[] donnes;
	private int tailleTrame;
	// Le numéro de la trame sera généré automatiquement
	private static int counter = 1; 
	public final int numeroTrame;  
	
	public Trame(byte[] donnes,int tailleTrame){
	    this.donnes=donnes; 
	    this.tailleTrame= tailleTrame; 
	    this.numeroTrame = counter++; 
	}
	
	public int getNumTrame(){
	    return this.numeroTrame; 
	}
	
	public byte[] getDonnes(){
		return donnes;
	}

	public void setDonnes(byte[] donnes) {
		this.donnes = donnes;
	}

	public int getTailleTrame() {
		return tailleTrame;
	}

	public void setTailleTrame(int tailleTrame) {
		this.tailleTrame = tailleTrame;
	}

	public void display() {
		System.out.print("[TRAME]\nNuméro trame: " + numeroTrame + "\nTaille trame: "
				+ tailleTrame + "\nContenu: [");
		for(byte octet : donnes) {
			System.out.print(octet + " ");
		}
		System.out.println("]\n");
	}

}

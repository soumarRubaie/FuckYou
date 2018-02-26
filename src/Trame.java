
public class Trame {
	private int numeroTrame;
	private byte[] donnes;
	private int tailleTrame;

	public int getNumeroTrame() {
		return numeroTrame;
	}

	public void setNumeroTrame(int numeroTrame) {
		this.numeroTrame = numeroTrame;
	}

	public byte[] getDonnes() {
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

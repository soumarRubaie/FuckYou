public class Tampon {
	private byte[][] tampon;
	private boolean[] isFree;
	private int positionLecture = 0;
	private int positionEcriture = 0;
	private int taille;

	public Tampon(int taille) {
		this.taille = taille;
		tampon = new byte[taille][];
		isFree = new boolean[taille];
	}

	public boolean ajouterTrame(byte[] trame) {
		if(isFree[positionEcriture]) {
			tampon[positionEcriture] = trame;
			isFree[positionEcriture] = false;
			positionEcriture++;
			if (positionEcriture >= taille) {
				positionEcriture = 0;
			}
			return true;
		} else {
			return false;
		}
	}
	public boolean estVide() {
		for(boolean free : isFree) {
			if (!free) {
				return false;
			}
		}
		return true;
	}

	public byte[] getLastTrame() {
		return tampon[positionLecture];
	}
	
	public void freeLastTrame() {
		isFree[positionLecture] = true;
		positionLecture++;
		if (positionLecture >= taille) {
			positionLecture = 0;
		}
	}
}

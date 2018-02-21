

public class Hamming {

	public static byte[] stepCode(byte[] ByteReceived) {

		boolean bits[] = fromOctetTobits(ByteReceived);

		int nombre_de_donnee = bits.length;
		int nombre_de_control = nombreDeControl(nombre_de_donnee);
		boolean BytesCoded[] = new boolean[nombre_de_donnee + nombre_de_control];
		HammingMethodes.extractionCodedBytes(bits, BytesCoded);
		return fromBitsToOctets(BytesCoded);
	}

	private static int nombreDeControl(int nombre_de_donnee) {
		int nombre_de_control = (int) Math.floor(((Math.log(nombre_de_donnee)) / Math.log(2)) + 1);
		return nombre_de_control;
	}

	//decoder les trames encodees par Hamming
	public static byte[] stepDecode(byte[] byteArray, int nombre_octets) {
		boolean bitArray[] = fromOctetTobits(byteArray);
		int nombre_de_control = nombreDeControlStepDecode(nombre_octets);
		int tailleTotal = nombre_de_control + nombre_octets * 8;
		boolean[] arrayOfbitsResultant = HammingMethodes.extractionDecode_detectionError(nombre_octets, bitArray, tailleTotal);
		return fromBitsToOctets(arrayOfbitsResultant);

	}

	private static int nombreDeControlStepDecode(int nombre_octets) {
		return (int) Math.floor(((Math.log(nombre_octets * 8)) / Math.log(2)) + 1);
	}
	
	// transformer le tableau Octets en tab bits
	public static boolean[] fromOctetTobits(byte[] bytes) {
		int nombre_octets = bytes.length;
		boolean[] bits = new boolean[nombre_octets * 8];
		for (int j = 0; j < nombre_octets * 8; j++) {
			if ((bytes[j / 8] & (1 << (7 - (j % 8)))) > 0) {
				bits[j] = true;
			}
		}
		return bits;
	}
	
	//  transformer un tableau de bits en tableau Octets (operation inverse de plus haut)
	public static byte[] fromBitsToOctets(boolean[] bits) {
		byte x;
		int nombre_bits = bits.length ;
		int size = (int) Math.floor(nombre_bits / 8);
		boolean bourrage = false;
		if (nombre_bits % 8 > 0) {
			size++;
			bourrage = true;
		}
		byte[] bytes = new byte[size];
		int j = 0;
		j = HammingMethodes.loopBourrage(bits, nombre_bits, size, bourrage, bytes, j);
		return bytes;
	}

	//Fonction de v�rification du bon d�codage des donn�es
	static boolean verifier(int i, boolean[] bitArray) {
		boolean res = false;
		for (int j = 0; j < bitArray.length; j++) {

			if (((j + 1) & (1 << i)) != 0) {
				res = res ^ bitArray[j];
			}
		}
		return res;

	}

}
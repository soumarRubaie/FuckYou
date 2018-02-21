
public class HammingMethodes {

	static int loopBourrage(boolean[] bits, int nombre_bits, int size, boolean bourrage, byte[] bytes, int j) {
		byte x;
		for (int i = 0; i < nombre_bits; i += 8, j++) {
			if (bourrage == true && j == size - 1) {
				int[] bit = new int[8];
				for (int l = 0; l < nombre_bits % 8; l++) {
					if (bits[nombre_bits - nombre_bits % 8 + l]) {
						bit[l] = 1;
					} else {
						bit[l] = 0;
					}
	
				}
				for (int l = (bits.length % 8); l < 8; l++) {
					bit[l] = 0;
	
				}
				x = (byte) ((bit[7]) + (bit[6] << 1) + (bit[5] << 2) + (bit[4] << 3) + (bit[3] << 4)
						+ (bit[2] << 5) + (bit[1] << 6) + (bit[0] << 7));
	
			} else {
				int bit0 = bits[i + 7] ? 1 : 0;
				int bit1 = bits[i + 6] ? 1 : 0;
				int bit2 = bits[i + 5] ? 1 : 0;
				int bit3 = bits[i + 4] ? 1 : 0;
				int bit4 = bits[i + 3] ? 1 : 0;
				int bit5 = bits[i + 2] ? 1 : 0;
				int bit6 = bits[i + 1] ? 1 : 0;
				int bit7 = bits[i] ? 1 : 0;
				x = (byte) ((bit0) + (bit1 * 2) + (bit2 * 4) + (bit3 * 8) + (bit4 * 16)
						+ (bit5 * 32) + (bit6 * 64) + (bit7 * 128));
			}
			bytes[j] = x;
		}
		return j;
	}

	static void extractionCodedBytes(boolean[] bits, boolean[] BytesCoded) {
		int j = 0;
		int x;
		for (int i = 0; i < BytesCoded.length; i++) {
			x = i + 1;
			if (!((x & -x) == x)) {
				BytesCoded[i] = bits[j];
				j++;
			}
		}
	
		for (int i = 0; i < BytesCoded.length; i++) {
			x = i + 1;
			if (((x & -x) == x)) {
				for (int l = 0; l < BytesCoded.length; l++) {
					if (((l + 1) & x) != 0) {
						BytesCoded[i] = BytesCoded[i] ^ BytesCoded[l];
					}
				}
			}
		}
	}

	static boolean[] extractionDecode_detectionError(int nombre_octets, boolean[] bitArray, int tailleTotal) {
		int x; int j = 0;
		int positionError = 0;
		
		boolean arrayOfbitsResultant[] = new boolean[nombre_octets * 8];
		for (int i = 0; i < tailleTotal; i++) {
	
			x = i + 1;
			if (((x & -x) == x)) {
				if (Hamming.verifier(j, bitArray)) {
					positionError = positionError + (1 << j);
				}
				j++;
			}
		}
		if (positionError != 0) {
			System.out.println("Erreur d�t�ct�e et corrig�e sur le bit de position " + positionError);
			bitArray[positionError - 1] = !bitArray[positionError - 1];
		}
		int cmpt = 0;
		for (int i = 0; i < tailleTotal; i++) {
			x = i + 1;
			if (!((x & -x) == x)) {
				arrayOfbitsResultant[cmpt] = bitArray[i];
				cmpt++;
			}
		}
		return arrayOfbitsResultant;
	}

}

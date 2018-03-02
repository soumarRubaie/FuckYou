// Source 
// http://www.java2s.com/Code/Android/Date-Type/ConvertabytearraytoabooleanarrayBit0isrepresentedwithfalseBit1isrepresentedwith1.htm
// http://www.java2s.com/Code/CSharp/File-Stream/BitArrayToByteArray.htm

public class HammingEncodeAndDecode {
	
	public static byte[] encode(byte[] Octets) {
		
		boolean bits[] = byteArray2BitArray(Octets);
		int nbDonnees = bits.length;
		int nbControl = (int) Math.floor(((Math.log(nbDonnees)) / Math.log(2)) + 1);
		boolean bitsCodee[] = new boolean[nbDonnees + nbControl];

		int j = 0;
		int temp;
		for (int i = 0; i < bitsCodee.length; i++) {
			temp = i + 1;
			if (!((temp & -temp) == temp)) {
				bitsCodee[i] = bits[j];
				j++;
			}
		}

		for (int i = 0; i < bitsCodee.length; i++) {
			temp = i + 1;
			if (((temp & -temp) == temp)) {
				for (int k = 0; k < bitsCodee.length; k++) {
					if (((k + 1) & temp) != 0) {
						bitsCodee[i] = bitsCodee[i] ^ bitsCodee[k];
					}
				}
			}
		}
		
		return bitArray2ByteArray(bitsCodee);
	}
	

	public static boolean[] byteArray2BitArray(byte[] bytes) {
		boolean[] bits = new boolean[bytes.length * 8];
		for (int i = 0; i < bytes.length * 8; i++) {
			if ((bytes[i / 8] & (1 << (7 - (i % 8)))) > 0) {
				bits[i] = true;
			}
		}
		return bits;
	}
	
	
	
	public static byte[] bitArray2ByteArray(boolean[] bits) {
		byte x;
		int taille = (int) Math.floor(bits.length / 8);
		boolean remplir = false;
		if (bits.length % 8 > 0) {
			taille++;
			remplir = true;
		}

		byte[] bytes = new byte[taille];
		int j = 0;
		for (int i = 0; i < bits.length; i += 8, j++) {
			if (remplir == true && j == taille - 1) {
				int[] bit = new int[8];
				for (int k = 0; k < bits.length % 8; k++) {
					if (bits[bits.length - bits.length % 8 + k]) {
						bit[k] = 1;
					} else {
						bit[k] = 0;
					}

				}
				for (int k = (bits.length % 8); k < 8; k++) {
					bit[k] = 0;

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
		return bytes;
	}

	
	
	public static byte[] decodeCorrection(byte[] byteArray, int nbOctets) {
		boolean bitArray[] = byteArray2BitArray(byteArray);
		int nbControl = (int) Math.floor(((Math.log(nbOctets * 8)) / Math.log(2)) + 1);
		int tailleTotal = nbControl + nbOctets * 8;
		int temp;
		int errorPos = 0;
		int j = 0;
		boolean bitArrayRes[] = new boolean[nbOctets * 8];
		for (int i = 0; i < tailleTotal; i++) {
			temp = i + 1;
			if (((temp & -temp) == temp)) {
				if (verifyFunction(j, bitArray)) {
					errorPos = errorPos + (1 << j);
				}
				j++;
			}
		}
		if (errorPos != 0) {
			System.out.println("Hamming: Error detected and fixed in the bits position number " + errorPos);
			bitArray[errorPos - 1] = !bitArray[errorPos - 1];
		}
		int cmpt = 0;
		for (int i = 0; i < tailleTotal; i++) {
			temp = i + 1;
			if (!((temp & -temp) == temp)) {
				bitArrayRes[cmpt] = bitArray[i];
				cmpt++;
			}
		}
		return bitArray2ByteArray(bitArrayRes);

	}
	
	public static boolean decodeDetection(byte[] byteArray, int nbOctets) {
		boolean bitArray[] = byteArray2BitArray(byteArray);
		int nbControl = (int) Math.floor(((Math.log(nbOctets * 8)) / Math.log(2)) + 1);
		int tailleTotal = nbControl + nbOctets * 8;
		int temp;
		int errorPos = 0;
		int j = 0;
		for (int i = 0; i < tailleTotal; i++) {
			temp = i + 1;
			if (((temp & -temp) == temp)) {
				if (verifyFunction(j, bitArray)) {
					errorPos = errorPos + (1 << j);
				}
				j++;
			}
		}
		if (errorPos != 0) {
			System.out.println("Hamming: Error detected in the bits position number " + errorPos);
			return true;
		} else {
			return false;
		}
	}
	
	public static byte[] decodeOnly(byte[] byteArray, int nbOctets) {
		boolean bitArray[] = byteArray2BitArray(byteArray);
		int nbControl = (int) Math.floor(((Math.log(nbOctets * 8)) / Math.log(2)) + 1);
		int tailleTotal = nbControl + nbOctets * 8;
		int temp;
		int errorPos = 0;
		int j = 0;
		boolean bitArrayRes[] = new boolean[nbOctets * 8];
		for (int i = 0; i < tailleTotal; i++) {
			temp = i + 1;
			if (((temp & -temp) == temp)) {
				if (verifyFunction(j, bitArray)) {
					errorPos = errorPos + (1 << j);
				}
				j++;
			}
		}
		if (errorPos != 0) {
			bitArray[errorPos - 1] = !bitArray[errorPos - 1];
		}
		int cmpt = 0;
		for (int i = 0; i < tailleTotal; i++) {
			temp = i + 1;
			if (!((temp & -temp) == temp)) {
				bitArrayRes[cmpt] = bitArray[i];
				cmpt++;
			}
		}
		return bitArray2ByteArray(bitArrayRes);

	}
	 
	private static boolean verifyFunction(int i, boolean[] bitArray) {
		boolean res = false;
		for (int j = 0; j < bitArray.length; j++) {
			if (((j + 1) & (1 << i)) != 0) {
				res = res ^ bitArray[j];
			}
		}
		return res;

	}

}




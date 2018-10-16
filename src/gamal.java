import java.io.*;
import java.math.*;
import java.nio.file.*;

public class gamal {

	private final String textFile = "Text.txt";														// z tekstowego(Text.txt)/z binarnego(binText.bin)
//	private final String cryptedTextFile = "Crypted.txt";
	private final int bitLength = 11;
	private String text;
	public User user1;
	public User user2;
	
	public class User {
		public BigInteger P;
		public BigInteger G;
		public BigInteger Y;
		public int x;
	}


	
	public static void main(String[] args) {
		gamal gamal = new gamal();
		gamal.run();
	}
	
	public void run() {
		user1 = new User();
		user2 = new User();
		generateKeys();
		readTextFile();
		byte[] byteArray = readBytesToArray(textFile);
		
		System.out.println("\nTekst: \n" + text + "\n\nOdczytane bajty z pliku:");
		System.out.print(" | ");
		for (int i=0; i<byteArray.length; i++) {
			System.out.print(byteArray[i] + " | ");	
		}
		
		BigInteger[] crypted = 	encrypt(user1.P, user1.Y, user2.x, byteArray);
		decrypt(user2.P, user2.Y, user1.x ,crypted);
		
		System.out.println("\nKoniec");
	}
	
	private BigInteger[] decrypt(BigInteger P, BigInteger A, int x, BigInteger[] cryptedArray) {
		BigInteger[] decrypted = new BigInteger[cryptedArray.length];
		
		BigDecimal dec, dec2;

		
		System.out.println("\nOdszyfrowane: ");
		System.out.print(" | ");
		for (int i=0; i<cryptedArray.length; i++) {
			dec2 = BigDecimal.valueOf(cryptedArray[i].longValue());
			dec = new BigDecimal(A.pow(5));
			dec = dec2.divide(dec, 100, RoundingMode.HALF_EVEN);
			decrypted[i] = BigInteger.valueOf(dec.longValue()).mod(P);
//			decrypted[i] = decrypted[i].mod(P);
			System.out.print(dec + " " + decrypted[i] + " | ");
		}
		return decrypted;
	}
	private BigInteger[] encrypt(BigInteger P, BigInteger Y, int k, byte[] byteArray) {
		BigInteger[] cryptedArray = new BigInteger[byteArray.length];
		for (int i=0; i<byteArray.length; i++) {
			cryptedArray[i] = new BigInteger(Byte.toString(byteArray[i]));
		}
		
		System.out.println("\nZaszyfrowane: ");
		System.out.print(" | ");

		for (int i=0; i<cryptedArray.length; i++) {
			cryptedArray[i] = Y.pow(k).multiply(cryptedArray[i]);
			cryptedArray[i] = cryptedArray[i].mod(P);
			System.out.print(cryptedArray[i] + " | ");
		}
		
		return cryptedArray;
	}
	
	private void generateKeys() {
		int p, g, x, k;
				
		p = primes();
		g = (int)(Math.random()*(p-1) + 1);
		x = (int)(Math.random()*p + 1);
		k = (int)(Math.random()*p + 1);	
		
//		p = ; g = ; x = ; k = ;
		
		user1.P = new BigInteger(Integer.toString(p));
		user2.P = new BigInteger(Integer.toString(p));
		user1.G = new BigInteger(Integer.toString(g));
		user2.G = new BigInteger(Integer.toString(g));
		
		user1.Y = user1.G.pow(x);
		user1.Y = user1.Y.mod(user1.P);
		
		user2.Y = user2.G.pow(k);
		user2.Y = user2.Y.mod(user2.P);
		
		user1.x = x;
		user2.x = k;
		//		BigInteger G, P, M;
//		p = 7873; g = 5046; x = 21; k =173;
		
//		M = new BigInteger("207");
//		G = new BigInteger(Integer.toString(g));
//		P = new BigInteger(Integer.toString(p));
		
//		Y = G.pow(x);
//		Y = Y.mod(P);
//		
//		A = G.pow(k);
//		A = A.mod(P);
//		
//		B = Y.pow(k);
//		B = B.multiply(M);
//		B = B.mod(P);
		
		System.out.println("Wartości do obliczeń: \np = " + p + ", g = " + g + ", x = " + x + ", k = " + k + ", Y = " + user1.Y.toString() + ", A = " + user2.Y.toString());
//		System.out.println("Wartości do obliczeń: \np = " + p + ", g = " + g + ", x = " + x + ", k = " + k);
	}

	public int primes() {								// wybiera liczby pierwsze
		int pr = (int)(Math.random()*(Math.pow(2,bitLength) - Math.pow(2,bitLength-1)) + Math.pow(2,bitLength-1));
		while(isPrime(pr) == false) {
			pr++;
		}
		return pr;
	}
	
	boolean isPrime(int N) {											// sprawdza czy liczba jest pierwsza
	    if (N%2==0) 
	    	return false;
	    for(int i=3; i<=N/2; i+=2) {
	        if (N%i == 0)
	            return false;
	    }
	    return true;
	}
	
	public void readTextFile() {										// wczytuje napis w postaci Stringa do zmeinnej
		try {
			BufferedReader reader = new BufferedReader(new FileReader(textFile));
			text = reader.readLine();
			reader.close();
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	public byte[] readBytesToArray(String fileName) {									// wczytuje kolejne bajty z pliku do tablicy
		byte[] arr = {0};
		Path path = Paths.get(fileName);
		try {
			arr = Files.readAllBytes(path);
			return arr;
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		return arr;
	}
}

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args) throws FileNotFoundException
    {       
        
//        Scanner sc= new Scanner(System.in); 
//     
//
//        System.out.println("Taille du tampon: "); 
//        // Lire la taille du tampon 
//        int tailleTampon = sc.nextInt(); 
//        
//        System.out.println("Délai de temporisation: ");
//        // Lire le délai de temporisation 
//        int timeOut = sc.nextInt(); 
//        
//        System.out.println("Nom du fichier: ");
//        // Lire le nom du fichier a copier 
//        String nomFichier = sc.next(); 
//        
//        // Lire l'emplacement de destination 
//        System.out.println("La destination: ");
//        String destination = sc.next();       
//        sc.close();
        int tailleTampon = 10;
        int timeOut = 10;
    	
//        Trame trameACK = new Trame(new byte[] {0,0,0,0}, 1, 2, 1);
//        trameACK.display();
//        byte[] test = trameACK.getTrameToByte();
//        Trame trameTest = Trame.getByteToTrame(test);
//        trameTest.display();
//        

        
    
        EmissionReception A2 = new EmissionReception(tailleTampon, 1);
        EmissionReception B2 = new EmissionReception(tailleTampon, 2);
        A1_B1_Thread A1 = new A1_B1_Thread(1, A2, 2, true);
        A1_B1_Thread B1 = new A1_B1_Thread(2, B2, 1, false);
        A1.setFileName("input.txt");
        
        
        Transmission C = new Transmission();
        C.setTempsLatence(timeOut);
        
        A2.setA1_B1(A1);
        A2.setCanal(C);
        B2.setA1_B1(B1);
        B2.setCanal(C);
        
        Thread A1_Thread = new Thread(A1,"A1");
        Thread A2_Thread = new Thread(A2,"A2");
        Thread B1_Thread = new Thread(B1,"B1");
        Thread B2_Thread = new Thread(B2,"B2");
        Thread C1_Thread = new Thread(C,"C");
        
        A1_Thread.start();
        A2_Thread.start();
        B1_Thread.start();
        B2_Thread.start();
        C1_Thread.start();
    }
    
}
import java.util.Scanner;

public class Main
{

    public static void main(String[] args)
    {       
        
        Scanner sc= new Scanner(System.in); 
     
        // LLC émetteur
        TestThread A1=  new TestThread("A1");  
        
        // MAC émetteur 
        TestThread A2 = new TestThread("A2");   
        
        // LLC récépteur
        TestThread B1 = new TestThread("B1"); 
        
        //MAC récépteur 
        TestThread B2 = new TestThread("B2"); 
        
        // Support de transmission 
        TestThread C = new TestThread("C"); 
            
        
        System.out.println("Taille du tampon: "); 
        // Lire la taille du tampon 
        int tailleTampon = sc.nextInt(); 
        
        System.out.println("Délai de temporisation: ");
        // Lire le délai de temporisation 
        int timeOut = sc.nextInt(); 
        
        System.out.println("Nom du fichier: ");
        // Lire le nom du fichier a copier 
        String nomFichier = sc.next(); 
        
        // Lire l'emplacement de destination 
        System.out.println("La destination: ");
        String destination = sc.next(); 
    
    }
    
}

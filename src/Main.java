import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args) throws FileNotFoundException
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
        
        File file = new File("ressources/emission/"+nomFichier); 
        
        FileInputStream fis = new FileInputStream(file); 
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        byte[] buf = new byte[1024]; 
        
        // Initialiser la taille de la trame au niveau de « A1 »
        int M=0;
        
        // Lire le fichier octet par octet et me
        try{
            for (int i;(i=fis.read(buf)) !=-1;){
                bos.write(buf,0,i);  
                M++; 
            }
        }
        catch (IOException ex) {
            System.out.println("erreur lors de la copie");
        }
        
        // mettre les données dans un tableau d'octet
        
        byte[] bytes = bos.toByteArray(); 
     
        // Crée un trame de M octets contenant l'information a transmettre et un numéro 
        // de trame
        
        Trame trameA1 = new Trame(bytes,M); 
        
        
    }
    
}

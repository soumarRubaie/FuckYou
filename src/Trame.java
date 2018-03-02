import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Trame {
	
	private byte[] donnes;
	
	private int tailleTrame;
	// Le numéro de la trame sera généré automatiquement
	private static int counter = 1; 
	public final int numeroTrame;  
	
	public int exp; 
	public int dest; 
	
	public int typeTrame; 
	
	public Trame(byte[] donnes, int tailleTrame, int exp, int dest){
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
	
	public byte[] getTrameToByte(){
	    
	    Integer numeTrame = new Integer(numeroTrame);  
	    Integer expe = new Integer(exp);  
	    Integer desti = new Integer(dest);  
        Integer type= new Integer(typeTrame);  
	     
        ByteBuffer a = ByteBuffer.allocate(Integer.BYTES* 4 + donnes.length ); 
        
        a.putInt(numeTrame); 
        a.putInt(expe);
        a.putInt(desti);
        a.putInt(type);
        
        a.put(donnes); 
        
	    return a.array(); 
	    
	}
	
	public Trame getByteToTrame(byte[] byteArray){
	    
	    Integer numeTrame = new Integer(numeroTrame);  
        Integer expe = new Integer(exp);  
        Integer desti = new Integer(dest);  
        Integer type= new Integer(typeTrame);  
	    
	    ByteBuffer a = ByteBuffer.allocate(byteArray.length); 
	    
        a.wrap(byteArray);
        
        numeTrame= a.getInt(); 
        expe= a.getInt();
        desti=a.getInt();
        type=a.getInt();
        
        byte[] arr = new byte[a.remaining()]; 
        
        a.get(arr,a.getInt()*4,arr.length); 
        
        Trame trame = new Trame(arr,exp,dest,byteArray.length);  
        
        return trame ; 
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

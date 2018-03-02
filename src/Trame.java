import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Trame {
	
	private byte[] donnes;
	
	private Integer tailleTrame;
	// Le numéro de la trame sera généré automatiquement
	private static Integer counter = 1; 
	public final Integer numeroTrame;  
	
	public Integer exp; 
	public Integer dest; 
	
	public Integer typeTrame; 
	
	public Trame(byte[] donnes, Integer exp, Integer dest){
	    this.donnes=donnes; 
	    this.numeroTrame = counter++; 
	    this.exp=exp; 
	    this.dest=dest; 
	    this.tailleTrame= Integer.BYTES* 4 + donnes.length; 
	    this.typeTrame= 0; 
	}
	
	public Integer getNumTrame(){
	    return this.numeroTrame; 
	}
	
	public byte[] getDonnes(){
		return donnes;
	}
	
	public boolean isACK(){
	    if (this.typeTrame==1)
	        return true;  
	    else
	        return false; 
	}
	
	public byte[] getTrameToByte(){
	    
        ByteBuffer a = ByteBuffer.allocate(Integer.BYTES* 4 + donnes.length ); 
        
        a.putInt(numeroTrame); 
        a.putInt(exp);
        a.putInt(dest);
        a.putInt(typeTrame);
        
        a.put(donnes); 
        
	    return a.array(); 
	    
	}
	
	public Trame getByteToTrame(byte[] byteArray){
	    
	    Integer numeTrame ;
        Integer expe;
        Integer desti ;
        Integer type;  
	 
        ByteBuffer wrapped = ByteBuffer.wrap(byteArray);
        
        numeTrame = wrapped.getInt(); 
        
        expe = wrapped.getInt(); 
        
        desti = wrapped.getInt(); 
        
        type = wrapped.getInt(); 
        
        // 16 est 4 entiers
        byte[] b = Arrays.copyOfRange(byteArray,16,byteArray.length);  
        
        Trame trame = new Trame(b,expe,desti);    
        
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

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Trame {
	
	private byte[] donnes;
	
	private Integer tailleTrame;
	// Le numéro de la trame sera généré automatiquement
	private static Integer counter = 1; 
	public Integer numeroTrame;  
	
	public Integer exp; 
	public Integer dest; 
	
	public Integer typeTrame; 
	
	public Trame(byte[] donnes, int exp, int dest, int typeTrame){
	    this.donnes=donnes; 
	    this.numeroTrame = counter++; 
	    this.exp=exp; 
	    this.dest=dest; 
	    this.tailleTrame= Integer.BYTES* 4 + donnes.length; 
	    this.typeTrame = typeTrame; 
	}
	
	public Integer getNumTrame(){
	    return this.numeroTrame; 
	}
	
	public void setNumTrame(Integer num){
	    numeroTrame = num;
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
	
	public static Trame getByteToTrame(byte[] byteArray){
	    
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
        
        Trame trame = new Trame(b, expe, desti, type);    
        trame.setNumTrame(numeTrame);
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
				+ tailleTrame + "\nExpéditeur: " + exp + "\nDestinataire:" + dest 
				+ "\nType de Trame: " + typeTrame +  "\nContenu: [");
		for(byte octet : donnes) {
			System.out.print(octet + " ");
		}
		System.out.println("]\n");
	}

}

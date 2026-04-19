public class Evento 
{
    private String id;
    private int tipoServidor;
    private boolean esFin;
    

    public Evento(String id, int tipoServidor) {
        this.id = id;
        this.tipoServidor = tipoServidor;
        this.esFin = false;
    }
    

    public Evento() {
        this.esFin = true;
        this.id = "FIN";
        this.tipoServidor = -1;
    }
    
    public String getId() {
        return id;
    }
    
    public int getTipoServidor() {
        return tipoServidor;
    }
    
    public boolean esFin() {
        return esFin;
    }
    
    @Override
    public String toString() {
        return esFin ? "Evento[FIN]" : "Evento[" + id + ", tipo=" + tipoServidor + "]";
    }
}
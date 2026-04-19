public class Monitor
{
    private int numClasificadores;
    private int terminados;
    
    public Monitor(int numClasificadores) {
        this.numClasificadores = numClasificadores;
        this.terminados = 0;
    }
    

    public synchronized boolean reportarTerminacion() {
        terminados++;
        boolean esUltimo = (terminados == numClasificadores);
        
        if (esUltimo) {
            System.out.println("*** Clasificador es el ÚLTIMO en terminar ***");
        } else {
            System.out.println("    Clasificador terminado (" + terminados + "/" + numClasificadores + ")");
        }
        
        return esUltimo;
    }
}
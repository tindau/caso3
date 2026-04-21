import java.util.Random;

public class Administrador implements Runnable
{
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;
    private int numClasificadores;
    private Random random;
    

    public Administrador(Buzon alertas, Buzon clasificacion, int numClasificadores) 
    {
        this.buzonAlertas = alertas;
        this.buzonClasificacion = clasificacion;
        this.numClasificadores = numClasificadores;
        this.random = new Random();
    }
    
    @Override
    public void run() {
            System.out.println("Administrador iniciado");
            
            while (true) {
                Evento evento = buzonAlertas.retirarSemi();
                
                if (evento.esFin()) {
                    System.out.println("Administrador recibio evento FIN");
                    break;
                }
                

                int numeroAleatorio = random.nextInt(21);
                boolean esNormal = (numeroAleatorio % 4 == 0);
                
                if (esNormal)
                {
                    System.out.println("Administrador: Evento " + evento.getId() +  " es INOFENSIVO, enviando a clasificacion");
                    buzonClasificacion.depositar(evento);
                } 

                else 
                {
                    System.out.println("Administrador: Evento " + evento.getId() + " es MALICIOSO, DESCARTANDO");
                }
            }
            
            System.out.println("Administrador TERMINO");
            
            // Enviar nc eventos de fin a los clasificadores
            for (int i = 0; i < numClasificadores; i++) 
            {
                buzonClasificacion.depositar(new Evento());
                System.out.println("Administrador: Evento FIN " + (i+1) + "/" + numClasificadores + " enviado a Clasificadores");
            }
            
        
    }
}

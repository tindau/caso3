import java.util.LinkedList;
import java.util.Queue;

public class Buzon {
    private Queue<Evento> cola;
    private int capacidad;
    private String nombre;
    

    public Buzon(String nombre, int capacidad) 
    {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.cola = new LinkedList<>();
    }
    

    public synchronized void depositar(Evento evento) throws InterruptedException 
    {
 
        while (capacidad > 0 && cola.size() >= capacidad)
        {
            System.out.println("[" + nombre + "] Lleno (" + cola.size() + "/" + capacidad + "), esperando espacio...");
            wait(); 
        }
        
        cola.add(evento);
        System.out.println("[" + nombre + "] Depositado: " + evento +  " (tamaño: " + cola.size() + ")");

        notifyAll();
    }
    

    public synchronized Evento retirar() throws InterruptedException 
    {

        while (cola.isEmpty()) 
        {
            System.out.println("[" + nombre + "] Vacío, esperando eventos...");
            wait(); 
        }
        
        Evento evento = cola.poll();
        System.out.println("[" + nombre + "] Retirado: " + evento + " (tamaño: " + cola.size() + ")");
        
        notifyAll();
        
        return evento;
    }

    public synchronized Evento retirarSemi() {
        try {
            return retirar();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[" + nombre + "] retirarSemi interrumpido");
            if (!cola.isEmpty()) {
                Evento evento = cola.poll();
                notifyAll();
                return evento;
            }
            return new Evento();
        }
    }

    public synchronized void depositarSemi(Evento evento) {
        try {
            depositar(evento);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[" + nombre + "] depositarSemi interrumpido");
        }
    }

    public synchronized void depositarPasiva(Evento evento) {
        depositarSemi(evento);
    }
    
    public synchronized boolean estaVacio() 
    {
        return cola.isEmpty();
    }
    
    public synchronized int getTamaño()
    {
        return cola.size();
    }
}
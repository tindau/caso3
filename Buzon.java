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
    

    public synchronized void depositar(Evento evento) 
    {
 
        while (capacidad > 0 && cola.size() >= capacidad)
        {
            System.out.println("[" + nombre + "] Lleno (" + cola.size() + "/" + capacidad + "), esperando espacio...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } 
        }
        
        cola.add(evento);
        System.out.println("[" + nombre + "] Depositado: " + evento +  " (tamano: " + cola.size() + ")");

        notifyAll();
    }
    

    public synchronized Evento retirar()  
    {

        while (cola.isEmpty()) 
        {
            System.out.println("[" + nombre + "] Vacio, esperando eventos...");
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        Evento evento = cola.poll();
        System.out.println("[" + nombre + "] Retirado: " + evento + " (tamano: " + cola.size() + ")");
        
        notifyAll();
        
        return evento;
    }

    public synchronized Evento retirarSemi() {
        while (cola.isEmpty()) {
            try {
                wait(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[" + nombre + "] retirarSemi interrumpido durante espera");
            }
            Thread.yield();
        }
        Evento e = cola.poll();
        notifyAll();
        System.out.println("[" + nombre + "] Retirado (semi): " + e);
        return e;
    }

    public synchronized void depositarSemi(Evento evento) {
        while (capacidad > 0 && cola.size() >= capacidad) {
            try {
                wait(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[" + nombre + "] depositarSemi interrumpido durante espera");
            }
            Thread.yield();
        }
        cola.add(evento);
        notifyAll();
        System.out.println("[" + nombre + "] Depositado (semi): " + evento);
    }

    public synchronized void depositarPasiva(Evento evento) {
        while(capacidad > 0 && cola.size() >= capacidad) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[" + nombre + "] depositarPasiva interrumpido durante espera");
            }
        }
        cola.add(evento);
        notifyAll();
        System.out.println("[" + nombre + "] Depositado (pasiva): " + evento);
    }
    
    public synchronized boolean estaVacio() 
    {
        return cola.isEmpty();
    }
    
    public synchronized int getTamano()
    {
        return cola.size();
    }
}

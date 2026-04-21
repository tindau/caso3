import java.io.*;
import java.util.*;


public class Main {
    public static void main(String[] args) 
    {
        try {

            Properties config = new Properties();
            try (FileInputStream fis = new FileInputStream("config.properties")) 
            {
                config.load(fis);
            }
            
            int numSensores = Integer.parseInt(config.getProperty("num_sensores"));
            int numEventosBase = Integer.parseInt(config.getProperty("num_eventos_base"));
            int numClasificadores = Integer.parseInt(config.getProperty("num_clasificadores"));
            int numServidores = Integer.parseInt(config.getProperty("num_servidores"));
            int capClasificacion = Integer.parseInt(config.getProperty("capacidad_buzon_clasificacion"));
            int capConsolidacion = Integer.parseInt(config.getProperty("capacidad_buzon_consolidacion"));
            

            int totalEventos = 0;
            for (int i = 1; i <= numSensores; i++) 
            {
                totalEventos += numEventosBase * i;
            }
            
            System.out.println("SISTEMA IoT - CONFIGURACION");
            System.out.println("Sensores: " + numSensores);
            System.out.println(" Eventos base: " + numEventosBase);
            System.out.println(" Total eventos: " + totalEventos);
            System.out.println(" Clasificadores: " + numClasificadores);
            System.out.println(" Servidores: " + numServidores);
            System.out.println(" Cap. Clasificacion: " + capClasificacion);
            System.out.println(" Cap. Consolidacion: " + capConsolidacion);
            System.out.println();
            

            Buzon buzonEntrada = new Buzon("ENTRADA", -1); 
            Buzon buzonAlertas = new Buzon("ALERTAS", -1); 
            Buzon buzonClasificacion = new Buzon("CLASIFICACION", capClasificacion);
            
            Buzon[] buzonesConsolidacion = new Buzon[numServidores];
            for (int i = 0; i < numServidores; i++) 
            {
                buzonesConsolidacion[i] = new Buzon("CONSOLIDACION-" + (i+1), capConsolidacion);
            }
            
   
            Monitor monitor = new Monitor(numClasificadores);
            
            System.out.println("Inicializando sensores.");
            List<Thread> threadsSensores = new ArrayList<>();
            for (int i = 1; i <= numSensores; i++) 
            {
                int numEventos = numEventosBase * i;
                Sensor sensor = new Sensor(i, numEventos, numServidores,  buzonEntrada);
                Thread thread = new Thread(sensor, "Sensor-" + i);
                threadsSensores.add(thread);
                thread.start();
            }
            

            System.out.println("Inicializando broker.");
            Broker broker = new Broker( totalEventos, buzonEntrada, buzonAlertas, buzonClasificacion);
            Thread threadBroker = new Thread(broker, "Broker");
            threadBroker.start();
            

            System.out.println("Inicializando broker.");
            Administrador admin = new Administrador(buzonAlertas, buzonClasificacion, numClasificadores);
            Thread threadAdmin = new Thread(admin, "Administrador");
            threadAdmin.start();
            
            
            System.out.println("Inicializando clasificadores");
            List<Thread> threadsClasificadores = new ArrayList<>();

            for (int i = 1; i <= numClasificadores; i++) 
            {
                Clasificador clasificador = new Clasificador(Integer.toString(i), buzonClasificacion, buzonesConsolidacion, monitor);
                Thread thread = new Thread(clasificador, "Clasificador-" + i);
                threadsClasificadores.add(thread);
                thread.start();
            }
            

            System.out.println("Inicializando servidores");
            List<Thread> threadsServidores = new ArrayList<>();
            for (int i = 1; i <= numServidores; i++) 
            {
                Servidor servidor = new Servidor(i, buzonesConsolidacion[i-1]);
                Thread thread = new Thread(servidor, "Servidor-" + i);
                threadsServidores.add(thread);
                thread.start();
            }
            
            System.out.println();
            System.out.println("Sistema en ejecucion.");
            System.out.println();
            
  
            for (Thread t : threadsSensores) 
            {
                t.join();
            }
            System.out.println("Todos los sensores han terminado");
            

            threadBroker.join();
            System.out.println("Broker ha terminado");
            

            threadAdmin.join();
            System.out.println("Administrador ha terminado");
            

            for (Thread t : threadsClasificadores) 
            {
                t.join();
            }
            System.out.println("Todos los clasificadores han terminado");


            for (Thread t : threadsServidores)
            {
                t.join();
            }
            System.out.println("Todos los servidores han terminado");
            

            System.out.println();
            System.out.println("Verificacion final.");
            System.out.println();


            if (buzonEntrada.estaVacio())
            {
                System.out.println("Buzon de entrada vacio.");
            }
            else
            {
                System.out.println("Buzon de entrada no-vacio.");
            }

            if (buzonAlertas.estaVacio())
            {
                System.out.println("Buzon de Alertas vacio.");
            }
            else
            {
                System.out.println("Buzon de Alertas no-vacio.");
            }

            if (buzonClasificacion.estaVacio())
            {
                System.out.println("Buzon de clasificacion vacio.");
            }
            else
            {
                System.out.println("Buzon de clasificacion no-vacio.");
            }


            for (int i = 0; i < buzonesConsolidacion.length; i++) 
            {
                if (buzonesConsolidacion[i].estaVacio())
                {
                    System.out.println("Buzon de Consolidacion No." + (i+1) + "Vacio");
                } 
                else
                {
                    System.out.println("Buzon de Consolidacion No." + (i+1) + "No-Vacio");
                }
            }
            

            System.out.println();
            System.out.println("Sistema finalizado");
            System.out.println();
            
        } 
        
        catch (IOException e) 
        {
            System.err.println("Error al cargar configuracion.");
        } 

        catch (InterruptedException e) 
        {
            System.err.println("Sistema interrumpido");
            Thread.currentThread().interrupt();
        }

        catch (Exception e) 
        {
            System.err.println("Error inesperado.");
        }
    }
}
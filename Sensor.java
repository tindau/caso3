import java.util.Random;

public class Sensor extends Thread {
    private int id;
    private int numEventos;
    private int ns;
    private Buzon buzonEventos;
    private Random random;

    public Sensor(int id, int base, int ns, Buzon buzonEventos) 
    {
        this.id = id;
        this.numEventos = base * id;
        this.ns = ns;
        this.buzonEventos = buzonEventos;
        this.random = new Random();
        setName("Sensor-" + id);
    }

    @Override
    public void run() {
        System.out.println("Sensor " + id + " iniciado (" + numEventos + " eventos)");
        for (int seq = 1; seq <= numEventos; seq++) 
        {
            int tipoServidor = random.nextInt(ns) + 1;
            Evento e = new Evento("S: " + id + ", seq: " + seq, tipoServidor);

            buzonEventos.depositar(e);
            

            Thread.yield();
        }
        System.out.println("Sensor " + id + " terminado");
    }
}

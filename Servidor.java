import java.util.Random;

public class Servidor extends Thread {
    private int id;
    private Buzon buzonServidores;
    private Random random;

    public Servidor(int id, Buzon buzonServidores) {
        this.id = id;
        this.buzonServidores = buzonServidores;
        this.random = new Random();
        setName("Servidor" + id);
    }

    @Override
    public void run() {
        System.out.println("Servidor " + id + " iniciado");
        while (true) {
            Evento e = buzonServidores.retirar();
            if (e.esFin()) {
                break;
            }
            procesarEvento(e);
        }
        System.out.println("Servidor " + id + " terminado");
    }

    private void procesarEvento(Evento e) {
        int ms = random.nextInt(901) + 100;
        System.out.println("Servidor " + id + " procesando " + e + " (" + ms + " ms)");
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    public long getId() {
        return id;
    }
}

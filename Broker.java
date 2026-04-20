import java.util.Random;

public class Broker extends Thread {
    private int totEventos;
    private Buzon buzonEventos;
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;
    private Random random;
    private int normales = 0;
    private int anomalos = 0;

    public Broker(int totEventos, Buzon buzonEntrada, Buzon buzonAlertas, Buzon buzonClasificacion) {
        this.totEventos = totEventos;
        this.buzonAlertas = buzonAlertas;
        this.buzonClasificacion = buzonClasificacion;
        this.buzonEventos = buzonEntrada;
        this.random = new Random();
    }

    @Override
    public void run() {
        System.out.println("Broker iniciado");
        int procesados = 0;
        while (procesados < totEventos) {
            Evento e = buzonEventos.retirarSemi();
            if (random.nextInt(201) % 8 == 0) {
                anomalos++;
                buzonAlertas.depositarSemi(e);
            } else {
                normales++;
                buzonClasificacion.depositarSemi(e);
            }
            procesados++;
        }
        buzonAlertas.depositarSemi(new Evento());
        System.out.println("Broker termino, normales: " + normales + ", anomalos: " + anomalos);
    }

    public int getNormales() {
        return normales;
    }

    public int getAnomalos() {
        return anomalos;
    }
}

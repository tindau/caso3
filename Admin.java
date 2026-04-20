import java.util.Random;

public class Admin extends Thread {
    private Buzon buzonAlertas;
    private Buzon buzonClasificacion;
    private int numClasificadores;
    private Random random;
    private int administrados = 0;

    public Admin(Buzon alertas, Buzon clasificacion, int numClasificadores) 
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
            Evento e = buzonAlertas.retirarSemi();
            if (e.esFin()) {
                System.out.println("Administrador recibio fin");
                break;
            }
            if (random.nextInt(21) % 4 == 0) {
                System.out.println("Administrador: evento " + e.getId() + " es inofensivo");
                buzonClasificacion.depositarSemi(e);
                administrados++;
            } else {
                System.out.println("Administrador: evento " + e.getId() + " es malisioso, por ende descartado");
            }
        }
        for (int i = 0; i < numClasificadores; i++) {
            buzonClasificacion.depositarSemi(new Evento());
            System.out.println("Administrador: evento fin " + (i + 1) + "/" + numClasificadores);
        }
        System.out.println("Administrador terminado");
    }

    public int totalAnomaliasAdministradas() {
        return administrados;
    }
}

public class Clasificador extends Thread {
    private Buzon buzonClasificacion;
    private Buzon[] buzonServidores;
    private Monitor monitor;

    public Clasificador(String nombre, Buzon buzonClasificacion, Buzon[] buzonConsolidacion, Monitor monitor) {
        this.buzonClasificacion = buzonClasificacion;
        this.buzonServidores = buzonConsolidacion;
        this.monitor = monitor;
        setName(nombre);
    }

    @Override
    public void run() {
        System.out.println(getName() + " iniciado");
        while (true) {
            Evento e = buzonClasificacion.retirarSemi();
            if (e.esFin()) {
                break;
            }
            int tipo = e.getTipoServidor() - 1;
            buzonServidores[tipo].depositarPasiva(e);
        }
        if (monitor.reportarTerminacion()) {
            enviarFinServidores();
        }
        System.out.println(getName() + " terminado");
    }

    private void enviarFinServidores() {
        for (Buzon b: buzonServidores) {
            b.depositarPasiva(new Evento());
        }
        System.out.println(getName() + ": evento fin enviado a todos los servidores");
    }
}

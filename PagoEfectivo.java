public class PagoEfectivo implements MetodoPago {
    private double recibido;
    private double cambio;

    public PagoEfectivo(double recibido) {
        this.recibido = recibido;
    }

    @Override
    public boolean validarPago(double total) {
        if (total <= 0) {
            return false;
        }
        return recibido >= total;
    }

    @Override
    public boolean procesarPago(double total) {
        boolean valido = validarPago(total);
        if (!valido) {
            return false;
        }
        cambio = redondear(recibido - total);
        return true;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    @Override
    public String getNombre() { return "Efectivo"; }
    public double getRecibido() { return recibido; }
    public double getCambio() { return cambio; }
}

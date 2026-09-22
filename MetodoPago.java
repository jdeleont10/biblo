public interface MetodoPago {
    String getNombre();
    boolean validarPago(double total);
    boolean procesarPago(double total);
}

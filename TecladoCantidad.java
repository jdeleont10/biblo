public class TecladoCantidad {
    private static final int CANTIDAD_MAXIMA = 100;
    private String entrada = "";

    public boolean agregarDigito(int digito) {
        if (digito < 0 || digito > 9) {
            throw new IllegalArgumentException("El valor debe ser un dígito entre 0 y 9.");
        }
        if (entrada.isEmpty() && digito == 0) {
            return false;
        }
        if (entrada.length() >= 2) {
            return false;
        }
        String candidato = entrada + digito;
        int valor = Integer.parseInt(candidato);
        if (valor > CANTIDAD_MAXIMA) {
            return false;
        }
        entrada = candidato;
        return true;
    }

    public int borrarDigito() {
        if (!entrada.isEmpty()) {
            entrada = entrada.substring(0, entrada.length() - 1);
        }
        return getCantidad();
    }

    public int limpiar() {
        entrada = "";
        return getCantidad();
    }

    public int getCantidad() {
        if (entrada.isEmpty()) {
            return 1;
        }
        return Integer.parseInt(entrada);
    }
}

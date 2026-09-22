public class PagoTarjeta implements MetodoPago {
    private String titular;
    private String numeroTarjeta;
    private boolean autorizada;

    public PagoTarjeta(String titular, String numeroTarjeta) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("Ingrese el nombre del titular.");
        }
        if (!esNumeroValido(numeroTarjeta)) {
            throw new IllegalArgumentException("Ingrese 16 dígitos de prueba.");
        }
        this.titular = titular.trim();
        this.numeroTarjeta = numeroTarjeta.replace(" ", "");
    }

    private boolean esNumeroValido(String numero) {
        if (numero == null) {
            return false;
        }
        String limpio = numero.replace(" ", "");
        if (limpio.length() != 16) {
            return false;
        }
        for (int i = 0; i < limpio.length(); i++) {
            if (!Character.isDigit(limpio.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validarPago(double total) {
        return total > 0;
    }

    @Override
    public boolean procesarPago(double total) {
        autorizada = validarPago(total);
        return autorizada;
    }

    public int calcularCuotasSugeridas(double total, double maximoPorCuota) {
        int cuotas = (int) (total / maximoPorCuota);
        if (total % maximoPorCuota != 0) {
            cuotas = cuotas + 1;
        }
        if (cuotas < 1) {
            cuotas = 1;
        }
        return cuotas;
    }

    public String getUltimosCuatro() {
        return numeroTarjeta.substring(12);
    }

    @Override
    public String getNombre() { return "Tarjeta ****" + getUltimosCuatro(); }
    public boolean isAutorizada() { return autorizada; }
}

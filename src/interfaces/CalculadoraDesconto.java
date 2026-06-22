package interfaces;

public interface CalculadoraDesconto {

    double getPrecoBase();

    int getPercentualDesconto();

    default double calcularDesconto(double valor) {
        double percentual = getPercentualDesconto();
        return valor - (valor * percentual / 100.0);
    }
}

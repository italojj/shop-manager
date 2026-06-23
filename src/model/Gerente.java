package model;
import java.util.List;

public class Gerente extends Funcionario {
    private double bonusMensal;

    public double getBonusMensal() {
        return bonusMensal;
    }

    public void setBonusMensal(double bonusMensal) {
        this.bonusMensal = bonusMensal;
    }

    public double calcularBonificacao() {
        return this.bonusMensal;
    }

    @Override
    public double calcularRemuneracao(List<Venda> vendas) {
        return getSalarioBase() + calcularBonificacao();
    }
}
import java.util.List;

public class Gerente extends Funcionario {
    private float bonusMensal;
    
    public float getBonusMensal() {
        return bonusMensal;
    }

    public void setBonusMensal(float bonusMensal) {
        this.bonusMensal = bonusMensal;
    }

    public float calcularBonificacao() {
        return this.bonusMensal;
    }

    public float calcularRemuneracao(List<Venda> vendas) {
        return getSalarioBase() + calcularBonificacao();
    }
}
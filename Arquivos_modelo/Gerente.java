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
        // A lógica do bônus vai aqui dentro depois
        return this.bonusMensal;
    }

    public float calcularRemuneracao(List<Venda> vendas) {
        // Gerente recebe Salário Base + Bônus Fixo
        return getSalarioBase() + calcularBonificacao();
    }
}
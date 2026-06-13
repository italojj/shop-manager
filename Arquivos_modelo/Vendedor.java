import java.util.List;

public class Vendedor extends Funcionario {
    private float comissaoPorVenda;
    
    public float getComissaoPorVenda() {
        return comissaoPorVenda;
    }

    public void setComissaoPorVenda(float comissaoPorVenda) {
        this.comissaoPorVenda = comissaoPorVenda;
    }

    public float calcularComissao() {
        // A lógica da fórmula vai aqui dentro depois
        return 0.0f; 
    }

    public float calcularRemuneracao(List<Venda> vendas) {
        // A lógica de somar o salário base + comissões vai aqui depois
        return getSalarioBase() + calcularComissao();
    }
}
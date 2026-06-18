package model;

import java.util.List;

public class Vendedor extends Funcionario {
    
    // comissaoPorVenda funciona como a taxa/porcentagem (ex: 0.05 para 5%)
    private float comissaoPorVenda;
    
    public float getComissaoPorVenda() {
        return comissaoPorVenda;
    }

    public void setComissaoPorVenda(float comissaoPorVenda) {
        this.comissaoPorVenda = comissaoPorVenda;
    }

    public float calcularComissao() {
        return this.comissaoPorVenda; 
    }

    @Override
    public float calcularRemuneracao(List<Venda> vendas) {
        // Se não houver vendas, o vendedor recebe apenas o salário base fixo
        if (vendas == null || vendas.isEmpty()) {
            return getSalarioBase();
        }

        float totalVendas = 0.0f;
        
        // Loop para somar o valor de todas as vendas da lista
        for (Venda venda : vendas) {
            // Nota: Certifique-se com o colega que está fazendo a classe Venda 
            // se o método dele realmente se chama getValorTotal()
            totalVendas += venda.getValorTotal();
        }

        // Calcula o valor da comissão (Total vendido multiplicado pela taxa de comissão)
        float valorComissaoFinal = totalVendas * this.comissaoPorVenda;

        // Retorna o salário base somado à comissão real obtida
        return getSalarioBase() + valorComissaoFinal;
    }
}
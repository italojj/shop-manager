package model;

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
        return this.comissaoPorVenda; 
    }

    @Override
    public float calcularRemuneracao(List<Venda> vendas) {
        if (vendas == null || vendas.isEmpty()) {
            return getSalarioBase();
        }

        float totalVendas = 0.0f;
        
        for (Venda venda : vendas) {
            totalVendas += venda.getValorTotal();
        }

        float valorComissaoFinal = totalVendas * this.comissaoPorVenda;

        
        return getSalarioBase() + valorComissaoFinal;
    }
}
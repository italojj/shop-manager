package model;

import java.util.List;

public class Vendedor extends Funcionario {
    
    private double comissaoPorVenda;

    public double getComissaoPorVenda() {
        return comissaoPorVenda;
    }

    public void setComissaoPorVenda(double comissaoPorVenda) {
        this.comissaoPorVenda = comissaoPorVenda;
    }

    public double calcularComissao() {
        return this.comissaoPorVenda;
    }

    @Override
    public double calcularRemuneracao(List<Venda> vendas) {
        if (vendas == null || vendas.isEmpty()) {
            return getSalarioBase();
        }

        double totalVendas = 0.0;

        for (Venda venda : vendas) {
            totalVendas += venda.getValorTotal();
        }

        double valorComissaoFinal = totalVendas * this.comissaoPorVenda;

        return getSalarioBase() + valorComissaoFinal;
    }
}
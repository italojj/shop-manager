package model;

import interfaces.CalculadoraDesconto;

import java.time.LocalDate;

public class ProdutoAlimenticio extends Produto implements CalculadoraDesconto {

    private LocalDate dataValidade;
    private String categoria;

    public ProdutoAlimenticio(int id, String nome, double preco, int quantidadeEstoque,
                               Fornecedor fornecedor, LocalDate dataValidade, String categoria) {
        super(id, nome, preco, quantidadeEstoque, fornecedor);
        this.dataValidade = dataValidade;
        this.categoria = categoria;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean estaVencido() {
        return dataValidade.isBefore(LocalDate.now());
    }

    @Override
    public double calcularDesconto(double valorOriginal, int quantidade) {
        return valorOriginal;
    }

    @Override
    public String getDescricao() {
        return String.format("Alimentício | Categoria: %s | Validade: %s%s",
                categoria,
                dataValidade,
                estaVencido() ? " [VENCIDO]" : "");
    }

    @Override
    public String toString() {
        return super.toString() + " | " + getDescricao();
    }
}

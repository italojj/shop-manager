package model;

import interfaces.CalculadoraDesconto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProdutoAlimenticio extends Produto implements CalculadoraDesconto {

    private static final int TETO_DESCONTO = 30;

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

    public long getDiasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
    }

    @Override
    public double getPrecoBase() {
        return getPreco();
    }

    @Override
    public int getPercentualDesconto() {
        long dias = getDiasParaVencer();
        int percentual;

        if (dias <= 3) {
            percentual = 25;
        } else if (dias <= 7) {
            percentual = 15;
        } else if (dias <= 15) {
            percentual = 8;
        } else {
            percentual = 0;
        }

        return Math.min(percentual, TETO_DESCONTO);
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

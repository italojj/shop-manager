package model;

import interfaces.CalculadoraDesconto;

public class ProdutoEletronico extends Produto implements CalculadoraDesconto {

    private static final int TETO_DESCONTO = 10;

    private String marca;
    private int garantiaMeses;

    public ProdutoEletronico(int id, String nome, double preco, int quantidadeEstoque,
                              Fornecedor fornecedor, String marca, int garantiaMeses) {
        super(id, nome, preco, quantidadeEstoque, fornecedor);
        this.marca = marca;
        this.garantiaMeses = garantiaMeses;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    public void setGarantiaMeses(int garantiaMeses) {
        this.garantiaMeses = garantiaMeses;
    }

    @Override
    public double getPrecoBase() {
        return getPreco();
    }

    @Override
    public int getPercentualDesconto() {
        int quantidade = getQuantidadeAtual();
        int percentual;

        if (quantidade >= 6) {
            percentual = 10;
        } else if (quantidade >= 3) {
            percentual = 5;
        } else {
            percentual = 0;
        }

        return Math.min(percentual, TETO_DESCONTO);
    }

    @Override
    public String getDescricao() {
        return String.format("Eletrônico | Marca: %s | Garantia: %d meses", marca, garantiaMeses);
    }

    @Override
    public String toString() {
        return super.toString() + " | " + getDescricao();
    }
}

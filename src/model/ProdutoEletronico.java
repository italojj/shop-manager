package model;

import interfaces.CalculadoraDesconto;

public class ProdutoEletronico extends Produto implements CalculadoraDesconto {

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
    public double calcularDesconto(double valorOriginal, int quantidade) {
        if (quantidade >= 6) {
            return valorOriginal * 0.90;
        } else if (quantidade >= 3) {
            return valorOriginal * 0.95;
        }
        return valorOriginal;
    }

    public int percentualDesconto(int quantidade) {
        if (quantidade >= 6) return 10;
        if (quantidade >= 3) return 5;
        return 0;
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

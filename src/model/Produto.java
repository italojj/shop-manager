package model;

public abstract class Produto {

    private int id;
    private String nome;
    private double preco;
    private int quantidadeEstoque;
    private int quantidadeAtual;
    private Fornecedor fornecedor;

    public Produto(int id, String nome, double preco, int quantidadeEstoque, Fornecedor fornecedor) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Quantidade em estoque não pode ser negativa.");
        }
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.fornecedor = fornecedor;
        this.quantidadeAtual = 0;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        if (quantidadeEstoque < 0) {
            throw new IllegalArgumentException("Estoque não pode ser negativo.");
        }
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        if (quantidadeAtual < 0) {
            throw new IllegalArgumentException("Quantidade atual não pode ser negativa.");
        }
        this.quantidadeAtual = quantidadeAtual;
    }

    public boolean temEstoqueSuficiente(int quantidade) {
        return this.quantidadeEstoque >= quantidade;
    }

    public void baixarEstoque(int quantidade) {
        if (quantidade > this.quantidadeEstoque) {
            throw new IllegalStateException("Estoque insuficiente para baixa.");
        }
        this.quantidadeEstoque -= quantidade;
    }

    public abstract String getDescricao();

    @Override
    public String toString() {
        return String.format("[%d] %s – R$ %.2f (estoque: %d)", id, nome, preco, quantidadeEstoque);
    }
}

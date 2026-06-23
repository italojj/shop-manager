package model;

import java.util.List;

public abstract class Funcionario extends Pessoa {
    private int idFuncionario;
    private double salarioBase;

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public abstract double calcularRemuneracao(List<Venda> vendas);
}
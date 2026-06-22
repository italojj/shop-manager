package model;

import java.util.List;

public abstract class Funcionario extends Pessoa {
    private int idFuncionario;
    private float salarioBase;

    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public float getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(float salarioBase) {
        this.salarioBase = salarioBase;
    }

    public abstract float calcularRemuneracao(List<Venda> vendas);
}
package menu;

import enums.StatusVenda;
import exception.CpfDuplicadoException;
import exception.EntidadeNaoEncontradaException;
import exception.EstoqueInsuficienteException;
import exception.ProdutoVencidoException;
import exception.TransicaoEstadoInvalidaException;
import model.*;
import repository.GerenciadorDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final GerenciadorDados<Cliente> clientes;
    private final GerenciadorDados<Funcionario> funcionarios;
    private final GerenciadorDados<Produto> produtos;
    private final GerenciadorDados<Fornecedor> fornecedores;
    private final GerenciadorDados<Venda> vendas;
    // Lê a entrada usando o MESMO charset do console (System.out.charset()),
    // assim acentos digitados pelo usuário não viram "?" (Cp1252/Cp850 x UTF-8).
    private final Scanner sc = new Scanner(System.in, System.out.charset());

    // Formato de data brasileiro (DD-MM-AAAA) para entrada e exibição no menu.
    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public MenuPrincipal(GerenciadorDados<Cliente> clientes,
            GerenciadorDados<Funcionario> funcionarios,
            GerenciadorDados<Produto> produtos,
            GerenciadorDados<Fornecedor> fornecedores,
            GerenciadorDados<Venda> vendas) {
        this.clientes = clientes;
        this.funcionarios = funcionarios;
        this.produtos = produtos;
        this.fornecedores = fornecedores;
        this.vendas = vendas;
    }

    public void iniciar() {
        int opcao;
        do {
            limparTela();
            System.out.println("    Menu Principal     ");
            System.out.println("1 - Clientes");
            System.out.println("2 - Funcionários");
            System.out.println("3 - Fornecedores");
            System.out.println("4 - Produtos");
            System.out.println("5 - Vendas");
            System.out.println("6 - Remuneração de funcionário");
            System.out.println("0 - Sair");
            opcao = lerInt("Escolha: ");
            switch (opcao) {
                case 1 -> menuClientes();
                case 2 -> menuFuncionarios();
                case 3 -> menuFornecedores();
                case 4 -> menuProdutos();
                case 5 -> menuVendas();
                case 6 -> calcularRemuneracao();
                case 0 -> System.out.println("Programa encerrado.");
                default -> System.out.println("Opção inválida.");
            }
            if (opcao != 0) {
                pausar();
            }
        } while (opcao != 0);
    }

    private void menuClientes() {
        limparTela();
        System.out.println("Clientes:");
        System.out.println("1 - Cadastrar | 2 - Listar | 3 - Editar | 4 - Remover | 0 - Voltar");
        switch (lerInt("Escolha: ")) {
            case 1 -> {
                String nome = lerNome("Nome: ");
                String cpf = lerCpf("CPF: ");
                try {
                    validarCpfUnico(cpf);
                } catch (CpfDuplicadoException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                Cliente c = new Cliente();
                c.setNome(nome);
                c.setCpf(cpf);
                c.setIdCliente(proximoIdCliente());
                c.setDataCadastro(LocalDate.now());
                clientes.adicionar(c);
                System.out.println("Cliente cadastrado com id " + c.getIdCliente() + ".");
            }
            case 2 -> {
                if (clientes.listarTodos().isEmpty())
                    System.out.println("(nenhum cliente)");
                for (Cliente c : clientes.listarTodos())
                    System.out.printf("[%d] %s - CPF %s%n", c.getIdCliente(), c.getNome(), formatarCpf(c.getCpf()));
            }
            case 3 -> {
                Cliente c = selecionarCliente();
                if (c == null)
                    return;
                String novoNome = lerTexto("Novo nome'" + c.getNome() + "': ");
                if (!novoNome.isEmpty())
                    c.setNome(novoNome);
                try {
                    clientes.atualizar(c);
                    System.out.println("Atualizado.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> {
                int id = lerInt("ID que será removido: ");
                try {
                    clientes.remover(id);
                    System.out.println("Removido.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> {
            }
        }
    }

    private void menuFuncionarios() {
        limparTela();
        System.out.println("Funcionários:");
        System.out.println("1 - Cadastrar | 2 - Listar | 3 - Editar | 4 - Remover | 0 - Voltar");
        switch (lerInt("Escolha: ")) {
            case 1 -> {
                int tipo = lerInt("1 - Vendedor | 2 - Gerente: ");
                if (tipo != 1 && tipo != 2) {
                    System.out.println("Tipo inválido. Escolha 1 ou 2. Cadastro cancelado.");
                    return;
                }
                String nome = lerNome("Nome: ");
                String cpf = lerCpf("CPF: ");
                try {
                    validarCpfUnico(cpf);
                } catch (CpfDuplicadoException e) {
                    System.out.println(e.getMessage());
                    return;
                }
                double salario = lerDouble("Salário base: ");
                Funcionario f;
                if (tipo == 1) {
                    Vendedor v = new Vendedor();
                    v.setComissaoPorVenda(lerDouble("Comissão por venda (ex: 0.05 = 5%): "));
                    f = v;
                } else {
                    Gerente g = new Gerente();
                    g.setBonusMensal(lerDouble("Bônus mensal: "));
                    f = g;
                }
                f.setNome(nome);
                f.setCpf(cpf);
                f.setSalarioBase(salario);
                f.setIdFuncionario(proximoIdFuncionario());
                funcionarios.adicionar(f);
                System.out.println("Funcionário cadastrado com id " + f.getIdFuncionario() + ".");
            }
            case 2 -> {
                if (funcionarios.listarTodos().isEmpty())
                    System.out.println("(nenhum funcionário)");
                for (Funcionario f : funcionarios.listarTodos()) {
                    String papel = (f instanceof Vendedor) ? "Vendedor" : "Gerente";
                    System.out.printf("[%d] %s (%s) - CPF %s%n",
                            f.getIdFuncionario(), f.getNome(), papel, formatarCpf(f.getCpf()));
                }
            }
            case 3 -> {
                Funcionario f = selecionarFuncionario();
                if (f == null)
                    return;
                String novoNome = lerTexto("Novo nome '" + f.getNome() + "': ");
                if (!novoNome.isEmpty())
                    f.setNome(novoNome);
                f.setSalarioBase(lerDouble("Novo salário base: "));
                if (f instanceof Vendedor v) {
                    v.setComissaoPorVenda(lerDouble("Nova comissão por venda: "));
                } else if (f instanceof Gerente g) {
                    g.setBonusMensal(lerDouble("Novo bônus mensal: "));
                }
                try {
                    funcionarios.atualizar(f);
                    System.out.println("Atualizado.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> {
                int id = lerInt("ID a remover: ");
                try {
                    funcionarios.remover(id);
                    System.out.println("Removido.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> {
            }
        }
    }

    private void menuFornecedores() {
        limparTela();
        System.out.println("Fornecedores:");
        System.out.println("1 - Cadastrar | 2 - Listar | 3 - Editar | 4 - Remover | 0 - Voltar");
        switch (lerInt("Escolha: ")) {
            case 1 -> {
                int id = proximoIdFornecedor();
                String nome = lerTexto("Nome: ");
                String cnpj = lerTexto("CNPJ: ");
                String tel = lerTexto("Telefone: ");
                String email = lerTexto("E-mail: ");
                fornecedores.adicionar(new Fornecedor(id, nome, cnpj, tel, email));
                System.out.println("Fornecedor cadastrado com id " + id + ".");
            }
            case 2 -> {
                if (fornecedores.listarTodos().isEmpty())
                    System.out.println("(nenhum fornecedor)");
                for (Fornecedor f : fornecedores.listarTodos())
                    System.out.println(f);
            }
            case 3 -> {
                Fornecedor f = selecionarFornecedor();
                if (f == null)
                    return;
                String nome = lerTexto("Novo nome '" + f.getNome() + "': ");
                if (!nome.isEmpty())
                    f.setNome(nome);
                String cnpj = lerTexto("Novo CNPJ '" + f.getCnpj() + "': ");
                if (!cnpj.isEmpty())
                    f.setCnpj(cnpj);
                String tel = lerTexto("Novo telefone '" + f.getTelefone() + "': ");
                if (!tel.isEmpty())
                    f.setTelefone(tel);
                String email = lerTexto("Novo e-mail '" + f.getEmail() + "': ");
                if (!email.isEmpty())
                    f.setEmail(email);
                try {
                    fornecedores.atualizar(f);
                    System.out.println("Atualizado.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> {
                int id = lerInt("ID a remover: ");
                try {
                    fornecedores.remover(id);
                    System.out.println("Removido.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> {
            }
        }
    }

    private void menuProdutos() {
        limparTela();
        System.out.println("Produtos:");
        System.out.println("1 - Cadastrar | 2 - Listar | 3 - Editar | 4 - Remover | 0 - Voltar");
        switch (lerInt("Escolha: ")) {
            case 1 -> {
                int tipo = lerInt("1 - Eletrônico | 2 - Alimentício: ");
                if (tipo != 1 && tipo != 2) {
                    System.out.println("Tipo inválido. Escolha 1 ou 2. Cadastro cancelado.");
                    return;
                }
                String nome = lerTexto("Nome: ");
                double preco = lerDouble("Preco: ");
                int estoque = lerInt("Quantidade em estoque: ");
                Fornecedor forn = selecionarFornecedor();
                int id = proximoIdProduto();
                if (tipo == 1) {
                    String marca = lerTexto("Marca: ");
                    int garantia = lerInt("Garantia (meses): ");
                    produtos.adicionar(new ProdutoEletronico(id, nome, preco, estoque, forn, marca, garantia));
                } else {
                    LocalDate validade = lerData("Validade (DD-MM-AAAA): ");
                    String categoria = lerTexto("Categoria: ");
                    produtos.adicionar(new ProdutoAlimenticio(id, nome, preco, estoque, forn, validade, categoria));
                }
                System.out.println("Produto cadastrado com id " + id + ".");
            }
            case 2 -> {
                if (produtos.listarTodos().isEmpty())
                    System.out.println("(nenhum produto)");
                for (Produto p : produtos.listarTodos())
                    System.out.println(p);
            }
            case 3 -> {
                Produto p = selecionarProduto();
                if (p == null)
                    return;
                String nome = lerTexto("Novo nome '" + p.getNome() + "': ");
                if (!nome.isEmpty())
                    p.setNome(nome);
                p.setPreco(lerDouble("Novo preço: "));
                p.setQuantidadeEstoque(lerInt("Nova quantidade em estoque: "));
                try {
                    produtos.atualizar(p);
                    System.out.println("Atualizado.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 4 -> {
                int id = lerInt("ID a remover: ");
                try {
                    produtos.remover(id);
                    System.out.println("Removido.");
                } catch (EntidadeNaoEncontradaException e) {
                    System.out.println(e.getMessage());
                }
            }
            default -> {
            }
        }
    }

    private void menuVendas() {
        limparTela();
        System.out.println("Vendas:");
        System.out.println("1 - Nova venda | 2 - Listar | 3 - Finalizar | 4 - Cancelar | 0 - Voltar");
        switch (lerInt("Escolha: ")) {
            case 1 -> novaVenda();
            case 2 -> listarVendas();
            case 3 -> mudarEstadoVenda(true);
            case 4 -> mudarEstadoVenda(false);
            default -> {
            }
        }
    }

    private void novaVenda() {
        if (clientes.listarTodos().isEmpty() || funcionarios.listarTodos().isEmpty()
                || produtos.listarTodos().isEmpty()) {
            System.out.println("Cadastre ao menos 1 cliente, 1 funcionário e 1 produto antes.");
            return;
        }
        Cliente c = selecionarCliente();
        if (c == null)
            return;
        Funcionario f = selecionarFuncionario();
        if (f == null)
            return;

        Venda venda = new Venda(proximoIdVenda(), c, f);
        boolean continuar = true;
        while (continuar) {
            Produto p = selecionarProduto();
            if (p != null) {
                int qtd = lerInt("Quantidade: ");
                try {
                    venda.adicionarItem(new ItemVenda(p, qtd));
                    System.out.println("Item adicionado.");
                } catch (EstoqueInsuficienteException | ProdutoVencidoException e) {
                    System.out.println("Não adicionado: " + e.getMessage());
                }
            }
            continuar = lerInt("Adicionar outro item? (1 = Sim, 0 = Não): ") == 1;
        }

        if (venda.getItens().isEmpty()) {
            System.out.println("Venda não contém itens, descartada.");
            return;
        }
        vendas.adicionar(venda);
        System.out.printf("Venda %d criada (ABERTA). Total atual: R$ %.2f%n",
                venda.getIdVenda(), venda.getValorTotal());
    }

    private void listarVendas() {
        if (vendas.listarTodos().isEmpty()) {
            System.out.println("(nenhuma venda)");
            return;
        }
        for (Venda v : vendas.listarTodos()) {
            System.out.printf("%nVenda %d | %s | Cliente: %s | Func: %s | Total: R$ %.2f%n",
                    v.getIdVenda(), v.getStatus(), v.getCliente().getNome(),
                    v.getFuncionario().getNome(), v.getValorTotal());
            for (ItemVenda i : v.getItens())
                System.out.println("   " + i);
        }
    }

    private void mudarEstadoVenda(boolean finalizar) {
        Venda v = selecionarVenda();
        if (v == null)
            return;
        try {
            if (finalizar) {
                v.finalizar();
                System.out.println("Venda FINALIZADA, estoque atualizado.");
            } else {
                v.cancelar();
                System.out.println("Venda CANCELADA.");
            }
            vendas.salvar();
            produtos.salvar();
        } catch (TransicaoEstadoInvalidaException | EstoqueInsuficienteException e) {
            System.out.println(e.getMessage());
        }
    }

    private void calcularRemuneracao() {
        limparTela();
        System.out.println("Remuneração de funcionário:");
        Funcionario f = selecionarFuncionario();
        if (f == null)
            return;
        List<Venda> doFuncionario = new ArrayList<>();
        for (Venda v : vendas.listarTodos()) {
            if (v.getStatus() == StatusVenda.FINALIZADA
                    && v.getFuncionario() != null
                    && v.getFuncionario().getIdFuncionario() == f.getIdFuncionario()) {
                doFuncionario.add(v);
            }
        }
        double r = f.calcularRemuneracao(doFuncionario);
        System.out.printf("Remuneração de %s: R$ %.2f%n", f.getNome(), r);
    }

    private Cliente selecionarCliente() {
        for (Cliente c : clientes.listarTodos())
            System.out.printf("[%d] %s%n", c.getIdCliente(), c.getNome());
        try {
            return clientes.buscarPorId(lerInt("ID do cliente: "));
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Funcionario selecionarFuncionario() {
        for (Funcionario f : funcionarios.listarTodos())
            System.out.printf("[%d] %s%n", f.getIdFuncionario(), f.getNome());
        try {
            return funcionarios.buscarPorId(lerInt("ID do funcionário: "));
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Produto selecionarProduto() {
        for (Produto p : produtos.listarTodos())
            System.out.println(p);
        try {
            return produtos.buscarPorId(lerInt("ID do produto: "));
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Fornecedor selecionarFornecedor() {
        if (fornecedores.listarTodos().isEmpty()) {
            System.out.println("(Sem fornecedores - O produto não terá fornecedor)");
            return null;
        }
        for (Fornecedor f : fornecedores.listarTodos())
            System.out.println(f);
        int id = lerInt("ID do fornecedor (0 = nenhum): ");
        if (id == 0)
            return null;
        try {
            return fornecedores.buscarPorId(id);
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private Venda selecionarVenda() {
        for (Venda v : vendas.listarTodos())
            System.out.printf("[%d] %s - R$ %.2f%n", v.getIdVenda(), v.getStatus(), v.getValorTotal());
        try {
            return vendas.buscarPorId(lerInt("ID da venda: "));
        } catch (EntidadeNaoEncontradaException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private int proximoIdCliente() {
        int max = 0;
        for (Cliente c : clientes.listarTodos())
            max = Math.max(max, c.getIdCliente());
        return max + 1;
    }

    private int proximoIdFuncionario() {
        int max = 0;
        for (Funcionario f : funcionarios.listarTodos())
            max = Math.max(max, f.getIdFuncionario());
        return max + 1;
    }

    private int proximoIdProduto() {
        int max = 0;
        for (Produto p : produtos.listarTodos())
            max = Math.max(max, p.getId());
        return max + 1;
    }

    private int proximoIdFornecedor() {
        int max = 0;
        for (Fornecedor f : fornecedores.listarTodos())
            max = Math.max(max, f.getId());
        return max + 1;
    }

    private int proximoIdVenda() {
        int max = 0;
        for (Venda v : vendas.listarTodos())
            max = Math.max(max, v.getIdVenda());
        return max + 1;
    }

    private int lerInt(String msg) {
        while (true) {
            System.out.print(msg);
            if (!sc.hasNextLine()) {
                return 0; // entrada encerrada (EOF): trata como "0" (Sair/Voltar)
            }
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private double lerDouble(String msg) {
        while (true) {
            System.out.print(msg);
            if (!sc.hasNextLine()) {
                return 0;
            }
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    private String lerTexto(String msg) {
        System.out.print(msg);
        if (!sc.hasNextLine()) {
            return "";
        }
        return sc.nextLine().trim();
    }

    private LocalDate lerData(String msg) {
        while (true) {
            System.out.print(msg);
            if (!sc.hasNextLine()) {
                return LocalDate.now();
            }
            String entrada = sc.nextLine().trim().replace("/", "-");
            try {
                return LocalDate.parse(entrada, DATA_BR);
            } catch (Exception e) {
                System.out.println("Data inválida. Use DD-MM-AAAA (ex: 31-12-2026).");
            }
        }
    }

    // Lê um nome: não pode ser vazio nem conter números.
    private String lerNome(String msg) {
        while (true) {
            System.out.print(msg);
            if (!sc.hasNextLine()) {
                return "";
            }
            String nome = sc.nextLine().trim();
            if (!nome.isEmpty() && !nome.matches(".*\\d.*")) {
                return nome;
            }
            System.out.println("Nome inválido. Não pode ser vazio nem conter números.");
        }
    }

    // Lê um CPF: aceita com ou sem pontuação, mas exige exatamente 11 dígitos.
    private String lerCpf(String msg) {
        while (true) {
            System.out.print(msg);
            if (!sc.hasNextLine()) {
                return "";
            }
            String digitos = sc.nextLine().trim().replaceAll("\\D", "");
            if (digitos.length() == 11) {
                return digitos;
            }
            System.out.println("CPF inválido. Digite exatamente 11 dígitos (somente números).");
        }
    }

    // Exibe o CPF com máscara 000.000.000-00.
    private String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    // Limpa a tela via códigos ANSI (suportados pelo Windows Terminal / PowerShell).
    // ESC[H = cursor no topo, ESC[2J = limpa a tela, ESC[3J = limpa o histórico de rolagem.
    private void limparTela() {
        System.out.print("[H[2J[3J");
        System.out.flush();
    }

    private void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
    }

    private void validarCpfUnico(String cpf) throws CpfDuplicadoException {
        for (Cliente c : clientes.listarTodos())
            if (c.getCpf() != null && c.getCpf().equals(cpf))
                throw new CpfDuplicadoException("CPF " + cpf + " já cadastrado (Cliente).");
        for (Funcionario f : funcionarios.listarTodos())
            if (f.getCpf() != null && f.getCpf().equals(cpf))
                throw new CpfDuplicadoException("CPF " + cpf + " já cadastrado (Funcionário).");
    }
}
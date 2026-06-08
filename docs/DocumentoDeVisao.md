# DOCUMENTO DE VISÃO
## Sistema de Gestão de Loja — ShopManager

**Disciplina:** Linguagem de Programação  
**Professor:** Jefferson Gomes Dutra  
**Atividade:** Unidade 03 | Versão 1.0  
**Grupo:** Membro 1 • Membro 2 • Membro 3  
**Local:** Natal, RN — 2026  

---

## 1. Introdução

Este documento descreve a visão geral do sistema **ShopManager**, uma aplicação de gestão de loja desenvolvida como trabalho avaliativo da disciplina de Linguagem de Programação. O objetivo é demonstrar, na prática, os principais conceitos de Programação Orientada a Objetos (POO) por meio de uma aplicação funcional com CRUD completo, hierarquia de classes com sentido real, polimorfismo vinculado a regras de negócio, estado dinâmico, tratamento de exceções personalizadas e persistência em arquivos.

---

## 2. Descrição do Sistema

O ShopManager é um sistema de gerenciamento de loja executado via terminal. Ele permite controlar clientes, dois tipos de funcionários (Vendedor e Gerente), dois tipos de produtos (Eletrônico e Alimentício), fornecedores e vendas. Cada venda possui um ciclo de vida com estados definidos e regras de transição, e os dados são persistidos em arquivos JSON para sobreviver ao encerramento da aplicação.

### 2.1 Contexto e Motivação

O contexto de gestão comercial foi escolhido por ser rico em situações naturais de herança com atributos específicos, polimorfismo com comportamento distinto por tipo, e estados que evoluem ao longo do tempo — todos requisitos centrais do trabalho. Um projeto anterior em C++ serviu como base conceitual e foi expandido para atender integralmente aos critérios do professor.

### 2.2 Tecnologia

| Item | Escolha |
|---|---|
| Linguagem | Java |
| Interface | Terminal (menu interativo via `Scanner`) |
| Persistência | Arquivos `.json` (biblioteca Gson ou Jackson) |
| Versionamento | GitHub (repositório colaborativo) |

---

## 3. Modelagem de Classes

O sistema é composto por **12 classes funcionais** (excluindo enums e constantes), organizadas em três hierarquias de herança.

| Classe | Responsabilidade | Subclasses / Filhas |
|---|---|---|
| `Pessoa` (abstrata) | Entidade base com `nome` e `cpf` | `Cliente`, `Funcionario` |
| `Cliente` | Herda `Pessoa`; adiciona `idCliente`, `dataCadastro`, histórico de compras | — |
| `Funcionario` (abstrata) | Herda `Pessoa`; adiciona `idFuncionario` e `salarioBase` | `Vendedor`, `Gerente` |
| `Vendedor` | Herda `Funcionario`; atributo `comissaoPorVenda` e método `calcularComissao()` | — |
| `Gerente` | Herda `Funcionario`; atributo `bonusMensal` e método `calcularBonificacao()` | — |
| `Produto` (abstrata) | `id`, `nome`, `precoVenda`, `quantidadeEstoque`; método abstrato `calcularDesconto()` | `ProdutoEletronico`, `ProdutoAlimenticio` |
| `ProdutoEletronico` | Herda `Produto`; adiciona `garantiaMeses`; desconto progressivo por quantidade | — |
| `ProdutoAlimenticio` | Herda `Produto`; adiciona `dataValidade`; bloqueia venda se vencido | — |
| `Fornecedor` | `cnpj`, `nomeFantasia`; não herda `Pessoa` (CNPJ ≠ CPF) | — |
| `ItemVenda` | Produto vendido + quantidade + `precoUnitarioSalvo`; calcula subtotal | — |
| `Venda` | Agrega `ItemVenda`, `Cliente` e `Funcionario`; estado dinâmico; calcula total | — |
| `GerenciadorDados<T>` | Genérico: CRUD em memória + persistência em arquivo JSON | — |

**Hierarquias principais:**

```
Pessoa (abstrata)
├── Cliente
└── Funcionario (abstrata)
    ├── Vendedor
    └── Gerente

Produto (abstrata)
├── ProdutoEletronico
└── ProdutoAlimenticio
```

---

## 4. Situações de Polimorfismo

O sistema implementa **duas situações de polimorfismo** vinculadas diretamente a regras de negócio. Em ambos os casos, as classes filhas possuem comportamentos e atributos próprios que justificam sua existência.

| # | Método Polimórfico | Classe Base | Comportamento por Subtipo |
|---|---|---|---|
| 1 | `calcularDesconto(double valor)` | `Produto` (abstrata) | `ProdutoEletronico` aplica desconto progressivo por quantidade; `ProdutoAlimenticio` sempre retorna `0.0` (proibido descontar perecíveis) |
| 2 | `calcularRemuneracao(List<Venda> vendas)` | `Funcionario` (abstrata) | `Vendedor` soma comissão percentual sobre cada venda finalizada; `Gerente` retorna `salarioBase + bonusMensal` fixo |

> **Obs.:** Os métodos polimórficos alteram o resultado financeiro da operação, não apenas a exibição — garantindo relevância ao contexto do sistema.

---

## 5. Regras de Negócio

| ID | Nome | Descrição |
|---|---|---|
| RN-01 | Estoque insuficiente | Ao adicionar um `ItemVenda`, o sistema valida o estoque disponível. Lança `EstoqueInsuficienteException` se quantidade > estoque. |
| RN-02 | Produto vencido | `ProdutoAlimenticio` bloqueia adição à venda se a `dataValidade` for anterior à data atual. Lança `ProdutoVencidoException`. |
| RN-03 | Transição de estado da Venda | Venda segue o fluxo `ABERTA → FINALIZADA → CANCELADA`. Não é possível cancelar uma venda já finalizada. |
| RN-04 | Cálculo de remuneração | `Vendedor` recebe comissão percentual sobre o total das suas vendas finalizadas. `Gerente` recebe bônus mensal fixo acumulado. |
| RN-05 | Desconto por quantidade | `ProdutoEletronico` aplica desconto progressivo: 5% para 3–5 unidades, 10% para 6+. `ProdutoAlimenticio` não concede desconto. |
| RN-06 | CPF duplicado | O sistema rejeita cadastro de `Cliente` ou `Funcionario` com CPF já registrado, lançando `CpfDuplicadoException`. |

---

## 6. Estado Dinâmico — Venda

A classe `Venda` possui um atributo `status` do tipo `enum StatusVenda`:

| Estado | Descrição |
|---|---|
| `ABERTA` | Venda em composição; itens podem ser adicionados ou removidos |
| `FINALIZADA` | Estoque já foi baixado; pagamento confirmado; imutável |
| `CANCELADA` | Só pode ser cancelada a partir de `ABERTA`; estoque é restaurado |

**Fluxo de transições permitidas:**

```
ABERTA ──────→ FINALIZADA   (confirma pagamento; baixa estoque)
ABERTA ──────→ CANCELADA    (cancela antes de finalizar; restaura estoque)
FINALIZADA ──→ ✗            (imutável; lança TransicaoEstadoInvalidaException)
```

---

## 7. Exceções Personalizadas

Todas as exceções abaixo são classes próprias da aplicação (não utilizam `RuntimeException` genérico):

| Exceção | Situação que dispara |
|---|---|
| `EstoqueInsuficienteException` | Tentativa de venda com quantidade maior que o estoque disponível |
| `ProdutoVencidoException` | Adição de `ProdutoAlimenticio` com `dataValidade` expirada à venda |
| `CpfDuplicadoException` | Cadastro de `Cliente` ou `Funcionario` com CPF já existente no sistema |
| `TransicaoEstadoInvalidaException` | Tentativa de mudar estado da `Venda` fora do fluxo permitido |
| `EntidadeNaoEncontradaException` | Busca por ID que não existe em qualquer gerenciador |

---

## 8. Persistência em Arquivos

A classe `GerenciadorDados<T>` é responsável por salvar e carregar os dados de cada entidade:

- **Ao encerrar:** cada gerenciador serializa sua lista em um arquivo `.json` (ex: `clientes.json`, `produtos.json`, `vendas.json`)
- **Ao iniciar:** cada gerenciador lê o arquivo correspondente e reconstrói os objetos em memória
- **Biblioteca:** Gson ou Jackson para serialização/desserialização
- **Primeira execução:** se o arquivo não existir, o gerenciador inicia com lista vazia

---

## 9. Requisitos

### 9.1 Requisitos Funcionais

| ID | Descrição |
|---|---|
| RF-01 | Cadastrar, consultar, editar e remover Clientes |
| RF-02 | Cadastrar, consultar, editar e remover Funcionários (Vendedor e Gerente) |
| RF-03 | Cadastrar, consultar, editar e remover Produtos (Eletrônico e Alimentício) |
| RF-04 | Cadastrar, consultar, editar e remover Fornecedores |
| RF-05 | Registrar vendas com múltiplos itens, cliente e funcionário responsável |
| RF-06 | Alterar o estado de uma Venda conforme fluxo definido |
| RF-07 | Calcular total da venda com descontos aplicáveis por tipo de produto |
| RF-08 | Calcular e exibir remuneração de Vendedor e bônus de Gerente |
| RF-09 | Persistir todos os dados em arquivos JSON ao encerrar a aplicação |
| RF-10 | Carregar dados dos arquivos JSON ao iniciar a aplicação |

### 9.2 Requisitos Não Funcionais

| ID | Descrição |
|---|---|
| RNF-01 | Interface via menu no terminal com validação de entradas |
| RNF-02 | Exceções personalizadas para todos os erros de negócio |
| RNF-03 | Diagrama de classes UML com todas as relações e multiplicidades |
| RNF-04 | Código versionado no GitHub com histórico de commits do grupo |

---

## 10. Divisão de Tarefas

| Responsável | Escopo |
|---|---|
| Membro 1 | Entidades base: `Pessoa`, `Cliente`, `Funcionario` (abstrata), `Vendedor`, `Gerente` · Exceções personalizadas · CPF duplicado (RN-06) |
| Membro 2 | `Produto` (abstrata), `ProdutoEletronico`, `ProdutoAlimenticio`, `Fornecedor`, `ItemVenda` · Regras RN-01, RN-02 e RN-05 |
| Membro 3 | `Venda` com estado dinâmico (RN-03) · `GerenciadorDados<T>` com persistência em JSON · Menu principal e integração no terminal |

> Todos os membros devem contribuir com commits no repositório GitHub. O diagrama UML é responsabilidade coletiva.

---

## 11. Conformidade com os Requisitos do Professor

| Requisito do Enunciado | Como é atendido neste projeto |
|---|---|
| Mínimo 11 classes funcionais | 12 classes (ver Seção 3) |
| Encapsulamento | Todos os atributos são `private` com getters/setters |
| 2 situações de polimorfismo com regra de negócio | `calcularDesconto()` e `calcularRemuneracao()` (ver Seção 4) |
| 5+ regras de negócio | 6 regras definidas — RN-01 a RN-06 (ver Seção 5) |
| Classe com estado dinâmico | `Venda` com `StatusVenda` e transições validadas (ver Seção 6) |
| Exceções personalizadas | 5 exceções próprias da aplicação (ver Seção 7) |
| Persistência em arquivos | `GerenciadorDados<T>` salva/carrega JSON (ver Seção 8) |
| Classes filhas com atributos/comportamentos próprios | `Vendedor` tem `comissao`; `Gerente` tem `bonus`; `ProdutoEletronico` tem `garantia`; `ProdutoAlimenticio` tem `dataValidade` |
| Polimorfismo vinculado a regras de negócio | Ambos os métodos polimórficos impactam cálculo financeiro |
| Diagrama UML | Será entregue junto ao código no repositório GitHub |

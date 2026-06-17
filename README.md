# NullBank

Projeto da disciplina **DIM0517 - Gerência de Configuração e Mudanças**.

## Equipe

- Allyson Gustavo Silva Do Carmo - [AllysonGustavo1](https://github.com/AllysonGustavo1)
- Gabriel Estacio De Souza Passos - [gabrielestacio](https://github.com/gabrielestacio)
- Jose Ben Hur Nascimento De Oliveira - [Benhurds12](https://github.com/Benhurds12)

## Linguagem e Stack de Desenvolvimento

- Linguagem: Java 21
- Build: Maven
- Interface: Console

## Estrutura do Projeto

```
src/main/java/com/nullbank/
├── App.java              # Ponto de entrada da aplicação
├── model/
│   └── Conta.java        # Entidade conta bancária
├── service/
│   └── ContaService.java # Lógica de negócio
└── ui/
    └── NullBankConsole.java # Interface console
```

## Como Executar

```bash
# Compilar o projeto
mvn clean compile

# Executar
mvn exec:java -Dexec.mainClass="com.nullbank.App"
ou
mvn exec:java "-Dexec.mainClass=com.nullbank.App"

# Ou gerar o JAR e executar
mvn clean package
java -jar target/nullbank-1.0-SNAPSHOT.jar
```

## Funcionalidades

- Cadastrar Conta
- Consultar Saldo
- Crédito
- Débito
- Transferência
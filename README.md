# IFSP Codelab App Bot
Discord bot para o IFSP Codelab.

## Funcionalidade implementadas
- Comando `ping` que devolve a resposta pong indicando que o bot está em funcionamento no servidor;
- Comando `relatorio-mensal-voluntario` que gera um PDF com o relatório mensal para membros voluntários do projeto.

## Como executar o bot
Configurar as variáveis de ambiente:

- `BOT_TOKEN`: o token do bot gerado no [Discord Developer Portal](https://discord.com/developers/docs)
- `PROJECT_TITLE`: título do projeto que será inserido nos relatórios
- `COORDINATOR_NAME`: nome do coordenador do projeto que será inserido nos relatórios

Executar via task `run` do gradle:
```
./gradlew run
```

## Como executar os comandos

**ping**
```
ping
```

**relatorio-mensal-voluntario**

Os dados do relatório devem ser enviados uma única mensagem, separando cada informação com quebra de linha, na seguinte ordem: tipo do relatório, data, nome, atividades planejadas, atividade realizadas, resultados obtidos.
```
relatorio-mensal-voluntario
01/03/2022
Nome completo do membro
Atividade planejada 1; Atividade planejada 2; Atividade planejada 3; Atividade planejada 4; Atividade planejada 5.
Atividade realizada 1; Atividade realizada 2; Atividade realizada 3; Atividade realizada 4; Atividade realizada 5.
Resultado obtido 1; Resultado obtido 2; Resultado obtido 3; Resultado obtido 4; Resultado obtido 5; Resultado obtido 6;
```

## Tecnologias utilizadas

- Aplicação Java - JDK 17
- Bot: consumo dos eventos Websocket e API REST do Discord [Java Discord API (JDA)](https://github.com/DV8FromTheWorld/JDA)
- Relatórios em PDF: a biblioteca [OpenPDF](https://github.com/LibrePDF/OpenPDF)
- Facilitar o desenvolvimento e implantação:
  - [Lombok Gradle Plugin](https://projectlombok.org/setup/gradle)
  - [Bean validation/Hibernate Validator](https://hibernate.org/validator)
  - [Gradle Shadow](https://github.com/johnrengelman/shadow)
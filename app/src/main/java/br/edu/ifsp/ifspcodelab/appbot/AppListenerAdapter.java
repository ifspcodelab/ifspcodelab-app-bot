package br.edu.ifsp.ifspcodelab.appbot;

import lombok.extern.java.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Log
public class AppListenerAdapter extends ListenerAdapter {
    private static final String SPLIT_REGEX = "\n";
    private static final String REPORT_FORMAT_ERROR = """
    Formato inválido. Enviar neste formato:
    ```
    relatorio-mensal-voluntario
    01/03/2022
    Nome completo do aluno
    Atividade planejada 1; Atividade planejada 2; Atividade planejada 3.
    Atividade realizada 1; Atividade realizada 2; Atividade realizada 3.
    Resultado obtido 1; Resultado 2;
    ```""";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        var message = event.getMessage().getContentRaw();
        var messageTokens = message.split(SPLIT_REGEX);
        var command = messageTokens[0];

        if(command.equals("ping")) {
            event.getMessage().reply("pong").queue();
        }

        if(command.equals("relatorio-mensal-voluntario")) {
            if(messageTokens.length != 6) {
                log.warning("Formato inválido: " + message);
                event.getMessage().reply(REPORT_FORMAT_ERROR).queue();
                return;
            }

            var data = messageTokens[1];
            var name = messageTokens[2];
            var planActivities = messageTokens[3];
            var executedActivities = messageTokens[4];
            var results = messageTokens[5];

            try {
                LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException exception) {
                event.getMessage().reply("Data formato inválido. Enviar no formato dd/mm/aaaa, ex: 05/11/2021").queue();
                return;
            }

            if(name.length() < 5 || name.length() > 100) {
                event.getMessage().reply("Nome deve ter no mínimo 5 e no máximo 100 caracteres").queue();
                return;
            }

            if(planActivities.length() < 100 || planActivities.length() > 600) {
                event.getMessage().reply("Atividades Planejadas deve ter no mínimo 100 e no máximo 600 caracteres").queue();
                return;
            }

            if(executedActivities.length() < 100 || planActivities.length() > 600) {
                event.getMessage().reply("Atividades Realizadas deve ter no mínimo 100 e no máximo 600 caracteres").queue();
                return;
            }

            if(results.length() < 100 || planActivities.length() > 600) {
                event.getMessage().reply("Resultados obtidos deve ter no mínimo 100 e no máximo 600 caracteres").queue();
                return;
            }

            event.getMessage().reply("relatorio").queue();
        }
    }
}

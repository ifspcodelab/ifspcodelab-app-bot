package br.edu.ifsp.ifspcodelab.appbot;

import br.edu.ifsp.ifspcodelab.appbot.model.CreateMonthlyReport;
import br.edu.ifsp.ifspcodelab.appbot.model.ParticipationType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

            var dataString = messageTokens[1];
            var name = messageTokens[2];
            var planActivities = messageTokens[3];
            var executedActivities = messageTokens[4];
            var results = messageTokens[5];

            LocalDate data;
            try {
                data = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException exception) {
                event.getMessage().reply("Data formato inválido. Enviar no formato dd/mm/aaaa, ex: 05/11/2021").queue();
                return;
            }

            CreateMonthlyReport createMonthlyReport = new CreateMonthlyReport(
                ParticipationType.VOLUNTEER, data, name, planActivities, executedActivities, results
            );

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();

            Set<ConstraintViolation<CreateMonthlyReport>> violations = validator.validate(createMonthlyReport);

            if(!violations.isEmpty()) {
                String errorMessage = "Seu pedido contem os seguinte erros: \n";
                errorMessage = errorMessage + violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
                event.getMessage().reply(errorMessage).queue();
                return;
            }

            event.getMessage().reply("relatorio").queue();
        }
    }
}

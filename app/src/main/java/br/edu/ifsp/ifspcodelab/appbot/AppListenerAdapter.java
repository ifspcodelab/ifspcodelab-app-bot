package br.edu.ifsp.ifspcodelab.appbot;

import br.edu.ifsp.ifspcodelab.appbot.models.CreateMonthlyReport;
import br.edu.ifsp.ifspcodelab.appbot.models.ParticipationType;
import br.edu.ifsp.ifspcodelab.appbot.services.MonthlyReportService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
@AllArgsConstructor
public class AppListenerAdapter extends ListenerAdapter {
    private static final String SPLIT_REGEX = "\n";
    private static final String REPORT_FORMAT_ERROR = "dsadsadas";

    private MonthlyReportService monthlyReportService;

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

            ByteArrayOutputStream reportBaos = monthlyReportService.generateReport(createMonthlyReport);

            if(reportBaos == null) {
                event.getMessage().reply("Erro ao criar o relatória").queue();
                return;
            }

            var fileName = UUID.randomUUID().toString() + ".pdf";
            event.getMessage()
                .reply("Aqui está seu relatório")
                .addFile(reportBaos.toByteArray(), fileName)
                .queue();
        }
    }
}

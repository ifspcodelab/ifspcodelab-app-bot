package br.edu.ifsp.ifspcodelab.appbot.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CreateMonthlyReport {
    @NonNull
    ParticipationType participationType;
    @NonNull
    LocalDate data;
    @NonNull @NotEmpty @Size(min = 5, max = 100, message = "Nome deve ter entre 5 e 100 caracteres")
    String name;
    @NonNull @NotEmpty @Size(min = 100, max = 600, message = "Atividades planejadas deve ter entre 5 e 100 caracteres")
    String planActivities;
    @NonNull @NotEmpty @Size(min = 100, max = 600, message = "Atividades realizadas deve ter entre 5 e 100 caracteres")
    String executedActivities;
    @NonNull @NotEmpty @Size(min = 100, max = 600, message = "Resultados obtidos deve deve ter entre 5 e 100 caracteres")
    String results;
}

package br.edu.ifsp.ifspcodelab.appbot;

import br.edu.ifsp.ifspcodelab.appbot.config.AppConfig;
import br.edu.ifsp.ifspcodelab.appbot.services.MonthlyReportService;
import lombok.extern.java.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

@Log
public class App {
    public static void main(String[] args) throws LoginException {
        AppConfig appConfig = new AppConfig();

        JDA jda = JDABuilder.createDefault(appConfig.getBootToken()).build();
        log.info("Bot started");

        MonthlyReportService monthlyReportService = new MonthlyReportService(appConfig);
        log.info("MonthlyReportService created");

        AppListenerAdapter appListenerAdapter = new AppListenerAdapter(monthlyReportService);
        log.info("AppListenerAdapter created");

        jda.addEventListener(appListenerAdapter);
        log.info("Bot configured");
    }
}

package br.edu.ifsp.ifspcodelab.appbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) throws LoginException {
        String token = System.getenv("BOT_TOKEN");
        JDA jda = JDABuilder.createDefault(token).build();
        LOGGER.info("Bot started");
        jda.addEventListener(new AppListenerAdapter());
        LOGGER.info("Bot configured");
    }
}

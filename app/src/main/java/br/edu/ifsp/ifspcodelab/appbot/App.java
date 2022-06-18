package br.edu.ifsp.ifspcodelab.appbot;

import lombok.extern.java.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

@Log
public class App {
    public static void main(String[] args) throws LoginException {
        String token = System.getenv("BOT_TOKEN");
        JDA jda = JDABuilder.createDefault(token).build();
        log.info("Bot started");
        jda.addEventListener(new AppListenerAdapter());
        log.info("Bot configured");
    }
}

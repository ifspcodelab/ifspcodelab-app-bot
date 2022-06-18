package br.edu.ifsp.ifspcodelab.appbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class App {
    public static void main(String[] args) throws LoginException {
        String token = System.getenv("BOT_TOKEN");
        JDA jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new AppListenerAdapter());
    }
}

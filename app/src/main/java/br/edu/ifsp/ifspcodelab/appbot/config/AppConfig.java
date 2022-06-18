package br.edu.ifsp.ifspcodelab.appbot.config;

import lombok.Value;

@Value
public class AppConfig {
    String projectTitle;
    String coordinatorName;
    String bootToken;

    public AppConfig() {
        projectTitle = System.getenv("PROJECT_TITLE");
        coordinatorName = System.getenv("COORDINATOR_NAME");
        bootToken = System.getenv("BOT_TOKEN");
    }
}

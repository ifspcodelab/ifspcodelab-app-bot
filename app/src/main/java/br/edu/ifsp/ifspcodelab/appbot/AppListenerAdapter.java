package br.edu.ifsp.ifspcodelab.appbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class AppListenerAdapter extends ListenerAdapter {
    private static final String SPLIT_REGEX = "\n";

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        var messageTokens = event.getMessage().getContentRaw().split(SPLIT_REGEX);
        var command = messageTokens[0];

        if(command.equals("ping")) {
            event.getMessage().reply("pong").queue();
        }
    }
}

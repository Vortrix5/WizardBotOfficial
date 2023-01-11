package tn.vortrix5.wizard.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;
import tn.vortrix5.wizard.command.CommandMap;

public class BotListener implements EventListener {

    public static List<Object[]> await = new ArrayList<>();
    public static Map<Guild, TextChannel> welcome = new HashMap<>();
    public static Map<Guild, TextChannel> leave = new HashMap<>();
    public static Map<Guild, Boolean> antiSwear = new HashMap<>();


    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) onMessage((MessageReceivedEvent) event);
        if (event instanceof GuildMemberJoinEvent) onJoin((GuildMemberJoinEvent) event);
        if (event instanceof GuildMemberLeaveEvent) onLeave((GuildMemberLeaveEvent) event);

    }

    private void onJoin(GuildMemberJoinEvent event) {
        if (event.getUser().equals(event.getJDA().getSelfUser())) return;

        if (welcome.containsKey(event.getGuild())) {
            TextChannel channel = welcome.get(event.getGuild());
            channel.sendMessage(":tada: **Welcome " + event.getUser().getAsMention() + " to " + event.getGuild().getName() + ". Hope you enjoy your stay here!**").queue();
        }
    }

    private void onLeave(GuildMemberLeaveEvent event) {
        if (event.getUser().equals(event.getJDA().getSelfUser())) return;

        if (leave.containsKey(event.getGuild())) {
            TextChannel channel = leave.get(event.getGuild());
            channel.sendMessage(":wave: **Farewell,  " + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + " left " + event.getGuild().getName() + " !**").queue();
        }
    }


    public void onMessage(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getTextChannel() == null || !event.getMessage().getContentRaw().startsWith("w!"))
            return;
        String[] Swear = new String[]{"insert swear words here"};
        String message = event.getMessage().getContentRaw();
        String[] args = message.split(" ");

        if (message.startsWith("w!")) {
            message = message.replaceFirst("w!", "");
            if (CommandMap.commandUser(event.getAuthor(), message, event.getMessage())) {
                if (event.getTextChannel() != null && event.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    event.getMessage().delete().queue();
                }
            }
            return;
        }
        if (antiSwear.getOrDefault(event.getGuild(), true)) {
            for (String arg : args) {
                for (String wordBanned : Swear) {
                    if (arg.toLowerCase().contains(wordBanned.toLowerCase())) {
                        event.getMessage().delete().queue();
                        event.getTextChannel().sendMessage(event.getMessage().getAuthor().getAsMention() + " **LANGUAGE!**")
                                .queue(msg -> Executors.newScheduledThreadPool(1).schedule((Runnable) msg.delete()::queue, 2, TimeUnit.SECONDS));
                        return;
                    }
                }
            }
        }


    }
}


		

		



	

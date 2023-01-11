package tn.vortrix5.wizard.command.defaults;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import okhttp3.*;
import org.json.JSONObject;
import org.w3c.dom.events.Event;
import tn.vortrix5.wizard.Database;
import tn.vortrix5.wizard.JDAManager;
import tn.vortrix5.wizard.WizardBot;
import tn.vortrix5.wizard.command.Command;
import tn.vortrix5.wizard.command.Command.ExecutorType;
import tn.vortrix5.wizard.event.BotListener;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CommandDefault {
    public static Map<Guild, Role> mute = new HashMap<>();

    public String flipCoin(int times) {
        for (times++; times >= 0; times--) {
            double rand = Math.random();
            if (rand > 0.5) {
                return "Heads";
            }
            return "Tails";
        }
        return null;

    }

    public Role getRoleByName(String name, Guild guild) {
        for (Role role : guild.getRoles()) {
            if (!role.getName().equalsIgnoreCase(name)) continue;
            else return role;
        }
        return null;
    }

    private WizardBot wizardBot;

    public CommandDefault(WizardBot wizardBot) {
        this.wizardBot = wizardBot;
    }

    @Command(name = "stop", type = ExecutorType.CONSOLE)
    private void stop() {
        this.wizardBot.setRunning(false);
    }

    @Command(name = "botinfo", type = ExecutorType.ALL, description = "--> Shows the bot informmations.")
    private void botinfo(MessageChannel channel, Guild guild) {
        if ((channel instanceof TextChannel)) {
            TextChannel textChannel = (TextChannel) channel;
            if (!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                return;
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Bot Information :");
        builder.setDescription("**Bot Name:** " + guild.getJDA().getSelfUser().getName()
                + "\n**Bot ID:** " + guild.getJDA().getSelfUser().getId()
                + "\n**Bot Creation Date:** " + guild.getJDA().getSelfUser().getCreationTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"))
                + "\n**Bot Creator:** Vortrix#1330"
                + "\n**Support Server:** https://discord.gg/tHYZtF9"
                + "\n**Bot Invite:** https://discordapp.com/oauth2/authorize?client_id=355730829357350912&scope=bot&permissions=8"
                + "\n:heart: **Donate:** https://www.patreon.com/WizardOfficial");

        builder.setThumbnail(guild.getJDA().getSelfUser().getAvatarUrl());
        builder.setColor(Color.YELLOW);
        builder.setTimestamp(Instant.now());
        channel.sendMessage(builder.build()).queue();
    }

    @Command(name = "game", type = ExecutorType.CONSOLE)
    private void game(JDA jda, String[] args) {
        StringBuilder builder = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = args).length;
        for (int i = 0; i < j; i++) {
            String str = arrayOfString[i];
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(str);
        }
        jda.getPresence().setGame(Game.playing(builder.toString()));
    }

    @Command(name = "userinfo", type = ExecutorType.User, description = "--> Shows the user informmations.")
    private void usinfo(User user, MessageChannel channel, Guild guild, Message message) {
        if ((channel instanceof TextChannel)) {
            TextChannel textChannel = (TextChannel) channel;
            if (!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                return;
            }
        }
        if (!message.getMentionedUsers().isEmpty()) {
            user = message.getMentionedUsers().get(0);
        }
        Member member = guild.getMember(user);

        EmbedBuilder builder = new EmbedBuilder();
        StringBuilder builder1 = new StringBuilder();
        for (Role role : member.getRoles()) {
            if (builder1.length() > 0) {
                builder1.append(", ");
            }
            builder1.append(role.getName());
        }
        builder.setAuthor(user.getName(), null, user.getAvatarUrl());
        builder.setTitle("User Information :");
        builder.setColor(Color.red);
        if (member.getRoles().isEmpty()) {
            builder.setDescription("**User Name:** " + user.getName() + "#" + user.getDiscriminator()
                    + "\n**User ID:** " + user.getId()
                    + "\n**Server Join Date:** " + guild.getMember(user).getJoinDate().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss"))
                    + "\n**Discord Join Date:** " + guild.getMember(user).getJoinDate().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss"))
                    + "\n**Roles:** none");
        } else {
            builder.setDescription("**User Name:** " + user.getName() + "#" + user.getDiscriminator()
                    + "\n**User ID:** " + user.getId()
                    + "\n**Server Join Date:** " + guild.getMember(user).getJoinDate().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss"))
                    + "\n**Discord Join Date:** " + guild.getMember(user).getJoinDate().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss"))
                    + "\n**Roles:** " + builder1.toString());
        }
        builder.setTimestamp(Instant.now());
        builder.setThumbnail(user.getAvatarUrl());

        channel.sendMessage(builder.build()).queue();
    }

    @Command(name = "ping", type = ExecutorType.User, description = "--> Shows the user ping.")
    private void ping(JDA jda, MessageChannel channel, Message message, User user, Guild guild, TextChannel txtChannel) {
        if ((channel instanceof TextChannel)) {
            TextChannel textChannel = (TextChannel) channel;
            if (!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
                return;
            }
        }
        Member member = guild.getMember(user);
        txtChannel.sendMessage(user.getAsMention() + " **your ping is : ** " + member.getJDA().getPing() + " ms").queue();
    }

    @Command(name = "serverinfo", type = ExecutorType.User, description = "--> Shows the user informmations.")
    private void serverinfo(User user, MessageChannel channel, Guild guild, Message message, Event event) {
        if ((channel instanceof TextChannel)) {
            TextChannel textChannel = (TextChannel) channel;
            if (!textChannel.getGuild().getSelfMember().hasPermission(Permission.MESSAGE_EMBED_LINKS)) {
                return;
            }
        }
        int onlines = 0;
        List<Member> online = new ArrayList<>();
        for (int i = 0; i < guild.getMembers().size(); i++) {
            if (guild.getMembers().get(i).getOnlineStatus().toString().equalsIgnoreCase("online")) {
                onlines++;
                online.add(guild.getMembers().get(i));
            }
        }
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Server Information :");
        builder.setDescription("**Server ID :** " + guild.getId()
                + "\n**Server Name :** " + guild.getName()
                + "\n**Server Owner** " + guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator()
                + "\n**Server Creation Date:**" + guild.getCreationTime().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss"))
                + "\n**Members:** " + guild.getMembers().size()
                + "\n**Members Online:** " + onlines);
        builder.setTimestamp(Instant.now());
        builder.setColor(Color.blue);
        builder.setThumbnail(guild.getIconUrl());
        channel.sendMessage(builder.build()).queue();
    }

    @Command(name = "stream", type = ExecutorType.CONSOLE)
    private void stream(JDA jda, String[] args) {
        StringBuilder builder = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = args).length;
        for (int i = 0; i < j; i++) {
            String str = arrayOfString[i];
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(str);
        }
        System.out.println("test");

        jda.getPresence().setGame(Game.streaming(builder.toString(), "https://twitch.tv/ComingSoon"));
    }

    @Command(name = "role", description = "--> Allows you to change your role by choosing an emote !")
    private void role(Guild guild, TextChannel channel) {
        if (guild.getIdLong() != 352300043820204034L) {
            return;
        }
        Emote ninja = guild.getEmoteById(371637734701531136L);
        Emote samurai = guild.getEmoteById(371640784773971968L);
        Emote witch = guild.getEmoteById(371641171430211584L);
        Emote gladiator = guild.getEmoteById(371640686199439361L);

        Message msg = channel.sendMessage("Click on one of the emojis to get your role !").complete();

        msg.addReaction(ninja).queue();
        msg.addReaction(samurai).queue();
        msg.addReaction(witch).queue();
        msg.addReaction(gladiator).queue();
    }

    @Command(name = "watch", type = ExecutorType.CONSOLE)
    private void watch(JDA jda, String[] args) {
        StringBuilder builder = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = args).length;
        for (int i = 0; i < j; i++) {
            String str = arrayOfString[i];
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(str);
        }
        jda.getPresence().setGame(Game.watching(builder.toString()));
    }

    @Command(name = "listen", type = ExecutorType.CONSOLE)
    private void listen(JDA jda, String[] args) {
        StringBuilder builder = new StringBuilder();
        String[] arrayOfString;
        int j = (arrayOfString = args).length;
        for (int i = 0; i < j; i++) {
            String str = arrayOfString[i];
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(str);
        }
        jda.getPresence().setGame(Game.listening(builder.toString()));
    }

    @Command(name = "ban", type = ExecutorType.User, description = "Allows to ban a user !")
    private void ban(User user, Guild guild, Message message, String[] args, TextChannel channel) {
        User target = message.getMentionedUsers().get(0);

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i] + " ");
        }

        if (reason.toString().isEmpty()) {
            channel.sendMessage(":x: **You have to specify a reason !**").queue();
            return;
        }

        if (guild.getMember(user).hasPermission(Permission.BAN_MEMBERS)) {
            if (guild.getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                if (guild.getIdLong() != 352300043820204034L) {
                    target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been banned from **" + guild.getName() + "** ! Reason : `" + reason.toString() + "`").queue((test) -> guild.getController().ban(target, 7).queueAfter(1, TimeUnit.SECONDS)));
                    channel.sendMessage(":white_check_mark: ** User banned for** `" + reason.toString() + "`").queue();
                } else {
                    target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been banned from **" + guild.getName() + "** ! Reason : `" + reason.toString() + "`").queue((test) -> guild.getController().ban(target, 7).queueAfter(1, TimeUnit.SECONDS)));
                    guild.getTextChannelById(393909363636699146L).sendMessage(":white_check_mark: " + target.getAsMention() + " **has been banned for** `" + reason.toString() + "`" + "**Responsible Staff :** " + user.getAsMention()).queue();
                    channel.sendMessage(":white_check_mark: ** User banned for** `" + reason.toString() + "`").queue();

                }
            } else {
                channel.sendMessage(":x: **I need the permission `Ban Members`**").queue();
            }
        } else {
            channel.sendMessage(":x: **You do not have permission to do that**").queue();
        }
    }


    @Command(name = "kick", type = ExecutorType.User, description = "Allows to kick a user !")
    private void kick(User user, Guild guild, Message message, String[] args, TextChannel channel) {

        User target = message.getMentionedUsers().get(0);

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i] + " ");
        }

        if (reason.toString().isEmpty()) {
            channel.sendMessage(":x: **You have to specify a reason !**").queue();
            return;
        }

        if (guild.getMember(user).hasPermission(Permission.KICK_MEMBERS)) {
            if (guild.getSelfMember().hasPermission(Permission.KICK_MEMBERS)) {
                if (guild.getIdLong() != 352300043820204034L) {
                    target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been kicked from **" + guild.getName() + "** ! Reason : `" + reason.toString() + "`").queue((test) -> guild.getController().kick(guild.getMember(target)).queueAfter(1, TimeUnit.SECONDS)));
                    channel.sendMessage(":white_check_mark: ** User kicked for** `" + reason.toString() + "`").queue();
                } else {
                    target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been kicked from **" + guild.getName() + "** ! Reason : `" + reason + "`").queue((test) -> guild.getController().kick(guild.getMember(target)).queueAfter(1, TimeUnit.SECONDS)));
                    guild.getTextChannelById(393909363636699146L).sendMessage(":white_check_mark: " + target.getAsMention() + " **has been kicked for** `" + reason.toString() + "`" + "**Responsible Staff :** " + user.getAsMention()).queue();
                    channel.sendMessage(":white_check_mark: ** User kicked for** `" + reason.toString() + "`").queue();

                }
            } else {
                channel.sendMessage(":x: **I need the permission `Kick Members`**").queue();
            }
        } else {
            channel.sendMessage(":x: **You do not have permission to do that**").queue();
        }
    }

    @Command(name = "mute", type = ExecutorType.User, description = "Allows to mute a user !")
    private void mute(User user, Guild guild, Message message, String[] args, TextChannel channel, Member member) {
        User target = message.getMentionedUsers().get(0);
        Role r2 = getRoleByName("Muted", guild);
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i] + " ");
        }


        String m = "Muted";

        if (!guild.getRolesByName(m, true).isEmpty()) {
            if (guild.getMember(user).hasPermission(Permission.MANAGE_ROLES)) {
                if (guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                    if (!guild.getMember(user).getRoles().contains(r2)) {
                        if (!reason.toString().isEmpty()) {
                            target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been muted in **" + guild.getName() + "** ! Reason : `" + reason.toString() + "`").queue((test) -> guild.getController().addRolesToMember(guild.getMember(target), r2).queueAfter(1, TimeUnit.SECONDS)));
                        } else {
                            target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been muted in **" + guild.getName() + "** !").queue((test) -> guild.getController().addRolesToMember(guild.getMember(target), r2).queueAfter(1, TimeUnit.SECONDS)));

                        }
                        if (!reason.toString().isEmpty()) {
                            channel.sendMessage(":white_check_mark: ** User muted for** `" + reason.toString() + "`").queue();
                        } else {
                            channel.sendMessage(":white_check_mark: **User muted.**").queue();
                        }


                    } else {
                        channel.sendMessage(":x: **This user is already muted!**").queue();
                    }

                } else {
                    channel.sendMessage(":x: **I need the permission `Manage Roles`**").queue();
                }
            } else {
                channel.sendMessage(":x: **You do not have permission to do that**").queue();
            }
        } else {
            guild.getController().createRole().setPermissions(Permission.getRaw(Permission.MESSAGE_READ)).setName("Muted").queue();
            if (!reason.toString().isEmpty()) {
                target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been muted in **" + guild.getName() + "** ! Reason : `" + reason.toString() + "`").queue((test) -> guild.getController().addRolesToMember(guild.getMember(target), r2).queueAfter(1, TimeUnit.SECONDS)));
            } else {
                target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been muted in **" + guild.getName() + "** !").queue((test) -> guild.getController().addRolesToMember(guild.getMember(target), r2).queueAfter(1, TimeUnit.SECONDS)));

            }

            if (!reason.toString().isEmpty()) {
                channel.sendMessage(":white_check_mark: ** User muted for** `" + reason.toString() + "`").queue();
            } else {
                channel.sendMessage(":white_check_mark: **User muted.**").queue();
            }
        }
    }


    @Command(name = "unmute", type = ExecutorType.User, description = "Allows to unmute a user !")
    private void unmute(User user, Guild guild, Message message, String[] args, TextChannel channel, Member member) {
        User target = message.getMentionedUsers().get(0);
        StringBuilder reason = new StringBuilder();
        Role role = getRoleByName("Muted", guild);
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i] + " ");
        }


        if (guild.getMember(user).hasPermission(Permission.MANAGE_ROLES)) {
            if (guild.getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
                if (guild.getMember(user).getRoles().contains(role)) {
                    if (!reason.toString().isEmpty()) {

                        target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been unmuted in **" + guild.getName() + "** ! Reason : `" + reason.toString() + "`").queue((test) -> guild.getController().removeRolesFromMember(guild.getMember(target), role).queueAfter(1, TimeUnit.SECONDS)));
                    } else {
                        target.openPrivateChannel().queue(pc -> pc.sendMessage("You have been unmuted in **" + guild.getName() + "** !").queue((test) -> guild.getController().removeRolesFromMember(guild.getMember(target), role).queueAfter(1, TimeUnit.SECONDS)));

                    }
                    if (!reason.toString().isEmpty()) {
                        channel.sendMessage(":white_check_mark: ** User unmuted for** `" + reason.toString() + "`").queue();
                    } else {
                        channel.sendMessage(":white_check_mark: **User unmuted.**").queue();
                    }
                } else {
                    channel.sendMessage(":x: **This user is not muted!**").queue();
                }
            } else {
                channel.sendMessage(":x: **I need the permission `Manage Roles`**").queue();
            }
        } else {
            channel.sendMessage(":x: **You do not have permission to do that**").queue();
        }

    }

    @Command(name = "purge", description = "Allows to delete a big amount of messages (100 max)")
    private void purge(TextChannel channel, User user, Guild guild, String[] args, MessageHistory history) {

        int number = 0;
        try {
            number = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            channel.sendMessage("**:x: The days have to be a number.**").queue();
        }

        if (guild.getMember(user).hasPermission(Permission.MESSAGE_MANAGE)) {
            if (guild.getSelfMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                channel.getHistory().retrievePast(number).queue((messages) -> channel.deleteMessages(messages).queue());
            } else {
                channel.sendMessage(":x: **I need the permission `Manage Messages`**").queue();
            }

        } else {
            channel.sendMessage(":x: **You do not have permission to do that**").queue();
        }


    }

    @Command(name = "report", description = "Allows to delete a big amount of messages (100 max)")
    private void report(TextChannel channel, User user, Guild guild, String[] args, Message message) {
        User target = message.getMentionedUsers().get(0);
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i] + " ");
        }

        if (reason.toString().isEmpty()) {
            channel.sendMessage(":x: **You have to specify a reason !**").queue();
            return;
        }
        if (guild.getIdLong() != 352300043820204034L) {
            channel.sendMessage(":white_check_mark: " + target.getAsMention() + " **has been reported for** `" + reason + "`").queue();
            user.openPrivateChannel().queue(pc -> pc.sendMessage(":white_check_mark:** User successfully reported !** " + ":warning: : **Do not abuse of this command or you will be punished !**").queue());

        } else {
            guild.getTextChannelById(393909363636699146L).sendMessage(":white_check_mark: " + target.getAsMention() + " **has been reported for** `" + reason + "`").queue();
            user.openPrivateChannel().queue(pc -> pc.sendMessage(":white_check_mark:** User successfully reported !** " + ":warning: : **Do not abuse of this command or you will be punished !**").queue());

        }
    }


    @Command(name = "stats", type = ExecutorType.User)
    private void stats(JDA jda, Guild guild, TextChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Wizard Stats :");
        builder.addField("**Guilds Count:**", "" + jda.getGuildCache().size(), true);
        builder.addField("**Total Users:**", "" + jda.getUserCache().size(), true);
        builder.addField("**TextChannels Count:**", "" + jda.getTextChannelCache().size(), true);
        builder.setColor(Color.CYAN);


        channel.sendMessage(builder.build()).queue();


    }

    @Command(name = "warn", description = "Allows to delete a big amount of messages (100 max)")
    private void warn(TextChannel channel, User user, Guild guild, String[] args, Message message) {
        User target = message.getMentionedUsers().get(0);
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i] + " ");
        }

        if (reason.toString().isEmpty()) {
            channel.sendMessage(":x: **You have to specify a reason !**").queue();
            return;
        }
        if (guild.getMember(user).hasPermission(Permission.KICK_MEMBERS)) {

            channel.sendMessage(":white_check_mark: " + target.getAsMention() + " **has been warned for** `" + reason + "`").queue();
            target.openPrivateChannel().queue(pc -> pc.sendMessage(":white_check_mark:**You have been warned by** " + user.getName() + "** ! **Reason : `" + reason + "`").queue());
        } else {
            channel.sendMessage(":x: **You do not have permission to do that !**").queue();
            return;
        }


    }

    @Command(name = "setwelcome")
    public void setWelcome(TextChannel channel, User user, Guild guild, Message message, String[] args) {
        if (guild.getMember(user).hasPermission(Permission.MANAGE_CHANNEL)) {
            if (!message.getMentionedChannels().isEmpty()) {
                BotListener.welcome.put(guild, message.getMentionedChannels().get(0));
                BotListener.leave.put(guild, message.getMentionedChannels().get(0));
                channel.sendMessage(":white_check_mark: **Your welcome/leave channel has been set to:** " + message.getMentionedChannels().get(0).getAsMention()).queue();
            } else {
                for (TextChannel c : guild.getTextChannels()) {
                    if (!c.getName().contains(String.join(" ", args))) continue;
                    else {
                        BotListener.welcome.put(guild, c);
                        BotListener.leave.put(guild, c);

                        channel.sendMessage(":white_check_mark: **Your welcome/leave channel has been set to:** " + c.getAsMention()).queue();
                        return;
                    }
                }
            }
        } else {
            channel.sendMessage(":x: **You need the permission `Manage Channels` to perform this command!**").queue();
        }
    }

    @Command(name = "tempmute", type = ExecutorType.User, description = "Allows to mute a user !")
    private void tempmute(User user, Guild guild, Message message, String[] args, TextChannel channel, Member member) {

        User target = message.getMentionedUsers().get(0);
        Role role = getRoleByName("Muted", guild);

        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            reason.append(args[i] + " ");
        }

        long time = 0;
        try {
            time = Long.parseLong(args[1]);
        } catch (NumberFormatException nfe) {
            time = 0;
            channel.sendMessage("**:x: The time has to be a number.**").queue();
            return;

        }

        final long time2 = time;

        String m = "Muted";

        if (!guild.getRolesByName(m, true).isEmpty()) {
            if (guild.getMember(user).hasPermission(Permission.MANAGE_ROLES)) {
                if (guild.getMember(user).getRoles().contains(role)) {

                    guild.getController().addRolesToMember(guild.getMember(target), role).queue((test) -> guild.getController().removeRolesFromMember(guild.getMember(target), role).queueAfter(time2, TimeUnit.MINUTES));
                    if (!reason.toString().isEmpty()) {
                        channel.sendMessage(":white_check_mark: **User muted for **`" + reason + "` for " + time + "** minutes !**").queue();
                    } else {
                        channel.sendMessage(":white_check_mark: **User muted for **" + time + " **minutes**.").queue();
                    }
                } else {
                    channel.sendMessage(":x: **This user is already muted!**").queue();
                }
            } else {

                channel.sendMessage(":x: **You do not have permission to do that**").queue();
            }

        } else {
            guild.getController().createRole().setPermissions(Permission.getRaw(Permission.MESSAGE_READ)).setName("Muted").queue();
            guild.getController().addRolesToMember(guild.getMember(target), role).queue((test) -> guild.getController().removeRolesFromMember(guild.getMember(target), role).queueAfter(time2, TimeUnit.MINUTES));
            if (!reason.toString().isEmpty()) {
                channel.sendMessage(":white_check_mark: **User muted for **`" + reason + "` **for** " + time + "** minutes !**").queue();
            } else {
                channel.sendMessage(":white_check_mark: **User muted for** " + time + " **minutes.**").queue();
            }
        }
    }

    @Command(name = "dbl", type = ExecutorType.CONSOLE)
    public static void toDiscordBotsList(JDA jda) {

        System.out.println("Posting servercount to DiscordBotsList");
        String url = "https://discordbots.org/api/bots/355730829357350912/stats";
        String discordbots_key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjM1NTczMDgyOTM1NzM1MDkxMiIsImJvdCI6dHJ1ZSwiaWF0IjoxNTE2Mjk5NTU3fQ.G2Obe83b6_X-bBA5WTPsCfCU_BR2Cqu_7EmL_PfXdlA";
        JSONObject data = new JSONObject();
        System.out.println(jda.getGuildCache().size());
        data.put("server_count", jda.getGuildCache().size());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "DiscordBot " + "Wizard")
                .addHeader("Authorization", discordbots_key)
                .build();

        try {
            Response r = new OkHttpClient().newCall(request).execute();
            System.out.println("Posted server count, response: " + r.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "db", type = ExecutorType.CONSOLE)
    public static void toDiscordBots(JDA jda) {
        System.out.println("Posting servercount to DiscordBots");
        String url = "https://bots.discord.pw/api/bots/355730829357350912/stats";
        String discordbots_key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySUQiOiIyODAwNjk5OTA1MDQ0NjQzODciLCJyYW5kIjo2NTIsImlhdCI6MTUxMzc5ODgzM30.LoTMSvKHnFkNe9lxidOHSTxLC9TKQgakXI33aVCKI_g";
        JSONObject data = new JSONObject();
        data.put("server_count", jda.getGuildCache().size());
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), data.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "DiscordBot " + "Wizard")
                .addHeader("Authorization", discordbots_key)
                .build();

        try {
            Response r = new OkHttpClient().newCall(request).execute();
            System.out.println("Posted server count, response: " + r.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Command(name = "antiSwear")
    public void antiSwear(Guild guild, User user, String[] args, MessageChannel channel, Database data) {
        Member author = guild.getMember(user);
        if (author.hasPermission(Permission.MESSAGE_MANAGE)) {
            if (args.length != 0 && (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off"))) {
                boolean state = args[0].equalsIgnoreCase("on");
                if (BotListener.antiSwear.getOrDefault(guild, true) != state) {
                    BotListener.antiSwear.put(guild, state);
                    channel.sendMessage(":white_check_mark: AntiSwear has been turned **" + args[0] + "**.").queue();
                } else channel.sendMessage(":x: AntiSwear is already set **" + state + "**.").queue();
            } else channel.sendMessage(":x: **Usage:** `w!antiSwear <on | off>`").queue();
        } else
            channel.sendMessage(":x: **You need the permission `Manage Messages` to perform this command!**").queue();

    }

}
      

      
      
      
      
      
      
      
      
      

    	  
      

      
     
      
      
      



        
       
        
      
     
      
      

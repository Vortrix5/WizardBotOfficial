package tn.vortrix5.wizard.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.requests.RestAction;
import org.apache.commons.lang3.StringUtils;
import tn.vortrix5.wizard.command.Command;
import tn.vortrix5.wizard.command.Command.ExecutorType;

public class MusicCommand {
    private final MusicManager manager = new MusicManager();

    public Role getRoleByName(String name, Guild guild) {
        for (Role role : guild.getRoles()) {
            if (!role.getName().equalsIgnoreCase(name)) continue;
            else return role;
        }
        return null;
    }


    @Command(name = "play", description = "--> Plays the music that you want ", type = Command.ExecutorType.User)
    private void play(TextChannel textChannel, User User, String[] args, Message message, Guild guild) {
        Guild g1 = textChannel.getGuild();
        List<Role> DJ = guild.getRolesByName("DJ", true);
        if (args.length == 0) {
            if (this.manager.getPlayer(g1).getAudioPlayer().isPaused()) {
                this.manager.getPlayer(g1).getAudioPlayer().setPaused(false);
                textChannel.sendMessage("**The track has been resumed **!").queue();
            } else if (this.manager.getPlayer(g1).getAudioPlayer().getPlayingTrack() != null) {
                textChannel.sendMessage("**Bot is already on the server.**").queue();
            } else if (this.manager.getPlayer(g1).getListener().getTracks() == null) {
                textChannel.sendMessage("**No tracks added !**").queue();
            }
        } else {
            if ((!guild.getAudioManager().isConnected()) && (!guild.getAudioManager().isAttemptingToConnect())) {
                VoiceChannel voiceChannel = message.getMember().getVoiceState().getChannel();
                if (voiceChannel == null) {
                    textChannel.sendMessage("**:x: You have to be connected in a voice channel.**").queue();
                    return;
                }
                guild.getAudioManager().openAudioConnection(voiceChannel);
            }
            String toPlay = StringUtils.join(args, " ");
            toPlay = "ytsearch: " + toPlay;

            this.manager.loadTrack(textChannel, toPlay, true, message);
        }
        String Music = "DJ";
        if (DJ.isEmpty()) {
            guild.getController().createRole().setName(Music).setColor(Color.PINK).queue();
        }
    }

    @Command(name = "skip", description = "--> Skips the played music.", type = Command.ExecutorType.User)
    private void skip(Guild guild, TextChannel textChannel, User user) {
        Role DJ = getRoleByName("DJ", guild);
        if ((!guild.getAudioManager().isConnected()) && (!guild.getAudioManager().isAttemptingToConnect())) {
            textChannel.sendMessage("**:x: There are no tracks in queue**.").queue();
            return;
        }
        MusicPLayer player = this.manager.getPlayer(textChannel.getGuild());
        if (player.getListener().getTrackSize() == 0) {
            textChannel.sendMessage("**:x: There are no tracks in queue, you can add one with the command** `w!play`").queue();
            return;
        }
        if (guild.getMember(user).getRoles().contains(DJ)) {
            this.manager.getPlayer(guild).skipTrack();
            textChannel.sendMessage("**:track_next: Track skipped**").queue();
        } else {
            textChannel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }

    }

    @Command(name = "mstop", description = "--> Stops the music.", type = Command.ExecutorType.ALL)
    private void mstop(Guild guild, TextChannel channel, User user) {
        Role DJ = getRoleByName("DJ", guild);
        if ((!guild.getAudioManager().isConnected()) && (!guild.getAudioManager().isAttemptingToConnect())) {
            channel.sendMessage("**:x: There are no tracks in queue**.").queue();
            return;
        }

        MusicPLayer player = this.manager.getPlayer(channel.getGuild());
        if (guild.getMember(user).getRoles().contains(DJ)) {
            player.getAudioPlayer().stopTrack();
            channel.sendMessage("**:stop_button: The music has been stopped and queue has been cleared.**").queue();
        } else {
            channel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }


    }

    @Command(name = "leave", description = "--> The bot leaves the channel", type = Command.ExecutorType.ALL)
    private void leave(Guild guild, TextChannel channel, User user) {
        Role DJ = getRoleByName("DJ", guild);
        if ((!guild.getAudioManager().isConnected()) && (!guild.getAudioManager().isAttemptingToConnect())) {
            return;
        }
        if (guild.getMember(user).getRoles().contains(DJ)) {
            this.manager.getPlayer(guild).getAudioPlayer().stopTrack();
            guild.getAudioManager().closeAudioConnection();
            channel.sendMessage("**:x: Bot has quit the channel.**").queue();
        } else {
            channel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }
    }

    @Command(name = "pause", description = "--> Pauses the playing music.", type = Command.ExecutorType.User)
    private void pause(Guild guild, TextChannel textChannel, User user) {
        Role DJ = getRoleByName("DJ", guild);
        if ((!guild.getAudioManager().isConnected()) && (!guild.getAudioManager().isAttemptingToConnect())) {
            textChannel.sendMessage("**:x: There are currently no tracks in play**.").queue();
            return;
        }
        if (guild.getMember(user).getRoles().contains(DJ)) {

            this.manager.getPlayer(guild).getAudioPlayer().setPaused(true);
            textChannel.sendMessage("** :play_pause: Music paused**").queue();
        } else {
            textChannel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }
    }


    @Command(name = "resume", description = "--> Resumes the music", type = Command.ExecutorType.User)
    private void resume(Guild guild, TextChannel channel, User user) {
        Role DJ = getRoleByName("DJ", guild);
        if ((!guild.getAudioManager().isConnected()) && (!guild.getAudioManager().isAttemptingToConnect())) {
            channel.sendMessage("**:x: There are currently no tracks in play.**").queue();
            return;
        }
        if (guild.getMember(user).getRoles().contains(DJ)) {

            this.manager.getPlayer(guild).getAudioPlayer().setPaused(false);
            channel.sendMessage("** :play_pause: Music resumed**").queue();
        } else {
            channel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }
    }

    @Command(name = "clear", description = "--> Clears the current queue.", type = Command.ExecutorType.User)
    private void clear(TextChannel textChannel, Guild guild, User user) {
        Role DJ = getRoleByName("DJ", guild);
        MusicPLayer player = this.manager.getPlayer(textChannel.getGuild());
        if (player.getListener().getTracks().isEmpty()) {
            textChannel.sendMessage(":x: There are no tracks in the queue.").queue();
            return;
        }
        if (guild.getMember(user).getRoles().contains(DJ)) {
            player.getListener().getTracks().clear();
            textChannel.sendMessage("**:stop_button: Queue cleared.**").queue();
        } else {
            textChannel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }
    }

    @Command(name = "volume", description = "--> Allows to modify the Bot's volume.", type = Command.ExecutorType.User)
    private void volume(String[] args, TextChannel textChannel, Guild guild, User user) {
        Role DJ = getRoleByName("DJ", guild);
        MusicPLayer player = this.manager.getPlayer(textChannel.getGuild());
        int volume = 0;
        try {
            volume = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            textChannel.sendMessage("**:x: The volume has to be a number.**").queue();
            return;
        }
        if (guild.getMember(user).getRoles().contains(DJ)) {
            player.getAudioPlayer().setVolume(volume);
            textChannel.sendMessage("**:arrow_double_up: The volume is now set at:** " + volume).queue();
        } else {
            textChannel.sendMessage(":x: **You need the role `DJ` to perform this command!**").queue();
        }
    }
}
      

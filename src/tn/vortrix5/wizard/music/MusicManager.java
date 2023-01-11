package tn.vortrix5.wizard.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.requests.RestAction;

public class MusicManager {
    private final AudioPlayerManager manager = new DefaultAudioPlayerManager();
    private final Map<String, MusicPLayer> players = new HashMap();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

    public MusicManager() {
        AudioSourceManagers.registerRemoteSources(this.manager);
        AudioSourceManagers.registerLocalSource(this.manager);
    }

    public synchronized MusicPLayer getPlayer(Guild guild) {
        if (!this.players.containsKey(guild.getId())) {
            this.players.put(guild.getId(), new MusicPLayer(this.manager.createPlayer(), guild));
        }
        return this.players.get(guild.getId());
    }

    public void loadTrack(final TextChannel channel, final String source, final boolean search, final Message message) {
        final MusicPLayer player = getPlayer(channel.getGuild());

        channel.getGuild().getAudioManager().setSendingHandler(player.getAudioHandler());

        this.manager.loadItemOrdered(player, source, new AudioLoadResultHandler() {
            public void trackLoaded(AudioTrack track) {

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("<:music:392817356046467072> Adding the track...");
                builder.addField("**Song Name :**", track.getInfo().title, false);
                builder.addField("**Song Author :**", track.getInfo().author, false);
                builder.addField("**Song Duration**	 : ", MusicManager.this.simpleDateFormat.format(Long.valueOf(track.getDuration())), false);
                builder.setColor(Color.RED);
                builder.setThumbnail("https://i.ytimg.com/vi/" + track.getInfo().identifier + "/hqdefault.jpg");
                builder.setFooter("Song added by " + message.getAuthor().getName(), null);
                Message msg = channel.sendMessage("**:mag_right: Searching... <:youtube:314349922885566475>**").complete();
                Message music = msg.editMessage(builder.build()).completeAfter(1, TimeUnit.SECONDS);
                player.playTrack(track);

            }

            public void playlistLoaded(AudioPlaylist playlist) {
                if (search) {
                    trackLoaded(playlist.getTracks().get(0));
                    return;
                }
                StringBuilder builder = new StringBuilder();
                builder.append("**Adding the playlist **").append(playlist.getName()).append("**\n");
                for (int i = 0; (i < playlist.getTracks().size()) && (i < 5); i++) {
                    AudioTrack track = playlist.getTracks().get(i);
                    builder.append("\n  **->** ").append(track.getInfo().title);
                    player.playTrack(track);
                }
                channel.sendMessage(builder.toString()).queue();
            }

            public void noMatches() {
                channel.sendMessage("**The track **`" + source + "` **cannot be found.**").queue();
            }

            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("**Cannot play the trak** (reason:" + exception.getMessage() + ")").queue();
            }
        });
    }


}
     

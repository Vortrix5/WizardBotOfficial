package tn.vortrix5.wizard.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Guild;

public class MusicPLayer {
    private final AudioPlayer audioPlayer;
    private final AudioListener listener;
    private final Guild guild;

    public MusicPLayer(AudioPlayer audioPlayer, Guild guild) {
        this.audioPlayer = audioPlayer;
        this.guild = guild;
        this.listener = new AudioListener(this);
        audioPlayer.addListener(this.listener);
    }

    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public AudioListener getListener() {
        return this.listener;
    }

    public AudioHandler getAudioHandler() {
        return new AudioHandler(this.audioPlayer);
    }

    public synchronized void playTrack(AudioTrack track) {
        this.listener.queue(track);
    }

    public synchronized void skipTrack() {
        this.listener.nextTrack();
    }
}

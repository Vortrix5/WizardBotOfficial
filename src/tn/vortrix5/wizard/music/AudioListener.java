     package tn.vortrix5.wizard.music;
     
     import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
     import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
     import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
     import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
     import java.util.concurrent.BlockingQueue;
     import java.util.concurrent.LinkedBlockingQueue;
     
     public class AudioListener
       extends AudioEventAdapter
     {
     private final BlockingQueue<AudioTrack> tracks = new LinkedBlockingQueue();
       private final MusicPLayer player;
       
       public AudioListener(MusicPLayer player)
       {
       this.player = player;
       }
       
       public BlockingQueue<AudioTrack> getTracks()
       {
       return this.tracks;
       }
       
       public int getTrackSize()
       {
       return this.tracks.size();
       }
       
       public void nextTrack()
       {
       this.player.getAudioPlayer().startTrack(this.tracks.poll(), false);
       }
       
       public void onTrackEnd(AudioPlayer player, AudioTrack rack, AudioTrackEndReason endReason)
       {
       if (endReason.mayStartNext) {
         nextTrack();
         }
       }
       
       public void queue(AudioTrack track)
       {
       if (!this.player.getAudioPlayer().startTrack(track, true)) {
         this.tracks.offer(track);
         }
       }
     }

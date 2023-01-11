package tn.vortrix5.wizard.command.defaults;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.UserImpl;
import net.dv8tion.jda.core.requests.RestAction;
import tn.vortrix5.wizard.command.Command;
import tn.vortrix5.wizard.command.Command.ExecutorType;
import tn.vortrix5.wizard.command.CommandMap;
import tn.vortrix5.wizard.command.SimpleCommand;

public class HelpCommand {
    private final CommandMap commandMap;

    public HelpCommand(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Command(name = "help", type = Command.ExecutorType.User, description = "--> Shows the commands list. ")
    private void help(User user, MessageChannel channel, Guild guild) {
        EmbedBuilder builder1 = new EmbedBuilder();
        EmbedBuilder builder2 = new EmbedBuilder();
        EmbedBuilder builder3 = new EmbedBuilder();

        builder1.setColor(Color.yellow);
        builder1.setThumbnail(guild.getJDA().getSelfUser().getAvatarUrl());
        builder1.setTitle("Wizard Commands :");
        builder1.setDescription("**Prefix : w!**"
                + "\n**Music Commands :**"
                + "\n `w!play` : Allows to play music."
                + "\n `w!mstop` : Allows to stop the music."
                + "\n `w!skip` : Allows to skip the played track."
                + "\n `w!clear` : Allows to clear the queue."
                + "\n `w!pause` : Allows to pause the music."
                + "\n `w!resume` : Allows to resume the music."
                + "\n `w!volume` : Allows to change the music's volume."
                + "\n `w!leave` : Makes the bot leave the channel."
                + "\n"
                + "\n **Information Commands:**"
                + "\n `w!help` : Gives Help about the commands."
                + "\n `w!userinfo` : Shows the user informmation."
                + "\n `w!serverinfo` : Shows the server informmation."
                + "\n `w!botinfo` : Shows the bot informmation."
                + "\n `w!stats` : Shows the bot stats."
                + "\n"
                + "\n**Moderation Commands:**"
                + "\n `w!ban` : Bans a user from the server"
                + "\n `w!kick` : Kicks the user from the server"
                + "\n `w!purge` : Allows to delete a certain amount of msgs (100 MAX)"
                + "\n `w!mute` : Mutes the user"
                + "\n `w!unmute` : Unmutes the user"
                + "\n `w!tempmute` : Temporarily mutes the user"
                + "\n `w!warn` : Warns a rule breaker"
                + "\n `w!report` : Reports a rule breaker"
                + "\n `w!antiSwear [on/off]` : Allows to enable/disable the AntiSwear"
                + "\n"
                + "\n**Other Commands:"
                + "\n `w!setwelcome [channel]` : Allows to set a welcome channel");


        builder1.setFooter("Thanks for using our bot ! Hope you are having fun with it !", null);


        if (!user.hasPrivateChannel()) {
            user.openPrivateChannel().complete();
        }
        ((UserImpl) user).getPrivateChannel().sendMessage(builder1.build()).queue();


        channel.sendMessage(user.getAsMention() + ", Help has been sent ! Check your DMs. ").queue();


    }


}




       


       



     
       
     

     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
     
package tn.vortrix5.wizard;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import tn.vortrix5.wizard.event.BotListener;

import javax.security.auth.login.LoginException;

public class JDAManager {
    private static ShardManager shardManager = buildShard();

    public static ShardManager getShardManager() {
        return shardManager;
    }

    private static ShardManager buildShard() {
        try {
            return new DefaultShardManagerBuilder()
                    .setToken(WizardBot.CONFIGURATION.getString("token", "Insert your token here!"))
                    .setShardsTotal(1)
                    .addEventListeners(new BotListener())
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return null;
    }
}

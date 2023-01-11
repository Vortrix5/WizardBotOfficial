package tn.vortrix5.wizard;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import tn.vortrix5.wizard.command.CommandMap;
import tn.vortrix5.wizard.event.BotListener;
import tn.vortrix5.wizard.utils.Configuration;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Scanner;

public class WizardBot implements Runnable {

    public static final Configuration CONFIGURATION;
    public static ShardManager jda;
    private final CommandMap commandMap = new CommandMap(this);
    private final Scanner scanner = new Scanner(System.in);
    private boolean running;

    public WizardBot()
            throws LoginException, IllegalArgumentException {
        jda = new DefaultShardManagerBuilder().setToken(WizardBot.CONFIGURATION.getString("token", "Insert your token here!")).setShardsTotal(1).build();
        jda.addEventListener(new Object[]{new BotListener()});
        System.out.println("Bot Connected");
        CONFIGURATION.save();

    }

    static {
        Configuration configuration = null;

        try {
            configuration = new Configuration("config.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
        CONFIGURATION = configuration;
    }

    public void run() {
        this.running = true;
        while (this.running) {
            if (this.scanner.hasNextLine()) {
                this.commandMap.commandConsole(this.scanner.nextLine());
            }
        }
        this.scanner.close();
        System.out.println("Bot Stopped");
        CONFIGURATION.save();
        jda.shutdown();
        System.exit(0);
    }

    public static void main(String... args) {
        if (CONFIGURATION == null) {
            System.out.println("Config file not couldn't load");
            CONFIGURATION.save();
            return;
        }
        try {
            WizardBot Vol = new WizardBot();
            new Thread(Vol, "bot").start();
            CONFIGURATION.save();

        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}












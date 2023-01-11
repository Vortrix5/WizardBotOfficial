package tn.vortrix5.wizard.command;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import tn.vortrix5.wizard.JDAManager;
import tn.vortrix5.wizard.WizardBot;
import tn.vortrix5.wizard.command.defaults.CommandDefault;
import tn.vortrix5.wizard.command.defaults.HelpCommand;
import tn.vortrix5.wizard.music.MusicCommand;

public final class CommandMap {
    private static final Map<String, SimpleCommand> commands = new HashMap<>();
    private final String tag = "w!";

    public CommandMap(WizardBot wizardBot) {
        registerCommands(new CommandDefault(wizardBot), new HelpCommand(this), new MusicCommand());
    }

    public String getTag() {
        return "w!";
    }

    public Collection<SimpleCommand> getCommands() {
        return this.commands.values();
    }

    public void registerCommands(Object... objects) {
        Object[] arrayOfObject;
        int j = (arrayOfObject = objects).length;
        for (int i = 0; i < j; i++) {
            Object object = arrayOfObject[i];
            registerCommand(object);
        }
    }

    public void registerCommand(Object object) {
        Method[] arrayOfMethod;
        int j = (arrayOfMethod = object.getClass().getDeclaredMethods()).length;
        for (int i = 0; i < j; i++) {
            Method method = arrayOfMethod[i];
            if (method.isAnnotationPresent(Command.class)) {
                Command command = method.getAnnotation(Command.class);
                method.setAccessible(true);
                SimpleCommand simpleCommand = new SimpleCommand(command.name(), command.description(), command.type(), object, method);
                this.commands.put(command.name(), simpleCommand);
            }
        }
    }

    public void commandConsole(String command) {
        Object[] object = getCommand(command);
        if ((object[0] == null) || (((SimpleCommand) object[0]).getExecutorType() == Command.ExecutorType.User)) {
            System.out.println("Unknow Command");
            return;
        }
        try {
            execute((SimpleCommand) object[0], command, (String[]) object[1], null);
        } catch (Exception exception) {
            System.out.println("The method " + ((SimpleCommand) object[0]).getMethod().getName() + " is not correctly initialised .");
        }
    }

    public static boolean commandUser(User user, String command, Message message) {
        Object[] object = getCommand(command);
        if ((object[0] == null) || (((SimpleCommand) object[0]).getExecutorType() == Command.ExecutorType.CONSOLE)) {
            return false;
        }
        try {
            execute((SimpleCommand) object[0], command, (String[]) object[1], message);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("The method " + ((SimpleCommand) object[0]).getMethod().getName() + " is not correctly initialised.");
        }
        return true;
    }

    private static Object[] getCommand(String command) {
        String[] commandSplit = command.split(" ");
        String[] args = new String[commandSplit.length - 1];
        for (int i = 1; i < commandSplit.length; i++) {
            args[(i - 1)] = commandSplit[i];
        }
        SimpleCommand simpleCommand = CommandMap.commands.get(commandSplit[0]);
        return new Object[]{simpleCommand, args};
    }

    private static void execute(SimpleCommand simpleCommand, String command, String[] args, Message message)
            throws Exception {
        Parameter[] parameters = simpleCommand.getMethod().getParameters();
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == String[].class) {
                objects[i] = args;
            } else if (parameters[i].getType() == User.class) {
                objects[i] = (message == null ? null : message.getAuthor());
            } else if (parameters[i].getType() == TextChannel.class) {
                objects[i] = (message == null ? null : message.getTextChannel());
            } else if (parameters[i].getType() == PrivateChannel.class) {
                objects[i] = (message == null ? null : message.getPrivateChannel());
            } else if (parameters[i].getType() == Guild.class) {
                objects[i] = (message == null ? null : message.getGuild());
            } else if (parameters[i].getType() == String.class) {
                objects[i] = command;
            } else if (parameters[i].getType() == Message.class) {
                objects[i] = message;
            } else if (parameters[i].getType() == JDA.class) {
                objects[i] = WizardBot.jda;
            } else if (parameters[i].getType() == MessageChannel.class) {
                objects[i] = (message == null ? null : message.getChannel());
            }
        }
        simpleCommand.getMethod().invoke(simpleCommand.getObject(), objects);
    }
}

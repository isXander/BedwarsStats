package co.uk.isxander.mcstats.command;

import co.uk.isxander.mcstats.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHandler {

    private static CommandHandler instance = null;
    public static CommandHandler getInstance() {
        if (instance == null)
            instance = new CommandHandler();
        return instance;
    }

    private List<CommandBase> commands;

    public CommandHandler() {
        commands = new ArrayList<>();
    }

    public void registerCommand(CommandBase command) {
        if (!commands.contains(command)) {
            commands.add(command);
        }
    }

    public boolean onCommand(String message) {
        // Remove the '@'
        message = message.substring(1);
        // Split the string. '/' is used because spaces are accounted in error message
        String[] split = message.split("\\/");

        // Get commands with name
        List<CommandBase> commands = this.commands.stream().filter(c -> c.getNames().contains(split[0])).collect(Collectors.toList());
        // No matches
        if (commands.size() == 0)
            return false;

        // Get arguments
        List<String> args = new ArrayList<>();
        for (int i = 0; i < split.length; i++) {
            if (i != 0) {
                args.add(split[i]);
            }
        }

        Log.warn("Executing command \"" + split[0] + "\" with arguments " + Arrays.toString(args.toArray(new String[0])));
        for (CommandBase command : commands) {
            command.onCommand(split[0], args);
        }

        return true;
    }

}

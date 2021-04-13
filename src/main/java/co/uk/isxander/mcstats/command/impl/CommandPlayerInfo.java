package co.uk.isxander.mcstats.command.impl;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.command.CommandBase;
import co.uk.isxander.mcstats.handlers.PlayerHandler;
import co.uk.isxander.mcstats.utils.Formatting;
import co.uk.isxander.mcstats.utils.Warning;
import co.uk.isxander.mcstats.window.impl.MainWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandPlayerInfo extends CommandBase {
    @Override
    public ArrayList<String> getNames() {
        return new ArrayList<>(Arrays.asList(
                "playerinfo",
                "info",
                "player",
                "stats"
        ));
    }

    @Override
    public void onCommand(String name, List<String> args) {
        if (args.size() > 0) {
            MainWindow.getInstance().addWarning(new Warning(4, Formatting.MC_GREEN + "Adding player info (" + args.get(0) + ")", 5000));
            PlayerHandler.getInstance().cachePlayer(args.get(0), true);
        }
    }
}

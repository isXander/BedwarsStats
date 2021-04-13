package co.uk.isxander.mcstats.command.impl;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.command.CommandBase;
import co.uk.isxander.mcstats.handlers.PlayerHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandClear extends CommandBase {

    @Override
    public ArrayList<String> getNames() {
        return new ArrayList<>(Arrays.asList(
                "clear",
                "clearcache",
                "reset"
        ));
    }

    @Override
    public void onCommand(String name, List<String> args) {
        PlayerHandler.getInstance().clearPlayerCache();
    }

}

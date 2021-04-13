package co.uk.isxander.mcstats.command.impl;

import co.uk.isxander.mcstats.command.CommandBase;
import co.uk.isxander.mcstats.window.impl.SetupWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandConfig extends CommandBase {

    @Override
    public ArrayList<String> getNames() {
        return new ArrayList<>(Arrays.asList(
                "config",
                "settings"
        ));
    }

    @Override
    public void onCommand(String name, List<String> args) {
        new SetupWindow().open();
    }

}

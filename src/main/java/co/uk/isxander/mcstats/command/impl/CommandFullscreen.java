package co.uk.isxander.mcstats.command.impl;

import co.uk.isxander.mcstats.command.CommandBase;
import co.uk.isxander.mcstats.handlers.WindowManager;
import co.uk.isxander.mcstats.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandFullscreen extends CommandBase {

    @Override
    public ArrayList<String> getNames() {
        return new ArrayList<>(Arrays.asList(
                "fullscreen"
        ));
    }

    @Override
    public void onCommand(String name, List<String> args) {
        Log.err("Ok.");
        WindowManager.fullscreenMinecraft();
    }

}

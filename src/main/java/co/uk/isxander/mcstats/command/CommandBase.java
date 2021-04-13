package co.uk.isxander.mcstats.command;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase {

    public abstract ArrayList<String> getNames();

    public abstract void onCommand(String name, List<String> args);

}

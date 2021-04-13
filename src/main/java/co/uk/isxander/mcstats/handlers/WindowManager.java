package co.uk.isxander.mcstats.handlers;

import co.uk.isxander.mcstats.utils.Log;
import co.uk.isxander.mcstats.utils.StringUtils;
import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

public class WindowManager {

    public static void fullscreenMinecraft() {
        final User32 user32 = User32.INSTANCE;
        for (DesktopWindow window : WindowUtils.getAllWindows(true)) {
            if (StringUtils.isWindowTitleMinecraft(window.getTitle())) {
                HWND hwnd = window.getHWND();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int style = user32.GetWindowLong(hwnd, WinUser.GWL_STYLE);
                if (style != 369295360) {
                    Log.err("State 1");
                    user32.SetWindowLong(hwnd, WinUser.GWL_STYLE, style & ~(WinUser.WS_CAPTION | WinUser.WS_THICKFRAME | WinUser.WS_MINIMIZE | WinUser.WS_MAXIMIZE | WinUser.WS_SYSMENU));
                    user32.MoveWindow(hwnd, 0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight(), true);
                } else {
                    Log.err("State 2");
                    user32.SetWindowLong(hwnd, WinUser.GWL_STYLE, style | (WinUser.WS_CAPTION | WinUser.WS_THICKFRAME | WinUser.WS_MINIMIZE | WinUser.WS_MAXIMIZE | WinUser.WS_SYSMENU));
                    user32.ShowWindow(hwnd, WinUser.SW_HIDE);
                    user32.ShowWindow(hwnd, WinUser.SW_MAXIMIZE);
                }


                break;
            }
        }
    }

}

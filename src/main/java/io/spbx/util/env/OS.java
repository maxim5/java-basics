package io.spbx.util.env;

import io.spbx.util.base.annotate.Stateless;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@Stateless
public class OS {
    // https://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
    private static final Family FAMILY = detectOsFamily();

    public static boolean isWindows() {
        return FAMILY == Family.WINDOWS;
    }

    public static boolean isLinux() {
        return FAMILY == Family.LINUX;
    }

    public static boolean isMac() {
        return FAMILY == Family.MAC;
    }

    public static boolean isSolaris() {
        return FAMILY == Family.SOLARIS;
    }

    private enum Family {
        WINDOWS,
        LINUX,
        MAC,
        SOLARIS,
        UNKNOWN,
    }

    private static @NotNull Family detectOsFamily() {
        String osName = System.getProperty("os.name", "unknown").toLowerCase(Locale.ENGLISH);
        if (osName.contains("win")) {
            // https://stackoverflow.com/questions/31909107/javas-os-name-for-windows-10
            return Family.WINDOWS;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return Family.LINUX;
        } else if (osName.contains("mac")) {
            return Family.MAC;
        } else if (osName.contains("sunos")) {
            return Family.SOLARIS;
        }
        return Family.UNKNOWN;
    }
}

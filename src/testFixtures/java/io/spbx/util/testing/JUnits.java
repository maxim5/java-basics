package io.spbx.util.testing;

import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.classpath.BasicClasspath;
import io.spbx.util.code.jvm.JavaNameValidator;
import io.spbx.util.lazy.Lazy;
import io.spbx.util.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.spbx.util.base.error.BasicExceptions.InternalErrors.assureNonNull;
import static io.spbx.util.base.error.Unchecked.Suppliers.runRethrow;
import static io.spbx.util.base.lang.EasyCast.castAny;
import static io.spbx.util.func.ScopeFunctions.also;

@Stateless
public class JUnits {
    private static final Logger log = Logger.forEnclosingClass();
    private static final Lazy<JUnitLaunch> launch = Lazy.of(JUnits::detectJUnitLaunch);

    public static @NotNull JUnitLaunch getJUnitLaunch() {
        return also(launch.get(), jUnitLaunch -> log.debug().log("Detected JUnitLaunch: %s", jUnitLaunch));
    }

    private static @NotNull JUnitLaunch detectJUnitLaunch() {
        Class<?> klass = BasicClasspath.classForNameOrNull("io.spbx.util.extern.intellij.IntellijForJUnit");
        if (klass == null) {
            return JUnitLaunch.UNKNOWN;
        }

        List<String> junitArgs =
            castAny(runRethrow(() -> assureNonNull(klass.getMethod("getThisProcessJUnitStarterCmdArgs")).invoke(null)));
        log.debug().log("JUnit command-line args: %s", junitArgs);
        if (junitArgs == null) {
            return JUnitLaunch.UNKNOWN;
        }

        List<String> targets = junitArgs.stream()
            .filter(line -> !line.isEmpty() && !line.startsWith("+") && !line.startsWith("-"))
            .toList();

        if (targets.size() == 1) {
            String target = targets.getFirst();
            if (target.contains(",")) {
                String[] split = target.split(",");
                if (split.length == 2) {
                    if (BasicClasspath.isInClasspath(split[0])) {
                        return JUnitLaunch.SINGLE_METHOD;
                    } else {
                        log.debug().log("JUnit arg class not found in class-path: `%s`", split[0]);
                    }
                } else {
                    log.debug().log("Unexpected JUnit arg: `%s`", target);
                }
                return JUnitLaunch.UNKNOWN;
            }
            if (JavaNameValidator.isValidJavaIdentifiersSeparatedByDots(target)) {
                if (BasicClasspath.isInClasspath(target)) {
                    return JUnitLaunch.SINGLE_CLASS;
                }
                return JUnitLaunch.SINGLE_PACKAGE;
            }
        }

        return JUnitLaunch.OTHER;
    }

    public enum JUnitLaunch {
        SINGLE_METHOD,
        SINGLE_CLASS,
        SINGLE_PACKAGE,
        OTHER,
        UNKNOWN,
    }
}

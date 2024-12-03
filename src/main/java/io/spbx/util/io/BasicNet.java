package io.spbx.util.io;

import io.spbx.util.base.annotate.CheckReturnValue;
import io.spbx.util.base.annotate.Stateless;
import io.spbx.util.base.error.BasicExceptions.IllegalArgumentExceptions;
import io.spbx.util.base.error.Unchecked;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;

import static io.spbx.util.base.lang.EasyCast.castAny;

@Stateless
@CheckReturnValue
public class BasicNet {
    public static int nextAvailablePort() throws UncheckedIOException {
        // https://stackoverflow.com/questions/2675362/how-to-find-an-available-port
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(false);
            return socket.getLocalPort();
        } catch (IOException e) {
            return Unchecked.rethrow(e);
        }
    }

    private static final Set<String> KNOWN_LOCAL_HOSTS = Set.of(
        "localhost",
        "127.0.0.1",
        "fe80:0:0:0:0:0:0:1%1",
        "0:0:0:0:0:0:0:1",
        "::1"
    );

    public static boolean isLocalhost(@NotNull String host) {
        return isKnownLocalhost(host) || isVerifiedLocalhost(host);
    }

    public static boolean isKnownLocalhost(@NotNull String host) {
        return KNOWN_LOCAL_HOSTS.contains(host.toLowerCase());
    }

    public static boolean isVerifiedLocalhost(@NotNull String host) {
        // https://stackoverflow.com/questions/2406341/how-to-check-if-an-ip-address-is-the-local-host-on-a-multi-homed-system
        try {
            return isLocalAddress(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static boolean isLocalAddress(@NotNull InetAddress address) {
        if (address.isAnyLocalAddress() || address.isLoopbackAddress()) {
            return true;  // Local sub-net.
        }
        // Check if the non-local address is defined on any local-interface.
        try {
            return NetworkInterface.getByInetAddress(address) != null;
        } catch (SocketException e) {
            return false;
        }
    }

    public static final int IP4_BYTES = 4;
    public static final int IP6_BYTES = 16;

    public static @NotNull InetAddress parseInetAddress(@NotNull String addr) throws UncheckedIOException {
        try {
            return InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            return Unchecked.rethrow(e);
        }
    }

    public static @NotNull Inet4Address parseIp4Address(@NotNull String addr) throws UncheckedIOException {
        InetAddress inet = parseInetAddress(addr);
        return inet instanceof Inet4Address inet4 ? inet4 : IllegalArgumentExceptions.fail("Invalid IPv4 address:", addr);
    }

    public static @NotNull Inet6Address parseIp6Address(@NotNull String addr) throws UncheckedIOException {
        InetAddress inet = parseInetAddress(addr);
        return inet instanceof Inet6Address inet6 ? inet6 : IllegalArgumentExceptions.fail("Invalid IPv6 address:", addr);
    }

    public static @NotNull InetAddress ipAddressFromBytes(byte @NotNull[] addr) throws UncheckedIOException {
        try {
            return InetAddress.getByAddress(addr);
        } catch (UnknownHostException e) {
            return Unchecked.rethrow(e);
        }
    }

    public static @NotNull Inet4Address ip4AddressFromBytes(byte @NotNull[] addr) throws UncheckedIOException {
        assert addr.length == IP4_BYTES : IllegalArgumentExceptions.fail("Invalid IPv4 address:", Arrays.toString(addr));
        return castAny(ipAddressFromBytes(addr));
    }

    public static @NotNull Inet6Address ip6AddressFromBytes(byte @NotNull[] addr) throws UncheckedIOException {
        assert addr.length == IP6_BYTES : IllegalArgumentExceptions.fail("Invalid IPv6 address:", Arrays.toString(addr));
        return castAny(ipAddressFromBytes(addr));
    }
}

package io.spbx.util.base.math;

import io.spbx.util.base.annotate.Pure;
import io.spbx.util.base.annotate.Stateless;

@Stateless
@Pure
public class MathOps {
    // Mod-inverse operations.
    // Explained in:
    //   https://marc-b-reynolds.github.io/math/2017/09/18/ModInverse.html
    //   https://learn.saylor.org/mod/page/view.php?id=27117
    //   https://math.stackexchange.com/questions/13187/computing-inverses-bmod-nk-by-hensel-lifting-or-newtons-method
    // Ref impl:
    //   https://github.com/faissaloo/telegram-micro/blob/master/src/app/Bouncycastle/BigInteger.java#L1456

    /**
     * Returns the 16-bit {@code short x} such that: {@code d * x == 1}.
     * Supports only odd {@code d} values.
     */
    public static short modInverse16(short d) {
        assert (d & 1) != 0 : "Value is even: inverse does not exist: " + d;
        // Newton's method with initial estimate "correct to 4 bits"
        int x = d + (((d + 1) & 4) << 1);   // d * x == 1 mod 2**4 or (d * x & 15) == 1
        x *= 2 - d * x;                     // d * x == 1 mod 2**8
        x *= 2 - d * x;                     // d * x == 1 mod 2**16
        /* assert (short) (d * x) == 1 : "Newton-Raphson inverse method failed for " + d; */
        return (short) x;
    }

    /**
     * Returns the 32-bit {@code int x} such that: {@code d * x == 1}.
     * Supports only odd {@code d} values.
     */
    public static int modInverse32(int d) {
        assert (d & 1) != 0 : "Value is even: inverse does not exist: " + d;
        // Newton's method with initial estimate "correct to 4 bits"
        int x = d + (((d + 1) & 4) << 1);   // d * x == 1 mod 2**4 or (d * x & 15) == 1
        x *= 2 - d * x;                     // d * x == 1 mod 2**8
        x *= 2 - d * x;                     // d * x == 1 mod 2**16
        x *= 2 - d * x;                     // d * x == 1 mod 2**32
        /* assert d * x == 1 : "Newton-Raphson inverse method failed for " + d; */
        return x;
    }

    /**
     * Returns the 64-bit {@code long x} such that: {@code d * x == 1L}.
     * Supports only odd {@code d} values.
     */
    public static long modInverse64(long d) {
        assert (d & 1) != 0 : "Value is even: inverse does not exist: " + d;
        // Newton's method with initial estimate "correct to 4 bits"
        long x = d + (((d + 1) & 4) << 1);  // d * x == 1 mod 2**4 or (d * x & 15) == 1
        x *= 2 - d * x;                     // d * x == 1 mod 2**8
        x *= 2 - d * x;                     // d * x == 1 mod 2**16
        x *= 2 - d * x;                     // d * x == 1 mod 2**32
        x *= 2 - d * x;                     // d * x == 1 mod 2**64
        /* assert d * x == 1 : "Newton-Raphson inverse method failed for " + d; */
        return x;
    }

    // Fast 64-bit by 64-bit multiplication
    // Impl Ref:
    //   https://github.com/PoslavskySV/libdivide4j/blob/master/src/main/java/cc/redberry/libdivide4j/FastDivision.java#L35

    /**
     * Returns highest 64 bits of (signed) long multiplication.
     *
     * @param x the number
     * @param y the number
     * @return highest 64 bits of (signed) long multiplication.
     */
    public static long multiplyHighSigned(long x, long y) {
        long x_high = x >> 32;
        long x_low = x & 0xffff_ffffL;
        long y_high = y >> 32;
        long y_low = y & 0xffff_ffffL;

        long z2 = x_low * y_low;
        long t = x_high * y_low + (z2 >>> 32);
        long z1 = t & 0xffff_ffffL;
        long z0 = t >> 32;
        z1 += x_low * y_high;
        return x_high * y_high + z0 + (z1 >> 32);
    }

    /**
     * Returns highest 64 bits of (unsigned) long multiplication.
     *
     * @param x the number
     * @param y the number
     * @return highest 64 bits of (unsigned) long multiplication.
     */
    public static long multiplyHighUnsigned(long x, long y) {
        long x_high = x >>> 32;
        long y_high = y >>> 32;
        long x_low = x & 0xffff_ffffL;
        long y_low = y & 0xffff_ffffL;

        long z2 = x_low * y_low;
        long t = x_high * y_low + (z2 >>> 32);
        long z1 = t & 0xffff_ffffL;
        long z0 = t >>> 32;
        z1 += x_low * y_high;
        return x_high * y_high + z0 + (z1 >>> 32);
    }

    // Fast 128-bit by 64-bit division
    // Impl Ref:
    //   https://github.com/jhump/bluegosling/blob/master/src/com/bluegosling/concurrent/Duration.java#L804
    //   https://github.com/PoslavskySV/libdivide4j/blob/master/src/main/java/cc/redberry/libdivide4j/FastDivision.java#L91

    /**
     * Divides a 128-bit unsigned integer by a 64-bit unsigned divisor and produces the
     * 64-bits of the quotient. Only works if the result fits in 64-bit.
     *
     * @param u1 high-order 64 bits of the dividend
     * @param u0 low-order 64 bits of the dividend
     * @param v the divisor
     * @return 64 bits of the quotient
     */
    public static long udivide128By64(long u1, long u0, long v) {
        long b = 1L << 32;      // Number base (16 bits).
        long
            un1, un0,           // Norm. dividend LSD's.
            vn1, vn0,           // Norm. divisor digits.
            q1, q0,             // Quotient digits.
            un32, un21, un10,   // Dividend digit pairs.
            rhat,               // A remainder.
            left, right;
        int s;                  // Shift amount for norm.

        s = Long.numberOfLeadingZeros(v);   // 0 <= s <= 63.
        v <<= s;
        vn1 = v >>> 32;
        vn0 = v & 0xffff_ffffL;

        if (s > 0) {
            un32 = (u1 << s) | (u0 >>> (64 - s));
            un10 = u0 << s;
        } else {
            un32 = u1;
            un10 = u0;
        }

        un1 = un10 >>> 32;
        un0 = un10 & 0xffff_ffffL;

        q1 = Long.divideUnsigned(un32, vn1);
        rhat = Long.remainderUnsigned(un32, vn1);

        left = q1 * vn0;
        right = (rhat << 32) + un1;

        while (true) {
            if (Long.compareUnsigned(q1, b) >= 0 || Long.compareUnsigned(left, right) > 0) {
                --q1;
                rhat += vn1;
                if (Long.compareUnsigned(rhat, b) < 0) {
                    left -= vn0;
                    right = (rhat << 32) | un1;
                    continue;
                }
            }
            break;
        }

        un21 = (un32 << 32) + (un1 - (q1 * v));

        q0 = Long.divideUnsigned(un21, vn1);
        rhat = Long.remainderUnsigned(un21, vn1);

        left = q0 * vn0;
        right = (rhat << 32) | un0;
        while (true) {
            if (Long.compareUnsigned(q0, b) >= 0 || Long.compareUnsigned(left, right) > 0) {
                --q0;
                rhat += vn1;
                if (Long.compareUnsigned(rhat, b) < 0) {
                    left -= vn0;
                    right = (rhat << 32) | un0;
                    continue;
                }
            }
            break;
        }

        return (q1 << 32) | q0;
    }

    /**
     * Returns the quotient and remainder of 128-bit integer division by 64-bit integer.
     * Originally the code is taken from Hacker's Delight: http://www.hackersdelight.org/HDcode/divlu.c.
     *
     * @param u1 highest 64 dividend bits
     * @param u0 lowest 64 dividend bits
     * @param v  the divider
     * @return {quotient, remainder}
     */
    public static long[] udivide128By64WithRemainder(long u1, long u0, long v) {
        long b = 1L << 32;      // Number base (16 bits).
        long
            un1, un0,           // Norm. dividend LSD's.
            vn1, vn0,           // Norm. divisor digits.
            q1, q0,             // Quotient digits.
            un64, un21, un10,   // Dividend digit pairs.
            rhat;               // A remainder.
        int s;                  // Shift amount for norm.

        if (u1 >= v)                            // If overflow, set rem.
            return new long[] { -1L, -1L };     // possible quotient.

        s = Long.numberOfLeadingZeros(v);       // Count leading zeros: 0 <= s <= 63.
        if (s > 0) {
            v = v << s;                         // Normalize divisor.
            un64 = (u1 << s) | ((u0 >>> (64 - s)) & (-s >> 31));
            un10 = u0 << s;                     // Shift dividend left.
        } else {
            // Avoid undefined behavior.
            un64 = u1 | u0;
            un10 = u0;
        }

        vn1 = v >>> 32;                         // Break divisor up into
        vn0 = v & 0xffff_ffffL;                 // two 32-bit digits.

        un1 = un10 >>> 32;                      // Break right half of
        un0 = un10 & 0xffff_ffffL;              // dividend into two digits.

        q1 = Long.divideUnsigned(un64, vn1);    // Compute the first
        rhat = un64 - q1 * vn1;                 // quotient digit, q1.
        while (true) {
            // Condition: (q1 >= b || q1 * vn0 > b * rhat + un1)
            if (Long.compareUnsigned(q1, b) >= 0 || Long.compareUnsigned(q1 * vn0, b * rhat + un1) > 0) {
                q1 = q1 - 1;
                rhat = rhat + vn1;
                if (Long.compareUnsigned(rhat, b) < 0)
                    continue;
            }
            break;
        }

        un21 = un64 * b + un1 - q1 * v;         // Multiply and subtract.

        q0 = Long.divideUnsigned(un21, vn1);    // Compute the second
        rhat = un21 - q0 * vn1;                 // quotient digit, q0.
        while (true) {
            if (Long.compareUnsigned(q0, b) >= 0 || Long.compareUnsigned(q0 * vn0, b * rhat + un0) > 0) {
                q0 = q0 - 1;
                rhat = rhat + vn1;
                if (Long.compareUnsigned(rhat, b) < 0)
                    continue;
            }
            break;
        }
        long r = (un21 * b + un0 - q0 * v) >>> s;   // return it.
        return new long[] { q1 * b + q0, r };
    }
}

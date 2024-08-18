package io.spbx.util.base;

import io.spbx.util.base.DataSize.Unit;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class DataSizeTest {
    /** {@link DataSize#parseInt} */

    @Test
    public void parse_int_simple() {
        assertDataSize(DataSize.parse("123")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("0")).isEqualToExactly(DataSize.ofBytes(0));
    }

    @Test
    public void parse_int_simple_spaces() {
        assertDataSize(DataSize.parse("  123  ")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123  ")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("  123")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123 b")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("\t123 \rb  \n\t")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("\t123 \rkb  \n\t")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("\t123 \rmb  \n\t")).isEqualToExactly(DataSize.ofMegabytes(123));
    }

    @Test
    public void parse_int_simple_suffixes() {
        assertDataSize(DataSize.parse("123b")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123kb")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123gb")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123tb")).isEqualToExactly(DataSize.ofTerabytes(123));
        assertDataSize(DataSize.parse("123pb")).isEqualToExactly(DataSize.ofPetabytes(123));
        assertDataSize(DataSize.parse("123eb")).isEqualToExactly(DataSize.ofExabytes(123));
    }

    @Test
    public void parse_int_huge_suffixes() {
        assertDataSize(DataSize.parse("123zb")).isEqualToExactly(DataSize.of(123, Unit.Zettabyte));
        assertDataSize(DataSize.parse("123yb")).isEqualToExactly(DataSize.of(123, Unit.Yottabyte));
        assertDataSize(DataSize.parse("123rb")).isEqualToExactly(DataSize.of(123, Unit.Ronnabyte));
        assertDataSize(DataSize.parse("123qb")).isEqualToExactly(DataSize.of(123, Unit.Quettabyte));
    }

    @Test
    public void parse_int_simple_ignore_case() {
        assertDataSize(DataSize.parse("123B")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123k")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123KB")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123m")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123Mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123g")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123Gb")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123t")).isEqualToExactly(DataSize.ofTerabytes(123));
        assertDataSize(DataSize.parse("123tB")).isEqualToExactly(DataSize.ofTerabytes(123));
    }

    @Test
    public void parse_int_eic_suffixes() {
        assertDataSize(DataSize.parse("123k")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123kib")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123mib")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123g")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123gib")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123t")).isEqualToExactly(DataSize.ofTerabytes(123));
        assertDataSize(DataSize.parse("123tib")).isEqualToExactly(DataSize.ofTerabytes(123));
        assertDataSize(DataSize.parse("123p")).isEqualToExactly(DataSize.ofPetabytes(123));
        assertDataSize(DataSize.parse("123pib")).isEqualToExactly(DataSize.ofPetabytes(123));
        assertDataSize(DataSize.parse("123eib")).isEqualToExactly(DataSize.ofExabytes(123));
        assertDataSize(DataSize.parse("123zib")).isEqualToExactly(DataSize.of(123, Unit.Zettabyte));
        assertDataSize(DataSize.parse("123rib")).isEqualToExactly(DataSize.of(123, Unit.Ronnabyte));
        assertDataSize(DataSize.parse("123qib")).isEqualToExactly(DataSize.of(123, Unit.Quettabyte));
    }

    @Test
    public void parse_int_simple_plus_sign() {
        assertDataSize(DataSize.parse("+123b")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("+123kb")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("+123mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("+123gb")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("+123tb")).isEqualToExactly(DataSize.ofTerabytes(123));
    }

    @Test
    public void parse_int_simple_minus_sign() {
        assertDataSize(DataSize.parse("-123b")).isEqualToExactly(DataSize.ofBytes(-123));
        assertDataSize(DataSize.parse("-123kb")).isEqualToExactly(DataSize.ofKilobytes(-123));
        assertDataSize(DataSize.parse("-123mb")).isEqualToExactly(DataSize.ofMegabytes(-123));
        assertDataSize(DataSize.parse("-123gb")).isEqualToExactly(DataSize.ofGigabytes(-123));
        assertDataSize(DataSize.parse("-123tb")).isEqualToExactly(DataSize.ofTerabytes(-123));
    }

    @Test
    public void parse_int_default_unit() {
        assertDataSize(DataSize.parse("123", Unit.Byte)).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123", Unit.Kilobyte)).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123", Unit.Megabyte)).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("0", Unit.Byte)).isEqualToExactly(DataSize.ofBytes(0));
        assertDataSize(DataSize.parse("0", Unit.Kilobyte)).isEqualToExactly(DataSize.ofBytes(0));
        assertDataSize(DataSize.parse("0", Unit.Gigabyte)).isEqualToExactly(DataSize.ofGigabytes(0));
    }

    @Test
    public void parse_int_large_amount() {
        assertDataSize(DataSize.parse("1000b")).isEqualToExactly(DataSize.ofBytes(1000));
        assertDataSize(DataSize.parse("1000000b")).isEqualToExactly(DataSize.ofBytes(1000000));
        assertDataSize(DataSize.parse("1000000000b")).isEqualToExactly(DataSize.ofBytes(1000000000));
        assertDataSize(DataSize.parse("9223372036854775807")).isEqualToExactly(DataSize.ofBytes(Long.MAX_VALUE));
        assertDataSize(DataSize.parse("-9223372036854775808")).isEqualToExactly(DataSize.ofBytes(Long.MIN_VALUE));
    }

    /** {@link DataSize#parseFloat} */

    @Test
    public void parse_float_simple() {
        assertDataSize(DataSize.parse("123.")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123.0")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("0.0")).isEqualToExactly(DataSize.ofBytes(0));
        assertDataSize(DataSize.parse("0.")).isEqualToExactly(DataSize.ofBytes(0));
        assertDataSize(DataSize.parse(".0")).isEqualToExactly(DataSize.ofBytes(0));
    }

    @Test
    public void parse_float_simple_spaces() {
        assertDataSize(DataSize.parse("  123.0  ")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123.0  ")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("  123.0")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123.0 b")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("\t123.0 \rb  \n\t")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("\t123.0 \rkb  \n\t")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("\t123.0 \rmb  \n\t")).isEqualToExactly(DataSize.ofMegabytes(123));
    }

    @Test
    public void parse_float_simple_suffixes() {
        assertDataSize(DataSize.parse("123.0b")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123.0kb")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123.0mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123.0gb")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123.0tb")).isEqualToExactly(DataSize.ofTerabytes(123));
        assertDataSize(DataSize.parse("123.0pb")).isEqualToExactly(DataSize.ofPetabytes(123));
        assertDataSize(DataSize.parse("123.0eb")).isEqualToExactly(DataSize.ofExabytes(123));
    }

    @Test
    public void parse_float_huge_suffixes() {
        assertDataSize(DataSize.parse("123.0zb")).isEqualToExactly(DataSize.of(123, Unit.Zettabyte));
        assertDataSize(DataSize.parse("123.0yb")).isEqualToExactly(DataSize.of(123, Unit.Yottabyte));
        assertDataSize(DataSize.parse("123.0yb")).isEqualToExactly(DataSize.of(123, Unit.Yottabyte));
        assertDataSize(DataSize.parse("123.0qb")).isEqualToExactly(DataSize.of(123, Unit.Quettabyte));
    }

    @Test
    public void parse_float_simple_ignore_case() {
        assertDataSize(DataSize.parse("123.0B")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123.0KB")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123.0Mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("123.0Gb")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("123.0tB")).isEqualToExactly(DataSize.ofTerabytes(123));
    }

    @Test
    public void parse_float_simple_plus_sign() {
        assertDataSize(DataSize.parse("+123.0b")).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("+123.0kb")).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("+123.0mb")).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("+123.0gb")).isEqualToExactly(DataSize.ofGigabytes(123));
        assertDataSize(DataSize.parse("+123.0tb")).isEqualToExactly(DataSize.ofTerabytes(123));
    }

    @Test
    public void parse_float_simple_minus_sign() {
        assertDataSize(DataSize.parse("-123.0b")).isEqualToExactly(DataSize.ofBytes(-123));
        assertDataSize(DataSize.parse("-123.0kb")).isEqualToExactly(DataSize.ofKilobytes(-123));
        assertDataSize(DataSize.parse("-123.0mb")).isEqualToExactly(DataSize.ofMegabytes(-123));
        assertDataSize(DataSize.parse("-123.0gb")).isEqualToExactly(DataSize.ofGigabytes(-123));
        assertDataSize(DataSize.parse("-123.0tb")).isEqualToExactly(DataSize.ofTerabytes(-123));
    }

    @Test
    public void parse_float_default_unit() {
        assertDataSize(DataSize.parse("123.0", Unit.Byte)).isEqualToExactly(DataSize.ofBytes(123));
        assertDataSize(DataSize.parse("123.0", Unit.Kilobyte)).isEqualToExactly(DataSize.ofKilobytes(123));
        assertDataSize(DataSize.parse("123.0", Unit.Megabyte)).isEqualToExactly(DataSize.ofMegabytes(123));
        assertDataSize(DataSize.parse("0.0", Unit.Byte)).isEqualToExactly(DataSize.ofBytes(0));
        assertDataSize(DataSize.parse("0.0", Unit.Kilobyte)).isEqualToExactly(DataSize.ofBytes(0));
        assertDataSize(DataSize.parse("0.0", Unit.Gigabyte)).isEqualToExactly(DataSize.ofGigabytes(0));
    }

    @Test
    public void parse_float_partial_amount() {
        assertDataSize(DataSize.parse("0.1kb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.KB * 0.1)));      // 102
        assertDataSize(DataSize.parse("0.1mb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.MB * 0.1)));      // 104857
        assertDataSize(DataSize.parse("0.001mb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.MB * 0.001)));  // 1048
        assertDataSize(DataSize.parse("0.001gb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.GB * 0.001)));  // 1073741
        assertDataSize(DataSize.parse("0.001pb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.PB * 0.001)));
        assertDataSize(DataSize.parse("0.001eb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.EB * 0.001)));

        assertDataSize(DataSize.parse("1.5mb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.MB * 1.5)));
        assertDataSize(DataSize.parse("2.5gb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.GB * 2.5)));
        assertDataSize(DataSize.parse("0.5tb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.TB * 0.5)));
        assertDataSize(DataSize.parse("3.5pb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.PB * 3.5)));
        assertDataSize(DataSize.parse("1.5eb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.EB * 1.5)));
    }

    @Test
    public void parse_float_partial_large_amount() {
        assertDataSize(DataSize.parse("100.0mb")).isEqualToExactly(DataSize.ofBytes(DataSize.MB * 100));
        assertDataSize(DataSize.parse("1000.0mb")).isEqualToExactly(DataSize.ofBytes(DataSize.MB * 1000));
        assertDataSize(DataSize.parse("10000.0mb")).isEqualToExactly(DataSize.ofBytes(DataSize.MB * 10000));
        assertDataSize(DataSize.parse("100000.0mb")).isEqualToExactly(DataSize.ofBytes(DataSize.MB * 100000));
        assertDataSize(DataSize.parse("100000.5mb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.MB * 100000.5)));
    }

    @Test
    public void parse_float_partial_amount_total_does_not_fit_64_bit() {
        assertDataSize(DataSize.parse("0.001zb")).isEqualToExactly(DataSize.ofKilobytes((long) (DataSize.EB * 0.001)));
        assertDataSize(DataSize.parse("2.9zb")).isEqualToInUnits(DataSize.ofKilobytes((long) (DataSize.EB * 2.9)), Unit.Terabyte);
        assertDataSize(DataSize.parse("23.5zb")).isEqualToInUnits(DataSize.ofMegabytes((long) (DataSize.PB * 23.5)), Unit.Terabyte);
        assertDataSize(DataSize.parse("-1.5zb")).isEqualToInUnits(DataSize.ofKilobytes((long) (DataSize.EB * -1.5)), Unit.Terabyte);

        assertDataSize(DataSize.parse("0.001yb")).isEqualToInUnits(DataSize.ofMegabytes((long) (DataSize.EB * 0.001)), Unit.Terabyte);
        assertDataSize(DataSize.parse("2.9yb")).isEqualToInUnits(DataSize.ofMegabytes((long) (DataSize.EB * 2.9)), Unit.Terabyte);
        assertDataSize(DataSize.parse("23.5yb")).isEqualToInUnits(DataSize.ofGigabytes((long) (DataSize.PB * 23.5)), Unit.Terabyte);
        assertDataSize(DataSize.parse("-1.5yb")).isEqualToInUnits(DataSize.ofMegabytes((long) (DataSize.EB * -1.5)), Unit.Terabyte);

        assertDataSize(DataSize.parse("0.001rb")).isEqualToInUnits(DataSize.ofGigabytes((long) (DataSize.EB * 0.001)), Unit.Petabyte);
        assertDataSize(DataSize.parse("2.9rb")).isEqualToInUnits(DataSize.ofGigabytes((long) (DataSize.EB * 2.9)), Unit.Petabyte);
        assertDataSize(DataSize.parse("23.5rb")).isEqualToInUnits(DataSize.ofTerabytes((long) (DataSize.PB * 23.5)), Unit.Petabyte);
        assertDataSize(DataSize.parse("-1.5rb")).isEqualToInUnits(DataSize.ofGigabytes((long) (DataSize.EB * -1.5)), Unit.Petabyte);

        assertDataSize(DataSize.parse("0.001qb")).isEqualToInUnits(DataSize.ofTerabytes((long) (DataSize.EB * 0.001)), Unit.Exabyte);
        assertDataSize(DataSize.parse("2.9qb")).isEqualToInUnits(DataSize.ofTerabytes((long) (DataSize.EB * 2.9)), Unit.Exabyte);
        assertDataSize(DataSize.parse("23.5qb")).isEqualToInUnits(DataSize.ofPetabytes((long) (DataSize.PB * 23.5)), Unit.Exabyte);
        assertDataSize(DataSize.parse("-1.5qb")).isEqualToInUnits(DataSize.ofTerabytes((long) (DataSize.EB * -1.5)), Unit.Exabyte);
    }

    @Test
    public void parse_float_exponent() {
        assertDataSize(DataSize.parse("1e0")).isEqualToExactly(DataSize.ofBytes(1));
        assertDataSize(DataSize.parse("1e1")).isEqualToExactly(DataSize.ofBytes(10));
        assertDataSize(DataSize.parse("1.e0")).isEqualToExactly(DataSize.ofBytes(1));
        assertDataSize(DataSize.parse("1.0e0")).isEqualToExactly(DataSize.ofBytes(1));
        assertDataSize(DataSize.parse("1e10")).isEqualToExactly(DataSize.ofBytes(10000000000L));
        assertDataSize(DataSize.parse("1e-3Mb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.MB * 1e-3)));
        assertDataSize(DataSize.parse("1e+3Kb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.KB * 1e+3)));

        assertDataSize(DataSize.parse("-1e0")).isEqualToExactly(DataSize.ofBytes(-1));
        assertDataSize(DataSize.parse("-1e1")).isEqualToExactly(DataSize.ofBytes(-10));
        assertDataSize(DataSize.parse("-1.e0")).isEqualToExactly(DataSize.ofBytes(-1));
        assertDataSize(DataSize.parse("-1.0e0")).isEqualToExactly(DataSize.ofBytes(-1));
        assertDataSize(DataSize.parse("-1e10")).isEqualToExactly(DataSize.ofBytes(-10000000000L));
        assertDataSize(DataSize.parse("-1e-3Mb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.MB * -1e-3)));
        assertDataSize(DataSize.parse("-1e+3Kb")).isEqualToExactly(DataSize.ofBytes((long) (DataSize.KB * -1e+3)));
    }

    /** {@link DataSize#toString()} */

    @Test
    public void toString_bytes() {
        assertThat(DataSize.ofBytes(1).toString()).isEqualTo("1b");
        assertThat(DataSize.ofBytes(2).toString()).isEqualTo("2b");
        assertThat(DataSize.ofBytes(20).toString()).isEqualTo("20b");
        assertThat(DataSize.ofBytes(64).toString()).isEqualTo("64b");
        assertThat(DataSize.ofBytes(123).toString()).isEqualTo("123b");
        assertThat(DataSize.ofBytes(256).toString()).isEqualTo("256b");
        assertThat(DataSize.ofBytes(512).toString()).isEqualTo("512b");
        assertThat(DataSize.ofBytes(1023).toString()).isEqualTo("1023b");
        assertThat(DataSize.ofBytes(1024).toString()).isEqualTo("1Kb");
        assertThat(DataSize.ofBytes(1025).toString()).isEqualTo("1025b");
        assertThat(DataSize.ofBytes(1234).toString()).isEqualTo("1234b");
        assertThat(DataSize.ofBytes(2048).toString()).isEqualTo("2Kb");
        assertThat(DataSize.ofBytes(4095).toString()).isEqualTo("4095b");
        assertThat(DataSize.ofBytes(4096).toString()).isEqualTo("4Kb");
        assertThat(DataSize.ofBytes(4097).toString()).isEqualTo("4097b");
        assertThat(DataSize.ofBytes(8191).toString()).isEqualTo("8191b");
        assertThat(DataSize.ofBytes(8192).toString()).isEqualTo("8Kb");
        assertThat(DataSize.ofBytes(8193).toString()).isEqualTo("8193b");
        assertThat(DataSize.ofBytes(10000).toString()).isEqualTo("10000b");
        assertThat(DataSize.ofBytes(30000).toString()).isEqualTo("29.30Kb");
        assertThat(DataSize.ofBytes(50000).toString()).isEqualTo("48.83Kb");
        assertThat(DataSize.ofBytes(65535).toString()).isEqualTo("64.00Kb");
        assertThat(DataSize.ofBytes(65536).toString()).isEqualTo("64Kb");
        assertThat(DataSize.ofBytes(65537).toString()).isEqualTo("64.001Kb");
        assertThat(DataSize.ofBytes(100000).toString()).isEqualTo("97.66Kb");
    }

    @Test
    public void toString_kilobytes() {
        assertThat(DataSize.ofKilobytes(1).toString()).isEqualTo("1Kb");
        assertThat(DataSize.ofKilobytes(2).toString()).isEqualTo("2Kb");
        assertThat(DataSize.ofKilobytes(20).toString()).isEqualTo("20Kb");
        assertThat(DataSize.ofKilobytes(64).toString()).isEqualTo("64Kb");
        assertThat(DataSize.ofKilobytes(123).toString()).isEqualTo("123Kb");
        assertThat(DataSize.ofKilobytes(256).toString()).isEqualTo("256Kb");
        assertThat(DataSize.ofKilobytes(512).toString()).isEqualTo("512Kb");
        assertThat(DataSize.ofKilobytes(1023).toString()).isEqualTo("1023Kb");
        assertThat(DataSize.ofKilobytes(1024).toString()).isEqualTo("1Mb");
        assertThat(DataSize.ofKilobytes(1025).toString()).isEqualTo("1025Kb");
        assertThat(DataSize.ofKilobytes(1234).toString()).isEqualTo("1234Kb");
        assertThat(DataSize.ofKilobytes(2048).toString()).isEqualTo("2Mb");
        assertThat(DataSize.ofKilobytes(4095).toString()).isEqualTo("4095Kb");
        assertThat(DataSize.ofKilobytes(4096).toString()).isEqualTo("4Mb");
        assertThat(DataSize.ofKilobytes(4097).toString()).isEqualTo("4097Kb");
        assertThat(DataSize.ofKilobytes(8191).toString()).isEqualTo("8191Kb");
        assertThat(DataSize.ofKilobytes(8192).toString()).isEqualTo("8Mb");
        assertThat(DataSize.ofKilobytes(8193).toString()).isEqualTo("8193Kb");
        assertThat(DataSize.ofKilobytes(10000).toString()).isEqualTo("10000Kb");
        assertThat(DataSize.ofKilobytes(30000).toString()).isEqualTo("29.30Mb");
        assertThat(DataSize.ofKilobytes(50000).toString()).isEqualTo("48.83Mb");
        assertThat(DataSize.ofKilobytes(65535).toString()).isEqualTo("64.00Mb");
        assertThat(DataSize.ofKilobytes(65536).toString()).isEqualTo("64Mb");
        assertThat(DataSize.ofKilobytes(65537).toString()).isEqualTo("64.001Mb");
        assertThat(DataSize.ofKilobytes(100000).toString()).isEqualTo("97.66Mb");
    }

    @Test
    public void toString_megabytes() {
        assertThat(DataSize.ofMegabytes(1).toString()).isEqualTo("1Mb");
        assertThat(DataSize.ofMegabytes(2).toString()).isEqualTo("2Mb");
        assertThat(DataSize.ofMegabytes(20).toString()).isEqualTo("20Mb");
        assertThat(DataSize.ofMegabytes(64).toString()).isEqualTo("64Mb");
        assertThat(DataSize.ofMegabytes(123).toString()).isEqualTo("123Mb");
        assertThat(DataSize.ofMegabytes(256).toString()).isEqualTo("256Mb");
        assertThat(DataSize.ofMegabytes(512).toString()).isEqualTo("512Mb");
        assertThat(DataSize.ofMegabytes(1023).toString()).isEqualTo("1023Mb");
        assertThat(DataSize.ofMegabytes(1024).toString()).isEqualTo("1Gb");
        assertThat(DataSize.ofMegabytes(1025).toString()).isEqualTo("1025Mb");
        assertThat(DataSize.ofMegabytes(1234).toString()).isEqualTo("1234Mb");
        assertThat(DataSize.ofMegabytes(2048).toString()).isEqualTo("2Gb");
        assertThat(DataSize.ofMegabytes(4095).toString()).isEqualTo("4095Mb");
        assertThat(DataSize.ofMegabytes(4096).toString()).isEqualTo("4Gb");
        assertThat(DataSize.ofMegabytes(4097).toString()).isEqualTo("4097Mb");
        assertThat(DataSize.ofMegabytes(8191).toString()).isEqualTo("8191Mb");
        assertThat(DataSize.ofMegabytes(8192).toString()).isEqualTo("8Gb");
        assertThat(DataSize.ofMegabytes(8193).toString()).isEqualTo("8193Mb");
        assertThat(DataSize.ofMegabytes(10000).toString()).isEqualTo("10000Mb");
        assertThat(DataSize.ofMegabytes(30000).toString()).isEqualTo("29.30Gb");
        assertThat(DataSize.ofMegabytes(50000).toString()).isEqualTo("48.83Gb");
        assertThat(DataSize.ofMegabytes(65535).toString()).isEqualTo("64.00Gb");
        assertThat(DataSize.ofMegabytes(65536).toString()).isEqualTo("64Gb");
        assertThat(DataSize.ofMegabytes(65537).toString()).isEqualTo("64.001Gb");
        assertThat(DataSize.ofMegabytes(100000).toString()).isEqualTo("97.66Gb");
    }

    @Test
    public void toString_gigabytes() {
        assertThat(DataSize.ofGigabytes(1).toString()).isEqualTo("1Gb");
        assertThat(DataSize.ofGigabytes(2).toString()).isEqualTo("2Gb");
        assertThat(DataSize.ofGigabytes(20).toString()).isEqualTo("20Gb");
        assertThat(DataSize.ofGigabytes(64).toString()).isEqualTo("64Gb");
        assertThat(DataSize.ofGigabytes(123).toString()).isEqualTo("123Gb");
        assertThat(DataSize.ofGigabytes(256).toString()).isEqualTo("256Gb");
        assertThat(DataSize.ofGigabytes(512).toString()).isEqualTo("512Gb");
        assertThat(DataSize.ofGigabytes(1023).toString()).isEqualTo("1023Gb");
        assertThat(DataSize.ofGigabytes(1024).toString()).isEqualTo("1Tb");
        assertThat(DataSize.ofGigabytes(1025).toString()).isEqualTo("1025Gb");
        assertThat(DataSize.ofGigabytes(1234).toString()).isEqualTo("1234Gb");
        assertThat(DataSize.ofGigabytes(2048).toString()).isEqualTo("2Tb");
        assertThat(DataSize.ofGigabytes(4095).toString()).isEqualTo("4095Gb");
        assertThat(DataSize.ofGigabytes(4096).toString()).isEqualTo("4Tb");
        assertThat(DataSize.ofGigabytes(4097).toString()).isEqualTo("4097Gb");
        assertThat(DataSize.ofGigabytes(8191).toString()).isEqualTo("8191Gb");
        assertThat(DataSize.ofGigabytes(8192).toString()).isEqualTo("8Tb");
        assertThat(DataSize.ofGigabytes(8193).toString()).isEqualTo("8193Gb");
        assertThat(DataSize.ofGigabytes(10000).toString()).isEqualTo("10000Gb");
        assertThat(DataSize.ofGigabytes(30000).toString()).isEqualTo("29.30Tb");
        assertThat(DataSize.ofGigabytes(50000).toString()).isEqualTo("48.83Tb");
        assertThat(DataSize.ofGigabytes(65535).toString()).isEqualTo("64.00Tb");
        assertThat(DataSize.ofGigabytes(65536).toString()).isEqualTo("64Tb");
        assertThat(DataSize.ofGigabytes(65537).toString()).isEqualTo("64.001Tb");
        assertThat(DataSize.ofGigabytes(100000).toString()).isEqualTo("97.66Tb");
    }

    @Test
    public void toString_large_units() {
        assertThat(DataSize.ofTerabytes(1).toString()).isEqualTo("1Tb");
        assertThat(DataSize.ofTerabytes(2).toString()).isEqualTo("2Tb");
        assertThat(DataSize.ofTerabytes(20).toString()).isEqualTo("20Tb");
        assertThat(DataSize.ofTerabytes(64).toString()).isEqualTo("64Tb");
        assertThat(DataSize.ofTerabytes(1000).toString()).isEqualTo("0.977Pb");
        assertThat(DataSize.ofTerabytes(1023).toString()).isEqualTo("0.999Pb");
        assertThat(DataSize.ofTerabytes(1024).toString()).isEqualTo("1Pb");
        assertThat(DataSize.ofTerabytes(1025).toString()).isEqualTo("1.001Pb");
        assertThat(DataSize.ofTerabytes(10000).toString()).isEqualTo("9.77Pb");
        assertThat(DataSize.ofTerabytes(20000).toString()).isEqualTo("19.53Pb");
        assertThat(DataSize.ofTerabytes(50000).toString()).isEqualTo("48.83Pb");
        assertThat(DataSize.ofTerabytes(65535).toString()).isEqualTo("64.00Pb");
        assertThat(DataSize.ofTerabytes(65536).toString()).isEqualTo("64Pb");
        assertThat(DataSize.ofTerabytes(65537).toString()).isEqualTo("64.001Pb");
        assertThat(DataSize.ofTerabytes(100000).toString()).isEqualTo("97.66Pb");
    }

    @Test
    public void toString_zero_num() {
        assertThat(DataSize.ofBytes(0).toString()).isEqualTo("0b");
        assertThat(DataSize.ofBytes(0).toString(Unit.Byte)).isEqualTo("0b");
        assertThat(DataSize.ofBytes(0).toString(Unit.Kilobyte)).isEqualTo("0Kb");
        assertThat(DataSize.ofBytes(0).toString(Unit.Megabyte)).isEqualTo("0Mb");
    }

    @Test
    public void toString_negative_num() {
        assertThat(DataSize.ofBytes(-1).toString()).isEqualTo("-1b");
        assertThat(DataSize.ofBytes(-1).toString(Unit.Byte)).isEqualTo("-1b");
        assertThat(DataSize.ofBytes(-1).toString(Unit.Kilobyte)).isEqualTo("-0.001Kb");
        assertThat(DataSize.ofBytes(-1).toString(Unit.Megabyte)).isEqualTo("-0.000Mb");

        assertThat(DataSize.ofKilobytes(-1).toString()).isEqualTo("-1Kb");
        assertThat(DataSize.ofKilobytes(-1).toString(Unit.Byte)).isEqualTo("-1024b");
        assertThat(DataSize.ofKilobytes(-1).toString(Unit.Kilobyte)).isEqualTo("-1Kb");
        assertThat(DataSize.ofKilobytes(-1).toString(Unit.Megabyte)).isEqualTo("-0.001Mb");
    }

    /** {@link DataSize#toString(Unit)} */

    @Test
    public void toString_force_unit() {
        assertThat(DataSize.ofKilobytes(1).toString(Unit.Byte)).isEqualTo("1024b");
        assertThat(DataSize.ofKilobytes(10).toString(Unit.Byte)).isEqualTo("10240b");
        assertThat(DataSize.ofKilobytes(100).toString(Unit.Byte)).isEqualTo("102400b");
        assertThat(DataSize.ofMegabytes(1).toString(Unit.Byte)).isEqualTo("1048576b");
        assertThat(DataSize.ofGigabytes(1).toString(Unit.Byte)).isEqualTo("1073741824b");
        assertThat(DataSize.ofTerabytes(1).toString(Unit.Byte)).isEqualTo("1099511627776b");
        assertThat(DataSize.ofPetabytes(1).toString(Unit.Byte)).isEqualTo("1125899906842624b");
        assertThat(DataSize.ofExabytes(1).toString(Unit.Byte)).isEqualTo("1152921504606846976b");
    }

    private static @NotNull DataSizeSubject assertDataSize(DataSize dataSize) {
        return new DataSizeSubject(dataSize);
    }

    private record DataSizeSubject(@NotNull DataSize dataSize) {
        public void isEqualToExactly(@NotNull DataSize expected) {
            assertThat(dataSize.toBytes128()).isEqualTo(expected.toBytes128());
        }

        public void isEqualToInUnits(@NotNull DataSize expected, @NotNull Unit unit) {
            assertThat(dataSize.toUnit(unit)).isEqualTo(expected.toUnit(unit));
        }
    }
}

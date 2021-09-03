/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010-2021 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.php.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.php.utils.LiteralUtils.stringLiteralValue;

public class LiteralUtilsTest {

  @Test
  public void decimal_values_test() {
    assertThat(LiteralUtils.longLiteralValue("100")).isEqualTo(100);
    assertThat(LiteralUtils.longLiteralValue("100_000")).isEqualTo(100000);
    assertThat(LiteralUtils.longLiteralValue("9223372036854775807")).isEqualTo(9223372036854775807L);
  }

  @Test
  public void binary_values_test() {
    assertThat(LiteralUtils.longLiteralValue("0b101")).isEqualTo(5);
    assertThat(LiteralUtils.longLiteralValue("0b10_1")).isEqualTo(5);

    assertThat(LiteralUtils.longLiteralValue("0B101")).isEqualTo(5);
    assertThat(LiteralUtils.longLiteralValue("0B10_1")).isEqualTo(5);
  }

  @Test
  public void hexadecimal_values_test() {
    assertThat(LiteralUtils.longLiteralValue("0x1F")).isEqualTo(31);
    assertThat(LiteralUtils.longLiteralValue("0x1_F")).isEqualTo(31);

    assertThat(LiteralUtils.longLiteralValue("0X1F")).isEqualTo(31);
    assertThat(LiteralUtils.longLiteralValue("0X1_F")).isEqualTo(31);
  }

  @Test
  public void single_quoted_string_value() {
    assertThat(stringLiteralValue("''")).isEmpty();
    assertThat(stringLiteralValue("'abc'")).isEqualTo("abc");

    assertThat(stringLiteralValue("'\\''")).isEqualTo("'");
    assertThat(stringLiteralValue("'\\\"'")).isEqualTo("\\\"");

    assertThat(stringLiteralValue("'\\abc'")).isEqualTo("\\abc");
    assertThat(stringLiteralValue("'\\\\abc'")).isEqualTo("\\abc");
    assertThat(stringLiteralValue("'\\\\\\abc'")).isEqualTo("\\\\abc");

    assertThat(stringLiteralValue("'\\n'")).isEqualTo("\\n");
    assertThat(stringLiteralValue("'\\r'")).isEqualTo("\\r");
    assertThat(stringLiteralValue("'\\t'")).isEqualTo("\\t");
    assertThat(stringLiteralValue("'\\v'")).isEqualTo("\\v");
    assertThat(stringLiteralValue("'\\e'")).isEqualTo("\\e");
    assertThat(stringLiteralValue("'\\f'")).isEqualTo("\\f");
    assertThat(stringLiteralValue("'\\$'")).isEqualTo("\\$");

    assertThat(stringLiteralValue("'\\x41'")).isEqualTo("\\x41");

    assertThat(stringLiteralValue("'\\102'")).isEqualTo("\\102");

    assertThat(stringLiteralValue("'\\u{0043}'")).isEqualTo("\\u{0043}");
  }

  @Test
  public void double_quoted_string_value() {
    assertThat(stringLiteralValue("\"\"")).isEmpty();
    assertThat(stringLiteralValue("\"abc\"")).isEqualTo("abc");

    assertThat(stringLiteralValue("\"\\'\"")).isEqualTo("\\'");
    assertThat(stringLiteralValue("\"\\\"\"")).isEqualTo("\"");

    assertThat(stringLiteralValue("\"\\abc\"")).isEqualTo("\\abc");
    assertThat(stringLiteralValue("\"\\\\abc\"")).isEqualTo("\\abc");
    assertThat(stringLiteralValue("\"\\\\\\abc\"")).isEqualTo("\\\\abc");

    assertThat(stringLiteralValue("\"\\\\n\"")).isEqualTo("\\n");
    assertThat(stringLiteralValue("\"\\n\"")).isEqualTo("\n");
    assertThat(stringLiteralValue("\"\\r\"")).isEqualTo("\r");
    assertThat(stringLiteralValue("\"\\t\"")).isEqualTo("\t");
    assertThat(stringLiteralValue("\"\\v\"")).isEqualTo("\u000b");
    assertThat(stringLiteralValue("\"\\e\"")).isEqualTo("\u001b");
    assertThat(stringLiteralValue("\"\\f\"")).isEqualTo("\f");
    assertThat(stringLiteralValue("\"\\$\"")).isEqualTo("$");

    assertThat(stringLiteralValue("\"\\x41\"")).isEqualTo("A");
    assertThat(stringLiteralValue("\"\\xx\"")).isEqualTo("\\xx");
    assertThat(stringLiteralValue("\"\\xa\"")).isEqualTo("\n");

    assertThat(stringLiteralValue("\"\\102\"")).isEqualTo("B");

    assertThat(stringLiteralValue("\"\\u{0043}\"")).isEqualTo("C");
    assertThat(stringLiteralValue("\"\\ux\"")).isEqualTo("\\ux");
  }
}

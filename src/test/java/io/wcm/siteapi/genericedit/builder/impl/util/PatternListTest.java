/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2023 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.siteapi.genericedit.builder.impl.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class PatternListTest {

  @Test
  void testCompiledPatterns() {
    PatternList underTest = new PatternList(
        Pattern.compile("^a.*$"),
        Pattern.compile("^b.*$"));

    assertTrue(underTest.matches("a1"));
    assertTrue(underTest.matches("b1"));
    assertFalse(underTest.matches("c1"));
  }

  @Test
  void testPatternStrings() {
    PatternList underTest = new PatternList(
        "^a.*$",
        "^b.*$",
        "^(c.*$"); // third pattern is invalid - ignored

    assertTrue(underTest.matches("a1"));
    assertTrue(underTest.matches("b1"));
    assertFalse(underTest.matches("c1"));
  }

}

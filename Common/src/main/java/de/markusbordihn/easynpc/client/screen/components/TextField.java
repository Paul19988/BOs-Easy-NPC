/*
 * Copyright 2023 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.easynpc.client.screen.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class TextField extends EditBox {

  private static final TextComponent EMPTY_TEXT_COMPONENT = new TextComponent("");
  private static final int DEFAULT_HEIGHT = 16;
  private static final int DEFAULT_MAX_LENGTH = 64;

  public TextField(Font font, int x, int y, int width) {
    this(font, x, y, width, DEFAULT_HEIGHT, EMPTY_TEXT_COMPONENT);
  }

  public TextField(Font font, int x, int y, int width, String value) {
    this(font, x, y, width, value, DEFAULT_MAX_LENGTH);
  }

  public TextField(Font font, int x, int y, int width, int value, int maxLength) {
    this(font, x, y, width, String.valueOf(value), maxLength);
  }

  public TextField(Font font, int x, int y, int width, String value, int maxLength) {
    this(font, x, y, width, DEFAULT_HEIGHT, EMPTY_TEXT_COMPONENT);
    this.setMaxLength(maxLength);
    this.setValue(value);
  }

  public TextField(Font font, int x, int y, int width, int height) {
    this(font, x, y, width, height, EMPTY_TEXT_COMPONENT);
  }

  private TextField(Font font, int x, int y, int width, int height, Component component) {
    super(font, x, y, width, height, component);
  }
}

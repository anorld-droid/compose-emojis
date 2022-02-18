/*
 * Copyright (C) 2016 - Niklas Baudy, Ruben Gees, Mario Đanić and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.ios_emoji;

import androidx.annotation.NonNull;
import java.util.Collection;
import java.util.Collections;

/**
 * Use this class to hide recent Emoji.
 */
public final class NoRecentEmoji implements RecentEmoji {
  public static final RecentEmoji INSTANCE = new NoRecentEmoji();

  private NoRecentEmoji() {
  }

  @NonNull @Override public Collection<IosEmoji> getRecentEmojis() {
    return Collections.emptyList();
  }

  @Override public void addEmoji(@NonNull final IosEmoji emoji) {
    // Do nothing.
  }

  @Override public void persist() {
    // Do nothing.
  }
}

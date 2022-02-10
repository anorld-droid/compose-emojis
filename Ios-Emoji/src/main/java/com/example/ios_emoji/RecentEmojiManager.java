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

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public final class RecentEmojiManager implements RecentEmoji {
  private static final String PREFERENCE_NAME = "emoji-recent-manager";
  private static final String TIME_DELIMITER = ";";
  private static final String EMOJI_DELIMITER = "~";
  private static final String RECENT_EMOJIS = "recent-emojis";
  static final int EMOJI_GUESS_SIZE = 5;
  static final int MAX_RECENTS = 40;

  @NonNull private EmojiList emojiList = new EmojiList(0);
  @NonNull private final SharedPreferences sharedPreferences;

  public RecentEmojiManager(@NonNull final Context context) {
    sharedPreferences = context.getApplicationContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
  }

  @Override @SuppressWarnings({ "PMD.AvoidDeeplyNestedIfStmts", "checkstyle:nestedifdepth" }) @NonNull public Collection<IosEmoji> getRecentEmojis() {
    if (emojiList.size() == 0) {
      final String savedRecentEmojis = sharedPreferences.getString(RECENT_EMOJIS, "");

      if (savedRecentEmojis.length() > 0) {
        final StringTokenizer stringTokenizer = new StringTokenizer(savedRecentEmojis, EMOJI_DELIMITER);
        emojiList = new EmojiList(stringTokenizer.countTokens());

        while (stringTokenizer.hasMoreTokens()) {
          final String token = stringTokenizer.nextToken();

          final String[] parts = token.split(TIME_DELIMITER);

          if (parts.length == 2) {
            final Emoji emoji = EmojiManager.getInstance().findEmoji(parts[0]);

            if (emoji != null && emoji.getLength() == parts[0].length()) {
              final long timestamp = Long.parseLong(parts[1]);

              emojiList.add(emoji, timestamp);
            }
          }
        }
      } else {
        emojiList = new EmojiList(0);
      }
    }

    return emojiList.getEmojis();
  }

  @Override public void addEmoji(@NonNull final IosEmoji emoji) {
    emojiList.add(emoji);
  }

  @Override public void persist() {
    if (emojiList.size() > 0) {
      final StringBuilder stringBuilder = new StringBuilder(emojiList.size() * EMOJI_GUESS_SIZE);

      for (int i = 0; i < emojiList.size(); i++) {
        final Data data = emojiList.get(i);
        stringBuilder.append(data.emoji.getUnicode())
            .append(TIME_DELIMITER)
            .append(data.timestamp)
            .append(EMOJI_DELIMITER);
      }

      stringBuilder.setLength(stringBuilder.length() - EMOJI_DELIMITER.length());

      sharedPreferences.edit().putString(RECENT_EMOJIS, stringBuilder.toString()).apply();
    }
  }

  static class EmojiList {
    static final Comparator<Data> COMPARATOR = new Comparator<Data>() {
      @Override public int compare(final Data lhs, final Data rhs) {
        return Long.valueOf(rhs.timestamp).compareTo(lhs.timestamp);
      }
    };

    @NonNull final List<Data> emojis;

    EmojiList(final int size) {
      emojis = new ArrayList<>(size);
    }

    void add(final Emoji emoji) {
      add(emoji, System.currentTimeMillis());
    }

    void add(final Emoji emoji, final long timestamp) {
      final Iterator<Data> iterator = emojis.iterator();

      final Emoji emojiBase = emoji.getBase();

      while (iterator.hasNext()) {
        final Data data = iterator.next();
        if (data.emoji.getBase().equals(emojiBase)) { // Do the comparison by base so that skin tones are only saved once.
          iterator.remove();
        }
      }

      emojis.add(0, new Data(emoji, timestamp));

      if (emojis.size() > MAX_RECENTS) {
        emojis.remove(MAX_RECENTS);
      }
    }

    Collection<Emoji> getEmojis() {
      Collections.sort(emojis, COMPARATOR);

      final Collection<Emoji> sortedEmojis = new ArrayList<>(emojis.size());

      for (final Data data : emojis) {
        sortedEmojis.add(data.emoji);
      }

      return sortedEmojis;
    }

    int size() {
      return emojis.size();
    }

    Data get(final int index) {
      return emojis.get(index);
    }
  }

  static class Data {
    final Emoji emoji;
    final long timestamp;

    Data(final Emoji emoji, final long timestamp) {
      this.emoji = emoji;
      this.timestamp = timestamp;
    }
  }
}

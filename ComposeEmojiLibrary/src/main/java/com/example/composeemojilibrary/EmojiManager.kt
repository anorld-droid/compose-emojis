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



import static com.example.ios_emoji.Utils.checkNotNull;

import android.content.Context;
import android.text.Spannable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * EmojiManager where an EmojiProvider can be installed for further usage.
 */
@SuppressWarnings("PMD.ForLoopCanBeForeach") public final class EmojiManager {
  private static final EmojiManager INSTANCE = new EmojiManager();
  private static final int GUESSED_UNICODE_AMOUNT = 3000;
  private static final int GUESSED_TOTAL_PATTERN_LENGTH = GUESSED_UNICODE_AMOUNT * 4;

  private static final Comparator<String> STRING_LENGTH_COMPARATOR = new Comparator<String>() {
    @Override public int compare(final String first, final String second) {
      final int firstLength = first.length();
      final int secondLength = second.length();

      return firstLength < secondLength ? 1 : firstLength == secondLength ? 0 : -1;
    }
  };

  private static final IosEmojiReplacer DEFAULT_EMOJI_REPLACER = new IosEmojiReplacer() {
    @Override public void replaceWithImages(final Context context, final Spannable text, final float emojiSize, final IosEmojiReplacer fallback) {
      final EmojiManager emojiManager = EmojiManager.getInstance();
      final EmojiSpan[] existingSpans = text.getSpans(0, text.length(), EmojiSpan.class);
      final List<Integer> existingSpanPositions = new ArrayList<>(existingSpans.length);

      final int size = existingSpans.length;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0; i < size; i++) {
        existingSpanPositions.add(text.getSpanStart(existingSpans[i]));
      }

      final List<EmojiRange> findAllEmojis = emojiManager.findAllEmojis(text);

      //noinspection ForLoopReplaceableByForEach
      for (int i = 0; i < findAllEmojis.size(); i++) {
        final EmojiRange location = findAllEmojis.get(i);

        if (!existingSpanPositions.contains(location.start)) {
          text.setSpan(new EmojiSpan(context, location.emoji, emojiSize),
                  location.start, location.end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
      }
    }
  };

  private final Map<String, IosEmoji> emojiMap = new LinkedHashMap<>(GUESSED_UNICODE_AMOUNT);
  private IosEmojiCategory[] categories;
  private Pattern emojiPattern;
  private Pattern emojiRepetitivePattern;
  private IosEmojiReplacer emojiReplacer;

  private EmojiManager() {
    // No instances apart from singleton.
  }

  public static EmojiManager getInstance() {
    synchronized (EmojiManager.class) {
      return INSTANCE;
    }
  }

  /**
   * Installs the given EmojiProvider.
   *
   * NOTE: That only one can be present at any time.
   *
   * @param provider the provider that should be installed.
   */
  public static void install(@NonNull final IosEmojiProvider provider) {
    synchronized (EmojiManager.class) {
      INSTANCE.categories = checkNotNull(provider.getCategories(), "categories == null");
      INSTANCE.emojiMap.clear();
      INSTANCE.emojiReplacer = provider instanceof IosEmojiReplacer ? (IosEmojiReplacer) provider : DEFAULT_EMOJI_REPLACER;

      final List<String> unicodesForPattern = new ArrayList<>(GUESSED_UNICODE_AMOUNT);

      final int categoriesSize = INSTANCE.categories.length;
      //noinspection ForLoopReplaceableByForEach
      for (int i = 0; i < categoriesSize; i++) {
       final IosEmoji[] emojis = checkNotNull(INSTANCE.categories[i].getEmojis(), "emojies == null");

        final int emojisSize = emojis.length;
        //noinspection ForLoopReplaceableByForEach
        for (int j = 0; j < emojisSize; j++) {
          final IosEmoji emoji = emojis[j];
          final String unicode = emoji.getUnicode();
          final List<IosEmoji> variants = emoji.getVariants();

          INSTANCE.emojiMap.put(unicode, emoji);
          unicodesForPattern.add(unicode);

          //noinspection ForLoopReplaceableByForEach
          for (int k = 0; k < variants.size(); k++) {
            final IosEmoji variant = variants.get(k);
            final String variantUnicode = variant.getUnicode();

            INSTANCE.emojiMap.put(variantUnicode, variant);
            unicodesForPattern.add(variantUnicode);
          }
        }
      }

      if (unicodesForPattern.isEmpty()) {
        throw new IllegalArgumentException("Your EmojiProvider must at least have one category with at least one emoji.");
      }

      // We need to sort the unicodes by length so the longest one gets matched first.
      Collections.sort(unicodesForPattern, STRING_LENGTH_COMPARATOR);

      final StringBuilder patternBuilder = new StringBuilder(GUESSED_TOTAL_PATTERN_LENGTH);

      final int unicodesForPatternSize = unicodesForPattern.size();
      for (int i = 0; i < unicodesForPatternSize; i++) {
        patternBuilder.append(Pattern.quote(unicodesForPattern.get(i))).append('|');
      }

      final String regex = patternBuilder.deleteCharAt(patternBuilder.length() - 1).toString();
      INSTANCE.emojiPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
      INSTANCE.emojiRepetitivePattern = Pattern.compile('(' + regex + ")+", Pattern.CASE_INSENSITIVE);
    }
  }

  /**
   * Destroys the EmojiManager. This means that all internal data structures are released as well as
   * all data associated with installed {@link IosEmoji}s. For the existing {@link IosEmojiProvider}s this
   * means the memory-heavy emoji sheet.
   *
   * @see #destroy()
   */
  public static void destroy() {
    synchronized (EmojiManager.class) {
      release();

      INSTANCE.emojiMap.clear();
      INSTANCE.categories = null;
      INSTANCE.emojiPattern = null;
      INSTANCE.emojiRepetitivePattern = null;
      INSTANCE.emojiReplacer = null;
    }
  }

  /**
   * Releases all data associated with installed {@link IosEmoji}s. For the existing {@link IosEmojiProvider}s this
   * means the memory-heavy emoji sheet.
   *
   * In contrast to {@link #destroy()}, this does <b>not</b> destroy the internal
   * data structures and thus, you do not need to {@link #install(IosEmojiProvider)} again before using the EmojiManager.
   *
   * @see #destroy()
   */
  public static void release() {
    synchronized (EmojiManager.class) {
      for (final IosEmoji emoji : INSTANCE.emojiMap.values()) {
        emoji.destroy();
      }
    }
  }

  public void replaceWithImages(final Context context, final Spannable text, final float emojiSize) {
    verifyInstalled();

    emojiReplacer.replaceWithImages(context, text, emojiSize, DEFAULT_EMOJI_REPLACER);
  }

  IosEmojiCategory[] getCategories() {
    verifyInstalled();
    return categories; // NOPMD
  }

  Pattern getEmojiRepetitivePattern() {
    return emojiRepetitivePattern;
  }

  @NonNull List<EmojiRange> findAllEmojis(@Nullable final CharSequence text) {
    verifyInstalled();

    final List<EmojiRange> result = new ArrayList<>();

    if (text != null && text.length() > 0) {
      final Matcher matcher = emojiPattern.matcher(text);

      while (matcher.find()) {
        final IosEmoji found = findEmoji(text.subSequence(matcher.start(), matcher.end()));

        if (found != null) {
          result.add(new EmojiRange(matcher.start(), matcher.end(), found));
        }
      }
    }

    return result;
  }

  @Nullable IosEmoji findEmoji(@NonNull final CharSequence candidate) {
    verifyInstalled();

    // We need to call toString on the candidate, since the emojiMap may not find the requested entry otherwise, because the type is different.
    return emojiMap.get(candidate.toString());
  }

  public void verifyInstalled() {
    if (categories == null) {
      throw new IllegalStateException("Please install an EmojiProvider through the EmojiManager.install() method first.");
    }
  }
}

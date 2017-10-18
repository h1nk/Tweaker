package me.hink.tweaker.util.naming;

import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public enum RandomNamePattern {
	RANDOM_NUMBER(Pattern.compile("(?:%)(?:rn)(?:\\{(\\d*)\\})?"), "Random number 0 to 9. Repeat using {n}"),
	RANDOM_ALPHANUMERIC(Pattern.compile("(?:%)(?:ra)(?:\\{(\\d*)\\})?"), "Random alphanumeric character. Repeat using {n}"),
	RANDOM_HEX(Pattern.compile("(?:%)(?:rx)(?:\\{(\\d*)\\})?"), "Random hexadecimal character. Repeat using {n}"),
	UUID(Pattern.compile("(?:%)(?:uuid)"), "Random UUID");

	private final Pattern pattern;
	private final String description;

	RandomNamePattern(final Pattern pattern, final String description) {
		this.pattern = pattern;
		this.description = description;
	}

	private String getValue(@Nullable int count) {
		switch (this) {
			case UUID: {
				return java.util.UUID.randomUUID().toString();
			}
			default: {
				return null;
			}
		}
	}

	public static String format(String string) {
		String copy = string;

		for (RandomNamePattern pattern : Arrays.asList(RandomNamePattern.values())) {
//			copy = copy.replaceAll(pattern.pattern, pattern.getValue());
		}

		return copy;
	}
}

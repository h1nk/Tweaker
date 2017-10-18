package me.hink.tweaker.util.naming;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public enum DateTimeNamePattern {
	CURRENT_YEAR("%y",				"Current year"),
	CURRENT_YEAR2("%yy",			"Current year (2 digits)"),
	CURRENT_MONTH("%mo",			"Current month"),
	CURRENT_MONTH_NAME("%mon",		"Current month name (Local language)"),
	CURRENT_MONTH_NAME_ENG("%mon2",	"Current month name (English)"),
	CURRENT_DAY("%d",				"Current day"),
	CURRENT_HOUR("%h",				"Current hour"),
	CURRENT_MINUTE("%mi",			"Current minute"),
	CURRENT_SECOND("%s",			"Current second"),
	CURRENT_MILLISECOND("%ms",		"Current millisecond"),
	GET_AM_PM("%pm",				"Gets AM/PM"),
	CURRENT_WEEK_NAME("%w",			"Current week name (Local language)"),
	CURRENT_WEEK_NAME2("%w2",		"Current week name (English)"),
	UNIX_TIMESTAMP("%unix",			"Unix timestamp");

	private final String pattern;
	private final String description;

	DateTimeNamePattern(final String pattern, final String description) {
		this.pattern = pattern;
		this.description = description;
	}

	public final String getValue() {
		Date date = new Date();

		switch (this) {
			case CURRENT_YEAR:
				return new SimpleDateFormat("yyyy").format(date);
			case CURRENT_YEAR2:
				return new SimpleDateFormat("yy").format(date);
			case CURRENT_MONTH:
				return new SimpleDateFormat("M").format(date);
			case CURRENT_MONTH_NAME:
				return "";
			case CURRENT_MONTH_NAME_ENG:
				return "";
			case CURRENT_DAY:
				return new SimpleDateFormat("yy").format(date);
			case CURRENT_HOUR:
				return new SimpleDateFormat("HH").format(date);
			case CURRENT_MINUTE:
				return "";
			case CURRENT_SECOND:
				return "";
			case CURRENT_MILLISECOND:
				return new SimpleDateFormat("S").format(date);
			case GET_AM_PM:
				return "";
			case CURRENT_WEEK_NAME2:
				return "";
			case UNIX_TIMESTAMP:
				return String.valueOf(System.currentTimeMillis() / 1000);
			default:
				return null;
		}
	}

	public static String format(String string) {
		String copy = string;

		List<DateTimeNamePattern> patterns = Arrays.asList(DateTimeNamePattern.values());
		Collections.reverse(patterns);

		for (DateTimeNamePattern pattern : patterns) {
			copy = copy.replaceAll(pattern.pattern, pattern.getValue());
		}

		return copy;
	}

	@Override
	public String toString() {
		return pattern;
	}
}
package me.hink.tweaker.util.naming;

/**
 * TODO
 *
 * @author	<a href="https://github.com/h1nk">h1nk</a>
 * @since	0.0.1
 */
public class NamePatternFormatter {
	public static String format(String string) {
		String copy = string;

		copy = DateTimeNamePattern.format(copy);
		copy = RandomNamePattern.format(copy);

		return copy;
	}
}

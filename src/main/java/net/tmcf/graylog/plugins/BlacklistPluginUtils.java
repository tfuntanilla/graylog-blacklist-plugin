package net.tmcf.graylog.plugins;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class BlacklistPluginUtils {

	public static boolean isValidRegex(String pattern) {

		try {
			Pattern.compile(pattern);
		} catch (PatternSyntaxException exception) {
			return false;
		}
		return true;
	}

}

package net.tmcf.graylog.plugins;

import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagPatternCondition extends BlacklistTagDescription {

	private Pattern regex;

	public TagPatternCondition() {
	}

	@JsonProperty
	public void setPattern(String value) {
		this.value = value;
		this.regex = Pattern.compile(value);
	}

	public boolean matchesPattern(Object value) {
		return regex.matcher(String.valueOf(value)).matches();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof TagPatternCondition)) return false;

		TagPatternCondition that = (TagPatternCondition) o;

		return regex.equals(that.regex);

	}

	@Override
	public int hashCode() {
		return regex.hashCode();
	}

}

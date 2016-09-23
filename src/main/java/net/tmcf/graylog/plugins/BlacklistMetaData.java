package net.tmcf.graylog.plugins;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.EnumSet;
import java.util.Set;

public class BlacklistMetaData implements PluginMetaData {

	@Override
	public String getUniqueId() {
		return "net.tmcf.graylog.plugins.BlacklistPlugin";
	}

	@Override
	public String getName() {
		return "Blacklist";
	}

	@Override
	public String getAuthor() {
		return "Trisha Funtanilla";
	}

	@Override
	public URI getURL() {
		return URI.create("https://www.graylog.org/");
	}

	@Override
	public Version getVersion() {
		return new Version(1, 0, 0);
	}

	@Override
	public String getDescription() {
		return "Skip indexing of flagged documents. Blacklist messages without rules.";
	}

	@Override
	public Version getRequiredVersion() {
		return new Version(1, 0, 0);
	}

	@Override
	public Set<ServerStatus.Capability> getRequiredCapabilities() {
		return EnumSet.of(ServerStatus.Capability.SERVER);
	}

}

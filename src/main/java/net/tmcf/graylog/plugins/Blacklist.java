package net.tmcf.graylog.plugins;

import java.util.Set;

import javax.inject.Inject;

import org.graylog2.database.NotFoundException;
import org.graylog2.plugin.Message;
import org.graylog2.plugin.filters.MessageFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple message filter for blacklisting
 * Looks for tags that are saved in Mongo or the standard attribute "flag"
 * and blacklist messages accordingly
 */
public class Blacklist implements MessageFilter {

	private static final Logger LOG = LoggerFactory.getLogger(Blacklist.class);

	private static final String NAME = "Blacklist";

	private BlacklistPluginService blacklistService;

	// standard attribute for flagging docs; any doc that has this field is automatically blacklisted
	private static final String FLAG = "flag";

	@Inject
	public Blacklist(BlacklistPluginService blacklistService) {
		this.blacklistService = blacklistService;
	}

	@Override
	public boolean filter(Message msg) {

		Set<BlacklistTagDescription> tags;
		try {
			tags = blacklistService.loadAll();
			for (BlacklistTagDescription tag : tags) {
				if (msg.hasField(FLAG)) {
					LOG.info("Message has 'flag' field. Blacklisting message...");
					return true;
				} else if (msg.hasField(tag.field)) {
					if (msg.getField(tag.field).equals(tag.value)) {
						LOG.info("Message has field & value pair that is tagged for flagging.");
						return true;
					} else if (BlacklistPluginUtils.isValidRegex(tag.value)
							&& String.valueOf(msg.getField(tag.field)).matches(tag.value)) {
						LOG.info("Message has field & value pair that is tagged for flagging.");
						return true;
					}
				}
			}
		} catch (NotFoundException e) {
			LOG.error("Exception while trying to load the flags.", e);
		} catch (Exception e) {
			LOG.error("Exception while trying to flag message.", e);
		}

		return false;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public int getPriority() {
		return 31; // run after rules
	}

}

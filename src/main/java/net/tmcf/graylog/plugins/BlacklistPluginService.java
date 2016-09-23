package net.tmcf.graylog.plugins;

import java.util.Set;

import org.graylog2.database.NotFoundException;
import org.graylog2.plugin.database.ValidationException;

public interface BlacklistPluginService {
	
	BlacklistTagDescription load(String tagId) throws NotFoundException;
	
	Set<BlacklistTagDescription> loadAll() throws NotFoundException;
	
	BlacklistTagDescription save(BlacklistTagDescription tag) throws ValidationException;

	boolean validate(BlacklistTagDescription tag);
	
	int delete(String tagId);

}

package net.tmcf.graylog.plugins;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.bson.types.ObjectId;
import org.graylog2.bindings.providers.MongoJackObjectMapperProvider;
import org.graylog2.database.MongoConnection;
import org.graylog2.database.NotFoundException;
import org.graylog2.plugin.database.ValidationException;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

@Singleton
public class BlacklistPluginServiceImpl implements BlacklistPluginService {

	public static final String TAGS = "tags";

	private final JacksonDBCollection<BlacklistTagDescription, ObjectId> dbCollection;

	@Inject
	protected BlacklistPluginServiceImpl(MongoConnection mongoConnection, MongoJackObjectMapperProvider mapper) {

		dbCollection = JacksonDBCollection.wrap(mongoConnection.getDatabase().getCollection(TAGS),
				BlacklistTagDescription.class, ObjectId.class, mapper.get());

	}
	
	@Override
	public Set<BlacklistTagDescription> loadAll() throws NotFoundException {
		
		final DBCursor<BlacklistTagDescription> blacklistTagDescriptions = dbCollection.find();
		Set<BlacklistTagDescription> tags = Sets.newHashSet();
		if (blacklistTagDescriptions.hasNext()) {
			Iterators.addAll(tags, blacklistTagDescriptions);
		}
		return tags;
	}


	@Override
	public int delete(String tagId) {
		return dbCollection.removeById(new ObjectId(tagId)).getN();
	}

	@Override
	public BlacklistTagDescription load(String tagId) throws NotFoundException {
		
		final BlacklistTagDescription tag = dbCollection.findOneById(new ObjectId(tagId));
		if (tag == null) {
			throw new NotFoundException();
		}
		return tag;
	
	}

	@Override
	public BlacklistTagDescription save(BlacklistTagDescription tag) throws ValidationException {
		
		if (!validate(tag)) {
			throw new ValidationException("Validation failed. The property 'field' cannot be null or empty.");
		}	
		final WriteResult<BlacklistTagDescription, ObjectId> writeResult = dbCollection.save(tag);
		
		return writeResult.getSavedObject();
	
	}

	@Override
	public boolean validate(BlacklistTagDescription tag) {
		
		if (tag.field == null) {
			return false;
		}
		if (tag.field.length() <= 0) {
			return false;
		}
		return true;
	}

}

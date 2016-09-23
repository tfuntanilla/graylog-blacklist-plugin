package net.tmcf.graylog.plugins;

import javax.persistence.Id;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TagFieldEqualityCondition.class, name = "string"),
    @JsonSubTypes.Type(value = TagPatternCondition.class, name = "regex")
})
public abstract class BlacklistTagDescription {
	
	@Id
    @org.mongojack.ObjectId
    @JsonProperty("id")
    public ObjectId _id;
	
	public String field;
	
	public String value;

	@Override
	public String toString() {
		return getClass().getSimpleName() + "{"
				+ "_id=" + _id.toString() +
				", field=" + field +
				", value=" + value + "}";
	}

}

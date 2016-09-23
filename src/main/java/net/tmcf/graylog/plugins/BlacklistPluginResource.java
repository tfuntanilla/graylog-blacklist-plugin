package net.tmcf.graylog.plugins;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.graylog2.database.NotFoundException;
import org.graylog2.plugin.database.ValidationException;
import org.graylog2.plugin.rest.PluginRestResource;
import org.graylog2.shared.rest.resources.RestResource;
import org.hibernate.validator.constraints.NotEmpty;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

@RequiresAuthentication
@Api(value = "Blacklist", description = "Skip indexing of flagged documents")
@Path("/filters/blacklistplugin")
public class BlacklistPluginResource extends RestResource implements PluginRestResource {
	
	private final BlacklistPluginService blacklistService;

	@Inject
	public BlacklistPluginResource(BlacklistPluginService blacklistService) {
		this.blacklistService = blacklistService;
	}

	@POST
    @Timed
    @ApiOperation(value = "Create a tag for blacklisting", notes = "It can take up to a second until the change is applied")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response createFlagObject(@ApiParam(name = "tag", required = true) 
		@Valid @NotNull BlacklistTagDescription blacklistTagDescription) throws ValidationException {

		final BlacklistTagDescription savedTag = blacklistService.save(blacklistTagDescription);
		final URI tagUri = getUriBuilderToSelf().path(BlacklistPluginResource.class).path("{tagId}").build(savedTag._id);
		return Response.created(tagUri).entity(savedTag).build();
	}

	@GET
	@Timed
	@ApiOperation("Get all tags")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<BlacklistTagDescription> getAll() {
		
		try {
			return blacklistService.loadAll();
		} catch (org.graylog2.database.NotFoundException e) {
			return Collections.emptySet();
		}
	}

	@GET
	@Timed
	@Path("/{tagId}")
	@ApiOperation("Get the existing tag")
	@Produces(MediaType.APPLICATION_JSON)
	public BlacklistTagDescription get(@ApiParam(name = "tagId", required = true) @PathParam("tagId") @NotEmpty String tagId) 
			throws org.graylog2.database.NotFoundException {
		return blacklistService.load(tagId);
	}

	@PUT
	@Timed
	@Path("/{tagId}")
	@ApiOperation(value = "Update an existing tag", notes = "It can take up to a second until the change is applied")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(@ApiParam(name = "tagId", required = true) @PathParam("tagId") String tagId, 
			@ApiParam(name = "tagEntry", required = true) BlacklistTagDescription tagEntry) 
			throws org.graylog2.database.NotFoundException, ValidationException {
		
		BlacklistTagDescription tag = blacklistService.load(tagId);

		// did the tag type change?
		if (!tag.getClass().equals(tagEntry.getClass())) {
			// copy the relevant fields from the saved tag and then use the new class
			tagEntry._id = tag._id;
			tag = tagEntry;
		} else {
			// just copy the changable fields
			tag.field = tagEntry.field;
			tag.value = tagEntry.value;
		}
		blacklistService.save(tag);
	}

	@DELETE
	@Timed
	@ApiOperation(value = "Remove the existing tag", notes = "It can take up to a second until the change is applied")
	@Path("/{tagId}")
	public void delete(@ApiParam(name = "tagId", required = true) 
		@PathParam("tagId") String tagId) throws NotFoundException {
		
		if (blacklistService.delete(tagId) == 0) {
			throw new NotFoundException();
		}
	}

}

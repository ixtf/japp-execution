package org.jzb.execution.interfaces.rest.resource;

import org.jzb.execution.domain.Channel;
import org.jzb.execution.domain.repository.ChannelRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("channels")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class ChannelResource {
    @Inject
    private ChannelRepository channelRepository;

    @GET
    public Collection<Channel> login(@Context SecurityContext sc) {
        return channelRepository.findAll().collect(Collectors.toList());
    }

}

package org.jzb.execution.interfaces.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.ApplicationEvents;
import org.jzb.execution.application.PlanService;
import org.jzb.execution.application.command.PlanDownloadCommand;
import org.jzb.execution.application.command.PlanUpdateCommand;
import org.jzb.execution.application.query.PlanQuery;
import org.jzb.execution.domain.Plan;
import org.jzb.execution.domain.PlanFollow;
import org.jzb.execution.domain.PlanInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.ChannelRepository;
import org.jzb.execution.domain.repository.PlanFollowRepository;
import org.jzb.execution.domain.repository.PlanRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.jzb.Constant.MAPPER;

/**
 * Created by jzb on 17-4-15.
 */
@Path("plans")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class PlanResource {
    @Inject
    private ChannelRepository channelRepository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private PlanFollowRepository planFollowRepository;
    @Inject
    private PlanService planService;
    @Inject
    private ApplicationEvents applicationEvents;

    @POST
    public Plan create(@Context SecurityContext sc, @Valid @NotNull PlanUpdateCommand command) {
        return planService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public Plan update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull PlanUpdateCommand command) throws Exception {
        return planService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public Plan update(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        return planRepository.find(id);
    }

    @Path("{id}/followersCount")
    @GET
    public JsonNode followersCount(@Valid @NotBlank @PathParam("id") String id) {
        long count = planFollowRepository.countByPlanId(id);
        return MAPPER.createObjectNode().put("followersCount", count);
    }

    @Path("{id}/followers")
    @GET
    public Collection<Operator> getFollowers(@Valid @NotBlank @PathParam("id") String id,
                                             @Valid @Min(0) @QueryParam("first") @DefaultValue("0") int first,
                                             @Valid @Min(10) @Max(100) @QueryParam("pageSize") @DefaultValue("50") int pageSize) {
        return planFollowRepository.queryByPlanId(id, first, pageSize)
                .map(PlanFollow::getFollower)
                .collect(Collectors.toList());
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        planService.delete(sc.getUserPrincipal(), id);
    }

    @GET
    public JsonNode query(@Context SecurityContext sc,
                          @Valid @Min(0) @QueryParam("first") @DefaultValue("0") int first,
                          @Valid @Min(10) @Max(100) @QueryParam("pageSize") @DefaultValue("50") int pageSize,
                          @QueryParam("channelId") String channelId,
                          @QueryParam("shared") Boolean shared,
                          @QueryParam("audited") Boolean audited,
                          @QueryParam("published") Boolean published) throws Exception {
        return new PlanQuery(sc.getUserPrincipal(), first, pageSize)
                .channelId(channelId)
                .shared(shared)
                .published(published)
                .audited(audited)
                .exe(planRepository)
                .result;
    }

    @Path("/{id}/inviteTicket")
    @GET
    public PlanInvite inviteToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return planService.inviteTicket(sc.getUserPrincipal(), id);
    }

    @Path("{id}/publish")
    @PUT
    public void publish(@Context SecurityContext securityContext,
                        @Valid @NotBlank @PathParam("id") String id) throws Exception {
        planService.publish(securityContext.getUserPrincipal(), id);
    }

    @Path("{id}/publish")
    @DELETE
    public void unPublish(@Context SecurityContext securityContext,
                          @Valid @NotBlank @PathParam("id") String id) throws Exception {
        planService.unPublish(securityContext.getUserPrincipal(), id);
    }

    @Path("{id}/follow")
    @PUT
    public void follow(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        planService.follow(sc.getUserPrincipal(), id);
    }

    @Path("{id}/follow")
    @DELETE
    public void unFollow(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        planService.unFollow(sc.getUserPrincipal(), id);
    }

    @Path("{id}/download")
    @POST
    public void downloadPlan(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull PlanDownloadCommand command) throws Exception {
        planService.download(sc.getUserPrincipal(), id, command);
    }

}

package org.jzb.execution.interfaces.rest.resource.admin;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.AdminService;
import org.jzb.execution.application.command.PlanAuditCommand;
import org.jzb.execution.application.query.PlanQuery;
import org.jzb.execution.domain.repository.PlanRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AdminPlanResource {
    @Inject
    private PlanRepository planRepository;
    @Inject
    private AdminService adminService;

    @GET
    public Response auditPlan(@Context SecurityContext sc,
                              @Valid @Min(0) @QueryParam("first") @DefaultValue("0") int first,
                              @Valid @Min(10) @Max(100) @QueryParam("pageSize") @DefaultValue("50") int pageSize) throws Exception {
        PlanQuery query = new PlanQuery(null, first, pageSize)
                .audited(false)
                .published(true)
                .exe(planRepository);
        return Response.ok(query.result).build();
    }

    @Path("{id}/audit")
    @PUT
    public void auditPlan(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotNull PlanAuditCommand command) throws Exception {
        adminService.auditPlan(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}/audit")
    @DELETE
    public void unAuditPlan(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        adminService.unAuditPlan(sc.getUserPrincipal(), id);
    }

}

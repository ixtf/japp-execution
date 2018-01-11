package org.jzb.execution.interfaces.rest.resource.admin;

import org.jzb.exception.JNonAuthorizationError;
import org.jzb.execution.application.AdminService;
import org.jzb.execution.application.command.ChannelUpdateCommand;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.PlanRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("admin")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AdminResource {
    @Inject
    private AdminService adminService;
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private AdminPaymentMerchantResource adminPaymentMerchantResource;
    @Inject
    private AdminChannelResource adminChannelResource;
    @Inject
    private AdminPlanResource adminPlanResource;

    private void checkAdmin(Principal principal) {
        final Operator operator = operatorRepository.find(principal);
        if (!operator.isAdmin()) {
            throw new JNonAuthorizationError();
        }
    }

    @Path("paymentMerchants")
    public AdminPaymentMerchantResource paymentMerchants(@Context SecurityContext sc) {
        checkAdmin(sc.getUserPrincipal());
        return adminPaymentMerchantResource;
    }

    @Path("channels")
    public AdminChannelResource channels(@Context SecurityContext sc, @Valid @NotNull ChannelUpdateCommand command) {
        checkAdmin(sc.getUserPrincipal());
        return adminChannelResource;
    }

    @Path("plans")
    public AdminPlanResource auditPlan(@Context SecurityContext sc) throws Exception {
        checkAdmin(sc.getUserPrincipal());
        return adminPlanResource;
    }

}

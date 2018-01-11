package org.jzb.execution.interfaces.rest.resource;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.PaymentMerchantService;
import org.jzb.execution.domain.PaymentMerchant;
import org.jzb.execution.domain.PaymentMerchantInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.execution.domain.repository.PaymentMerchantRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Path("paymentMerchants")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class PaymentMerchantResource {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private PaymentMerchantRepository paymentMerchantRepository;
    @Inject
    private PaymentMerchantService paymentMerchantService;

    @GET
    public Collection<PaymentMerchant> list(@Context SecurityContext sc) {
        final Operator manager = operatorRepository.find(sc.getUserPrincipal());
        return paymentMerchantRepository.queryByManager(manager).collect(Collectors.toList());
    }

    @Path("{id}")
    @GET
    public PaymentMerchant get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return paymentMerchantRepository.find(id);
    }

    @Path("{id}/inviteTicket")
    @GET
    public PaymentMerchantInvite inviteToken(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) throws Exception {
        return paymentMerchantService.inviteTicket(sc.getUserPrincipal(), id);
    }

    @Path("{id}/managers/{managerId}")
    @DELETE
    public void get(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id, @Valid @NotBlank @PathParam("managerId") String managerId) throws Exception {
        paymentMerchantService.deleteManager(sc.getUserPrincipal(), id, managerId);
    }
}

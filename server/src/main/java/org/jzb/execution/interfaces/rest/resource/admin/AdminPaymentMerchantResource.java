package org.jzb.execution.interfaces.rest.resource.admin;

import org.hibernate.validator.constraints.NotBlank;
import org.jzb.execution.application.PaymentMerchantService;
import org.jzb.execution.application.command.PaymentMerchantUpdateCommand;
import org.jzb.execution.domain.PaymentMerchant;
import org.jzb.execution.domain.repository.PaymentMerchantRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class AdminPaymentMerchantResource {
    @Inject
    private PaymentMerchantRepository paymentMerchantRepository;
    @Inject
    private PaymentMerchantService paymentMerchantService;

    @POST
    public PaymentMerchant create(@Context SecurityContext sc, @Valid @NotNull PaymentMerchantUpdateCommand command) {
        return paymentMerchantService.create(sc.getUserPrincipal(), command);
    }

    @Path("{id}")
    @PUT
    public PaymentMerchant update(@Context SecurityContext sc,
                                  @Valid @NotBlank @PathParam("id") String id,
                                  @Valid @NotNull PaymentMerchantUpdateCommand command) {
        return paymentMerchantService.update(sc.getUserPrincipal(), id, command);
    }

    @Path("{id}")
    @GET
    public PaymentMerchant get(@Valid @NotBlank @PathParam("id") String id) {
        return paymentMerchantRepository.find(id);
    }

    @Path("{id}")
    @DELETE
    public void delete(@Context SecurityContext sc, @Valid @NotBlank @PathParam("id") String id) {
        paymentMerchantService.delete(sc.getUserPrincipal(), id);
    }

    @GET
    public Collection<PaymentMerchant> list() {
        return paymentMerchantRepository.findAll().collect(Collectors.toList());
    }

    @Path("{id}/managers/{managerId}")
    @PUT
    public void addManager(@Context SecurityContext sc,
                           @Valid @NotBlank @PathParam("id") String id,
                           @Valid @NotBlank @PathParam("managerId") String managerId) {
        paymentMerchantService.addManager(sc.getUserPrincipal(), id, managerId);
    }

    @Path("{id}/managers/{managerId}")
    @DELETE
    public void removeManager(@Context SecurityContext sc,
                              @Valid @NotBlank @PathParam("id") String id,
                              @Valid @NotBlank @PathParam("managerId") String managerId) {
        paymentMerchantService.removeManager(sc.getUserPrincipal(), id, managerId);
    }

}

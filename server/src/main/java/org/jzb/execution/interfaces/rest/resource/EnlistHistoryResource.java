package org.jzb.execution.interfaces.rest.resource;

import org.jboss.resteasy.annotations.GZIP;
import org.jzb.execution.domain.Enlist;
import org.jzb.execution.domain.repository.EnlistRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.Collection;
import java.util.stream.Collectors;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by jzb on 17-4-15.
 */
@GZIP
@Path("enlistHistory")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Stateless
public class EnlistHistoryResource {
    @Inject
    private EnlistRepository enlistRepository;

    @GET
    public Collection<Enlist> current(@Context SecurityContext sc,
                                      @QueryParam("q") String q,
                                      @Valid @Min(0) @DefaultValue("0") @QueryParam("first") int first,
                                      @Valid @Min(1) @Max(1000) @DefaultValue("50") @QueryParam("pageSize") int pageSize) throws Exception {
        return enlistRepository.query(sc.getUserPrincipal())
                .filter(enlist -> enlist.isFinish())
                .skip(first)
                .limit(pageSize)
                .collect(Collectors.toSet());
    }
}

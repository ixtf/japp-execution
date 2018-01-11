package org.jzb.execution.application.query;

import com.fasterxml.jackson.databind.JsonNode;
import org.jzb.execution.domain.repository.PlanRepository;
import org.jzb.search.JPageQuery;

import java.security.Principal;

/**
 * Created by jzb on 17-4-23.
 */
public class PlanQuery extends JPageQuery<JsonNode> {
    private String channelId;
    private Boolean published;
    private Boolean audited;
    private Boolean shared;

    public PlanQuery(Principal principal, int first, Integer pageSize) {
        super(principal, first, pageSize);
    }

    public PlanQuery exe(PlanRepository planRepository) throws Exception {
        planRepository.query(this);
        return this;
    }


    public PlanQuery channelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    public PlanQuery published(Boolean published) {
        this.published = published;
        return this;
    }

    public PlanQuery audited(Boolean audited) {
        this.audited = audited;
        return this;
    }

    public PlanQuery shared(Boolean shared) {
        this.shared = shared;
        return this;
    }

    public String getChannelId() {
        return channelId;
    }

    public Boolean getPublished() {
        return published;
    }

    public Boolean getAudited() {
        return audited;
    }

    public Boolean getShared() {
        return shared;
    }
}

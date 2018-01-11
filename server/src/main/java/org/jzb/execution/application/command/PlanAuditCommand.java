package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by jzb on 17-4-15.
 */

public class PlanAuditCommand implements Serializable {
    @NotNull
    private EntityDTO channel;

    public EntityDTO getChannel() {
        return channel;
    }

    public void setChannel(EntityDTO channel) {
        this.channel = channel;
    }
}
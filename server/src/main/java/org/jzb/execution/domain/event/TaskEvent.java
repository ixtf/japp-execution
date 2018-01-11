package org.jzb.execution.domain.event;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by jzb on 17-4-15.
 */
@Entity
@Table(name = "T_TASKEVENT")
//@Table(name = "T_TASKLOGEVENT")
@NamedQueries({
        @NamedQuery(name = "TaskEvent.findAll", query = "SELECT o FROM TaskEvent o"),
        @NamedQuery(name = "TaskEvent.countByTaskId", query = "SELECT COUNT(o) FROM TaskEvent o WHERE o.entityId=:taskId"),
        @NamedQuery(name = "TaskEvent.queryByTaskId", query = "SELECT o FROM TaskEvent o WHERE o.entityId=:taskId ORDER BY o.dateTime DESC"),
})
public class TaskEvent extends EventEntity {
}

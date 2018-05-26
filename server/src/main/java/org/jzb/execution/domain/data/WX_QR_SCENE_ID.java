package org.jzb.execution.domain.data;

/**
 * @author jzb
 */
public enum WX_QR_SCENE_ID {

    PAYMENTMERCHANT_INVITE(1),
    PLAN_INVITE(2),
    EXAMQUESTIONLAB_INVITE(3),
    TASK_FOLLOW_INVITE(4),
    TASKS_INVITE(5),
    //邀请成为计划管理员，加入 follower，多计划
    TASKS_FOLLOW_INVITE(6),
    ENLIST_INVITE(7),
    REDENVELOPEORGANIZATION_INVITE(8);

    private final int scene_id;

    WX_QR_SCENE_ID(int scene_id) {
        this.scene_id = scene_id;
    }

    public int scene_id() {
        return scene_id;
    }
}

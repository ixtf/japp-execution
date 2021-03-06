package org.jzb.execution.domain.data;

/**
 * @author jzb
 */
//修改的时候不要改变顺序
public enum EnlistStatus {

    RUN("进行"), FINISH("完成");
    private final String displayName;

    EnlistStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}

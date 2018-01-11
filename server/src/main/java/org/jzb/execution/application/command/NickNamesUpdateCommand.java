package org.jzb.execution.application.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

public class NickNamesUpdateCommand extends TaskBatchBaseCommand {
    @Size(min = 1)
    @NotNull
    private Map<String, String> nickNameMap;

    public Map<String, String> getNickNameMap() {
        return nickNameMap;
    }

    public void setNickNameMap(Map<String, String> nickNameMap) {
        this.nickNameMap = nickNameMap;
    }
}

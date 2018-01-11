package org.jzb.execution.application.internal;

import org.jzb.execution.application.ExamQuestionLabService;
import org.jzb.execution.application.command.ExamQuestionLabUpdateCommand;
import org.jzb.execution.domain.extra.ExamQuestionLab;
import org.jzb.execution.domain.extra.ExamQuestionLabInvite;
import org.jzb.execution.domain.operator.Operator;
import org.jzb.execution.domain.repository.ExamQuestionLabRepository;
import org.jzb.execution.domain.repository.OperatorRepository;
import org.jzb.weixin.mp.MpClient;
import org.jzb.weixin.mp.qrcode.MpQrcodeResponse;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jzb.execution.domain.data.WX_QR_SCENE_ID.EXAMQUESTIONLAB_INVITE;

/**
 * Created by jzb on 17-2-27.
 */
@Stateless
public class ExamQuestionLabServiceImpl implements ExamQuestionLabService {
    @Inject
    private OperatorRepository operatorRepository;
    @Inject
    private ExamQuestionLabRepository examQuestionLabRepository;
    @Inject
    private MpClient mpClient;

    private void checkUpdateAuth(Principal principal, String id) {
        Operator operator = operatorRepository.find(principal);
        ExamQuestionLab lab = examQuestionLabRepository.find(id);
        if (lab.isDeleted() || operator.isDeleted() || !lab.getCreator().equals(operator)) {
            throw new RuntimeException();
        }
    }

    @Override
    public ExamQuestionLab create(Principal principal, ExamQuestionLabUpdateCommand command) {
        return save(principal, new ExamQuestionLab(), command);
    }

    private ExamQuestionLab save(Principal principal, ExamQuestionLab o, ExamQuestionLabUpdateCommand command) {
        o.setName(command.getName());
        final Operator operator = operatorRepository.find(principal);
        o._loginfo(operator);
        return examQuestionLabRepository.save(o);
    }

    @Override
    public ExamQuestionLab update(Principal principal, String id, ExamQuestionLabUpdateCommand command) {
        if ("0".equals(id))
            throw new RuntimeException();
        return save(principal, examQuestionLabRepository.find(id), command);
    }

    @Override
    public void delete(Principal principal, String id) {
        checkUpdateAuth(principal, id);
        examQuestionLabRepository.delete(id);
    }

    @Override
    public void deleteParticipant(Principal principal, String id, String participantId) {
        ExamQuestionLab lab = examQuestionLabRepository.find(id);
        // lab.getParticipants().stream()
        //         .parallel()
        //         .filter(it -> Objects.equals(principal.getName(), it.getId()))
        //         .findFirst()
        //         .orElseThrow(() -> new JNonAuthorizationError());
        Collection<Operator> participants = lab.getParticipants()
                .stream()
                .parallel()
                .filter(it -> !Objects.equals(participantId, it.getId()))
                .collect(Collectors.toSet());
        lab.setParticipants(participants);
        final Operator operator = operatorRepository.find(principal);
        lab._loginfo(operator);
        examQuestionLabRepository.save(lab);
    }

    @Override
    public ExamQuestionLabInvite inviteTicket(Principal principal, String id) throws Exception {
        // checkUpdateAuth(principal, id);
        ExamQuestionLab lab = examQuestionLabRepository.find(id);
        final MpQrcodeResponse res = mpClient.qrcode()
                .expire_seconds(2592000)
                .action_name("QR_SCENE")
                .scene_id(EXAMQUESTIONLAB_INVITE.scene_id())
                .build()
                .call();
        ExamQuestionLabInvite invite = new ExamQuestionLabInvite();
        invite.setLab(lab);
        invite.setTicket(res.ticket());
        invite.setExpireSeconds(res.expire_seconds());
        return examQuestionLabRepository.save(invite);
    }


}

package com.hupu.deep.comment.web.controller;

import com.hupu.deep.black.facade.BlackFacade;
import com.hupu.deep.black.request.BlackRequest;
import com.hupu.deep.black.service.BlackService;
import com.hupu.deep.black.service.BlackSubjectService;
import com.hupu.deep.comment.enums.BlackStatusEnum;
import com.hupu.deep.comment.model.BlackRecordModel;
import com.hupu.deep.comment.model.subject.BlackSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.foundation.result.SimpleResult;
import com.hupu.foundation.util.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author jiangfangyuan
 * @since 2020-03-11 16:03
 */
@RestController
public class BlackController implements BlackFacade {

    @Autowired
    private BlackService blackService;

    @Autowired
    private BlackSubjectService blackSubjectService;


    /**
     * 点灭
     *
     * @param request
     * @return
     */
    @Override
    public SimpleResult<String> black(@RequestBody BlackRequest request) {

        AssertUtil.notNull(request.getUserId(), () -> "userId null");

        String subjectId = blackSubjectService.getOrCreateSubjectId(request.getOutBizKey());
        blackService.black(request.getUserId(), subjectId);
        return SimpleResult.success(Globals.Y);
    }


    /**
     * 取消点灭
     *
     * @param request
     * @return
     */
    @Override
    public SimpleResult<String> cancelBlack(@RequestBody BlackRequest request) {

        AssertUtil.notNull(request.getUserId(), () -> "userId null");

        String subjectId = blackSubjectService.getOrCreateSubjectId(request.getOutBizKey());
        blackService.cancelBlack(request.getUserId(), subjectId);
        return SimpleResult.success(Globals.Y);
    }

    /**
     * 是否点灭
     *
     * @param request
     * @return
     */
    @Override
    public SimpleResult<String> hasBlack(@RequestBody BlackRequest request) {

        AssertUtil.notNull(request.getUserId(), () -> "userId null");

        String subjectId = blackSubjectService.getOrCreateSubjectId(request.getOutBizKey());
        BlackRecordModel BlackRecordModel = blackService.getUserBlackRecordModel(request.getUserId(), subjectId);
        if (null == BlackRecordModel) {
            return SimpleResult.success(Globals.N);
        }
        if (Objects.equals(BlackRecordModel.getBlackStatusEnum(), BlackStatusEnum.CANCEL_BLACK)) {
            return SimpleResult.success(Globals.N);
        }
        if (Objects.equals(BlackRecordModel.getBlackStatusEnum(), BlackStatusEnum.BLACK)) {
            return SimpleResult.success(Globals.Y);
        }
        throw new RuntimeException("can not arrive here");
    }

    @Override
    public SimpleResult<BlackSubjectModel> subjectInfo(@RequestBody BlackRequest request) {

        OutBizKey outBizKey = request.getOutBizKey();
        String subjectId = blackSubjectService.getSubjectId(outBizKey);
        if (StringUtils.isBlank(subjectId)) {
            return SimpleResult.success(null);
        }
        BlackSubjectModel blackSubjectModel = new BlackSubjectModel(outBizKey, subjectId);
        blackSubjectModel.setBlackCount(blackSubjectService.getBlackCount(outBizKey));
        return SimpleResult.success(blackSubjectModel);
    }
}

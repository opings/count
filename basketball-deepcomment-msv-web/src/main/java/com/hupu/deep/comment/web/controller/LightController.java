package com.hupu.deep.comment.web.controller;

import com.hupu.deep.comment.enums.LightStatusEnum;
import com.hupu.deep.comment.model.LightRecordModel;
import com.hupu.deep.comment.model.subject.LightSubjectModel;
import com.hupu.deep.comment.types.OutBizKey;
import com.hupu.deep.comment.util.Globals;
import com.hupu.deep.light.facade.LightFacade;
import com.hupu.deep.light.request.LightRequest;
import com.hupu.deep.light.service.LightService;
import com.hupu.deep.light.service.LightSubjectService;
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
public class LightController implements LightFacade {

    @Autowired
    private LightService lightService;

    @Autowired
    private LightSubjectService lightSubjectService;


    /**
     * 点亮
     *
     * @param request
     * @return
     */
    @Override
    public SimpleResult<String> light(@RequestBody LightRequest request) {

        AssertUtil.notNull(request.getUserId(), () -> "userId null");

        String subjectId = lightSubjectService.getOrCreateSubjectId(request.getOutBizKey());
        lightService.light(request.getUserId(), subjectId);
        return SimpleResult.success(Globals.Y);
    }


    /**
     * 取消点亮
     *
     * @param request
     * @return
     */
    @Override
    public SimpleResult<String> cancelLight(@RequestBody LightRequest request) {

        AssertUtil.notNull(request.getUserId(), () -> "userId null");

        String subjectId = lightSubjectService.getOrCreateSubjectId(request.getOutBizKey());
        lightService.cancelLight(request.getUserId(), subjectId);
        return SimpleResult.success(Globals.Y);
    }

    /**
     * 是否点亮
     *
     * @param request
     * @return
     */
    @Override
    public SimpleResult<String> hasLight(@RequestBody LightRequest request) {

        AssertUtil.notNull(request.getUserId(), () -> "userId null");

        String subjectId = lightSubjectService.getOrCreateSubjectId(request.getOutBizKey());
        LightRecordModel lightRecordModel = lightService.getUserLightRecordModel(request.getUserId(), subjectId);
        if (null == lightRecordModel) {
            return SimpleResult.success(Globals.N);
        }
        if (Objects.equals(lightRecordModel.getLightStatusEnum(), LightStatusEnum.CANCEL_LIGHT)) {
            return SimpleResult.success(Globals.N);
        }
        if (Objects.equals(lightRecordModel.getLightStatusEnum(), LightStatusEnum.LIGHT)) {
            return SimpleResult.success(Globals.Y);
        }
        throw new RuntimeException("can not arrive here");
    }

    @Override
    public SimpleResult<LightSubjectModel> subjectInfo(@RequestBody LightRequest request) {

        OutBizKey outBizKey = request.getOutBizKey();
        String subjectId = lightSubjectService.getSubjectId(outBizKey);
        if (StringUtils.isBlank(subjectId)) {
            return SimpleResult.success(null);
        }
        LightSubjectModel lightSubjectModel = new LightSubjectModel(outBizKey, subjectId);
        lightSubjectModel.setLightCount(lightSubjectService.getLightCount(outBizKey));
        return SimpleResult.success(lightSubjectModel);
    }
}

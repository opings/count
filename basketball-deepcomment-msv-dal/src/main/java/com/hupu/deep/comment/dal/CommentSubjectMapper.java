package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.entity.CommentSubjectDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentSubjectMapper {


    int insertSelective(CommentSubjectDO record);

    CommentSubjectDO selectBySubjectId(@Param("subjectId") String subjectId);

    CommentSubjectDO selectByOutBizKey(@Param("outBizType") String outBizType, @Param("outBizNo") String outBizNo);


    List<String> outBizTypeList();

}
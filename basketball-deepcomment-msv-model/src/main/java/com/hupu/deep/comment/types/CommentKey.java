package com.hupu.deep.comment.types;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;
import com.hupu.foundation.util.AssertUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * @author jiangfangyuan
 * @since 2020-03-13 01:08
 */
@ToString
@EqualsAndHashCode
public class CommentKey {

    /**
     * 主体id
     */
    @Getter
    private String subjectId;

    /**
     * 评论Id
     */
    @Getter
    private String commentId;

    @JsonCreator
    public CommentKey(@JsonProperty("subjectId") String subjectId, @JsonProperty("commentId") String commentId) {

        AssertUtil.notBlank(subjectId, () -> "subjectId empty");
        AssertUtil.notBlank(commentId, () -> "commentId empty");

        this.subjectId = subjectId;
        this.commentId = commentId;
    }

    public String getKey() {

        return Joiner.on(":").join(subjectId, commentId);
    }


}

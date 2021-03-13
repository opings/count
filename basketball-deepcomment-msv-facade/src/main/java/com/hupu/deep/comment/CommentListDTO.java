package com.hupu.deep.comment;

import com.hupu.deep.comment.model.CommentModel;
import com.hupu.deep.comment.request.CommentCursor;
import lombok.Data;

import java.util.List;

/**
 * @author jiangfangyuan
 * @since 2020-05-15 21:06
 */
@Data
public class CommentListDTO {

    private List<CommentModel> commentModelList;

    private CommentCursor commentCursor;
}

package com.hupu.deep.comment.dal;

import com.hupu.deep.comment.BaseTest;
import com.hupu.deep.comment.entity.CommentRecordDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhuwenkang
 * @Time 2020年03月12日 18:33:00
 */
public class CommentRecordMapperTest extends BaseTest {

    @Autowired
    private CommentRecordMapper commentRecordMapper;

    @Test
    public void insertTest(){
        CommentRecordDO commentRecordDO = new CommentRecordDO();
        commentRecordDO.setSubjectId("23423");
        commentRecordDO.setCommentId("34234234");
        commentRecordDO.setUserId(2323L);
        commentRecordDO.setCommentContent("sjbdhbcsdjjfhcsd");
       // commentRecordMapper.insertSelective(commentRecordDO);
    }
}

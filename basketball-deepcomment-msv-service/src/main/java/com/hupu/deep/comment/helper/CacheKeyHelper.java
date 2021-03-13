package com.hupu.deep.comment.helper;

import com.google.common.base.Joiner;

/**
 * 2 * @Author: xuronghua
 * 3 * @Date: 2020/8/5 9:54 PM
 * 4
 */
public class CacheKeyHelper {

    public static String buildLastDanmakus(String subjectId, Integer startIndex, Integer limit,String version) {
        return Joiner.on("-").join("lastDanmakus", subjectId, startIndex,limit,version );
    }
}

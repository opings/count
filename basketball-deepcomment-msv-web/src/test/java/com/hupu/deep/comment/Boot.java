package com.hupu.deep.comment;

import com.google.common.collect.Lists;
import com.hupu.basketball.live.enums.UserIdentityEnum;
import com.hupu.basketball.live.model.UserIdentityModel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author jiangfangyuan
 * @since 2020-07-29 21:42
 */
public class Boot {

    public static void main(String[] args) {
        List<UserIdentityModel> userIdentityList = Lists.newArrayList();
//        userIdentityList.add(new UserIdentityModel(11L, 11L, "HOUSE_MANAGER"));
//        userIdentityList.add(new UserIdentityModel(22L, 22L, "FANS"));


        if (userIdentityList.stream().anyMatch(item -> StringUtils.equalsIgnoreCase(item.getIdentityCode(),
                UserIdentityEnum.HOUSE_MANAGER.getCode()))) {
            System.out.println("hous");
        }


    }
}

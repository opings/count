package com.hupu.deep.comment;

import java.util.concurrent.TimeUnit;

/**
 * @author jiangfangyuan
 * @since 2020-03-15 19:02
 */
public class IdTest {


    public static void main(String[] args) throws Exception {


        Long userId = 93399465L;

        System.out.println(userId % 4);

        while (true) {
            long id = getId(userId);
            System.out.println(id);
            System.out.println(id % 4);
            TimeUnit.SECONDS.sleep(1);
        }
    }


    /**
     * 1010
     *
     * @param userId
     * @return
     */
    private static Long getId(Long userId) {
        Long prefixOrderId = System.currentTimeMillis();
        int leftMoveBit = leftMoveBit();

        /**
         * 加入userId基因
         */
        Long lastOrderId = ((prefixOrderId << leftMoveBit) | (userId % 8));
        return lastOrderId;
    }


    private static int leftMoveBit() {
        return Integer.toBinaryString(8).length() - 1;
    }
}

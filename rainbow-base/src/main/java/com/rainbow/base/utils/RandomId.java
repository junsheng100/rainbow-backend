package com.rainbow.base.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.UUID;

public class RandomId {
    public static final String splitor = ",";
    public static final String code = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z";
    public static final String nums = "0,1,2,3,4,5,6,7,8,9";

    public static String[] chars;
    public static String[] codes;


    static {
        if (null == codes || codes.length == 0) {
            codes = (code + splitor + nums).split(splitor);
        }
        if (null == chars || chars.length == 0) {
            chars = (code + splitor + nums + splitor + splitor + code.toLowerCase() + splitor).split(splitor);
        }
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.randomUUID().toString();
    }

    public static String generateShortCode() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        for (int i = 0; i < 8; ++i) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 36);
            shortBuffer.append(chars[x % 62]);
        }

        return shortBuffer.toString();
    }

    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, Character.MAX_RADIX);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    public static String generateShortUuid(Integer len) {
        StringBuffer shortBuffer = new StringBuffer();

        if (len <= 8) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            for (int i = 0; i < len; i++) {
                String str = uuid.substring(i * 4, i * 4 + 4);
                int x = Integer.parseInt(str, Character.MAX_RADIX);
                shortBuffer.append(chars[x % 0x3E]);
            }
        } else {
            generateShortUuid(len, shortBuffer);
        }
        return shortBuffer.toString();
    }

    public static void generateShortUuid(Integer len, StringBuffer shortBuffer) {
        Integer left = len / 8;

        Integer le = len % 8;

        while (left > 0) {
            String str = generateShortUuid();
            shortBuffer.append(str);
            left--;
        }

        if (le > 0) {
            String str = generateShortUuid(le);
            shortBuffer.append(str);
        }
    }

//	public static void main(String[] argss) {
//		Long uuid = IdUtils.getSeqNo();
//		System.out.println(uuid);
//		System.out.println(String.valueOf(uuid).length());
//	}


    public static BigInteger getSeqNo() {
        return getSeqNo(3);
    }

    public static BigInteger getSeqNo(int n) {
        Long time = System.currentTimeMillis();
        String prev = String.valueOf(time);
        Double random = (Math.random() * Math.pow(10, n));
        Long no = random.longValue();
        String id = prev  + StringUtils.leftPad(no.toString(), n, "0");
        return  new BigInteger(id);
    }

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString();
    }


}

/*
 * 작성일 : 2015. 8. 17.
 * 작성자 : kjs
 *
 * 설명
 */

package com.soom.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * @author kjs
 *         <p>
 *         ***************************
 *         코드 수정 히스토리
 *         날짜          작업자         태그
 *         2015. 8. 17.  kjs
 *         ***************************
 */
public class StringUtil extends StringUtils {
    public final static String SEPERATOR_AMP = "&";
    public final static String SEPERATOR_COMMA = ",";
    public final static String SEPERATOR_UNDERBAR = "_";
    public final static String SEPERATOR_HYPHEN = "-";

    private final static char[] PSWD_CHAR_SET = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '{', '}', '[', ']', '/', '?', '.', ',', ';', ':', '|', ')', '*',
            '*', '~', '`', '!', '^', '-', '_', '+', '@', '#', '$',
            '%', '&', '=', '(', '\'', '"', '\\'};

    public static String nullToWhiteSpace(String value) {
        if (value == null) {
            value = "";
        }

        return value;
    }

    public static String whiteSpaceToNull(String value) {
        if (value != null) {
            if (value.equals("") || value.isEmpty()) {
                value = null;
            }
        }


        return value;
    }

    public static String removeSpecialCharacter(String str) {
        String result = StringUtil.nullToWhiteSpace(str).replaceAll("[\\+\\*\\^%\\$#@&!~`=\"\\?><,\\./\\\\]+", "");

        return result;
    }

    /**
     * 전각공백과 반각공백을 trim처리한다.
     *
     * @param s
     * @return
     */
    public static String trim(String s) {
        String replaced = replace(s, "　", " ");
        String trim = StringUtils.trim(replaced);
        return trim;
    }

    public static String lTrim(String source, char chr) {
        if (source == null) return null;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == chr) {
            } else {
                return source.substring(i, source.length());
            }
        }
        return null;
    }

    public static String[] splitBySeperator(String str, String seperator) {
        String[] splitedStr = str.split(seperator);
        return splitedStr;
    }

    /**
     * <p>버전 비교
     * <ul>
     * <li>strVersion1 > strVersion2 : true
     * <li>strVersion1 =< strVersion2 : false
     * <li>v3.0.0.5, v3.0.0.1 : true
     * <li>v3.0.0.5, v3.0.0.5 : false
     * <li>v3.0.0.5, v3.0.1.0 : false
     * <li>3.0.0.5, 3.0.0.1 : true
     * <li>3.0.0.5, 3.0.0.5 : false
     * <li>3.0.0.5, 3.0.1.0 : false
     * </ul>
     * </p>
     *
     * @param strVersion1 기준 버전
     * @param strVersion2 비교 대상 버전
     * @return 비교 결과
     */
    public static boolean isGreaterThanToVersion(String strVersion1, String strVersion2) {

        if (strVersion1 == null || "".equals(strVersion1) || strVersion2 == null || "".equals(strVersion2)) {
            return false;
        }
        if (strVersion1.indexOf("v") > -1) {
            strVersion1 = strVersion1.substring(strVersion1.indexOf("v") + 1);
        }
        if (strVersion2.indexOf("v") > -1) {
            strVersion2 = strVersion2.substring(strVersion2.indexOf("v") + 1);
        }

        DefaultArtifactVersion agentV = new DefaultArtifactVersion(strVersion1);
        DefaultArtifactVersion userAgentUploadV = new DefaultArtifactVersion(strVersion2);

        return agentV.compareTo(userAgentUploadV) > 0;
    }

    /**
     * <p>버전 비교
     * <ul>
     * <li>strVersion1 >= strVersion2 : true
     * <li>strVersion1 < strVersion2 : false
     * <li>v3.0.0.5, v3.0.0.1 : true
     * <li>v3.0.0.5, v3.0.0.5 : true
     * <li>v3.0.0.5, v3.0.1.0 : false
     * <li>3.0.0.5, 3.0.0.1 : true
     * <li>3.0.0.5, 3.0.0.5 : true
     * <li>3.0.0.5, 3.0.1.0 : false
     * </ul>
     * </p>
     *
     * @param strVersion1 기준 버전
     * @param strVersion2 비교 대상 버전
     * @return 비교 결과
     */
    public static boolean isGreaterThanOrEqualToVersion(String strVersion1, String strVersion2) {

        if (strVersion1 == null || "".equals(strVersion1) || strVersion2 == null || "".equals(strVersion2)) {
            return false;
        }
        if (strVersion1.indexOf("v") > -1) {
            strVersion1 = strVersion1.substring(strVersion1.indexOf("v") + 1);
        }
        if (strVersion2.indexOf("v") > -1) {
            strVersion2 = strVersion2.substring(strVersion2.indexOf("v") + 1);
        }

        DefaultArtifactVersion agentV = new DefaultArtifactVersion(strVersion1);
        DefaultArtifactVersion userAgentUploadV = new DefaultArtifactVersion(strVersion2);

        return agentV.compareTo(userAgentUploadV) > -1;
    }

    public static String ifTrueGet(Boolean b, String defaultValue) {
        return b ? defaultValue : "";
    }

    /**
     * 패스워드로 사용 가능한 문자로 되어있는지 체크
     * <p>
     * 패스워드로 사용 가능한 문자 : 영숫자, 특수문자 { } [ ] / ? . , ; : | \ ) * ~ ` ! ^ - _ + @ # $ % & = ( ' "
     *
     * @param target 대상
     * @return 체크 결과
     */
    public static boolean isValidatePassword(String target) {
        if (!StringUtils.isEmpty(target)) {
            char[] chars = target.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (StringUtils.indexOfAny(String.valueOf(chars[i]), PSWD_CHAR_SET) < 0) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static String replacePattern(final String source, final String regex, final String replacement) {
        return StringUtils.replacePattern(source, regex, replacement);
    }

    /**
     * 반각, 전각 공백을 제거한다.
     * null이면 null을 반환한다.
     *
     * @param str
     * @return
     */
    public static String removeSpace(String str) {
        if (str == null) {
            return null;
        } else {
            return StringUtil.replacePattern(str, "　| ", "");
        }
    }

    /**
     * String 앞 또는 뒤를 특정문자로 지정한 길이만큼 채워주는 함수    <BR>
     * (예) pad("1234","0", 6, 1) --> "123400"   <BR>
     *
     * @param src    Source string
     * @param pad    pad string
     * @param totLen total length
     * @param mode   앞/뒤 구분 (-1:front, 1:back)
     * @return String
     */
    public static String pad(String src, String pad, int totLen, int mode) {
        String paddedString = "";
        if (src == null) return "";
        int srcLen = src.length();
        if ((totLen < 1) || (srcLen >= totLen)) return src;
        for (int i = 0; i < (totLen - srcLen); i++) {
            paddedString += pad;
        }
        if (mode == -1)
            paddedString += src; // front padding
        else
            paddedString = src + paddedString; //back padding
        return paddedString;
    }
}

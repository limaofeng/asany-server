package cn.asany.organization.utils;

import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;

public class CommonUtils {

    /**
     * 字符串中取数字
     * @param string
     * @return
     */
    public static Long getValue(String string) {
        String s = RegexpUtil.replace(string,"[^0-9]","");
        if (StringUtil.isNotBlank(s)){
            return Long.valueOf(s);
        }
        return null;
    }

}

package com.hxzm.common.kit;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author zhangyaohai
 * @create 2018-08-10 17:34
 **/
public class PinYinKit {


    public static String prasePinYin(String input){

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < input.length(); i++) {
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            char c = input.charAt(i);
            String[] pinyinArray = null;

            try {
                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
            }


            if (pinyinArray != null) {
                result.append(pinyinArray[0]);
            } else if (c != ' ') {
                result.append(input.charAt(i));
            }
        }
        return  result.toString();
    }

}

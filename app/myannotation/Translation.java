package myannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description:用于标注字段的中英文翻译信息
 * @Author yc
 * @Date 2025/6/12
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Translation {
    String value() default ""; //中文描述
    String english() default ""; // 英文描述
}
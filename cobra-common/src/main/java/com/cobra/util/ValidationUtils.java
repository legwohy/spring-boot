package com.cobra.util;

import com.cobra.param.BaseResponse;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.MemberOf;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * 参数校验
 *
 * @author admin
 * @date 2019/9/2
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class ValidationUtils {
    private static final Validator VALIDATOR = new Validator();

    private ValidationUtils() {
    }

    /**
     * 校验单个对象
     */
    public static <T> BaseResponse validate(T t) {
        List<ConstraintViolation> validate = VALIDATOR.validate(t);
        if (CollectionUtils.isNotEmpty(validate)) {
            return new BaseResponse("404", validate.get(0).getMessage());
        }

        return new BaseResponse(true);
    }

    /**
     * 校验集合里面的参数
     *
     * @param list
     * @return
     */
    public static <T> BaseResponse validate(List<T> list) {
        for (T t : list) {
            List<ConstraintViolation> validate = VALIDATOR.validate(t);
            if (CollectionUtils.isNotEmpty(validate)) {
                return new BaseResponse("404", validate.get(0).getMessage());
            }
        }

        return new BaseResponse(true);
    }

    public static void main(String[] args) {
        TempUser user = new TempUser();
        // user.setName("john");
        BaseResponse response = ValidationUtils.validate(user);
        if(!response.isSuccess()){
            System.out.println("校验结果:" + response.getMsg());
        }

    }

    private static class TempUser {
        @NotNull(message = "缺少必要参数[name]")
        @NotBlank(message = "姓名不能为空")
        @MemberOf(value = {"jack", "rose"}, message = "姓名不正确")
        private String name;

        public TempUser() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}

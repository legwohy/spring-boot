package com.cobra.refactor;



/**
 * 类型转换
 * 1、反转类必须是子类实现
 * 2、子类反写 重要方法
 * 3、doForward油具体实现处理，反转类做异常处理
 *
 * tip:
 *  A、B 仅仅是传参的顺序问题 可以理解为映射
 *    @see com.google.common.base.Converter  转换方法具体参考
 */
@SuppressWarnings({"unchecked"})
public abstract class AbstractDTOConvert<A,B>{

    /** 1、reverse属性必须是子类实现*/
    private transient AbstractDTOConvert<B,A> reverse;

    protected abstract B doForward(A a);
    protected abstract A doBackward(B b);

    /** 钩子方法*/
    public B convert(A a){
        return this.correctedDoForward(a);
    }

    /** 2、转换时此方法必须被子类继承并重写交换*/
    B correctedDoForward(A a){
        return doForward(a);
    }
    A correctedDoBackward(B b){
        return doBackward(b);
    }


    public AbstractDTOConvert<B,A> reverse(){
        AbstractDTOConvert<B, A> result = reverse;
        return (result == null) ? reverse = new ReverseConvertAbstract<A, B>(this) : result;
    }

    /** 私有*/
    private static final class ReverseConvertAbstract<A,B> extends AbstractDTOConvert<B,A> {

        final AbstractDTOConvert<A,B> original;

        /** 构造时原始参数*/
        public ReverseConvertAbstract(AbstractDTOConvert<A,B> original){
            this.original = original;
        }

        /** 转换方法*/
        @Override
         A correctedDoForward(B b){
            return original.doBackward(b);
        }

        @Override
         B correctedDoBackward(A a){
            return original.doForward(a);
        }



        /** 拒接转换*/
        @Override
        protected A doForward(B b) {
            throw  new AssertionError("暂不支持该类型转换");
        }

        @Override
        protected B doBackward(A a) {
            throw  new AssertionError("暂不支持该类型转换");
        }

        @Override
        public AbstractDTOConvert<A, B> reverse() {
            return original;
        }

    }



}


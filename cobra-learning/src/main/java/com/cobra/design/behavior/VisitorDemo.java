package com.cobra.design.behavior;


/**
 * 访问者模式 这种模式 我中有你 你中有我 这种接口之间的耦合性比较高
 */
public class VisitorDemo
{
    public static void main(String[] args)
    {
        // 优酷的用户
        Visitor visitor = new YoukuVistor();

        // 微博登陆
        Login login = new WeiboLogin();

        visitor.visit(login);

    }
}

class WeiboLogin implements Login{

    @Override
    public void accept(Visitor visitor)
    {
        System.out.println(""+visitor.getClass().getSimpleName()+"\t通过了 微博登陆");
    }
}
class WeChatLogin implements Login{

    @Override
    public void accept(Visitor visitor)
    {
        System.out.println(""+visitor.getClass().getSimpleName()+"\t通过了 微信登陆");
    }
}
class QqLogin implements Login{

    @Override
    public void accept(Visitor visitor)
    {
        System.out.println(""+visitor.getClass().getSimpleName()+"\t通过了 qq登陆");
    }
}

interface Login{
    /** 对于登陆而言 不确定具体的访问者*/
    void accept(Visitor visitor);
}

/**
 * 优酷既可以使用微博登陆也可以使用微信登陆
 */
class YoukuVistor implements Visitor{

    @Override
    public void visit(Login login)
    {
        System.out.println("优酷访问者");
        login.accept(this);

    }


}
class IQiYiVistor implements Visitor{

    @Override
    public void visit(Login login)
    {
        System.out.println("爱奇艺访问者");
        login.accept(this);

    }


}

/** 访问者*/
interface Visitor{
    /** 对于访问者 不确定是哪一种登陆方式*/
    void visit(Login login);
}

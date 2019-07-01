package com.cobra.design;


public class PatternMain extends Calc
{
    public static void main(String[] args)
    {
        Calc calc = new PatternMain();
        calc.calc();
    }

    @Override
    protected void add()
    {

    }

    @Override
    protected void minus()
    {

    }

    @Override
    protected void multi()
    {

    }
}

abstract class Calc{
    protected abstract void add();
    protected abstract void minus();
    protected abstract void multi();

    public final void calc(){
        add();
        minus();
        multi();
    }
}


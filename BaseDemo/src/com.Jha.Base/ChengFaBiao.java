package com.Jha.Base;

public class ChengFaBiao {
    public static void main(String[] args) {
        //九九乘法表
        for(int i=1;i<=9;i++)
        {
            for(int j=1;j<=i;j++)
            {
                System.out.print(j+"*"+i+"="+(i*j)+"  ");
            }
            System.out.println();
        }
    }
}

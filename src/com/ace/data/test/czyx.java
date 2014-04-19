package com.ace.data.test;
import java.util.Scanner;
public class czyx {
    public static void main(final String[] args){
        final Scanner in=new Scanner(System.in);
        System.out.println("请输入数字：1.剪刀 2.石头 3.布 ");
        final int people=in.nextInt();
        final int computer=(int)(Math.random()*3)+1;
        String Marks="拳头";
        String Marks2="拳头";
        switch(people){
        case 1:
            Marks="剪刀";
        case 2:
            Marks="石头";
        case 3:
            Marks="布";
        }
        switch(computer){
        case 1:
            Marks2="剪刀";
        case 2:
            Marks2="石头";
        case 3:
            Marks2="布";
        }
        if(people==computer){
            System.out.println("你出的是 "+Marks+"  电脑出的是 "+Marks2+"  平局");
        }else if(people==1&&computer==2||people==2&&computer==3||people==3&&computer==1){
            System.out.println("你出的是 "+Marks+"  电脑出的是 "+Marks2+"  你输了");
        }else{
            System.out.println("你出的是 "+Marks+"  电脑出的是 "+Marks2+"  你赢了");
        }
    }
}

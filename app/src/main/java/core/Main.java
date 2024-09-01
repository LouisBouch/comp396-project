package core;

import package1.Helper;

public class Main {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new Main().getGreeting());
        System.out.println(new More().moreGreetings());
        System.out.println(new Helper().helperGreetings());
    }
}

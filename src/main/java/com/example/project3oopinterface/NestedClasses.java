package com.example.project3oopinterface;

public class NestedClasses {

    public class InnerClass {
        public void display() {
            System.out.println("Inside InnerClass");
        }
    }

    public void methodWithLocalClass() {
        class LocalClass {
            public void display() {
                System.out.println("Inside LocalClass");
            }
        }
        LocalClass local = new LocalClass();
        local.display();
    }

    public void methodWithAnonymousClass() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Inside AnonymousClass");
            }
        };
        new Thread(runnable).start();
    }

    public static void main(String[] args) {
        NestedClasses outer = new NestedClasses();

        NestedClasses.InnerClass inner = outer.new InnerClass();
        inner.display();

        outer.methodWithLocalClass();

        outer.methodWithAnonymousClass();
    }
}

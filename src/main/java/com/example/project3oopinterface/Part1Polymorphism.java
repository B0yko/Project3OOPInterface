package com.example.project3oopinterface;
import java.util.ArrayList;

// Part 1.1 Basic case of polymorphic method
class Superclass {
    void theMethod() {
        System.out.println("Method of superclass");
    }
}


class Subclass extends Superclass {
    @Override
    void theMethod() {
        System.out.println("Method of subclass");
    }
}

// Part 1.2 Animal sound example
abstract class Animal {
    abstract void makeSound();
}
class Cat extends Animal {
    @Override
    void makeSound() {
        System.out.println("Meow");
    }
}

class Dog extends Animal {
    @Override
    void makeSound() {
        System.out.println("Woof");
    }
}

class Cow extends Animal {
    @Override
    void makeSound() {
        System.out.println("Moo");
    }
}

class Pig extends Animal {
    @Override
    void makeSound() {
        System.out.println("Oink");
    }
}

public class Part1Polymorphism {
    public static void main(String[] args) {
        Superclass a = new Subclass();

        a.theMethod();
        /* Explanation: In this case, the method of the subclass will be called.
        This is because Java uses dynamic method dispatch to determine
        which version of the method to call based on the actual type of the object
        at runtime. Even though the reference type is superclass, since the object
        being referred to is of type Subclass, the overridden method in Subclass
        will be invoked.*/

        ArrayList<Animal> list = new ArrayList<>();

        list.add(new Cat());
        list.add(new Dog());
        list.add(new Cow());
        list.add(new Pig());

        for (Animal animal : list) {
            animal.makeSound();
        }
    }
}


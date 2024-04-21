package com.example.project3oopinterface;

import soundmakers.SoundMaker;
import soundmakers.Cat;
import soundmakers.Dog;
import soundmakers.Cow;
import soundmakers.Pig;
import soundmakers.Bee;
import soundmakers.Cricket;
import soundmakers.Frog;

import java.util.ArrayList;

public class TestSoundMakers {
    public static void main(String[] args) {
        ArrayList<SoundMaker> list = new ArrayList<>();

        list.add(new Cat());
        list.add(new Dog());
        list.add(new Cow());
        list.add(new Pig());
        list.add(new Bee());
        list.add(new Cricket());
        list.add(new Frog());

        for (SoundMaker soundMaker : list) {
            soundMaker.makeSound();
        }
    }
}



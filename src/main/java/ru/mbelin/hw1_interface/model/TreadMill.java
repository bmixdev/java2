package ru.mbelin.hw1_interface.model;

import ru.mbelin.hw1_interface.impl.ImplAction;
import ru.mbelin.hw1_interface.impl.ImplRunAndJump;

import java.util.UUID;

public class TreadMill implements ImplAction {

    private double length;
    private String uid;

    public TreadMill(double length) {
        this.length = length;
        this.uid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Беговая дорожка длиной " + length + " метров";
    }

    public boolean doAction(ImplRunAndJump player) {
       int val = player.run();
       if (this.length >=  val) {
           System.out.println(player + " не смог пробежать расстояние длиной  " + this.length + " метров на дорожке " + this.uid);
           return false;
       } else {
           System.out.println(player + " пробежал всё расстояние на дорожке " + this.uid);
           return true;
       }
    }
}

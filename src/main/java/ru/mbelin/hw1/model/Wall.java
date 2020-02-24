package ru.mbelin.hw1.model;

import ru.mbelin.hw1.impl.ImplAction;
import ru.mbelin.hw1.impl.ImplRunAndJump;

import java.util.UUID;

public class Wall implements ImplAction {
    private int height;
    private String uid;

    public Wall(int height) {
        this.height = height;
        this.uid = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return "Стена высотой " + height + " метров";
    }

    public boolean doAction(ImplRunAndJump player) {
        int val = player.jump();
        if (this.height >=  val) {
            System.out.println(player + " не смог перепрыгнуть стену " + this.uid + " высотой " + this.height + " метров");
            return false;
        } else {
            System.out.println(player + " перепрыгнул стену " + this.uid);
            return true;
        }
    }
}

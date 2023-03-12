package com.appdev.terra.services;

import com.appdev.terra.models.UserModel;

import org.junit.jupiter.api.Test;

class UserServiceTest {

    @Test
    void get() {
        UserService userService = new UserService();
        UserModel model = userService.get("Re86sT1MJa3Pm30whdqr");
        System.out.println(model);
    }

    /*@Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void remove() {
    }*/
}
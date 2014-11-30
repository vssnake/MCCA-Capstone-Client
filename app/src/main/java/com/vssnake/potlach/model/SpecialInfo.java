package com.vssnake.potlach.model;

import java.util.Date;

/**
 * Created by vssnake on 24/10/2014.
 */
public class SpecialInfo {

    String date;

    private User[] usersOfTheDay = new User[3];

    private Gift[] giftsOfTheDay = new Gift[3];

    public SpecialInfo(){}

    /**
     * The Ids of the users
     */
    public User[] getUsersOfTheDay() {
        return usersOfTheDay;
    }

    public void setUsersOfTheDay(User[] usersOfTheDay) {
        this.usersOfTheDay = usersOfTheDay;
    }

    /**
     * The Ids of the gifts
     */
    public Gift[] getGiftsOfTheDay() {
        return giftsOfTheDay;
    }

    public void setGiftsOfTheDay(Gift[] giftsOfTheDay) {
        this.giftsOfTheDay = giftsOfTheDay;
    }
}


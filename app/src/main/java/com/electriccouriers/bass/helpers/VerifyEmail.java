package com.electriccouriers.bass.helpers;

/**
 * Created by Tan Bui on 31/03/2019
 */

public class VerifyEmail {

    public boolean verify(String email) {
        StringBuilder buffer = new StringBuilder(email);
        System.out.println(email);
        boolean containsApestaartje;

        int c = 0;
        while(true){
            if (buffer.charAt(c) == '@') {
                containsApestaartje = true;
                break;
            } else if (c == buffer.length() - 1){
                containsApestaartje = false;
                break;
            } else {
                c++;
            }
        }

        return containsApestaartje;
    }
}
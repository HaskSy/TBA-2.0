package com.hasksy.tba.services;

import org.slf4j.Logger;

public class GoogleService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(GoogleService.class);

    public static void updateUserName(long userId, String newName, long timestampMillis, long chatId) {
        log.info("TEST!!! Updating user name");
    }

}

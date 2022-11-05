package com.bankin.app.enums;

public enum UserStatus {
    EMAIL_PHONE(1),
    USER_INFO(2),
    FACE_INFO(3),
    CREATED(4);

    public final int step;

    private UserStatus(int step){
        this.step = step;
    }
}

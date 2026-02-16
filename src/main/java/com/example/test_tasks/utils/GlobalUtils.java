package com.example.test_tasks.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class GlobalUtils {



    public String randomCodeGenerator(){

        return UUID.randomUUID().toString();
    }

}

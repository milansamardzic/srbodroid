package com.srbodroid.app.data;

/**
 * Created by pedja on 6.11.14. 15.41.
 * This class is part of the NovaBanka
 * Copyright © 2014 ${OWNER}
 */
public interface DataProvider<T>
{
    int REQUEST_CODE_NEWS_RSS = 401;
    boolean load();
    T getResult();
}

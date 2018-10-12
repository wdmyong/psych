package com.wdm.psych.data.dao;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.wdm.psych.data.model.User;

/**
 * @author wdmyong
 */
@Lazy
@Repository
public class UserDao {

    private final static AtomicLong count = new AtomicLong();

    public User getById(long id) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        System.out.println(LocalDateTime.now() + ":\tload by db......" + count.incrementAndGet());
        return new User(id, String.valueOf(id), ThreadLocalRandom.current().nextInt(100));
    }
}

package com.wdm.psych.service.account.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.wdm.psych.data.dao.UserDao;
import com.wdm.psych.data.model.User;
import com.wdm.psych.service.account.UserService;

/**
 * @author wdmyong
 */
@Lazy
@Service
public class UserServiceImpl implements UserService {

    private static ListeningExecutorService singleThreadExecutor = MoreExecutors.listeningDecorator(
            Executors.newSingleThreadExecutor());

    @Autowired
    private UserDao userDao;

    LoadingCache<Long, User> userLocalCache = CacheBuilder.newBuilder() //
            /**
             * 达到指定时间的访问会重写
             * 但是需要自己启动线程来实现reload，否则也是同步load
             */
        .refreshAfterWrite(5, TimeUnit.SECONDS) //
            // 一直在指定时间内有读，不会过期
            // com.google.common.cache.LocalCache.Segment.recordRead()
            /**
             * 一直在指定时间内有读，不会过期
             * com.google.common.cache.LocalCache.Segment.recordRead()
             * 会这样更新entry.setAccessTime(now);
             * com.google.common.cache.LocalCache#isExpired(com.google.common.cache.ReferenceEntry, long)
             * 会这样判断(now - entry.getAccessTime() >= expireAfterAccessNanos;
             */
        .expireAfterAccess(10, TimeUnit.SECONDS) //
        .build(new CacheLoader<Long, User>() {
            @Override
            public User load(@Nonnull Long id) throws Exception {
                return userDao.getById(id);
            }

            @Override
            public ListenableFuture<User> reload(@Nonnull Long id, User oldValue) throws Exception {
                // 自己启动一个线程才会异步refresh
                return singleThreadExecutor.submit(() -> userDao.getById(id));
            }
        });

    @Override
    public User getById(long id) {
        try {
            return userLocalCache.get(id);
        } catch (ExecutionException e) {
            return new User();
        }
    }
}

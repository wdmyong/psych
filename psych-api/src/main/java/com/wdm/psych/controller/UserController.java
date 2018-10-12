package com.wdm.psych.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.wdm.psych.service.account.UserService;

/**
 * @author wdmyong
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/{id}")
    Map<String, Object> byId(@PathVariable("id") long id) {
        Map<String, Object> result = Maps.newHashMap();
        result.put(String.valueOf(id), userService.getById(id));
        return result;
    }
}

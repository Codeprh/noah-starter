package com.noah.starter.demo.web.arthas;

import org.springframework.stereotype.Service;

@Service
public class ArthasWatchUse {

    public ArthasUser methodForWatch(int id, ArthasUser user) {

        String name = user.getName();
        user.setName(name + "---aaa");
        return user;

    }
}

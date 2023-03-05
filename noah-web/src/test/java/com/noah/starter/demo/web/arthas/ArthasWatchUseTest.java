package com.noah.starter.demo.web.arthas;

import com.noah.starter.demo.web.NoahDemoApplication;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = NoahDemoApplication.class)
class ArthasWatchUseTest {

    @Resource
    ArthasWatchUse arthasWatchUse;

    @SneakyThrows
    @Test
    void methodForWatch() {

        for (int i = 0; i < 10000; i++) {

            try {
                arthasWatchUse.methodForWatch(i, new ArthasUser("hanmeimei", 16, Arrays.asList("pubg", "lol")));
                arthasWatchUse.methodForWatch(i, new ArthasUser("liming", 17, Collections.singletonList("pubg")));
                arthasWatchUse.methodForWatch(i, new ArthasUser("tom", 18, Collections.singletonList("running")));
                arthasWatchUse.methodForWatch(i, new ArthasUser("jacky", 19, Collections.singletonList("food")));

                arthasWatchUse.methodForWatch(i, null);

            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                TimeUnit.SECONDS.sleep(10);
            }
        }
    }
}
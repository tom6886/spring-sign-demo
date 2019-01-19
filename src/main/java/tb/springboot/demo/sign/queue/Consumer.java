package tb.springboot.demo.sign.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author TB
 */
@Component
public class Consumer {
    @Autowired
    private SimpMessagingTemplate template;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @JmsListener(destination = "signIn")
    public void SignIn(Map<String, Object> user) {
        System.out.println("接收到消息：" + user.toString());
        String sjh = user.get("sjh").toString();
        redisTemplate.opsForHash().put("sign", sjh, user);
        template.convertAndSend("/topic/signIn", user);
    }

    @JmsListener(destination = "signOut")
    public void SignOut(String sjh) {
        System.out.println("接收到消息：" + sjh);
        redisTemplate.opsForHash().delete("sign", sjh);
        template.convertAndSend("/topic/signOut", sjh);
    }
}

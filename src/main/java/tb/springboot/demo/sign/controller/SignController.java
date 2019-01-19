package tb.springboot.demo.sign.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tb.springboot.demo.sign.util.R;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author TB
 */
@RestController
@RequestMapping("/")
public class SignController {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping(value = "/in", method = RequestMethod.POST, consumes = "application/json")
    public R signIn(@RequestBody Map<String, Object> user) {
        jmsMessagingTemplate.convertAndSend("signIn", user);
        return R.ok();
    }

    @RequestMapping(value = "/out", method = RequestMethod.POST, consumes = "application/json")
    public R signOut(@RequestBody String sjh) {
        jmsMessagingTemplate.convertAndSend("signOut", sjh);
        return R.ok();
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = "application/json")
    public List<Object> list() {
        return this.redisTemplate.opsForHash().values("sign");
    }

    @RequestMapping(value = "/size", method = RequestMethod.POST, consumes = "application/json")
    public Long size() {
        return this.redisTemplate.opsForHash().size("sign");
    }
}

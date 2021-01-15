package soap.b线程安全问题;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @Description
 * @Author ZhangPY
 * @Date 2021/1/15
 */
@Slf4j(topic = "s.Test12_设计模式_生产者消费者")
public class Test12_设计模式_生产者消费者 {
    public static void main(String[] args) throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(3);



        for (int i = 0; i < 4; i++) {
            int id = i;
            new Thread(()->{
                messageQueue.put(new Message(id , "消息"+id));
            },id+"").start();
        }


        while (true) {
            Thread.sleep(1000);
            /** 消费消息 **/
            Message take = messageQueue.take();
            log.debug("消费到消息: {}" , take);
        }

    }

}

/**
 * 消息
 */
@Slf4j(topic = "s.Message")
final class Message {
    private Integer id;
    private Object value;

    public Integer getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    public Message(Integer id, Object value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}


/**
 * 队列
 */
@Slf4j(topic = "s.MessageQueue")
class MessageQueue {
    private LinkedList<Message> list = new LinkedList();;

    private Integer capacity;

    public MessageQueue(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * 从队列中获取消息
     */
    public Message take() {
        synchronized (list) {
            while (list.isEmpty()) {
                try {

                    log.debug("队列已经空了,消费者休眠");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            /** 从尾部取 **/
            Message message = list.removeLast();
            list.notifyAll();
            return message;
        }
    }

    public void put(Message message) {
        synchronized (list) {
            while (list.size() >= capacity) {
                try {

                    log.debug("队列已经满了,生产者休眠");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("生产者生产消息");
            list.push(message);
            list.notifyAll();
        }
    }
}
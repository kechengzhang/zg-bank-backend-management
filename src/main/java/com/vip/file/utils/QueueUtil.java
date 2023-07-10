package com.vip.file.utils;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author zw
 * @date 2022/8/3 9:44
 */
public class QueueUtil {

    /**
     * 初始化有界队列
     */
    private  static final LinkedBlockingDeque<String> LINKED_BLOCKING_DEQUE = new LinkedBlockingDeque<>(50);
//    private  static final LinkedList<String> queue = new LinkedList<>();


        /**
         * 添加一个元素并返回true,如果队列已满，返回false
         * @param event
         * @return
         */
        public static boolean offer(String event){
            return LINKED_BLOCKING_DEQUE.offer(event);
        }

        /**
         * 移除并返回队列头部的元素,如果队列为空，返回null
         * @return
         */
        public static String poll(){
            return LINKED_BLOCKING_DEQUE.poll();
        }

        /**
         * 判断队列是否为空
         * @return
         */
        public static boolean isEmpty(){
            return LINKED_BLOCKING_DEQUE.isEmpty();
        }

        /**
         * 返回队列长度
         * @return
         */
        public static Integer length(){
                return LINKED_BLOCKING_DEQUE.size();
        }

        /**
         * 删除队列中第一个出现的元素
         * @param event
         * @return
         */
        public static boolean remove(String event){
            return LINKED_BLOCKING_DEQUE.removeFirstOccurrence(event);
        }

        /**
         * 清空队列
         */
        public static void clear(){
            LINKED_BLOCKING_DEQUE.clear();
        }


}

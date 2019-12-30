package com.lian.threaddemo.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

@Service
public class ExecService {

    private final static Logger log = LogManager.getLogger(ExecService.class);


    private static CountDownLatch latch = new CountDownLatch(2);

    @Async
    public String ExecShellScript(ConcurrentLinkedQueue<String> concurrentLinkedQueue) throws IOException, InterruptedException {
        String homeDirectory = System.getProperty("user.home");
        Process process;
        process = Runtime.getRuntime().exec("cmd.exe /c ping -n 10 192.168.1.1");
        InputStream in= process.getInputStream();
        String out = readIn(in, concurrentLinkedQueue);
        latch.await();
        return out;
    }


    private String readIn(InputStream in, ConcurrentLinkedQueue<String> concurrentLinkedQueue) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "GBK"));
        String str = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ( (str = reader.readLine()) != null){
            stringBuilder.append(str + "\n");
            concurrentLinkedQueue.offer(str);
            log.info("--readIn-->" + str);

        }
        in.close();


        return stringBuilder.toString();

    }

    public String getIn(ConcurrentLinkedQueue<String> concurrentLinkedQueue){

        StringBuilder str = new StringBuilder();
        while (!concurrentLinkedQueue.isEmpty())
            str.append(concurrentLinkedQueue.poll());
        latch.countDown();
        return str.toString();
    }
}

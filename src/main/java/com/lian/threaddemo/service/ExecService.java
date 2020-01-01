package com.lian.threaddemo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

@Service
public class ExecService {

    private final static Logger log = LogManager.getLogger(ExecService.class);


    private static CountDownLatch latch = new CountDownLatch(2);
    private ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

    @Async
    public String ExecShellScript() throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        Map to_text = new HashMap();
        Process process;
        process = Runtime.getRuntime().exec("cmd.exe /c ping -n 10 192.168.1.1");
        InputStream in= process.getInputStream();
        String out = readIn(in);
        latch.await();

        to_text.put("begin_date", new Date());
        to_text.put("log", out);
        String text = mapper.writeValueAsString(to_text);
        writeText(text);
        return out;
    }


    private String readIn(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, "GBK"));
        String str = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ( (str = reader.readLine()) != null){
            stringBuilder.append(str);
            concurrentLinkedQueue.offer(str);
            log.info("--readIn-->" + str);

        }
        in.close();


        return stringBuilder.toString();

    }

    public String getIn() throws InterruptedException {

        StringBuilder str = new StringBuilder();
        while (!concurrentLinkedQueue.isEmpty())
            str.append(concurrentLinkedQueue.poll());
        latch.countDown();
        return str.toString();
    }

    public String sendLog(Session session) throws InterruptedException, IOException {

        StringBuilder str = new StringBuilder();
        while (!concurrentLinkedQueue.isEmpty())
            str.append(concurrentLinkedQueue.poll());
        latch.countDown();
        Thread.sleep(800);
        session.getBasicRemote().sendText(str.toString());
        return str.toString();
    }


    private void writeText(String in) throws IOException {
        File file =new File("D:\\O\\test_appendfile.txt");

        if(!file.exists()){

            file.createNewFile();

        }

        FileWriter fw = new FileWriter(file, true);

        PrintWriter pw = new PrintWriter(fw);
        pw.println(in);

        pw.flush();
        fw.flush();

        pw.close();

        fw.close();
        log.info("文件已写入！！！");
    }
}

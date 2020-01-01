package com.lian.threaddemo.controller;


import com.lian.threaddemo.service.ExecService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentLinkedQueue;

@Controller
@RequestMapping("/")
public class HomeController {

    private final static Logger log = LogManager.getLogger(HomeController.class);

    @RequestMapping("index")
    @ResponseBody
    public ModelAndView homeMav(){

        return new ModelAndView("index");
    }

    @RequestMapping("pages/charts/chartjs")
    @ResponseBody
    public ModelAndView chartMav(){

        return new ModelAndView("pages/charts/chartjs");
    }

    @RequestMapping("pages/charts/morris")
    @ResponseBody
    public ModelAndView morrisMav(){

        return new ModelAndView("pages/charts/morris");
    }

    @RequestMapping("index2")
    @ResponseBody
    public ModelAndView index2Mav(){

        return new ModelAndView("index2");
    }

    @RequestMapping("starter")
    @ResponseBody
    public ModelAndView starterMav(){

        return new ModelAndView("starter");
    }

    @RequestMapping("pages/thread/myThread")
    @ResponseBody
    public ModelAndView myThreadMav(){

        return new ModelAndView("pages/thread/myThread");
    }

    @RequestMapping("rt_normal")
    public void todo(HttpServletRequest request,
                     HttpServletResponse response) throws Exception {
        //这里来个休眠
        Thread.sleep(10000);
        response.setCharacterEncoding("utf-8");
        response.getWriter().println("这是【正常】的请求返回");
    }

    @Autowired
    ExecService execService;
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    @RequestMapping("pages/thread/openThread")
    @ResponseBody
    public String openThread() throws Exception {
        //这里来个休眠
        execService.ExecShellScript();

        return "running";
    }

    String str_queue = null;
    @RequestMapping("pages/thread/getThread")
    @ResponseBody
    public String getThread() throws Exception {


        str_queue =  execService.getIn() ;


        return str_queue;
    }


}

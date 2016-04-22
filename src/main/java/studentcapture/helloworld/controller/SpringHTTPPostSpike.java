package studentcapture.helloworld.controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import studentcapture.helloworld.model.HelloModel;

@RestController
public class SpringHTTPPostSpike {
    private HelloModel model = new HelloModel();

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public HelloModel hello() {
        return model;
    }

    @RequestMapping(value = "postToHello", method = RequestMethod.GET)
    public void postToHello() {
        PostToOtherService sender = new PostToOtherService("http://localhost:8080");
        MultiValueMap<String, String> parts = new LinkedMultiValueMap<String, String>();
        parts.add("greeting", "our_greeting");

        String response = sender.post("/dbc", parts, String.class);
        System.out.println(response);

    }

    @RequestMapping(value = "hello", method = RequestMethod.POST)
    public String hello(@RequestParam(value = "greeting", required = false) String greeting) {
        if (greeting != null) {
            model.setGreeting(greeting);


            //terminal: curl --data "greeting=hello" localhost:8080/hello
            // greeting -> localhost:8080/databasemanager

        }

        return greeting;
    }
}

package studentcapture.helloworld.controller;

import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by c12osn on 2016-04-21.
 */
public class PostToOtherService {

    private String address;
    private RestTemplate template;

    public PostToOtherService(String address){
        this.address = address;
        this.template = new RestTemplate();

    }

    public String post(String service, MultiValueMap<String, String> parts, Class<String> stringClass){
        return template.postForObject(address+service, parts, stringClass);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}

package cn.asany.organization.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author 2u
 * @version 0.1
 */
public class RestTemplateUtils {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * http post
     * @param url
     * @param request
     * @param responseType
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType) {
        return restTemplate.postForEntity(url, request, responseType);
    }

    /**
     * http excel
     * @param url
     * @param responseType
     * @param form
     * @param <T>
     * @return
     */
    public <T> ResponseEntity<T> postForExcel(String url, Class<T> responseType, MultiValueMap<String, Object> form) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.setContentType(MediaType.parseMediaType("multipart/form-data; charset=UTF-8"));
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(form, headers);
        return this.postForEntity(url, formEntity, responseType);
    }


}

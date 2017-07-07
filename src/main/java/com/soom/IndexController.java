package com.soom;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * summary:
 * <p> description:
 * <p><b>History:</b>
 * - 작성자, 2017-07-05 최초 작성<br/>
 *
 * @author Kevin
 * @see
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        return "index";
    }
}

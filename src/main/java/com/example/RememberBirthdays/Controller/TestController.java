@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Public endpoint";
    }

    @GetMapping("/secure/hello")
    public String secureHello() {
        return "Secure endpoint";
    }
}

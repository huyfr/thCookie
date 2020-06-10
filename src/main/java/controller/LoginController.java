package controller;

import model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class LoginController {

    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView loadIndex(@CookieValue(value = "setUser", defaultValue = "") String setUser) {
        ModelAndView index = null;
        Cookie cookie;
        try {
            index = new ModelAndView("index");
            cookie = new Cookie("setUser", setUser);
            index.addObject("cookieValue", cookie);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return index;
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public ModelAndView doLogin(@ModelAttribute("user") User user, @CookieValue(value = "setUser", defaultValue = "") String setUser, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView doLogin = null;
        Cookie cookie;
        Cookie[] cookies;
        try {
            doLogin = new ModelAndView("index");

            if (user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("123456")) {
                if (user.getEmail() != null) {
                    setUser = user.getEmail();
                }
                cookie = new Cookie("setUser",setUser );
                cookie.setMaxAge(24*60*60);
                response.addCookie(cookie);

                cookies = request.getCookies();
                for (Cookie ck : cookies) {
                    if (ck.getName().equals("setUser")) {
                        doLogin.addObject("cookieValue", ck);
                        break;
                    } else {
                        ck.setValue("");
                        doLogin.addObject("cookieValue", ck);
                        break;
                    }
                }
                doLogin.addObject("message", "Login success. Welcome ");
            } else {
                user.setEmail("");
                cookie = new Cookie("setUser", setUser);
                doLogin.addObject("cookieValue", cookie);
                doLogin.addObject("message", "Login failed. Try again");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doLogin;
    }
}

package com.twitchable.project.authentication;


import com.twitchable.project.model.Provider;
import com.twitchable.project.model.Role;
import com.twitchable.project.model.User;
import com.twitchable.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by ristes on 3/15/16.
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

  private Provider provider;
  private Role role;

  public LoginSuccessHandler(Provider provider, Role role) {
    this.provider = provider;
    this.role = role;
  }

  @Autowired
  private UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    HttpSession session = httpServletRequest.getSession();
    User user = userRepository.findUserByName(authentication.getName());
    System.out.println(authentication.getDetails().toString());
    System.out.println("NADVOR OD IFOT ****************");
    if (user == null) {
      System.out.println("VNATRE OD IFOT ****************");
      user = new User();
      user.setName(authentication.getName());
      userRepository.save(user);
    }
    session.setAttribute("user", user);
    super.onAuthenticationSuccess(httpServletRequest, httpServletResponse, authentication);
  }
}

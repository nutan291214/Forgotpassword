package com.example.springsocial.security.oauth2;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.example.springsocial.dto.UserDto;
import com.example.springsocial.exception.OAuth2AuthenticationProcessingException;
import com.example.springsocial.model.AuthProvider;
import com.example.springsocial.model.ERole;
import com.example.springsocial.model.Role;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.RoleRepository;
import com.example.springsocial.repository.UserDtoRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.UserPrincipal;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfo;
import com.example.springsocial.security.oauth2.user.OAuth2UserInfoFactory;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserDtoRepository userDtoRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
      throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
    try {
      return processOAuth2User(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
        oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
    }

    User userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
    User user;
    UserDto userDto = new UserDto();
    if (!(userOptional == null)) {
      user = userOptional;
      if (!user.getProvider().equals(
          AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
        throw new OAuth2AuthenticationProcessingException(
            "Looks like you're signed up with " + user.getProvider() + " account. Please use your "
                + user.getProvider() + " account to login.");
      }
      user = updateExistingUser(user, oAuth2UserInfo);
      userDto = updateExistingUserDto(userDto, oAuth2UserInfo);
    } else {
      user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
      userDto = registerNewUserDto(oAuth2UserRequest, oAuth2UserInfo);
    }

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user = new User();
    user.setProvider(
        AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
    user.setProviderId(oAuth2UserInfo.getId());
    user.setName(oAuth2UserInfo.getName());
    user.setEmail(oAuth2UserInfo.getEmail());
    // user.setImageUrl(oAuth2UserInfo.getImageUrl());
    final HashSet<Role> roles = new HashSet<Role>();
    roles.add(roleRepository.findByName(ERole.ROLE_USER));
    System.out.println("roles : " + roles);
    user.setEmailVerified(true);
    user.setRoles(roles);

    return userRepository.save(user);
  }

  private UserDto registerNewUserDto(OAuth2UserRequest oAuth2UserRequest,
      OAuth2UserInfo oAuth2UserInfo) {
    UserDto userDto = new UserDto();
    userDto.setName(oAuth2UserInfo.getName());
    userDto.setEmail(oAuth2UserInfo.getEmail());
    // user.setImageUrl(oAuth2UserInfo.getImageUrl());
    final HashSet<Role> roles = new HashSet<Role>();
    roles.add(roleRepository.findByName(ERole.ROLE_USER));
    userDto.setRoles(roles);

    return userDtoRepository.save(userDto);
  }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setName(oAuth2UserInfo.getName());
    return userRepository.save(existingUser);
  }

  private UserDto updateExistingUserDto(UserDto existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setName(oAuth2UserInfo.getName());
    return userDtoRepository.save(existingUser);
  }

}

package com.crode.todo_list_api.service;

import com.crode.todo_list_api.dto.UserRegisterDTO;
import com.crode.todo_list_api.model.User;

import java.util.List;

public interface UserService {

    User registerNewUserAccount(UserRegisterDTO userDto);

    boolean emailExists(String email);

    boolean usernameExists(String username);
}

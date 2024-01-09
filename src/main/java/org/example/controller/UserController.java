package org.example.controller;

import org.example.UserRepository;
import org.example.model.User;
import org.example.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getuser/{id}")
    public User getUser(@PathVariable Long id) {
        //Получаем пользователя по id
        User user;
        try {
            user = userRepository.findById(id).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @PostMapping("/createuser")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());

        userRepository.save(user);
        return ResponseEntity.ok("User created successfully. Id: " + user.getId());
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("User has been deleted");
    }

    @PutMapping("updateUser/{id}")
    public ResponseEntity<String> updateUsers(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Обновляем поля пользователя с новыми данными из userRequest
            user.setUsername(userRequest.getUsername());
            user.setEmail(userRequest.getEmail());

            // Сохраняем обновленного пользователя в базе данных
            userRepository.save(user);
            return ResponseEntity.ok("User has been updated");
        } else {
            // Обработка случая, если пользователя с заданным id не существует
            return ResponseEntity.notFound().build();
            //throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

}


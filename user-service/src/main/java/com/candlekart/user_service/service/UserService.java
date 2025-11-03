package com.candlekart.user_service.service;

import com.candlekart.user_service.dto.AddressResponse;
import com.candlekart.user_service.dto.UserRequest;
import com.candlekart.user_service.dto.UserResponse;
import com.candlekart.user_service.exc.NotFoundException;
import com.candlekart.user_service.model.Address;
import com.candlekart.user_service.model.User;
import com.candlekart.user_service.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public static User toEntity(UserRequest request){
        if (request == null) return null;

//        Todo Use ModelMapper Library

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .build();

        if (request.getAddresses() != null) {
            List<Address> addresses = request.getAddresses().stream()
                    .map(addressRequest -> Address.builder()
                            .street(addressRequest.getStreet())
                            .city(addressRequest.getCity())
                            .state(addressRequest.getState())
                            .postalCode(addressRequest.getPostalCode())
                            .country(addressRequest.getCountry())
                            .user(user) // set the relationship
                            .build())
                    .toList();

            user.setAddresses(addresses);
        }

        return user;
    }
    public static UserResponse toDto(User user) {

        //        Todo Use ModelMapper Library

        if (user == null) return null;

        List<AddressResponse> addressResponses = user.getAddresses() == null
                ? Collections.emptyList()
                : user.getAddresses().stream()
                .map(address -> AddressResponse.builder()
                        .street(address.getStreet())
                        .city(address.getCity())
                        .state(address.getState())
                        .postalCode(address.getPostalCode())
                        .country(address.getCountry())
                        .build())
                .toList();

        return UserResponse.builder()
                .user_id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .addresses(addressResponses)
                .build();
    }

    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = toEntity(request);
        user = userRepository.save(user);
        return toDto(user);
    }

    public UserResponse getUserById(String userId) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + userId) {
                });
        return toDto(user);
    }

    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username) {
                });
        return toDto(user);
    }

    public UserResponse getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException("User not found with phone number: " + phoneNumber) {
                });
        return toDto(user);
    }

    @Transactional
    public UserResponse updateUser(UserRequest request) {
        // 1️⃣ Find the user by ID
        User existingUser = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + request.getUserId()) {
                });

        // 2️⃣ Update only non-null fields (partial update)
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            existingUser.setUsername(request.getUsername());
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            existingUser.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            existingUser.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            existingUser.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getRole() != null) {
            existingUser.setRole(request.getRole());
        }

        // 3️⃣ Update addresses only if provided
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            existingUser.getAddresses().clear();

            List<Address> updatedAddresses = request.getAddresses().stream()
                    .map(addr -> Address.builder()
                            .street(addr.getStreet())
                            .city(addr.getCity())
                            .state(addr.getState())
                            .postalCode(addr.getPostalCode())
                            .country(addr.getCountry())
                            .user(existingUser)
                            .build())
                    .toList();

            existingUser.getAddresses().addAll(updatedAddresses);
        }

        // 4️⃣ Save updated entity
        User updatedUser = userRepository.save(existingUser);

        // 5️⃣ Convert to response DTO
        return toDto(updatedUser);
    }

    public String deleteUser(String userId) {
        userRepository.deleteById(Long.valueOf(userId));
        return "User is deleted with user id = " + userId;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> usersResponse = new ArrayList<>();

        for(User user : users)
            usersResponse.add(toDto(user));

        return usersResponse;
    }
}
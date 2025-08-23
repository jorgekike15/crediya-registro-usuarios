package co.com.pragma.usecase.user;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public Flux<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public Mono<User> findUserByNumber(String id) {
        return userRepository.findUserByNumber(id);
    }

    public Mono<User> editUser(User user) {
        return userRepository.editUser(user);
    }

//    public Mono<void> deleteUser(String id) {
//        userRepository.deleteUser(id);
//    }
}

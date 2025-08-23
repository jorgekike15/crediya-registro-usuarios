package co.com.pragma.r2dbc;

import co.com.pragma.model.user.User;
import co.com.pragma.model.user.gateways.UserRepository;
import co.com.pragma.r2dbc.entity.UserEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        Integer,
        MyReactiveRepository
        > implements UserRepository {

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> saveUser(User user) {
        return repository.save(mapper.map(user, UserEntity.class))
                .map(e -> mapper.map(e, User.class));
    }

    @Override
    public Flux<User> findAllUsers() {
        return repository.findAll().map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findUserByNumber(String id) {
        return repository.findById(Integer.valueOf(id))
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> editUser(User user) {
        return repository.save(mapper.map(user, UserEntity.class))
                .map(e -> mapper.map(e, User.class));
    }

}

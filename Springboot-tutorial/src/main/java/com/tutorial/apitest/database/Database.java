package com.tutorial.apitest.database;

import com.tutorial.apitest.models.User;
import com.tutorial.apitest.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger =  LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository){
    return new CommandLineRunner() {
        @Override
        public void run(String... args) throws Exception {
          User u1 = new User("abc","abc","abc","abc","abc","abc");
          User u2 = new User("vvbc","vbbc","nnc","abc","abc","abb");
            User u3 = new User("vvbc","vbbc","nnc","abc","abc","abb");
            logger.info("insert data : " + userRepository.save(u1));
            logger.info("insert data : " + userRepository.save(u2));
            logger.info("insert data : " + userRepository.save(u3));
        }
    };

}
}

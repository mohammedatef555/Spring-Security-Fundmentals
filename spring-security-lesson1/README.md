# Spring Security Fundamentals - Lesson 1 - First steps
###### following [Spring Security Fundamentals 2022 from Laur Spilca](https://www.youtube.com/playlist?list=PLEocw3gLFc8X_a8hGWGaBnSkPFJmbb8QP "Spring Security Fundamentals 2022 Laur Spilca")

## Basic Dependencies
We need these dependencies to make a simple demo:
    
    - Spring web 
    - spring security  

## Default behavior

By just adding these dependencies, spring boot will configure the simplest configuration for the spring security to test and make sure spring security works properly

When you run the spring boot application, without writing a single line of code
You will see a generated password like this foe example `9799dd7b-f439-4489-a697-1eac2199c92a`

## What do we secure in web apps?
We secure endpoints or sometimes referred to as resources.

Resource in web apps: refers usually to endpoints through which the client calling the server can either get data or change data on the server

## Demo
Now we will create a simple endpoint, by default all the endpoints are secured, so you need to provide the user and the password in order to access the resource, if the credentials aren’t provided you will get 401 unauthenticated 

Spring boot pre-configure the spring security authentication as Http Basic by default.

In http basic we use authorization header to send our credentials, and they must be `base64` encoded
So when we send the credentials, we put it in this form `user:password` and encode this with base64 encoding then we prefix it with `Basic` keyword
So the final result would be

`Basic user:password`

But user:password  will be encoded, and then we send them in the authorization header, we put them in  authorization header, although we use it for authentication purpose.

Now the user:password was encoded not encrypted


### DemoController
```java
@RestController
public class DemoController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

}
```

### The Log
After running our application, we get a generated password
```
Using generated security password: 9799dd7b-f439-4489-a697-1eac2199c92a

This generated password is for development use only. Your security configuration must be updated before running your application in production.
```

### Using PostMan
Now as we said, if we didn't provide the credentials we will get `401 unauthenticated status code`:
![401 unauthenticated status code](screenshots/postman-401.png "401 unauthenticated status code").

Now we will add the credentials in the authorization header, and we should success get the endpoint
![200 status code](screenshots/postman-200.png "200 status code").
as we see, everything works as expected.

### Using curl
Without Credentials, we didn't get `hello` 
```
curl localhost:8080/hello -v

*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /hello HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.84.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 401
< Set-Cookie: JSESSIONID=F33817B1157546E3451ABEFD75A088C9; Path=/; HttpOnly
< WWW-Authenticate: Basic realm="Realm"
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Length: 0
< Date: Mon, 14 Nov 2022 11:09:38 GMT
<
* Connection #0 to host localhost left intact
```
With Credentials,we hit the endpoint and get the `hello`
```
curl -u user:e81c6892-1fb8-4e62-926c-b3e735277c20 localhost:8080/hello -v
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
* Server auth using Basic with user 'user'
> GET /hello HTTP/1.1
> Host: localhost:8080
> Authorization: Basic dXNlcjplODFjNjg5Mi0xZmI4LTRlNjItOTI2Yy1iM2U3MzUyNzdjMjA=
> User-Agent: curl/7.84.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Set-Cookie: JSESSIONID=A661C8744C33E716F43623C5DF30B51C; Path=/; HttpOnly
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 6
< Date: Mon, 14 Nov 2022 11:09:46 GMT
<
Hello
* Connection #0 to host localhost left intact
```

## What is the difference between Encoding, Encryption, Hash Function?
- Encoding: is  only a function that is always possible to reverse somehow, it might or might not imply the need of a secret, so it can be just a mathematical transformation that doesn’t even need a secret to be known to revert it, so it’s just a mathematical function that transforms an input to an output in such a way in which you can always reverse the output and find the input if you know the rule 
	
- Encryption: transforming an input to an output, but to go back from the output to the input you always need a secret, we can say it’s a particular way of encoding, its still a transformation function but you always need a secret to go back to the input, so not everyone can find the inout from the output unless having the secret 

- Hash Function:
  - From an input we can get an output, but from the output you can never by any means find the input again, so going from an output to input is not possible.
  - Same input will always result in the same output.

Usually the hash function is the preferred way to store temporary or permanently passwords, so if someone steel the hashed password he can never know the input

## UserDetailsService
How to create your own UserDetailsService?

UserDetailsService: is a component that manages the user details, so its a component you implement in your web application to tell spring security where to  get the credentials from.

When you create an instance of UserDetailsService, spring security will no longer generate a random password, when the application runs, because we have created our own user management through UserDetailsService.

### Create a UserDetailsService @Bean
we will also need a `PasswordEncoder`

```java
package com.mohammed.springsecuritylesson1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsManager uds = new InMemoryUserDetailsManager();

        UserDetails u1 = User.withUsername("mohamed")
                .password("12345")
                .authorities("read")
                .build();

        uds.createUser(u1);

        return uds;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
```
as we see in the above code snippet we have a `UserDetailsService` bean and `PasswordEncoder`

When you create an instance of UserDetailsService, spring security will no longer generate a random password, when the application runs, because we have created our own user management through UserDetailsService

so let's rerun out application again:

### test with curl
Without credentials:
```
curl localhost:8080/hello -v
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /hello HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.84.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 401
< Set-Cookie: JSESSIONID=9177593BADFB0A9EFE495D663D972570; Path=/; HttpOnly
< WWW-Authenticate: Basic realm="Realm"
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Length: 0
< Date: Mon, 14 Nov 2022 11:18:54 GMT
<
* Connection #0 to host localhost left intact
```

with credentials:
```
curl -u mohamed:12345 localhost:8080/hello -v
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
* Server auth using Basic with user 'mohamed'
> GET /hello HTTP/1.1
> Host: localhost:8080
> Authorization: Basic bW9oYW1lZDoxMjM0NQ==
> User-Agent: curl/7.84.0
> Accept: */*
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Set-Cookie: JSESSIONID=AC03322C3823A967EE5D79CD11DCD9C4; Path=/; HttpOnly
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 1; mode=block
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: DENY
< Content-Type: text/plain;charset=UTF-8
< Content-Length: 6
< Date: Mon, 14 Nov 2022 11:20:08 GMT
<
Hello
* Connection #0 to host localhost left intact
```

this is simple example, more on this topic in the coming lessons.

## Architecture
This is the big picture, we have just touched `UserDetailsService` and `PasswordEncoder`
![](screenshots/1.png "")

```mermaid
graph TD;
    HttpBasicFilter-->AuthenticationManager;
    AuthenticationManager-->AuthenticationProvider;
    AuthenticationProvider-->UserDetailsService;
    AuthenticationProvider-->PasswordEncoder;
```

Laur Spilca said it's actually chain of filters and there is spring security context as well.
# Spring Security Fundamentals - Lesson 4 - Multiple authentication providers

###### following [Spring Security Fundamentals 2022 from Laur Spilca](https://www.youtube.com/playlist?list=PLEocw3gLFc8X_a8hGWGaBnSkPFJmbb8QP "Spring Security Fundamentals 2022 Laur Spilca")


## Multiple Authentication Providers
In the new spring security version

Custom filter and default filter can’t use the same Authentication Manager


## HttpSecurity
What is the httpSecurity we inject in SecurityFilterChain?
* It allows configuring web based security for specific http requests. By default it will be applied to all requests, but can be restricted using requestMatcher(RequestMatcher) or other similar methods.

* it's an object that was previously used but by extending the WebSecurityConfigurerAdapter and override the configure() method, but now it’s in the spring context so you can pass it as an argument to @Bean method or even with @AutoWired


In this example we implement multiple mechanisms to authenticate users in Spring Security.

Either you use

    - username and password (Basic Auth)
    - Custom Authentication (x-api-key)





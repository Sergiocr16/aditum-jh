package com.lighthouse.aditum.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String MANAGER = "ROLE_MANAGER";

    public static final String OFFICER = "ROLE_OFFICER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String RH = "ROLE_RH";

    public static final String JD = "ROLE_JD";

    public static final String OFFICERMACRO= "ROLE_OFFICER_MACRO";

    public static final String MANAGERMACRO= "ROLE_MANAGER_MACRO";



    private AuthoritiesConstants() {
    }
}

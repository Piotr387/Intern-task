package com.example.interntask.responde;

public enum ErrorMessages {
    ERROR_WRITING_FILE("There was an error writing to file"),
    LECTURE_NOT_FOUND_BY_NAME("Nie znaleziono prelekcji o podanej nazwie"),
    NO_USER_FOUND_WITH_PROVIDED_LOGIN("No user found with provided login"),
    NO_ROLE_FOUND_WITH_PROVIDED_NAME("No role found with provided name"),
    LOGIN_ALREADY_TAKEN("Podany login jest już zajęty"),
    EMAIL_ALREADY_TAKEN("Podany email jest już zajęty"),
    ACCOUNT_ALREADY_REGISTER("Znaleziono konto o takich danych. Zapisz się na prelekcje z poziomu konta"),
    USER_TAKEN_AT_THIS_HOUR("User already sign in on lecture at this time"),
    NO_FREE_SEATS_AT_LECTURE("No free seats in the lecture"),
    EMAIL_ERROR_SAME_AS_PREVIOUS("Provided new email is same as previous one"),
    REFRESH_TOKEN_MISSING("Refresh token is missing"),
    NO_TOKEN_PROVIDED("No token provided"),
    DELETE_ERROR("No lecture name found in user's repository. Make sure you provided good lecture name."),
    AUTHENTICATION_FAIL("Authentication Failed. Make sure you provided corret login and password"),
    LOGIN_MISS_PATTERN("Login doesn't match pattern"),
    EMAIL_MISS_PATTERN("Email is not up to RFC 5322 standards");


    private String errorMessage;
    ErrorMessages(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

package org.example.stellog.auth.api.userInfo;

public class GoogleUserInfo implements OAuthUserInfo {
    private final UserInfo attributes;

    public GoogleUserInfo(UserInfo attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getEmail() {
        return attributes.email();
    }

    @Override
    public String getName() {
        return attributes.name();
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getPicture() {
        return attributes.picture();
    }
}


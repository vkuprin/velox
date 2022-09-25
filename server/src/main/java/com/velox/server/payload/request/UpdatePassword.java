package com.velox.server.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdatePassword {

    @NotBlank
    private String oldPassword;
    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

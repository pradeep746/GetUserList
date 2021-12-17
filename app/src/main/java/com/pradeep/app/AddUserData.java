package com.pradeep.app;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

public class AddUserData extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> firstName = new MutableLiveData<>();
    public MutableLiveData<String> lastName = new MutableLiveData<>();
    public MutableLiveData<String> emailError = new MutableLiveData<>();
    public MutableLiveData<String> firstNameError = new MutableLiveData<>();
    public MutableLiveData<String> lastNameError = new MutableLiveData<>();
    private MutableLiveData<UserDetails> userMutableLiveData;
    public AddUserData() {

    }

    public void onSignInClicked() {
        Log.e("TAG","click1");
        emailError.setValue(null);
        firstNameError.setValue(null);
        lastNameError.setValue(null);
        UserDetails user = new UserDetails(firstName.getValue(),lastName.getValue(),email.getValue());
        if (firstName.getValue() == null || firstName.getValue().isEmpty()) {
            firstNameError.setValue("Enter first name.");
            return;
        }

        if (user.firstNameCheck()) {
            firstNameError.setValue("firstName should more than 3 value");
            return;
        }
        if (lastName.getValue() == null || lastName.getValue().isEmpty()) {
            lastNameError.setValue("Enter last name.");
            return;
        }

        if (user.lastNameCheck()) {
            lastNameError.setValue("last name should more than 3 value");
            return;
        }
        if (email.getValue() == null || email.getValue().isEmpty()) {
            emailError.setValue("Enter email address.");
            Log.e("TAG","click: "+user.getLastName());
            return;
        }

        if (!user.isEmailValid()) {
            emailError.setValue("Enter a valid email address.");
            return;
        }
        Log.e("TAG","click");
        userMutableLiveData.setValue(user);
    }

    public LiveData<UserDetails> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void setDefault(){
        firstName.setValue("");
        lastName.setValue("");
        email.setValue("");
        emailError.setValue(null);
        firstNameError.setValue(null);
        lastNameError.setValue(null);
    }

    public void setUserData(User data){
        emailError.setValue(null);
        firstNameError.setValue(null);
        lastNameError.setValue(null);
        firstName.setValue(data.getFirstName());
        lastName.setValue(data.getLastName());
        email.setValue(data.getEmailId());
    }
}

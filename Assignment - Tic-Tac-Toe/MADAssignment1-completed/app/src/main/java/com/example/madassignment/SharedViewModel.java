package com.example.madassignment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private static final MutableLiveData<Boolean> gameAgainst = new MutableLiveData<>();
    private static final MutableLiveData<Integer> gameMode = new MutableLiveData<>();
    private static final MutableLiveData<Integer> winCondition = new MutableLiveData<>();
    private static final MutableLiveData<ArrayList<Profile>> profileList = new MutableLiveData<>(new ArrayList<>());
    private static final MutableLiveData<Profile> player1 = new MutableLiveData<>();
    private static final MutableLiveData<Profile> player2 = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> triggerUndo = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> triggerReset = new MutableLiveData<>();

    public void setGameAgainst(boolean value) {
        gameAgainst.setValue(value);
    }

    public LiveData<Boolean> getGameAgainst() {
        return gameAgainst;
    }

    public void setGameMode(int mode) {
        gameMode.setValue(mode);
    }

    public LiveData<Integer> getGameMode() {
        return gameMode;
    }

    public void setWinCondition(int value) {
        winCondition.setValue(value);
    }

    public LiveData<Integer> getWinCondition() {
        return winCondition;
    }

    public void addProfile(Profile newProfile) {
        ArrayList<Profile> currentProfiles = profileList.getValue();
        if (currentProfiles == null) {
            currentProfiles = new ArrayList<>();
        }
        currentProfiles.add(newProfile);
        profileList.setValue(currentProfiles);
    }

    public boolean profileExists(String name) {
        ArrayList<Profile> currentProfiles = profileList.getValue();
        if (currentProfiles == null) return false;

        for (Profile profile : currentProfiles) {
            if (profile.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public MutableLiveData<ArrayList<Profile>> getProfileList() {
        return profileList;
    }

    public void addWin(String name){
        int index = 0;
        ArrayList<Profile> currentProfiles = profileList.getValue();
        for (Profile profile : currentProfiles) {
            if (profile.getName().equals(name)) {
                profile.setWin(profile.getWin()+1);
                currentProfiles.set(index,profile);
                profileList.setValue(currentProfiles);
            }
            index+=1;
        }
    }

    public void addDraw(String name){
        int index = 0;
        ArrayList<Profile> currentProfiles = profileList.getValue();
        for (Profile profile : currentProfiles) {
            if (profile.getName().equals(name)) {
                profile.setDraw(profile.getDraw()+1);
                currentProfiles.set(index,profile);
                profileList.setValue(currentProfiles);
            }
            index+=1;
        }
    }

    public void addLoss(String name){
        int index = 0;
        ArrayList<Profile> currentProfiles = profileList.getValue();
        for (Profile profile : currentProfiles) {
            if (profile.getName().equals(name)) {
                profile.setLoss(profile.getLoss()+1);
                currentProfiles.set(index,profile);
                profileList.setValue(currentProfiles);
            }
            index+=1;
        }
    }

    public int getProfileCount(){
        int count = 0;
        ArrayList<Profile> currentProfiles = profileList.getValue();
        for (Profile profile : currentProfiles) {
            count+=1;
        }
        return count;
    }

    public Profile getProfile(String name){
        Profile foundProfile = new Profile();
        ArrayList<Profile> currentProfiles = profileList.getValue();
        for (Profile profile : currentProfiles) {
            if(profile.getName().equals(name)){
                foundProfile = profile;
            }
        }
        return foundProfile;
    }

    public void setPlayer1(Profile profile){
        player1.setValue(profile);
    }

    public Profile getPlayer1(){
        return player1.getValue();
    }

    public void setPlayer2(Profile profile){
        player2.setValue(profile);
    }

    public Profile getPlayer2(){
        return player2.getValue();
    }

    public LiveData<Boolean> getTriggerUndo() {
        return triggerUndo;
    }

    public void setTriggerUndo(boolean value) {
        triggerUndo.setValue(value);
    }

    public LiveData<Boolean> getTriggerReset() {
        return triggerReset;
    }

    public void setTriggerReset(boolean value) {
        triggerReset.setValue(value);
    }
}

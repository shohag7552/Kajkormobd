package com.example.kajkormobd.support;

public class LocalDatabase {
    private static LocalDatabase localDatabase;
    public static LocalDatabase getInstance(){
        if(localDatabase == null){
            localDatabase = new LocalDatabase();
        }
        return localDatabase;
    }

    private String selectedWork;

    public String getSelectedWork() {
        return selectedWork;
    }

    public void setSelectedWork(String selectedWork){
        this.selectedWork = selectedWork;
    }
}

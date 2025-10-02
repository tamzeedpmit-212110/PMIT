package com.example.medreminder;

public class ModelMed {
    String name,id,time1,time2,time3,pres_quantity,quantity,userId,type,lastTaken;
    int takenQuantity;

    ModelMed() {

    }
    public ModelMed(String name, String id, String time1, String time2, String time3,String pres_quantity, String quantity,
                    String userId,String type,String lastTaken,int takenQuantity ) {
        this.name = name;
        this.id = id;
        this.time1 = time1;
        this.time2 = time2;
        this.time3 = time3;
        this.pres_quantity=pres_quantity;
        this.quantity = quantity;
        this.userId = userId;
        this.type = type;
        this.lastTaken = lastTaken;
        this.takenQuantity = takenQuantity;

    }

    public String getType() {
        return type;
    }

    public String getLastTaken() {
        return lastTaken;
    }

    public void setLastTaken(String lastTaken) {
        this.lastTaken = lastTaken;
    }


    public String getPres_quantity() {
        return pres_quantity;
    }

    public void setPres_quantity(String pres_quantity) {
        this.pres_quantity = pres_quantity;
    }

    public int getTakenQuantity() {
        return takenQuantity;
    }

    public void setTakenQuantity(int takenQuantity) {
        this.takenQuantity = takenQuantity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getTime3() {
        return time3;
    }

    public void setTime3(String time3) {
        this.time3 = time3;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

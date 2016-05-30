package com.appenjoyer.eda;

/**
 * Created by Joan on 26/05/2016.
 */
public class Sessio implements Comparable<Sessio>{

    private int percent;
    private boolean state;
    private String name;


    public Sessio(String name,int percent, boolean state){
        this.name = name;
        this.percent = percent;
        this.state = state;

    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Name: " + this.name + " Percent: " +this.percent+ " State: " + this.state;
    }

    @Override
    public int compareTo(Sessio SessioToCheck) {
        String SessioNametoCheck = SessioToCheck.getName().toLowerCase();
        String SessioThis = this.getName().toLowerCase();

        return( SessioThis.compareTo( SessioNametoCheck ) );
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {   return false;  }
        if (getClass() != obj.getClass()) {  return false;   }
        final Sessio other = (Sessio) obj;
        return this.name.equals(other.name);
    }
}

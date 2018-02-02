package com.obinna.redeemfinance;

import java.io.Serializable;

/**
 * Created by hp 14 on 1/11/2017.
 */

public class Profits implements Serializable {

        String profitname;
        String income_type;
        String date;
        String type;

    public Profits(String profitname,String income_type,String date,String type)
    {
        this.profitname=profitname;
        this.income_type=income_type;
        this.date=date;
        this.type=type;
    }




    /*
        public int getid() {
            return this.id;
        }
        public void setid(int id) {
            this.id = id;
        } */
        public String getprofitname() {
            return profitname;
        }


        public void setprofitname(String profitname) {
            this.profitname = profitname;
        }

        public String getincome_type() {
                    return income_type;
                }

        public void setincome_type(String income_type) {
                    this.income_type = income_type;
        }

        public String getdate() {
        return date;
    }


         public void setdate(String date) {
        this.date = date;
    }

    public String gettype() {
        return type;
    }


    public void settype(String type) {
        this.type = type;
    }

        /*        @Override
    *     public String toString() {
                  return income_type + " " + profitname;
              }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profits contact = (Profits) o;

        //if (!phone.equals(contact.phone)) return false;

        return true;
    }*/

}

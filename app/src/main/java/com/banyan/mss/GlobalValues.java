package com.banyan.mss;

        import android.app.Application;

public class GlobalValues extends Application {


    public String from,to,date,address,boardingname;

    int fare,tax,ticketcount;


    public String getfrom()
    {
        return from;
    }
    public String getto()
    {
        return to;
    }
    public String getdate()
    {
        return date;
    }

    public String getaddress()
    {
        return address;
    }

    public String getboardingname()
    {
        return boardingname;
    }

    public int getfare()
    {
        return fare;
    }
    public int gettax()
    {
        return tax;
    }
    public int getcount()
    {
        return ticketcount;
    }


    public String setfrom(String from)
    {
        this.from=from;
        return from;
    }
    public void setto(String to)
    {
        this.to=to;
    }
    public void setdate(String date)
    {
        this.date=date;
    }
    public void setaddress(String address)
    {
        this.address=address;
    }
    public void setboardingname(String boardingname)
    {
        this.boardingname=boardingname;
    }

    public void setfare(int fare)
    {
        this.fare=fare;
    }
    public void settax(int tax)
    {
        this.tax=tax;
    }

    public void setcount(int ticketcount)
    {
        this.ticketcount=ticketcount;
    }

}


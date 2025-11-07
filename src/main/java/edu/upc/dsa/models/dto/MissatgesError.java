package edu.upc.dsa.models.dto;

public class MissatgesError
{
    String message;

    public MissatgesError() {}

    public MissatgesError(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}

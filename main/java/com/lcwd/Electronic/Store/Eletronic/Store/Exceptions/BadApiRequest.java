package com.lcwd.Electronic.Store.Eletronic.Store.Exceptions;

public class BadApiRequest extends RuntimeException{

    public BadApiRequest(String message)
    {
        super(message);
    }

    public BadApiRequest()
    {
        super("Bad Request !");
    }


}

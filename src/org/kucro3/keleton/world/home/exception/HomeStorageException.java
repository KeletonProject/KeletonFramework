package org.kucro3.keleton.world.home.exception;

public class HomeStorageException extends HomeException {
    public HomeStorageException()
    {
    }

    public HomeStorageException(String msg)
    {
        super(msg);
    }

    public HomeStorageException(Throwable e)
    {
        super(e);
    }

    public HomeStorageException(String msg, Throwable e)
    {
        super(msg, e);
    }
}

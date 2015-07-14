package org.mongeez;

/**
 * Used to fail-fast on mongeez processing errors.
 *
 * @author dtserekhman
 * @since 11/20/2013
 */
public class MongeezException extends RuntimeException
{
    public MongeezException()
    {
    }

    public MongeezException(String s)
    {
        super(s);
    }

    public MongeezException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public MongeezException(Throwable throwable)
    {
        super(throwable);
    }
}

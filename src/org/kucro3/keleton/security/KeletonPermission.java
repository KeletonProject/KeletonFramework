package org.kucro3.keleton.security;

import java.security.Permission;
import java.util.Arrays;

public class KeletonPermission extends Permission {
    public KeletonPermission(String name, String subname, String action)
    {
        super(name + "." + subname);
        this.action = action;
        this.majorName = name;
        this.subName = subname;
    }

    @Override
    public boolean implies(Permission permission)
    {
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof KeletonPermission))
            return false;

        KeletonPermission instance = (KeletonPermission) obj;

        if(!getName().equals(instance.getName()))
            return false;

        if(!getAction().equals(instance.getAction()))
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(new String[] {getName(), action});
    }

    @Override
    public String getActions()
    {
        return action;
    }

    public String getAction()
    {
        return action;
    }

    public final String getMajorName()
    {
        return majorName;
    }

    public final String getSubName()
    {
        return subName;
    }

    private final String majorName;

    private final String subName;

    private final String action;
}

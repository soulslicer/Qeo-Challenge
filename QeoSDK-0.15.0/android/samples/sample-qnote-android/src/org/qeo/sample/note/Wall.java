/**************************************************************
 ********          THIS IS A GENERATED FILE         ***********
 **************************************************************/

package org.qeo.sample.note;

import org.qeo.Key;

public class Wall
{
    /**
     * Unique ID of the wall. This is a randomly generated number by the Wall.
     */
    @Key
    public int id;

    /**
     * Description of the wall (e.g kitchen wall)
     */
    public String description;

    public Wall()
    {
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        final Wall myObj = (Wall)obj;
        if (id != myObj.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
}

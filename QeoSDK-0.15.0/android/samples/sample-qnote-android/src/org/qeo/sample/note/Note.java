/**************************************************************
 ********          THIS IS A GENERATED FILE         ***********
 **************************************************************/

package org.qeo.sample.note;

import org.qeo.Key;

public class Note
{
    /**
     * Unique ID of every note. This ID is a random generated number by the client.
     */
    @Key
    public int id;

    /**
     * Unique ID of the wall to which this note belongs
     */
    @Key
    public int wallId;

    /**
     * The content of the note
     */
    public String message;

    public Note()
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
        final Note myObj = (Note)obj;
        if (id != myObj.id) {
            return false;
        }
        if (wallId != myObj.wallId) {
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
        result = prime * result + wallId;
        return result;
    }
}

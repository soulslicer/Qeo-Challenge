/**************************************************************
 ********          THIS IS A GENERATED FILE         ***********
 **************************************************************/

package org.qeo.sample.note;

public class NoteRequest
{
    /**
     * Unique ID of the note request. This ID will be a randomly generated integer and should be unique for all running QNote applications. It is very important that the ID is chosen by the client application because it is necessary to indicate what kind of action the NoteRequest represents.
     */
    public int id;

    /**
     * Unique ID of the wall where this request is addressed to. Combined with the ID of the NoteRequest, it will be used to indicate what kind of action this request represents. If the Wall that receives the request has the same Wall ID as the NoteRequest it will either be a note creation (if the wall does not have a note with the given ID yet) or a modification (if the wall already has a note with the given ID). If the Wall ID of the request does not match the ID of the receiving wall the wall should delete it (if the wall has a note with the same note ID) or ignore it (if it doesn't have a note with a matching ID).
     */
    public int wallId;

    /**
     * The content of the note.
     */
    public String message;

    public NoteRequest()
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
        final NoteRequest myObj = (NoteRequest)obj;
        if (id != myObj.id) {
            return false;
        }
        if (wallId != myObj.wallId) {
            return false;
        }
        if (!message.equals(myObj.message)) {
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
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        return result;
    }
}

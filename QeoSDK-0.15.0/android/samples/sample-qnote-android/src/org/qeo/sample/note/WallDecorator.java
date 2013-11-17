/************** COPYRIGHT AND CONFIDENTIALITY INFORMATION *********************
 **                                                                          **
 ** Copyright (c) 2013 Technicolor                                           **
 ** All Rights Reserved                                                      **
 **                                                                          **
 ** This program contains proprietary information which is a trade           **
 ** secret of TECHNICOLOR and/or its affiliates and also is protected as     **
 ** an unpublished work under applicable Copyright laws. Recipient is        **
 ** to retain this program in confidence and is not permitted to use or      **
 ** make copies thereof other than as permitted in a written agreement       **
 ** with TECHNICOLOR, UNLESS OTHERWISE EXPRESSLY ALLOWED BY APPLICABLE LAWS. **
 **                                                                          **
 ******************************************************************************/
package org.qeo.sample.note;

/**
 * Wrapper class to override the toString method of the generated Wall class.
 */
public class WallDecorator
        extends Wall
{
    /**
     * Constructs a new WallDecorator.
     * 
     * @param w The wall.
     */
    public WallDecorator(Wall w)
    {
        super();
        this.description = w.description;
        this.id = w.id;
    }

    @Override
    public String toString()
    {
        return this.description;
    }
}

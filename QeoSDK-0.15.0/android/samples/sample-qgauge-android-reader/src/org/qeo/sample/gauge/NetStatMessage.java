/**************************************************************
 ********          THIS IS A GENERATED FILE         ***********
 **************************************************************/

package org.qeo.sample.gauge;

import org.qeo.Key;

/**
 * NetStatMessage represents different statistics of a network interface.
 */
public class NetStatMessage
{
    /**
     * The DeviceId of the host of this network interface.
     */
    @Key
    public org.qeo.system.DeviceId deviceId;

    /**
     * The name of the network interface.
     */
    @Key
    public String ifName;

    /**
     * The number of bytes received.
     */
    public long bytesIn;

    /**
     * The number of packets received.
     */
    public long packetsIn;

    /**
     * The number of bytes transmitted.
     */
    public long bytesOut;

    /**
     * The number of packets transmitted.
     */
    public long packetsOut;

    /**
     * The timestamp (in milliseconds) at which this NetStatMessage was updated.
     */
    public long timestamp;

    public NetStatMessage()
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
        final NetStatMessage myObj = (NetStatMessage)obj;
        if (!deviceId.equals(myObj.deviceId)) {
            return false;
        }
        if (!ifName.equals(myObj.ifName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        result = prime * result + ((ifName == null) ? 0 : ifName.hashCode());
        return result;
    }
}

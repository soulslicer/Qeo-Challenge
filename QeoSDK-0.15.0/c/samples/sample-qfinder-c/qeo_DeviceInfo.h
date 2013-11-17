/**************************************************************
 ********          THIS IS A GENERATED FILE         ***********
 **************************************************************/

#ifndef QDM_QEO_DEVICEINFO_H_
#define QDM_QEO_DEVICEINFO_H_

#include <qeo/types.h>
#include "qeo_types.h"


/**
 * Basic device info published by any Qeo-enabled device. 	 On Android, the Qeo Service will handle the job of publishing this information.
 */
typedef struct {
    /**
     * [Key] The device identification generated by Qeo.
     */
    org_qeo_system_DeviceId_t deviceId;
    /**
     * The manufacturer of the device (human readable string).
     */
    char * manufacturer;
    /**
     * Model name of the device (human readable string).
     */
    char * modelName;
    /**
     * Identifier of the class of product for which the serial number applies. That is, for a given manufacturer, this parameter is used to identify the product or class of product over which the SerialNumber parameter is unique. This value MUST remain fixed over the lifetime of the device, including across firmware updates.
     */
    char * productClass;
    /**
     * Identifier of the particular device that is unique for the indicated class of product and manufacturer. This value MUST remain fixed over the lifetime of the device, including across firmware updates.
     */
    char * serialNumber;
    /**
     * A string identifying the particular CPE model and version.
     */
    char * hardwareVersion;
    /**
     * A string identifying the software version currently installed in the device (i.e. version of the overall device firmware). To allow version comparisons, this element SHOULD be in the form of dot-delimited integers, where each successive integer represents a more minor category of variation. For example, 3.0.21 where the components mean: Major.Minor.Build.
     */
    char * softwareVersion;
    /**
     * Descriptive name of the device.
     */
    char * userFriendlyName;
    /**
     * URL for configuration page that allows to view and/or configure device settings.
     */
    char * configURL;
} org_qeo_system_DeviceInfo_t;
extern const DDS_TypeSupport_meta org_qeo_system_DeviceInfo_type[];


#endif /* QDM_QEO_DEVICEINFO_H_ */


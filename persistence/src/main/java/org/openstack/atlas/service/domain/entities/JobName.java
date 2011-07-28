package org.openstack.atlas.service.domain.entities;

public enum JobName {
    LB_USAGE_POLLER,
    LB_USAGE_ROLLUP,
    ACCOUNT_USAGE_POLLER,
    LB_DELETION_JOB,
    HOST_ENDPOINT_POLLER,
    HOST_USAGE_POLLER,
    ALERT_DELETION_JOB,
    ATOM_EVENTS_DELETION_JOB,
    RATE_LIMIT_DELETION_JOB,
    DAILY_DELETION_JOB,
    ARCHIVE,
    FILECOPY,
    FILECOPY_PARENT,
    FILEASSEMBLE,
    WATCHDOG,
    FILES_SPLIT,
    MAPREDUCE;
}
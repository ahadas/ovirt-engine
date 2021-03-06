package org.ovirt.engine.core.bll.storage.disk;

import javax.inject.Inject;

import org.ovirt.engine.core.bll.NonTransactiveCommandAttribute;
import org.ovirt.engine.core.bll.context.CommandContext;
import org.ovirt.engine.core.common.AuditLogType;
import org.ovirt.engine.core.common.action.VmDiskOperationParameterBase;
import org.ovirt.engine.core.common.errors.EngineMessage;
import org.ovirt.engine.core.common.vdscommands.VDSCommandType;
import org.ovirt.engine.core.dao.VmDeviceDao;

@NonTransactiveCommandAttribute
public class HotUnPlugDiskFromVmCommand<T extends VmDiskOperationParameterBase> extends HotPlugDiskToVmCommand<T> {

    @Inject
    private VmDeviceDao vmDeviceDao;

    public HotUnPlugDiskFromVmCommand(T parameters, CommandContext commandContext) {
        super(parameters, commandContext);
    }

    @Override
    protected void updateDeviceProperties() {
        oldVmDevice.setPlugged(false);
        oldVmDevice.setAddress("");
        oldVmDevice.setLogicalName(null);
        vmDeviceDao.update(oldVmDevice);
    }

    @Override
    protected void setActionMessageParameters() {
        addValidationMessage(EngineMessage.VAR__ACTION__HOT_UNPLUG);
        addValidationMessage(EngineMessage.VAR__TYPE__DISK);
    }

    @Override
    public AuditLogType getAuditLogTypeValue() {
        return getSucceeded() ? AuditLogType.USER_HOTUNPLUG_DISK : AuditLogType.USER_FAILED_HOTUNPLUG_DISK;
    }

    @Override
    protected VDSCommandType getPlugAction() {
        return VDSCommandType.HotUnPlugDisk;
    }
}

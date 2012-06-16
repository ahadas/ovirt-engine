package org.ovirt.engine.ui.webadmin.section.main.view.popup.pool;

import org.ovirt.engine.ui.common.view.popup.AbstractVmPopupView;
import org.ovirt.engine.ui.common.widget.uicommon.popup.pool.PoolEditPopupWidget;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.ApplicationResources;
import org.ovirt.engine.ui.webadmin.section.main.presenter.popup.pool.PoolEditPopupPresenterWidget;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class PoolEditPopupView extends AbstractVmPopupView implements PoolEditPopupPresenterWidget.ViewDef {

    @Inject
    public PoolEditPopupView(EventBus eventBus, ApplicationResources resources, ApplicationConstants constants) {
        super(eventBus, resources, new PoolEditPopupWidget(constants));
    }

}

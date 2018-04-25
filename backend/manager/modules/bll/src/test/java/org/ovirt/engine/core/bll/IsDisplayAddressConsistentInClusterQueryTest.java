package org.ovirt.engine.core.bll;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.ovirt.engine.core.common.businessentities.VDS;
import org.ovirt.engine.core.common.queries.IdQueryParameters;

// this class contains only tests for the isDisplayAddressPartiallyOverridden method
@RunWith(Theories.class)
public class IsDisplayAddressConsistentInClusterQueryTest extends
        AbstractUserQueryTest<IdQueryParameters, IsDisplayAddressConsistentInClusterQuery<IdQueryParameters>> {

    // create data point with 1,2,3 and many VDS (all has overridden console address)
    @DataPoints
    public static OverriddenConsoleAddress[] overriddens = new OverriddenConsoleAddress[]{
            new OverriddenConsoleAddress(1),
            new OverriddenConsoleAddress(2),
            new OverriddenConsoleAddress(3),
            new OverriddenConsoleAddress(20),
    };

    // create data point with 1,2,3 and many VDS (none has overridden console address)
    @DataPoints
    public static DefaultConsoleAddress[] defaults = new DefaultConsoleAddress[]{
            new DefaultConsoleAddress(1),
            new DefaultConsoleAddress(2),
            new DefaultConsoleAddress(3),
            new DefaultConsoleAddress(20),
    };

    @Test
    public void nullHostsAreNotMismatched() {
        assertThat(getQuery().isDisplayAddressPartiallyOverridden(null), is(false));
    }

    @Test
    public void emptyHostsAreNotMismatched() {
        assertThat(getQuery().isDisplayAddressPartiallyOverridden(new ArrayList<>()), is(false));
    }

    @Theory
    public void whenAllHostsAreDefaultTheyAreNotMismatched(DefaultConsoleAddress defaultAddress) {
        assertThat(getQuery().isDisplayAddressPartiallyOverridden(defaultAddress.getAllVds()), is(false));
    }

    @Theory
    public void whenAllHostsAreOverriddenTheyAreNotMismatched(OverriddenConsoleAddress overriddenAddress) {
        assertThat(getQuery().isDisplayAddressPartiallyOverridden(overriddenAddress.getAllVds()), is(false));
    }

    @Theory
    public void anyCombinationOfDefaulfAndOverriddenHostsAreMismatched(DefaultConsoleAddress defaultAddress, OverriddenConsoleAddress overriddenAddress) {
        List<VDS> mergedAddresses = new ArrayList<>();
        mergedAddresses.addAll(defaultAddress.getAllVds());
        mergedAddresses.addAll(overriddenAddress.getAllVds());
        assertThat(getQuery().isDisplayAddressPartiallyOverridden(mergedAddresses), is(true));
    }

    private abstract static class BaseVdsContainer {

        private List<VDS> content;

        public BaseVdsContainer(int numOfVds) {
            for (int i = 0; i < numOfVds; i++) {
                addVds();
            }
        }

        protected abstract void addVds();

        public void addVds(String returnValue) {
            if (content == null) {
                content = new ArrayList<>();
            }

            VDS vds = mock(VDS.class);
            when(vds.getConsoleAddress()).thenReturn(returnValue);
            content.add(vds);
        }

        public List<VDS> getAllVds() {
            return content;
        }
    }

    private static class OverriddenConsoleAddress extends BaseVdsContainer {

        public OverriddenConsoleAddress(int numOfVds) {
            super(numOfVds);
        }

        @Override
        protected void addVds() {
            super.addVds("some overridden value"); //$NON-NLS-1$
        }
    }

    private static class DefaultConsoleAddress extends BaseVdsContainer {

        public DefaultConsoleAddress(int numOfVds) {
            super(numOfVds);
        }

        @Override
        protected void addVds() {
            super.addVds(null);
        }
    }
}

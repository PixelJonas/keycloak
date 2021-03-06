package org.keycloak.testsuite.console.events;

import org.jboss.arquillian.graphene.page.Page;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.testsuite.console.AbstractConsoleTest;
import org.keycloak.testsuite.console.page.events.Config;

import static org.junit.Assert.*;

/**
 * @author mhajas
 */
public class ConfigTest extends AbstractConsoleTest {

    @Page
    private Config configPage;

    @Before
    public void beforeConfigTest() {
        configPage.navigateTo();
    }

    @Test
    public void configLoginEventsTest() {
        configPage.form().setSaveEvents(true);
        configPage.form().addSaveType("REGISTER_NODE");
        //after removeSavedType method stay input focused -> in phantomjs drop menu doesn't appear after first click
        configPage.form().removeSaveType("LOGIN");
        configPage.form().setExpiration("50", "Days");
        configPage.form().save();
        assertFlashMessageSuccess();

        RealmRepresentation realm = testRealmResource().toRepresentation();
        assertTrue(realm.isEventsEnabled());
        assertFalse(realm.getEnabledEventTypes().contains("LOGIN"));
        assertTrue(realm.getEnabledEventTypes().contains("REGISTER_NODE"));
        assertEquals(4320000L, realm.getEventsExpiration().longValue());
    }

    @Test
    public void configAdminEventsTest() {
        configPage.form().setSaveAdminEvents(true);
        configPage.form().setIncludeRepresentation(true);
        configPage.form().save();
        assertFlashMessageSuccess();

        RealmRepresentation realm = testRealmResource().toRepresentation();
        assertTrue(realm.isAdminEventsEnabled());
        assertTrue(realm.isAdminEventsDetailsEnabled());
    }
}

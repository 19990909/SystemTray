/*
 * Copyright 2015 dorkbox, llc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dorkbox;

import java.net.URL;

import dorkbox.systemTray.Menu;
import dorkbox.systemTray.MenuEntry;
import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.SystemTrayMenuAction;

/**
 * Icons from 'SJJB Icons', public domain/CC0 icon set
 */
public
class TestTray {

    public static final URL BLACK_MAIL = TestTray.class.getResource("transport_bus_station.p.000000.32.png");
    public static final URL GREEN_MAIL = TestTray.class.getResource("transport_bus_station.p.39AC39.32.png");
    public static final URL LT_GRAY_MAIL = TestTray.class.getResource("transport_bus_station.p.999999.32.png");

    public static
    void main(String[] args) {
        // make sure JNA jar is on the classpath!
        new TestTray();
    }

    private SystemTray systemTray;
    private SystemTrayMenuAction callbackGreen;
    private SystemTrayMenuAction callbackGray;

    public
    TestTray() {
        this.systemTray = SystemTray.get();
        if (systemTray == null) {
            throw new RuntimeException("Unable to load SystemTray!");
        }

        systemTray.setImage(LT_GRAY_MAIL);
        systemTray.setStatus("No Mail");

        callbackGreen = new SystemTrayMenuAction() {
            @Override
            public
            void onClick(final SystemTray systemTray, final Menu parent, final MenuEntry menuEntry) {
                systemTray.setStatus("Some Mail!");
                systemTray.setImage(GREEN_MAIL);

                menuEntry.setCallback(callbackGray);
                menuEntry.setImage(BLACK_MAIL);
                menuEntry.setText("Delete Mail");
//                systemTray.remove(menuEntry);
            }
        };

        callbackGray = new SystemTrayMenuAction() {
            @Override
            public
            void onClick(final SystemTray systemTray, final Menu parent, final MenuEntry menuEntry) {
                systemTray.setStatus(null);
                systemTray.setImage(BLACK_MAIL);

                menuEntry.setCallback(null);
//                systemTray.setStatus("Mail Empty");
                systemTray.remove(menuEntry);
                System.err.println("POW");
            }
        };

        MenuEntry menuEntry = this.systemTray.addEntry("Green Mail", GREEN_MAIL, callbackGreen);
        // case does not matter
        menuEntry.setShortcut('G');

        this.systemTray.addSeparator();

        final Menu submenu = this.systemTray.addMenu("Options", BLACK_MAIL);
        submenu.addEntry("Disable menu", LT_GRAY_MAIL, new SystemTrayMenuAction() {
            @Override
            public
            void onClick(final SystemTray systemTray, final Menu parent, final MenuEntry entry) {
                submenu.setEnabled(false);
            }
        });
        submenu.addEntry("Remove menu", GREEN_MAIL, new SystemTrayMenuAction() {
            @Override
            public
            void onClick(final SystemTray systemTray, final Menu parent, final MenuEntry entry) {
                submenu.remove();
            }
        });


        systemTray.addEntry("Quit", new SystemTrayMenuAction() {
            @Override
            public
            void onClick(final SystemTray systemTray, final Menu parent, final MenuEntry menuEntry) {
                systemTray.shutdown();
                //System.exit(0);  not necessary if all non-daemon threads have stopped.
            }
        }).setShortcut('q'); // case does not matter
    }
}

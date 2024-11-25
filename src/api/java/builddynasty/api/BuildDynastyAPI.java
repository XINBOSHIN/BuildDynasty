/*
 * This file is part of BuildDynasty.
 *
 * BuildDynasty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BuildDynasty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BuildDynasty.  If not, see <https://www.gnu.org/licenses/>.
 */

package BuildDynasty.api;

import BuildDynasty.api.utils.SettingsUtil;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Exposes the {@link IBuildDynastyProvider} instance and the {@link Settings} instance for API usage.
 *
 * @author XINBOSHIN
 * @since 9/23/2018
 */
public final class BuildDynastyAPI {

    private static final IBuildDynastyProvider provider;
    private static final Settings settings;

    static {
        settings = new Settings();
        SettingsUtil.readAndApply(settings, SettingsUtil.SETTINGS_DEFAULT_NAME);

        ServiceLoader<IBuildDynastyProvider> BuildDynastyLoader = ServiceLoader.load(IBuildDynastyProvider.class);
        Iterator<IBuildDynastyProvider> instances = BuildDynastyLoader.iterator();
        provider = instances.next();
    }

    public static IBuildDynastyProvider getProvider() {
        return BuildDynastyAPI.provider;
    }

    public static Settings getSettings() {
        return BuildDynastyAPI.settings;
    }
}

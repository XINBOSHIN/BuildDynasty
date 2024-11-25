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

package BuildDynasty.launch.mixins;

import BuildDynasty.api.BuildDynastyAPI;
import BuildDynasty.api.IBuildDynasty;
import BuildDynasty.api.event.events.TabCompleteEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TabCompleter.class)
public abstract class MixinTabCompleter {

    @Shadow
    @Final
    protected GuiTextField textField;

    @Shadow
    protected boolean requestedCompletions;

    @Shadow
    public abstract void setCompletions(String... newCompl);

    @Unique
    protected boolean dontComplete = false;

    @Inject(
            method = "requestCompletions",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRequestCompletions(String prefix, CallbackInfo ci) {
        if (!((Object) this instanceof GuiChat.ChatTabCompleter)) {
            return;
        }

        IBuildDynasty BuildDynasty = BuildDynastyAPI.getProvider().getPrimaryBuildDynasty();

        TabCompleteEvent event = new TabCompleteEvent(prefix);
        BuildDynasty.getGameEventHandler().onPreTabComplete(event);

        if (event.isCancelled()) {
            ci.cancel();
            return;
        }

        if (event.completions != null) {
            ci.cancel();

            this.dontComplete = true;

            try {
                this.requestedCompletions = true;
                setCompletions(event.completions);
            } finally {
                this.dontComplete = false;
            }
        }
    }
}

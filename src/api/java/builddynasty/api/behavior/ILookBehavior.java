
package builddynasty.api.behavior;

import builddynasty.api.Settings;
import builddynasty.api.behavior.look.IAimProcessor;
import builddynasty.api.utils.Rotation;

public interface ILookBehavior extends IBehavior {


    void updateTarget(Rotation rotation, boolean blockInteract);


    IAimProcessor getAimProcessor();
}

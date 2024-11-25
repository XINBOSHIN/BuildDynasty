

package builddynasty.api.behavior.look;

import builddynasty.api.utils.Rotation;


public interface IAimProcessor {


    Rotation peekRotation(Rotation desired);

    ITickableAimProcessor fork();
}
